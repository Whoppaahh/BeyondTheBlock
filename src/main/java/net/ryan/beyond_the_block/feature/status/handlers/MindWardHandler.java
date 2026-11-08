package net.ryan.beyond_the_block.feature.status.handlers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import net.ryan.beyond_the_block.utils.accessors.EnchantmentCacheAccessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class MindWardHandler {

    private static final int TICK_INTERVAL = 20;

    private MindWardHandler() {
    }

    public static void onTick(LivingEntity entity) {
        if (entity.age % TICK_INTERVAL != 0) {
            return;
        }

        if (!(entity instanceof PlayerEntity player)) {
            return;
        }

        int level;
        if (player instanceof EnchantmentCacheAccessor cache) {
            level = cache.btb$getCachedLevel(ModEnchantments.MIND_WARD);
        } else {
            level = getEnchantmentLevelForPlayer(player, ModEnchantments.MIND_WARD);
        }

        if (level <= 0) {
            return;
        }

        reduceNegativeEffectDurations(player, level);
    }

    private static int getEnchantmentLevelForPlayer(PlayerEntity player, Enchantment enchantment) {
        for (ItemStack armorPiece : player.getArmorItems()) {
            int level = EnchantmentHelper.getLevel(enchantment, armorPiece);
            if (level > 0) {
                return level;
            }
        }
        return 0;
    }

    private static void reduceNegativeEffectDurations(PlayerEntity player, int level) {
        List<StatusEffectInstance> effectsToAdd = new ArrayList<>();
        List<StatusEffectInstance> currentEffects = new ArrayList<>(player.getStatusEffects());

        boolean changed = false;
        for (StatusEffectInstance effect : currentEffects) {
            if (effect.getEffectType().isBeneficial()) {
                continue;
            }

            int reduction = 20 * level;
            int newDuration = effect.getDuration() - reduction;

            player.removeStatusEffect(effect.getEffectType());
            changed = true;

            if (newDuration > 0) {
                effectsToAdd.add(new StatusEffectInstance(
                        effect.getEffectType(),
                        newDuration,
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.shouldShowParticles(),
                        effect.shouldShowIcon()
                ));
            }
        }

        if (changed) {
            for (StatusEffectInstance effect : effectsToAdd) {
                player.addStatusEffect(effect);
            }
        }
    }
}