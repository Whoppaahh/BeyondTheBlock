package net.ryan.beyond_the_block.utils.helpers;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class NightstrideHelper {

    private static final Map<UUID, Long> invisibilityCooldownUntil = new HashMap<>();

    private static final int CHECK_INTERVAL = 10;
    private static final int EFFECT_DURATION = 40;
    private static final int EFFECT_REFRESH_THRESHOLD = 20;

    private NightstrideHelper() {
    }

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(NightstrideHelper::onEndWorldTick);

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (!(entity instanceof ServerPlayerEntity player)) {
                return;
            }

            int level = getNightstrideLevel(player);

            if (level < 3) {
                return;
            }

            long now = world.getTime();
            long cooldownUntil = invisibilityCooldownUntil.getOrDefault(
                    player.getUuid(),
                    0L
            );

            if (now < cooldownUntil) {
                return;
            }

            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.INVISIBILITY,
                    100,
                    0,
                    false,
                    false,
                    true
            ));

            invisibilityCooldownUntil.put(player.getUuid(), now + 100L);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            long now = server.getOverworld().getTime();

            invisibilityCooldownUntil.entrySet().removeIf(
                    entry -> entry.getValue() <= now
            );
        });
    }

    private static void onEndWorldTick(ServerWorld world) {

        // Only perform Nightstride checks every 10 ticks.
        if (world.getTime() % CHECK_INTERVAL != 0) {
            return;
        }

        for (ServerPlayerEntity player : world.getPlayers()) {
            tickPlayer(player);
        }
    }

    private static void tickPlayer(ServerPlayerEntity player) {

        int level = getNightstrideLevel(player);

        if (level <= 0) {
            return;
        }

        ServerWorld world = player.getWorld();

        // Night automatically activates Nightstride.
        // Avoid unnecessary light checks during nighttime.
        boolean active = world.isNight();

        if (!active) {
            BlockPos pos = player.getBlockPos();

            active = world.getLightLevel(LightType.BLOCK, pos) <= 7;

            if (!active) {
                active = world.getLightLevel(LightType.SKY, pos) <= 7;
            }
        }

        if (active) {
            applyDarknessBonuses(player, level);
        }
    }

    private static void applyDarknessBonuses(ServerPlayerEntity player, int level) {

        // Speed scales with enchantment level.
        ensureEffect(
                player,
                StatusEffects.SPEED,
                Math.max(0, level - 1)
        );

        // Jump Boost requires Nightstride level 2 or higher.
        if (level >= 2) {
            ensureEffect(
                    player,
                    StatusEffects.JUMP_BOOST,
                    Math.max(0, level - 2)
            );
        }
    }

    private static void ensureEffect(
            ServerPlayerEntity player,
            StatusEffect effect,
            int amplifier
    ) {
        StatusEffectInstance current = player.getStatusEffect(effect);

        // Apply the effect if it is missing.
        if (current == null) {
            addEffect(player, effect, amplifier);
            return;
        }

        // Don't overwrite a stronger effect from another source.
        if (current.getAmplifier() > amplifier) {
            return;
        }

        // Refresh only when the current effect is about to expire.
        if (current.getAmplifier() == amplifier
                && current.getDuration() > EFFECT_REFRESH_THRESHOLD) {
            return;
        }

        // The effect is weaker or nearly expired.
        addEffect(player, effect, amplifier);
    }

    private static void addEffect(
            ServerPlayerEntity player,
            StatusEffect effect,
            int amplifier
    ) {
        player.addStatusEffect(new StatusEffectInstance(
                effect,
                EFFECT_DURATION,
                amplifier,
                false,
                false,
                false
        ));
    }

    private static int getNightstrideLevel(ServerPlayerEntity player) {
        return EnchantmentHelper.getLevel(
                ModEnchantments.NIGHT_STRIDE,
                player.getEquippedStack(EquipmentSlot.LEGS)
        );
    }
}