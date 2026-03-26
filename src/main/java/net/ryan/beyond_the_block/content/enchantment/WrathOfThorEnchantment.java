package net.ryan.beyond_the_block.content.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;

public class WrathOfThorEnchantment extends Enchantment {
    public WrathOfThorEnchantment(Rarity weight, EnchantmentTarget type, @Nullable EnchantmentTarget type1, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);

    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem || stack.isOf(Items.BOOK);
    }
}
