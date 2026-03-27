package net.ryan.beyond_the_block.feature.xp_orbs;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.*;

/**
 * Optimized XP orb manager.
 *
 * Keeps existing behavior:
 * - ticks every few server ticks
 * - merges owner-compatible orbs
 * - caps merged orbs at MAX_XP and spawns overflow
 * - homes toward valid target
 * - discards orb if no valid target exists
 *
 * Main optimization:
 * - collect candidate orbs once
 * - bucket by owner once
 * - merge in-memory per owner group
 * - avoid per-orb world entity queries for merge candidates
 */
public final class HomingXPManager {
    private static final int MAX_XP = 16383;
    private static final int TICK_INTERVAL = 5;
    private static final int MAX_ORBS_PER_TICK = 200;

    private static final double MERGE_RADIUS = 5.0;
    private static final double MERGE_RADIUS_SQUARED = MERGE_RADIUS * MERGE_RADIUS;
    private static final double HOMING_RADIUS = 64.0;
    private static final double HOMING_RADIUS_SQUARED = HOMING_RADIUS * HOMING_RADIUS;

    private HomingXPManager() {}

    public static void tick(ServerWorld world) {
        if ((world.getServer().getTicks() % TICK_INTERVAL) != 0) {
            return;
        }

        if (world.getPlayers().isEmpty()) {
            return;
        }

        Set<ExperienceOrbEntity> allOrbs = collectCandidateOrbs(world);
        if (allOrbs.isEmpty()) {
            return;
        }

        Map<UUID, List<ExperienceOrbEntity>> byOwner = new HashMap<>();
        Map<ExperienceOrbEntity, UUID> orbOwners = new HashMap<>();
        Map<ExperienceOrbEntity, PlayerEntity> orbOwnerPlayers = new HashMap<>();

        bucketByOwner(allOrbs, byOwner, orbOwners, orbOwnerPlayers);

        Set<ExperienceOrbEntity> alreadyMerged = new HashSet<>();
        int processed = 0;

        // Merge pass
        for (List<ExperienceOrbEntity> group : byOwner.values()) {
            processed = mergeOwnerGroup(world, group, alreadyMerged, orbOwnerPlayers, processed);
            if (processed >= MAX_ORBS_PER_TICK) {
                break;
            }
        }

        // Homing pass
        processed = 0;
        for (ExperienceOrbEntity orb : allOrbs) {
            if (processed >= MAX_ORBS_PER_TICK) {
                break;
            }
            if (orb.isRemoved()) {
                continue;
            }

            UUID owner = orbOwners.get(orb);
            handleHoming(world, orb, owner);
            processed++;
        }
    }

    private static Set<ExperienceOrbEntity> collectCandidateOrbs(ServerWorld world) {
        Set<ExperienceOrbEntity> allOrbs = new HashSet<>();

        for (PlayerEntity player : world.getPlayers()) {
            if (!player.isAlive() || player.isSpectator()) {
                continue;
            }

            Box area = player.getBoundingBox().expand(HOMING_RADIUS);
            allOrbs.addAll(world.getEntitiesByClass(
                    ExperienceOrbEntity.class,
                    area,
                    orb -> !orb.isRemoved() && orb.age >= 10
            ));
        }

        return allOrbs;
    }

    private static void bucketByOwner(
            Set<ExperienceOrbEntity> allOrbs,
            Map<UUID, List<ExperienceOrbEntity>> byOwner,
            Map<ExperienceOrbEntity, UUID> orbOwners,
            Map<ExperienceOrbEntity, PlayerEntity> orbOwnerPlayers
    ) {
        for (ExperienceOrbEntity orb : allOrbs) {
            if (orb.isRemoved()) {
                continue;
            }

            PlayerEntity ownerPlayer = ((OrbExtension) orb).beyond_the_block$getTargetPlayer();
            UUID owner = ownerPlayer != null ? ownerPlayer.getUuid() : null;

            orbOwners.put(orb, owner);
            orbOwnerPlayers.put(orb, ownerPlayer);
            byOwner.computeIfAbsent(owner, ignored -> new ArrayList<>()).add(orb);
        }
    }

