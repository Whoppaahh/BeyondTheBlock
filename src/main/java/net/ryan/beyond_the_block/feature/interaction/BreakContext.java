package net.ryan.beyond_the_block.feature.interaction;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public final class BreakContext {
    private final BlockState state;
    private final BlockEntity blockEntity;
    private final BlockPos pos;
    private final ItemStack tool;
    private boolean suppressVanillaDrops;

    public BreakContext(BlockState state, BlockEntity blockEntity, BlockPos pos, ItemStack tool) {
        this.state = state;
        this.blockEntity = blockEntity;
        this.pos = pos;
        this.tool = tool;
        this.suppressVanillaDrops = false;
    }

    public BlockState getState() {
        return state;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getTool() {
        return tool;
    }

    public boolean isSuppressVanillaDrops() {
        return suppressVanillaDrops;
    }

    public void setSuppressVanillaDrops(boolean suppressVanillaDrops) {
        this.suppressVanillaDrops = suppressVanillaDrops;
    }
}