package net.ryan.beyond_the_block.utils.helpers;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.client.util.BloodTargetHelper;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.effect.ModEffects;

import java.util.*;

public class BleedingParticleHandler {

    public static final BleedingParticleHandler INSTANCE = new BleedingParticleHandler();
    private static final int BLOOD_INTERVAL = 10;

    private final Map<UUID, Integer> cooldowns = new HashMap<>();

    private static final BlockStateParticleEffect BLOOD_PARTICLE =
            new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState());

    private static final net.minecraft.entity.effect.StatusEffect BLEED_EFFECT = ModEffects.BLEED;

    public void onWorldTick(ServerWorld world) {
        if (!Configs.client().visuals.blood.enabled) return;

        cooldowns.replaceAll((uuid, cd) -> Math.max(cd - 1, 0));

        float radius = 32.0f;
        Set<UUID> allProcessedEntities = new HashSet<>();

        for (PlayerEntity player : world.getPlayers()) {
            Box searchBox = new Box(
                    player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                    player.getX() + radius, player.getY() + radius, player.getZ() + radius
            );

            List<LivingEntity> nearbyEntities = world.getEntitiesByClass(LivingEntity.class, searchBox, e -> true);

            for (LivingEntity living : nearbyEntities) {
                if (living.isDead()) continue;
                if (!BloodTargetHelper.shouldShowBlood(living)) continue;

                UUID id = living.getUuid();
                allProcessedEntities.add(id);

                boolean lowHealth = BloodTargetHelper.passesHealthThreshold(living, Configs.client().visuals.blood.healthPercentThreshold);

                StatusEffectInstance bleedInstance = living.getStatusEffect(BLEED_EFFECT);
                boolean hasBleedEffect = bleedInstance != null && bleedInstance.getDuration() > 0;

                if (!lowHealth && !hasBleedEffect) continue;
                if (cooldowns.getOrDefault(id, 0) > 0) continue;

                Vec3d pos = living.getPos();
                Vec3d velocity = living.getVelocity();

                if (velocity.lengthSquared() > 0.0025) {
                    Vec3d trailPos = pos.subtract(velocity.normalize().multiply(0.3));
                    world.spawnParticles(
                            BLOOD_PARTICLE,
                            trailPos.x,
                            trailPos.y + 0.1,
                            trailPos.z,
                            world.random.nextBetween(1, 5),
                            0.01,
                            0.0,
                            0.01,
                            0.1
                    );
                }

                if (velocity.y < -0.1) {
                    for (int i = 0; i < 2; i++) {
                        double dx = (world.random.nextDouble() - 0.5) * 0.2;
                        double dz = (world.random.nextDouble() - 0.5) * 0.2;
                        world.spawnParticles(
                                BLOOD_PARTICLE,
                                pos.x + dx,
                                pos.y + 0.5 - i * 0.2,
                                pos.z + dz,
                                1,
                                0.0,
                                -0.05,
                                0.0,
                                0.02
                        );
                    }
                }

                if (velocity.lengthSquared() <= 0.0025) {
                    if (world.getBlockState(living.getBlockPos().down()).isOpaque()) {
                        int particles = 4 + world.random.nextInt(4);
                        double spread = 0.2 + world.random.nextDouble() * 0.3;

                        for (int i = 0; i < particles; i++) {
                            double dx = (world.random.nextDouble() - 0.5) * spread;
                            double dz = (world.random.nextDouble() - 0.5) * spread;
                            world.spawnParticles(
                                    BLOOD_PARTICLE,
                                    pos.x + dx,
                                    pos.y + 0.05,
                                    pos.z + dz,
                                    1,
                                    0.01,
                                    0.0,
                                    0.01,
                                    0.05
                            );
                        }
                    } else {
                        world.spawnParticles(
                                BLOOD_PARTICLE,
                                pos.x,
                                pos.y + world.random.nextBetween(0, 1),
                                pos.z,
                                2,
                                0.0,
                                0.0,
                                0.0,
                                0.3
                        );
                    }
                }

                cooldowns.put(id, BLOOD_INTERVAL);
            }
        }

        cooldowns.keySet().retainAll(allProcessedEntities);
    }

    public static void spawnBloodSplatter(ServerWorld world, LivingEntity target, Vec3d attackDir, float damage) {
        if (!Configs.client().visuals.blood.enabled) return;
        if (!BloodTargetHelper.shouldShowBlood(target)) return;

        Vec3d pos = target.getPos();
        Vec3d norm = attackDir.lengthSquared() > 1.0E-6 ? attackDir.normalize() : new Vec3d(0, 0, 0);

        double spread = 0.2 + (damage * 0.02);
        int particles = 8 + (int) (damage * 1.2);
        double upwardBase = 0.05 + damage * 0.005;

        for (int i = 0; i < particles; i++) {
            double forward = 0.2 + world.random.nextDouble() * 0.4;
            double sideways = (world.random.nextDouble() - 0.5) * spread;
            double upward = upwardBase + world.random.nextDouble() * 0.05;

            Vec3d side = norm.lengthSquared() > 1.0E-6
                    ? norm.crossProduct(new Vec3d(0, 1, 0))
                    : Vec3d.ZERO;

            if (side.lengthSquared() > 1.0E-6) {
                side = side.normalize();
            }

            Vec3d offset = norm.multiply(forward).add(side.multiply(sideways));

            world.spawnParticles(
                    BLOOD_PARTICLE,
                    pos.x + offset.x,
                    pos.y + 0.6 + upward,
                    pos.z + offset.z,
                    1,
                    0,
                    0,
                    0,
                    0.05
            );
        }
    }
}