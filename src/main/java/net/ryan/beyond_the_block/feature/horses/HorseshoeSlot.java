package net.ryan.beyond_the_block.feature.horses;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.ryan.beyond_the_block.content.registry.ModItems;

public class HorseshoeSlot extends Slot {

    public HorseshoeSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(ModItems.HORSESHOES);
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}