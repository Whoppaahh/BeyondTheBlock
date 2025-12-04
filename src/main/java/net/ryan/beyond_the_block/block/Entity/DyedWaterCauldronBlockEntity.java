package net.ryan.beyond_the_block.block.Entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class DyedWaterCauldronBlockEntity extends BlockEntity {

    private int color = 0x3F76E4; // default Minecraft water tint

    public DyedWaterCauldronBlockEntity(BlockPos pos, net.minecraft.block.BlockState state) {
        super(ModBlockEntities.DYED_WATER_CAULDRON_BLOCK_ENTITY, pos, state);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int rgb, int count) {
        this.color = rgb;
        markDirty();
    }

    /**
     * Average old color with new dye to create blending.
     */
    public void updateColorWithNewDye(int newColor) {
        int r1 = (color >> 16) & 0xFF, g1 = (color >> 8) & 0xFF, b1 = color & 0xFF;
        int r2 = (newColor >> 16) & 0xFF, g2 = (newColor >> 8) & 0xFF, b2 = newColor & 0xFF;

        int r = (r1 + r2) / 2;
        int g = (g1 + g2) / 2;
        int b = (b1 + b2) / 2;

        color = (r << 16) | (g << 8) | b;

        markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        color = nbt.getInt("Color");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Color", color);
    }
}
