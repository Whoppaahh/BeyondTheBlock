package net.ryan.beyond_the_block.feature.player.ranged;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class PlayerArrowSelectionHandler {
    private PlayerArrowSelectionHandler() {}

    public static ItemStack selectArrow(PlayerEntity player, ItemStack weaponStack) {
        if (!(weaponStack.getItem() instanceof BowItem || weaponStack.getItem() instanceof CrossbowItem)) {
            return ItemStack.EMPTY;
        }

        ItemStack best = findBestArrow(player);
        best = InfinityArrowRules.maybeCreateFallbackArrow(weaponStack, best);
        best = InfinityArrowRules.maybeReturnProtectedCopy(weaponStack, best);

        return best;
    }

    private static ItemStack findBestArrow(PlayerEntity player) {
        ItemStack tipped = ItemStack.EMPTY;
        ItemStack normal = ItemStack.EMPTY;

        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;

            if (stack.isOf(Items.SPECTRAL_ARROW)) return stack;
            if (stack.isOf(Items.TIPPED_ARROW) && tipped.isEmpty()) tipped = stack;
            if (stack.isOf(Items.ARROW) && normal.isEmpty()) normal = stack;
        }

        if (!tipped.isEmpty()) return tipped;
        if (!normal.isEmpty()) return normal;
        return ItemStack.EMPTY;
    }
}