package net.ryan.beyond_the_block.feature.combat.handlers;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;

public final class LifeStealHandler {
    private LifeStealHandler() {}

    public static void afterDamage(LivingEntity victim, DamageSource source, float amount) {
        if (!(source.getAttacker() instanceof LivingEntity attacker)) return;

        ItemStack weapon = attacker.getMainHandStack();
        int level = EnchantmentHelper.getLevel(ModEnchantments.LIFE_STEAL, weapon);
        if (level <= 0) return;

        float healAmount = amount * 0.1f * level;
        if (healAmount > 0.0f) {
            attacker.heal(healAmount);
        }
    }
}