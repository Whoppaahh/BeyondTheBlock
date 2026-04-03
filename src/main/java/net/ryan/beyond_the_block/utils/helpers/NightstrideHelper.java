package net.ryan.beyond_the_block.utils.helpers;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class NightstrideHelper {

    private static final Map<UUID, Long> invisibilityCooldownUntil = new HashMap<>();

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
            long cooldownUntil = invisibilityCooldownUntil.getOrDefault(player.getUuid(), 0L);
            if (now < cooldownUntil) {
                return;
            }

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 100, 0, false, false, true));
            invisibilityCooldownUntil.put(player.getUuid(), now + 100L);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            long now = server.getOverworld().getTime();
            invisibilityCooldownUntil.entrySet().removeIf(entry -> entry.getValue() <= now);
        });
    }

    private static void onEndWorldTick(ServerWorld world) {
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
        BlockPos pos = player.getBlockPos();

        boolean isDark = world.getLightLevel(LightType.BLOCK, pos) <= 7
                || world.getLightLevel(LightType.SKY, pos) <= 7;
        boolean isNight = world.isNight();

        if (isDark || isNight) {
            applyDarknessBonuses(player, level);
        }
    }

    private static void applyDarknessBonuses(ServerPlayerEntity player, int level) {
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SPEED,
                40,
                Math.max(0, level - 1),
                false,
                false,
                false
        ));

        if (level >= 2) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.JUMP_BOOST,
                    40,
                    Math.max(0, level - 2),
                    false,
                    false,
                    false
            ));
        }
    }

    private static int getNightstrideLevel(ServerPlayerEntity player) {
        return EnchantmentHelper.getLevel(
                ModEnchantments.NIGHT_STRIDE,
                player.getEquippedStack(EquipmentSlot.LEGS)
        );
    }
}