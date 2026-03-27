package net.ryan.beyond_the_block.utils;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;

public final class ReachHelper {

    private ReachHelper() {
    }

    public static boolean isValidLongReachTool(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        Item item = stack.getItem();
        return item instanceof PickaxeItem;
    }

    public static int getReachLevel(ItemStack stack) {
        if (!isValidLongReachTool(stack)) {
            return 0;
        }

        return EnchantmentHelper.getLevel(ModEnchantments.SMELTING_STRIKE, stack);
    }

    public static float getReachBonusFloat(ItemStack stack) {
        int level = getReachLevel(stack);
        return level > 0 ? 1.2F * level : 0.0F;
    }

    public static double getReachBonusDouble(ItemStack stack) {
        int level = getReachLevel(stack);
        return level > 0 ? 1.2D * level : 0.0D;
    }
}