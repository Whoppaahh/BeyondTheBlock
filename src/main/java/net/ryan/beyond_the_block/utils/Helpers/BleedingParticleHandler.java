package net.ryan.beyond_the_block.utils.Helpers;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.config.Configs;
import net.ryan.beyond_the_block.effect.ModEffects;

import java.util.*;

public class BleedingParticleHandler {

    private static final float BLOOD_HEALTH_FRACTION = Configs.client().visuals.blood.healthFraction;
    private static final int BLOOD_INTERVAL = 10;

    private final Map<UUID, Integer> cooldowns = new HashMap<>();

    private static final BlockStateParticleEffect BLOOD_PARTICLE =
            new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState());

    private static final net.minecraft.entity.effect.StatusEffect BLEED_EFFECT = ModEffects.BLEED;

    public void onWorldTick(ServerWorld world) {
        if (!Configs.client().visuals.blood.enabled) return;

        cooldowns.replaceAll((uuid, cd) -> Math.max(cd - 1, 0));

        float radius = 32.0f;
        for (PlayerEntity player : world.getPlayers()) {
            Box searchBox = new Box(
                    player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                    player.getX() + radius, player.getY() + radius, player.getZ() + radius
            );

            List<LivingEntity> nearbyEntities = world.getEntitiesByClass(LivingEntity.class, searchBox, e -> true);
            Set<UUID> processedEntities = new HashSet<>();

            for (LivingEntity living : nearbyEntities) {
                if (living.isDead()) continue;

                UUID id = living.getUuid();
                boolean lowHealth = living.getHealth() / living.getMaxHealth() <= BLOOD_HEALTH_FRACTION;
                StatusEffectInstance bleedInstance = living.getStatusEffect(BLEED_EFFECT);
                boolean hasBleedEffect = bleedInstance != null && bleedInstance.getDuration() > 0;

                if (!lowHealth && !hasBleedEffect) continue;

                processedEntities.add(id);
                if (cooldowns.getOrDefault(id, 0) > 0) continue;

                Vec3d pos = living.getPos();
                Vec3d velocity = living.getVelocity();

                if (velocity.lengthSquared() > 0.0025) {
                    // 👣 Trail
                    Vec3d trailPos = pos.subtract(velocity.normalize().multiply(0.3));
                    world.spawnParticles(BLOOD_PARTICLE, trailPos.x, trailPos.y + 0.1, trailPos.z, world.random.nextBetween(1, 5), 0.01, 0.0, 0.01, 0.1);
                }

                if (velocity.y < -0.1) {
                    // 💧 Dripping
                    for (int i = 0; i < 2; i++) {
                        double dx = (world.random.nextDouble() - 0.5) * 0.2;
                        double dz = (world.random.nextDouble() - 0.5) * 0.2;
                        world.spawnParticles(BLOOD_PARTICLE, pos.x + dx, pos.y + 0.5 - i * 0.2, pos.z + dz, 1, 0.0, -0.05, 0.0, 0.02);
                    }
                }

                if (velocity.lengthSquared() <= 0.0025) {
                    // 🩸 Pooling
                    if (world.getBlockState(living.getBlockPos().down()).isOpaque()) {
                        int particles = 4 + world.random.nextInt(4);
                        double spread = 0.2 + world.random.nextDouble() * 0.3;

                        for (int i = 0; i < particles; i++) {
                            double dx = (world.random.nextDouble() - 0.5) * spread;
                            double dz = (world.random.nextDouble() - 0.5) * spread;
                            world.spawnParticles(BLOOD_PARTICLE, pos.x + dx, pos.y + 0.05, pos.z + dz, 1, 0.01, 0.0, 0.01, 0.05);
                        }
                    } else {
                        // Fallback
                        world.spawnParticles(BLOOD_PARTICLE, pos.x, pos.y + world.random.nextBetween(0, 1), pos.z, 2, 0.0, 0.0, 0.0, 0.3);
                    }
                }

                cooldowns.put(id, BLOOD_INTERVAL);
            }

            cooldowns.keySet().retainAll(processedEntities);
        }
    }

    // 💥 Public helper for hit splatter
    public static void spawnBloodSplatter(ServerWorld world, Vec3d pos, Vec3d attackDir, float damage) {
        if (!Configs.client().visuals.blood.enabled) return;
        Vec3d norm = attackDir.normalize();
        double spread = 0.2 + (damage * 0.02); // more scatter with more damage
        int particles = 8 + (int)(damage * 1.2); // more particles for higher damage
        double upwardBase = 0.05 + damage * 0.005; // higher arc if more forceful hit

        for (int i = 0; i < particles; i++) {
            double forward = 0.2 + world.random.nextDouble() * 0.4;
            double sideways = (world.random.nextDouble() - 0.5) * spread;
            double upward = upwardBase + world.random.nextDouble() * 0.05;

            Vec3d offset = norm.multiply(forward)
                    .add(norm.crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(sideways));

            world.spawnParticles(BLOOD_PARTICLE,
                    pos.x + offset.x,
                    pos.y + 0.6 + upward,
                    pos.z + offset.z,
                    1, 0, 0, 0, 0.05);
        }
    }


}
