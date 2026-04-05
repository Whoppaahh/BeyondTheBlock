package net.ryan.beyond_the_block.feature.player.ranged;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class InfinityArrowRules {

    private InfinityArrowRules() {}

    public static int getInfinityLevel(ItemStack weaponStack) {
        return EnchantmentHelper.getLevel(Enchantments.INFINITY, weaponStack);
    }

    public static boolean allowsFallbackArrow(ItemStack weaponStack) {
        return getInfinityLevel(weaponStack) >= 2;
    }

    public static boolean shouldProtectSpecialArrowConsumption(ItemStack weaponStack, ItemStack arrowStack) {
        int level = getInfinityLevel(weaponStack);
        if (level < 3) return false;
        if (arrowStack.isEmpty()) return false;

        return arrowStack.isOf(Items.TIPPED_ARROW) || arrowStack.isOf(Items.SPECTRAL_ARROW);
    }

    public static ItemStack maybeCreateFallbackArrow(ItemStack weaponStack, ItemStack selectedArrow) {
        if (!selectedArrow.isEmpty()) return selectedArrow;
        if (!allowsFallbackArrow(weaponStack)) return ItemStack.EMPTY;

        return new ItemStack(Items.ARROW);
    }

    public static ItemStack maybeReturnProtectedCopy(ItemStack weaponStack, ItemStack arrowStack) {
        if (shouldProtectSpecialArrowConsumption(weaponStack, arrowStack)) {
            return arrowStack.copy();
        }
        return arrowStack;
    }
}