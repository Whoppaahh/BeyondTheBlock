package net.ryan.beyond_the_block.enchantment.Tools.swords;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class FlameSweepEnchantment extends Enchantment {

    public FlameSweepEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return 3; // Max level
    }
}
