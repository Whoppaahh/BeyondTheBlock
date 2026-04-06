package net.ryan.beyond_the_block.feature.status.handlers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;

import java.util.ArrayList;
import java.util.List;

public final class MindWardHandler {

    private static final int TICK_INTERVAL = 20;

    private MindWardHandler() {
    }

    public static void onTick(LivingEntity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }

        int level = getEnchantmentLevelForPlayer(player, ModEnchantments.MIND_WARD);
        if (level <= 0) {
            return;
        }

        if (player.age % TICK_INTERVAL != 0) {
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
        List<StatusEffectInstance> effects = new ArrayList<>(player.getStatusEffects());

        for (StatusEffectInstance effect : effects) {
            if (effect.getEffectType().isBeneficial()) {
                continue;
            }

            int newDuration = effect.getDuration() - (20 * level);

            player.removeStatusEffect(effect.getEffectType());

            if (newDuration > 0) {
                player.addStatusEffect(new StatusEffectInstance(
                        effect.getEffectType(),
                        newDuration,
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.shouldShowParticles(),
                        effect.shouldShowIcon()
                ));
            }
        }
    }
}