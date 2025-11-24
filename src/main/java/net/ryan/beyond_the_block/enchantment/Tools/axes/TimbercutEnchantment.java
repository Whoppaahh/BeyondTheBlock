package net.ryan.beyond_the_block.enchantment.Tools.axes;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class TimbercutEnchantment extends Enchantment {
    public TimbercutEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }
    //Logic handled by EnchantmentHelper and EnchantmentEventListener
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || stack.isOf(Items.BOOK);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
