package net.ryan.beyond_the_block.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class SavedBlock {
    private final BlockPos pos;
    private final BlockState state;
    private final NbtCompound blockEntityData;
    private final DefaultedList<ItemStack> inventory;

    public SavedBlock(BlockPos pos, BlockState state, BlockEntity be) {
        this.pos = pos.toImmutable();
        this.state = state;
        this.blockEntityData = be != null ? be.createNbtWithIdentifyingData() : null;
        this.inventory = be instanceof Inventory inv ? copyInventory(inv) : null;

    }

    private DefaultedList<ItemStack> copyInventory(Inventory inv) {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); i++) {
            list.set(i, inv.getStack(i).copy());
        }
        return list;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void restore(ServerWorld world) {
        world.setBlockState(pos, state, 3);

        if (blockEntityData != null) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be != null) {
                be.readNbt(blockEntityData);
                be.markDirty();

                // Restore inventory if applicable
                if (inventory != null && be instanceof Inventory container) {
                    for (int i = 0; i < inventory.size(); i++) {
                        container.setStack(i, inventory.get(i));
                    }
                    container.markDirty();
                }
            }
        }
    }
}

