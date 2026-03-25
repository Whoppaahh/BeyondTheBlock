package net.ryan.beyond_the_block.village.GuardVillager.Goals;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.config.Configs;
import net.ryan.beyond_the_block.mixin.CrossbowAccessor;
import net.ryan.beyond_the_block.village.GuardVillager.GuardEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

import static net.ryan.beyond_the_block.village.ModVillagers.GUARD_VILLAGER;

public class RangedCrossbowAttackPassiveGoal<T extends PathAwareEntity & RangedAttackMob & CrossbowUser> extends Goal {
    public static final UniformIntProvider PATHFINDING_DELAY_RANGE = TimeHelper.betweenSeconds(1, 2);
    private final T mob;
    private int chargingTime = 0;
    private final double speedModifier;
    private final float attackRadiusSqr;
    protected double wantedX;
    protected double wantedY;
    protected double wantedZ;
    private CrossbowState crossbowState = CrossbowState.UNCHARGED;
    private int seeTime;
    private int attackDelay;
    private int updatePathDelay;

    public RangedCrossbowAttackPassiveGoal(T pMob, double pSpeedModifier, float pAttackRadius) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.attackRadiusSqr = pAttackRadius * pAttackRadius;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.isValidTarget() && this.isHoldingCrossbow();
    }

    private boolean isHoldingCrossbow() {
        return this.mob.isHolding(is -> is.getItem() instanceof CrossbowItem);
    }

    @Override
    public boolean shouldContinue() {
        return this.isValidTarget() && (this.canStart() || !this.mob.getNavigation().isIdle()) && this.isHoldingCrossbow();
    }

    private boolean isValidTarget() {
        return this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAttacking(false);
        this.mob.setTarget(null);
        this.seeTime = 0;
        if (this.mob.isUsingItem()) {
            this.mob.stopUsingItem();
            this.mob.setCharging(false);
        }
        this.mob.setPose(EntityPose.STANDING);
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.mob.setAttacking(true);
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            double distance = this.mob.squaredDistanceTo(target);
            boolean canSee = this.mob.getVisibilityCache().canSee(target);
            boolean hasSeenEntityRecently = this.seeTime > 0;

            this.seeTime += canSee ? 1 : -1;

            if (distance <= 4.0D) {
                this.mob.getMoveControl().strafeTo(this.mob.isUsingItem() ? -0.5F : -3.0F, 0.0F);
                this.mob.lookAtEntity(target, 30.0F, 30.0F);
            }
            if (this.mob.getRandom().nextInt(50) == 0) {
                this.mob.setPose(this.mob.getPose() == EntityPose.STANDING ? EntityPose.CROUCHING : EntityPose.STANDING);
            }
            boolean shouldMove = (distance > this.attackRadiusSqr || this.seeTime < 5) && this.attackDelay == 0;
            if (shouldMove) {
                if (--this.updatePathDelay <= 0) {
                    this.mob.getNavigation().startMovingTo(target, this.canRun() ? this.speedModifier : this.speedModifier * 0.5D);
                    this.updatePathDelay = PATHFINDING_DELAY_RANGE.get(this.mob.getRandom());
                }
            } else {
                this.updatePathDelay = 0;
                this.mob.getNavigation().stop();
            }
            this.mob.lookAtEntity(target, 30.0F, 30.0F);
            this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);
            if (this.friendlyInLineOfSight() && Configs.server().features.guards.friendlyFire)
                this.crossbowState = CrossbowState.FIND_NEW_POSITION;

            switch (this.crossbowState) {
                case FIND_NEW_POSITION -> {
                    this.mob.stopUsingItem();
                    this.mob.setCharging(false);
                    if (this.findPosition())
                        this.mob.getNavigation().startMovingTo(this.wantedX, this.wantedY, this.wantedZ, this.mob.isSneaking() ? 0.5F : 1.2D);
                    this.crossbowState = CrossbowState.UNCHARGED;
                }

                case UNCHARGED -> {
                    if (hasSeenEntityRecently) {
                        this.mob.setCurrentHand(BeyondTheBlock.getHandWith(this.mob, item -> item instanceof CrossbowItem));
                        this.mob.setCharging(true);
                        this.crossbowState = CrossbowState.CHARGING;
                    }
                }

                case CHARGING -> {
                    chargingTime++;
                    int requiredPullTime = 25;
                    int useTime = this.mob.getItemUseTime();
                    ItemStack itemStack = this.mob.getActiveItem();

                    if (useTime >= requiredPullTime || CrossbowItem.isCharged(itemStack) || chargingTime > 60) {
                        this.mob.stopUsingItem();
                        this.mob.setCharging(false);
                        this.attackDelay = 10 + this.mob.getRandom().nextInt(5);
                        this.crossbowState = CrossbowState.CHARGED;
                        chargingTime = 0;
                    }
                }


                case CHARGED -> {
                    if (--this.attackDelay <= 0) {
                        this.crossbowState = CrossbowState.READY_TO_ATTACK;
                    }
                }

                case READY_TO_ATTACK -> {
                    if (!canSee) return;

                    ItemStack crossbowStack = this.mob.getStackInHand(BeyondTheBlock.getHandWith(this.mob, item -> item instanceof CrossbowItem));
                    Hand hand = BeyondTheBlock.getHandWith(this.mob, item -> item instanceof CrossbowItem);
                    CrossbowItem crossbowItem = (CrossbowItem) crossbowStack.getItem();

                    if (!CrossbowItem.isCharged(crossbowStack)) {
                        ItemStack ammo = ((GuardEntity)this.mob).getProjectileType(crossbowStack);
                        if (ammo.isEmpty()) ammo = new ItemStack(Items.ARROW);

                        // Create the charged projectiles NBT tag
                        NbtList chargedProjectiles = new NbtList();
                        NbtCompound projectileNbt = new NbtCompound();
                        ammo.copy().writeNbt(projectileNbt);
                        chargedProjectiles.add(projectileNbt);

                        // Write it to the crossbow
                        NbtCompound tag = crossbowStack.getOrCreateNbt();
                        tag.put("ChargedProjectiles", chargedProjectiles);

                    }
                    BeyondTheBlock.LOGGER.info("Shooting crossbow from crossbow goal");
                    CrossbowAccessor.callShootAll(this.mob.getWorld(), this.mob, hand, crossbowStack, 2.0F, 1.0F);

                    this.mob.setCharging(false);
                    this.crossbowState = CrossbowState.UNCHARGED;
                }
            }
        }
    }

    private boolean friendlyInLineOfSight() {
        List<Entity> list = this.mob.getWorld().getOtherEntities(this.mob, this.mob.getBoundingBox().expand(5.0D));
        for (Entity guard : list) {
            if (guard != this.mob.getTarget()) {
                boolean isVillager = ((GuardEntity) this.mob).getOwner() == guard || guard.getType() == EntityType.VILLAGER || guard.getType() == GUARD_VILLAGER || guard.getType() == EntityType.IRON_GOLEM;
                if (isVillager) {
                    Vec3d direction = this.mob.getRotationVector();
                    Vec3d toEntity = guard.getPos().relativize(this.mob.getPos()).normalize();
                    toEntity = new Vec3d(toEntity.x, toEntity.y, toEntity.z);
                    if (toEntity.dotProduct(direction) < 1.0D && this.mob.canSee(guard) && guard.distanceTo(this.mob) <= 4.0D)
                        return true;
                }
            }
        }
        return false;
    }

    public boolean findPosition() {
        Vec3d vector3d = this.getPosition();
        if (vector3d == null) {
            return false;
        } else {
            this.wantedX = vector3d.x;
            this.wantedY = vector3d.y;
            this.wantedZ = vector3d.z;
            return true;
        }
    }

    @Nullable
    protected Vec3d getPosition() {
        if (this.isValidTarget())
            return NoPenaltyTargeting.findFrom(this.mob, 16, 7, this.mob.getTarget().getPos());
        else
            return NoPenaltyTargeting.find(this.mob, 16, 7);
    }

    private boolean canRun() {
        return this.crossbowState == CrossbowState.UNCHARGED;
    }

    public enum CrossbowState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK,
        FIND_NEW_POSITION
    }
}