    private static int mergeOwnerGroup(
            ServerWorld world,
            List<ExperienceOrbEntity> group,
            Set<ExperienceOrbEntity> alreadyMerged,
            Map<ExperienceOrbEntity, PlayerEntity> orbOwnerPlayers,
            int processed
    ) {
        for (int i = 0; i < group.size(); i++) {
            if (processed >= MAX_ORBS_PER_TICK) {
                break;
            }

            ExperienceOrbEntity orb = group.get(i);
            if (orb.isRemoved() || alreadyMerged.contains(orb)) {
                continue;
            }

            // Skip merging if already full
            if (orb.getExperienceAmount() >= MAX_XP) {
                processed++;
                continue;
            }

            int totalXp = orb.getExperienceAmount();
            boolean mergedAny = false;

            for (int j = i + 1; j < group.size(); j++) {
                ExperienceOrbEntity other = group.get(j);

                if (other.isRemoved() || alreadyMerged.contains(other)) {
                    continue;
                }
                if (other.getExperienceAmount() >= MAX_XP) {
                    continue;
                }
                if (orb.squaredDistanceTo(other) > MERGE_RADIUS_SQUARED) {
                    continue;
                }

                totalXp += other.getExperienceAmount();
                alreadyMerged.add(other);
                other.discard();
                mergedAny = true;
            }

            if (mergedAny) {
                spawnMergeParticles(world, orb);
                applyMergedXp(world, orb, totalXp, orbOwnerPlayers.get(orb), alreadyMerged);
            }

            processed++;
        }

        return processed;
    }

    private static void applyMergedXp(
            ServerWorld world,
            ExperienceOrbEntity orb,
            int totalXp,
            PlayerEntity ownerPlayer,
            Set<ExperienceOrbEntity> alreadyMerged
    ) {
        if (totalXp > MAX_XP) {
            ((OrbExtension) orb).beyond_the_block$setAmount(MAX_XP);

            int remaining = totalXp - MAX_XP;
            ExperienceOrbEntity leftover = new ExperienceOrbEntity(
                    world,
                    orb.getX(),
                    orb.getY(),
                    orb.getZ(),
                    remaining
            );

            ((OrbExtension) leftover).beyond_the_block$setTargetPlayer(ownerPlayer);

            // Prevent immediate re-merge in the same tick
            alreadyMerged.add(leftover);
            world.spawnEntity(leftover);
        } else {
            ((OrbExtension) orb).beyond_the_block$setAmount(totalXp);
        }
    }

    private static void spawnMergeParticles(ServerWorld world, ExperienceOrbEntity orb) {
        world.spawnParticles(
                ParticleTypes.PORTAL,
                orb.getX(),
                orb.getY() + 0.5,
                orb.getZ(),
                8,
                0.2,
                0.2,
                0.2,
                0.1
        );
    }

    private static void handleHoming(ServerWorld world, ExperienceOrbEntity orb, UUID owner) {
        if (orb.isRemoved()) {
            return;
        }

        PlayerEntity target = getValidTarget(world, orb, owner);
        if (target != null) {
            Vec3d toPlayer = target.getPos().add(0.0, 1.0, 0.0).subtract(orb.getPos());
            double distance = toPlayer.length();

            if (distance > 0.1) {
                Vec3d direction = toPlayer.normalize();
                double acceleration = 0.05 + Math.min(distance * 0.03, 0.3);
                Vec3d newVelocity = orb.getVelocity()
                        .add(direction.multiply(acceleration))
                        .multiply(0.6);

                orb.setVelocity(newVelocity);
            }
        } else {
            // Intentional behavior in your mod
            orb.discard();
        }
    }

    private static PlayerEntity getValidTarget(ServerWorld world, ExperienceOrbEntity orb, UUID owner) {
        if (owner != null) {
            PlayerEntity player = world.getPlayerByUuid(owner);
            if (player != null
                    && player.isAlive()
                    && !player.isSpectator()
                    && orb.squaredDistanceTo(player) <= HOMING_RADIUS_SQUARED) {
                return player;
            }
        }

        return world.getClosestPlayer(orb, HOMING_RADIUS);
    }
}