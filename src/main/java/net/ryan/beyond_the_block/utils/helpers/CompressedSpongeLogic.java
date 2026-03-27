package net.ryan.beyond_the_block.utils.helpers;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class CompressedSpongeLogic {

    public static int absorb(World world, BlockPos origin, int radius) {
        int absorbed = 0;

        for (BlockPos pos : BlockPos.iterate(
                origin.add(-radius, -radius, -radius),
                origin.add(radius, radius, radius))) {

            if (pos.equals(origin)) continue; // skip the sponge itself

            BlockState state = world.getBlockState(pos);
            FluidState fluid = world.getFluidState(pos);

            if (!fluid.isEmpty() && fluid.isIn(FluidTags.WATER)
                    && state.getBlock() != Blocks.SPONGE
                    && state.getBlock() != Blocks.WET_SPONGE
                    && world.getBlockEntity(pos) == null) {

                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                absorbed++;
            }
        }

        return absorbed;
    }

    private CompressedSpongeLogic() {}
}
