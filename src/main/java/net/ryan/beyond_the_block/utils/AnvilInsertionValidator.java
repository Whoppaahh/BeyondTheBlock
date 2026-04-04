package net.ryan.beyond_the_block.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;

import java.util.Map;

public final class AnvilInsertionValidator {
    private AnvilInsertionValidator() {
    }

    public static boolean isValidLeftInput(ItemStack proposedLeft, ItemStack currentRight) {
        if (proposedLeft.isEmpty()) {
            return true;
        }

        // Allow staging a target item in the left slot when there is no donor yet.
        if (currentRight.isEmpty()) {
            return true;
        }

        return areCompatible(proposedLeft, currentRight);
    }

    public static boolean isValidRightInput(ItemStack currentLeft, ItemStack proposedRight) {
        if (proposedRight.isEmpty()) {
            return true;
        }

        // Strict behavior: do not allow a donor/book without a target item.
        if (currentLeft.isEmpty()) {
            return false;
        }

        return areCompatible(currentLeft, proposedRight);
    }

    public static boolean canGoIntoEitherInput(ItemStack left, ItemStack right, ItemStack candidate) {
        return isValidLeftInput(candidate, right) || isValidRightInput(left, candidate);
    }

    public static boolean areCompatible(ItemStack target, ItemStack donor) {
        if (target.isEmpty() || donor.isEmpty()) {
            return true;
        }

        Map<Enchantment, Integer> donorEnchantments = getTransferEnchantments(donor);
        if (donorEnchantments.isEmpty()) {
            // No enchantments being transferred; leave repairs/renames/material logic to vanilla.
            return true;
        }

        Map<Enchantment, Integer> targetEnchantments = EnchantmentHelper.get(target);

        for (Enchantment incoming : donorEnchantments.keySet()) {
            // Item applicability check: this is your main missing validation.
            if (!incoming.isAcceptableItem(target)) {
                return false;
            }

            // Existing enchants on the target must be compatible with the incoming enchant.
            for (Enchantment existing : targetEnchantments.keySet()) {
                if (existing == incoming) {
                    continue;
                }

                if (!canCoexist(existing, incoming)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean canCoexist(Enchantment a, Enchantment b) {
        return a.canCombine(b) && b.canCombine(a);
    }

    private static Map<Enchantment, Integer> getTransferEnchantments(ItemStack stack) {
        if (stack.getItem() instanceof EnchantedBookItem) {
            return EnchantmentHelper.fromNbt(EnchantedBookItem.getEnchantmentNbt(stack));
        }

        return EnchantmentHelper.get(stack);
    }
}