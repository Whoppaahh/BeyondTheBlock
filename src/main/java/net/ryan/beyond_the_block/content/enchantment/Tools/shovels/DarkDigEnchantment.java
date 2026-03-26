package net.ryan.beyond_the_block.content.enchantment.Tools.shovels;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;

public class DarkDigEnchantment extends Enchantment {
    public DarkDigEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ShovelItem || stack.isOf(Items.BOOK);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
