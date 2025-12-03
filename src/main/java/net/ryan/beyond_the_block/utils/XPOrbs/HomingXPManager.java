package net.ryan.beyond_the_block.utils.XPOrbs;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class HomingXPManager {
    static final int MAX_XP = 16383;
    private static final int TICK_INTERVAL = 5;
    private static final double MERGE_RADIUS = 5;
    private static final double HOMING_RADIUS = 64;

    public static void tick(ServerWorld world) {
        if ((world.getServer().getTicks() % TICK_INTERVAL) != 0) return;

        List<ExperienceOrbEntity> allOrbs = new ArrayList<>();
        for (PlayerEntity player : world.getPlayers()) {
            Box area = player.getBoundingBox().expand(HOMING_RADIUS);
            allOrbs.addAll(world.getEntitiesByClass(ExperienceOrbEntity.class, area, orb -> orb.age >= 10));
        }

        Set<ExperienceOrbEntity> alreadyMerged = new HashSet<>();

        for (ExperienceOrbEntity orb : allOrbs) {
            if (orb.isRemoved() || alreadyMerged.contains(orb)) continue;

            PlayerEntity ownerPlayer = ((OrbExtension) orb).beyond_the_block$getTargetPlayer();
            UUID owner = ownerPlayer != null ? ownerPlayer.getUuid() : null;

            // ✅ Skip merging if this orb is already full
            if (orb.getExperienceAmount() >= MAX_XP) {
                handleHoming(world, orb, owner, ownerPlayer);
                continue;
            }

            // 🔍 Find merge candidates that are not full
            List<ExperienceOrbEntity> nearby = world.getEntitiesByClass(
                    ExperienceOrbEntity.class,
                    orb.getBoundingBox().expand(MERGE_RADIUS),
                    other -> {
                        if (other == orb || other.isRemoved() || alreadyMerged.contains(other)) return false;

                        PlayerEntity otherOwner = ((OrbExtension) other).beyond_the_block$getTargetPlayer();
                        UUID otherUuid = otherOwner != null ? otherOwner.getUuid() : null;

                        return Objects.equals(otherUuid, owner) && other.getExperienceAmount() < MAX_XP;
                    }
            );

            int totalXp = orb.getExperienceAmount();
            for (ExperienceOrbEntity other : nearby) {
                totalXp += other.getExperienceAmount();
                alreadyMerged.add(other);
                other.discard();
            }

            if (!nearby.isEmpty()) {
                // ✨ Particle position (average center of merge)
                double px = orb.getX();
                double py = orb.getY();
                double pz = orb.getZ();

                // 💥 Spawn particle effect
                world.spawnParticles(ParticleTypes.PORTAL, px, py + 0.5, pz, 8, 0.2, 0.2, 0.2, 0.1);

                if (totalXp > MAX_XP) {
                    ((OrbExtension) orb).beyond_the_block$setAmount(MAX_XP);

                    int remaining = totalXp - MAX_XP;

                    ExperienceOrbEntity leftover = new ExperienceOrbEntity(world, orb.getX(), orb.getY(), orb.getZ(), remaining);
                    ((OrbExtension) leftover).beyond_the_block$setTargetPlayer(ownerPlayer);

                    // ✅ Mark newly spawned overflow orb to prevent it from merging again this tick
                    alreadyMerged.add(leftover);
                    world.spawnEntity(leftover);
                } else {
                    ((OrbExtension) orb).beyond_the_block$setAmount(totalXp);
                }
            }

            // Homing after merging
            handleHoming(world, orb, owner, ownerPlayer);
        }
    }

    private static void handleHoming(ServerWorld world, ExperienceOrbEntity orb, UUID owner, PlayerEntity ownerPlayer) {
        PlayerEntity target = getValidTarget(world, orb, owner);
        if (target != null) {
            Vec3d toPlayer = target.getPos().add(0, 1, 0).subtract(orb.getPos());
            double distance = toPlayer.length();
            if (distance > 0.1) {
                Vec3d direction = toPlayer.normalize();
                double acceleration = 0.05 + Math.min(distance * 0.03, 0.3);
                Vec3d newVelocity = orb.getVelocity().add(direction.multiply(acceleration)).multiply(0.6);
                orb.setVelocity(newVelocity);
            }
        } else {
            orb.discard();
        }
    }

    private static PlayerEntity getValidTarget(ServerWorld world, ExperienceOrbEntity orb, UUID owner) {
        if (owner != null) {
            PlayerEntity player = world.getPlayerByUuid(owner);
            if (player != null && player.isAlive() && !player.isSpectator() &&
                    orb.squaredDistanceTo(player) <= HOMING_RADIUS * HOMING_RADIUS) {
                return player;
            }
        }
        return world.getClosestPlayer(orb, HOMING_RADIUS);
    }
}
