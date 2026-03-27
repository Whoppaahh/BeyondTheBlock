package net.ryan.beyond_the_block.content.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitherZombie extends SkeletonEntity implements RangedAttackMob {

    public final boolean usesBow;



    public WitherZombie(EntityType<? extends SkeletonEntity> type, World world) {
        super(type, world);
        this.usesBow = this.random.nextBoolean();
    }


    /* ------------------------------------------------------------ */
    /* Initialization & Mount Setup                                 */
    /* ------------------------------------------------------------ */

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty,
                                           SpawnReason spawnReason, @Nullable EntityData entityData,
                                           @Nullable NbtCompound entityNbt) {

        //        if (world instanceof ServerWorld serverWorld && this.random.nextFloat() < 0.2F) {
//            ZombieHorseEntity horse = this.random.nextFloat() < 0.5F
//                    ? ModEntities.WITHER_SKELETON_HORSE.create(serverWorld)
//                    : ModEntities.WITHER_ZOMBIE_HORSE.create(serverWorld);
//
//            if (horse != null) {
//                horse.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
//                horse.setTame(true);
//                horse.setPersistent();
//                serverWorld.spawnEntity(horse);
//
//
//                // Defer mounting by one tick (IMPORTANT)
//                serverWorld.getServer().execute(() -> {
//                    if (this.isAlive() && horse.isAlive()) {
//                        this.startRiding(horse, true);
//                    }
//                });
//            }
//        }

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    /* ------------------------------------------------------------ */
    /* Tick Logic                                                   */
    /* ------------------------------------------------------------ */

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isClient && this.world.getDifficulty() == Difficulty.HARD && this.age % 60 == 0) {
            List<PlayerEntity> players = this.world.getEntitiesByClass(
                    PlayerEntity.class,
                    this.getBoundingBox().expand(2.0),
                    p -> true
            );

            for (PlayerEntity player : players) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 60, 0));
            }
        }

        if (!this.world.isClient && this.hasVehicle() && this.getVehicle() instanceof LivingEntity vehicle) {
            this.setYaw(vehicle.getYaw());
            this.prevYaw = vehicle.prevYaw;
            this.bodyYaw = vehicle.bodyYaw;
            this.headYaw = vehicle.headYaw;
        }
    }

    /* ------------------------------------------------------------ */
    /* Combat                                                       */
    /* ------------------------------------------------------------ */

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        this.setAttacking(true);

        ArrowEntity arrow = new ArrowEntity(this.world, this);
        double dx = target.getX() - this.getX();
        double dy = target.getBodyY(0.3333333333333333) - arrow.getY();
        double dz = target.getZ() - this.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);

        arrow.setVelocity(dx, dy + dist * 0.2, dz, 1.6F,
                14 - this.world.getDifficulty().getId() * 4);
        arrow.addEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 0));

        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F,
                1.0F / (this.random.nextFloat() * 0.4F + 0.8F));

        this.world.spawnEntity(arrow);
    }

    /* ------------------------------------------------------------ */
    /* Equipment & Attributes                                       */
    /* ------------------------------------------------------------ */

    @Override
    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty difficulty) {
        this.equipStack(EquipmentSlot.MAINHAND,
                usesBow ? new ItemStack(Items.BOW) : new ItemStack(Items.STONE_SWORD));
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.05F);
    }

    public static DefaultAttributeContainer.Builder createWitherZombieAttributes() {
        return ZombieEntity.createZombieAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D)
                .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, 0.2D);
    }

    /* ------------------------------------------------------------ */
    /* Sounds                                                       */
    /* ------------------------------------------------------------ */

    @Override protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ENTITY_ZOMBIE_HURT; }
    @Override protected SoundEvent getAmbientSound() {
        return this.random.nextFloat() < 0.5F
                ? SoundEvents.ENTITY_ZOMBIE_AMBIENT
                : SoundEvents.ENTITY_SKELETON_AMBIENT;
    }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_ZOMBIE_DEATH; }

    /* ------------------------------------------------------------ */
    /* Cleanup                                                      */
    /* ------------------------------------------------------------ */

    @Override
    public void remove(RemovalReason reason) {
        if (!this.world.isClient) {
            this.stopRiding();
        }
        super.remove(reason);
    }

    @Override
    public boolean isAttacking() {
        return usesBow && this.isUsingItem() || super.isAttacking();
    }
}
