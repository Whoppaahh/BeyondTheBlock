package net.ryan.beyond_the_block.feature.horses;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

public class HorseshoeInventory extends SimpleInventory {

    private final HorseEquipmentAccessor accessor;

    public HorseshoeInventory(HorseEquipmentAccessor accessor) {
        super(1);
        this.accessor = accessor;
        this.setStack(0, accessor.btb$getHorseshoes());
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
        accessor.btb$setHorseshoes(stack);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = super.removeStack(slot, amount);
        accessor.btb$setHorseshoes(getStack(0));
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = super.removeStack(slot);
        accessor.btb$setHorseshoes(ItemStack.EMPTY);
        return stack;
    }
}