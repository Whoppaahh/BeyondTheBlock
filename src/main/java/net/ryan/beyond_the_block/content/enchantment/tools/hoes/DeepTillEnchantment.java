package net.ryan.beyond_the_block.content.enchantment.tools.hoes;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class DeepTillEnchantment extends Enchantment {
    public DeepTillEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }
    //Logic handled by EnchantmentHelper and EnchantmentEventListener
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof HoeItem || stack.isOf(Items.BOOK);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
