package net.ryan.beyond_the_block.content.enchantment.Armour.chestplates;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class DurabilityBoostEnchantment extends Enchantment {
    public DurabilityBoostEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armour && armour.getSlotType() == EquipmentSlot.CHEST || stack.isOf(Items.BOOK);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
