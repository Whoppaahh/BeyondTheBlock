package net.ryan.beyond_the_block.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class WitherZombie extends SkeletonEntity implements RangedAttackMob {

    public final boolean usesBow;
    private boolean triedMountSpawn = false;


    public WitherZombie(EntityType<? extends SkeletonEntity> type, World world) {
        super(type, world);
        this.usesBow = new Random().nextBoolean(); // 50% chance
    }

    @Override
    protected void initGoals() {
        super.initGoals(); // Inherit skeleton goals
    }

    @Override
    public void tick() {
        super.tick();
        // Mount setup (runs only once, server-side, after entity exists client-side)
        if (!this.world.isClient && !triedMountSpawn) {
            triedMountSpawn = true;

            if (this.random.nextFloat() < 0.2F) {
                ServerWorld serverWorld = (ServerWorld) this.world;
                ZombieHorseEntity horse = this.random.nextFloat() < 0.5F
                        ? ModEntities.WITHER_SKELETON_HORSE.create(serverWorld)
                        : ModEntities.WITHER_ZOMBIE_HORSE.create(serverWorld);

                if (horse != null) {
                    horse.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                    horse.setTame(true);
                    horse.setPersistent();
                    serverWorld.spawnEntity(horse);

                    // Safe to call immediately here — both are now tracked
                    this.startRiding(horse, true);
                }
            }
        }

        if (this.world.getDifficulty() == Difficulty.HARD) {
            if (!this.world.isClient && this.age % 60 == 0) {
                List<PlayerEntity> nearby = this.world.getEntitiesByClass(PlayerEntity.class, this.getBoundingBox().expand(2.0), e -> true);
                for (PlayerEntity player : nearby) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 60, 0)); // 3 seconds
                }
            }
        }
        if (!this.world.isClient && this.hasVehicle() && this.getVehicle() instanceof LivingEntity vehicle) {
            this.setYaw(vehicle.getYaw());
            this.prevYaw = vehicle.prevYaw;
            this.bodyYaw = vehicle.bodyYaw;
            this.headYaw = vehicle.headYaw;
        }
    }

    @Override
    public boolean isAttacking() {
        return usesBow && this.isUsingItem() || super.isAttacking();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.getRandom().nextFloat() < 0.5F ? SoundEvents.ENTITY_ZOMBIE_AMBIENT : SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    @Override
    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty localDifficulty) {
        if (usesBow) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        } else {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        }
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.05F); // Rare weapon drop
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty,
                                           SpawnReason spawnReason, @Nullable EntityData entityData,
                                           @Nullable NbtCompound entityNbt) {
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }


    public static DefaultAttributeContainer.Builder createWitherZombieAttributes() {
        return ZombieEntity.createZombieAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D)
                .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, 0.2D);
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        this.setAttacking(true);

        //  EmeraldEmpire.LOGGER.info("Attacking: " + this.isAttacking());
        ArrowEntity arrow = new ArrowEntity(this.world, this);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - arrow.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        arrow.setVelocity(d, e + g * 0.2, f, 1.6F, 14 - this.world.getDifficulty().getId() * 4);
        arrow.addEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 0));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(arrow);
    }
}
