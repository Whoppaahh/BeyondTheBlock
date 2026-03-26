package net.ryan.beyond_the_block.content.block.Entity;


import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class ModdedFluidCauldronBlockEntity extends BlockEntity {

    public ModdedFluidCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MODDED_FLUID_CAULDRON_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }
}
