package net.ryan.beyond_the_block.enchantment.Tools;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import org.jetbrains.annotations.Nullable;

public class FishingCookingEnchantment extends Enchantment {
    public FishingCookingEnchantment(Rarity weight, EnchantmentTarget type, @Nullable EnchantmentTarget type1, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);

    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof FishingRodItem || stack.isOf(Items.BOOK);
    }
}
