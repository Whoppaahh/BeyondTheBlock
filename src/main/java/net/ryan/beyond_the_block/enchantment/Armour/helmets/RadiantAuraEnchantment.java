package net.ryan.beyond_the_block.enchantment.Armour.helmets;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class RadiantAuraEnchantment extends Enchantment {
    public RadiantAuraEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    //Logic handled by Mixin

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armour && armour.getSlotType() == EquipmentSlot.HEAD || stack.isOf(Items.BOOK);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
