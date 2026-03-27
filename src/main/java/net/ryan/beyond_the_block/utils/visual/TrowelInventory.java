package net.ryan.beyond_the_block.utils.visual;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

public class TrowelInventory extends SimpleInventory {

    public static final int SIZE = 5;
    private final ItemStack trowelStack;

    public TrowelInventory(ItemStack stack) {
        super(SIZE);
        this.trowelStack = stack;

        // Load from NBT
        NbtList list = stack.getOrCreateNbt().getList("TrowelInv", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size() && i < SIZE; i++) {
            this.setStack(i, ItemStack.fromNbt(list.getCompound(i)));
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        saveToNBT();
    }

    private void saveToNBT() {
        NbtList list = new NbtList();

        for (int i = 0; i < SIZE; i++) {
            list.add(this.getStack(i).writeNbt(new NbtCompound()));
        }

        trowelStack.getOrCreateNbt().put("TrowelInv", list);
    }

    // Static helper for TrowelItem placement logic
    public static DefaultedList<ItemStack> readStacks(ItemStack stack) {
        DefaultedList<ItemStack> items = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);

        NbtList list = stack.getOrCreateNbt().getList("TrowelInv", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size() && i < SIZE; i++) {
            items.set(i, ItemStack.fromNbt(list.getCompound(i)));
        }

        return items;
    }

    public static void writeStacks(ItemStack stack, DefaultedList<ItemStack> items) {
        NbtList list = new NbtList();
        for (ItemStack item : items) {
            list.add(item.writeNbt(new NbtCompound()));
        }
        stack.getOrCreateNbt().put("TrowelInv", list);
    }
}

