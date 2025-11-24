package net.ryan.beyond_the_block.enchantment.Tools.swords;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;

public class ResilientStrikeEnchantment extends Enchantment {
    public ResilientStrikeEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || stack.isOf(Items.BOOK);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (!user.world.isClient) {
            // Logic for applying effect (e.g., granting damage resistance for 3 seconds)
            if (target instanceof LivingEntity livingTarget && level > 0) {
                // Inflict a debuff on the target, such as poison or slowness
                livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100 * level, 0));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 60 * level, 0, false, false, false)); // 60 ticks = 3 seconds
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 3; // The maximum level of this enchantment
    }
}
