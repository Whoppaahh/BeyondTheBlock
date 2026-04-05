package net.ryan.beyond_the_block.feature.combat.handlers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public final class TemporalSliceHandler {

    private static final List<PendingTemporalSlice> PENDING_HITS = new ArrayList<>();

    private TemporalSliceHandler() {
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerWorld world : server.getWorlds()) {
                tickWorld(world);
            }
        });
    }

    public static void queue(ServerPlayerEntity attacker, LivingEntity target, float actualDamageDealt, int level) {
        if (!(target.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }

        if (actualDamageDealt <= 0.0F) {
            return;
        }

        float multiplier = switch (level) {
            case 1 -> 0.25F;
            case 2 -> 0.40F;
            default -> 0.60F;
        };

        float delayedDamage = actualDamageDealt * multiplier;
        long releaseTick = serverWorld.getTime() + 40L; // 2 seconds

        PENDING_HITS.add(new PendingTemporalSlice(
                attacker.getUuid(),
                target.getUuid(),
                target.getWorld().getRegistryKey(),
                delayedDamage,
                releaseTick
        ));

        serverWorld.spawnParticles(
                ParticleTypes.ENCHANT,
                target.getX(),
                target.getBodyY(0.5),
                target.getZ(),
                16,
                0.4,
                0.5,
                0.4,
                0.02
        );

        serverWorld.playSound(
                null,
                target.getBlockPos(),
                SoundEvents.ENTITY_ILLUSIONER_PREPARE_MIRROR,
                SoundCategory.PLAYERS,
                0.5F,
                1.2F
        );
    }

    private static void tickWorld(ServerWorld world) {
        long now = world.getTime();
        Iterator<PendingTemporalSlice> iterator = PENDING_HITS.iterator();

        while (iterator.hasNext()) {
            PendingTemporalSlice pending = iterator.next();

            if (!pending.worldKey().equals(world.getRegistryKey())) {
                continue;
            }

            Entity entity = world.getEntity(pending.targetUuid());
            if (!(entity instanceof LivingEntity target) || !target.isAlive()) {
                iterator.remove();
                continue;
            }

            if (pending.releaseTick() > now) {
                spawnHoldingParticles(world, target);
                continue;
            }

            BeyondTheBlock.LOGGER.warn("[TemporalSlice] Releasing delayed hit: damage=" + pending.damage()
                    + ", target=" + target.getName().getString()
                    + ", now=" + now
                    + ", releaseTick=" + pending.releaseTick());

            boolean damaged = target.damage(DamageSource.GENERIC, pending.damage());

            BeyondTheBlock.LOGGER.warn("[TemporalSlice] damage() returned " + damaged
                    + ", target health now=" + target.getHealth());

            if (damaged) {
                world.spawnParticles(
                        ParticleTypes.CRIT,
                        target.getX(),
                        target.getBodyY(0.5),
                        target.getZ(),
                        20,
                        0.35,
                        0.45,
                        0.35,
                        0.15
                );

                world.spawnParticles(
                        ParticleTypes.SWEEP_ATTACK,
                        target.getX(),
                        target.getBodyY(0.5),
                        target.getZ(),
                        3,
                        0.15,
                        0.15,
                        0.15,
                        0.0
                );

                world.playSound(
                        null,
                        target.getBlockPos(),
                        SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,
                        SoundCategory.PLAYERS,
                        0.8F,
                        0.8F
                );
            }

            iterator.remove();
        }
    }

    private static void spawnHoldingParticles(ServerWorld world, LivingEntity target) {
        Vec3d center = target.getPos().add(0.0, target.getHeight() * 0.6, 0.0);

        world.spawnParticles(
                ParticleTypes.ENCHANT,
                center.x,
                center.y,
                center.z,
                3,
                0.2,
                0.25,
                0.2,
                0.01
        );
    }

    private record PendingTemporalSlice(
            UUID attackerUuid,
            UUID targetUuid,
            RegistryKey<World> worldKey,
            float damage,
            long releaseTick
    ) {}
}