package net.ryan.beyond_the_block.utils.visual;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class BigSlot extends Slot {
    public BigSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }
    @Override
    public int getMaxItemCount() {
        return 512; // your new stack limit
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return 512; // also override for stack-specific checks
    }
}
