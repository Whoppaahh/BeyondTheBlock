package net.ryan.beyond_the_block.feature.player.movement;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import net.ryan.beyond_the_block.utils.accessors.EnchantmentCacheAccessor;

public final class GracefulMovementHandler {
    private GracefulMovementHandler() {}

    public static Float getFallDamageOverride(PlayerEntity player, float fallDistance) {
        int level;
        if (player instanceof EnchantmentCacheAccessor cache) {
            level = cache.btb$getCachedLevel(ModEnchantments.GRACEFUL_MOVEMENT);
        } else {
            level = EnchantmentHelper.getLevel(
                    ModEnchantments.GRACEFUL_MOVEMENT,
                    player.getEquippedStack(EquipmentSlot.LEGS)
            );
        }
        
        if (level <= 0) return null;
        if (fallDistance <= 3.0f) return null;

        float reduced = fallDistance * 0.5f * (level == 1 ? 0.5f : 0.25f);
        return Math.max(0.0f, reduced);
    }

    public static void onTick(PlayerEntity player) {
        if (player.isOnGround()) return;
        if (player.age % 10 != 0) return;

        int level;
        if (player instanceof EnchantmentCacheAccessor cache) {
            level = cache.btb$getCachedLevel(ModEnchantments.GRACEFUL_MOVEMENT);
        } else {
            level = EnchantmentHelper.getLevel(
                    ModEnchantments.GRACEFUL_MOVEMENT,
                    player.getEquippedStack(EquipmentSlot.LEGS)
            );
        }

        if (level <= 0) return;

        StatusEffectInstance current = player.getStatusEffect(StatusEffects.SLOW_FALLING);
        if (current != null && current.getDuration() > 20) return;

        int duration = (level == 1) ? 40 : 60;
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, duration, 0, false, false, false));
    }
}