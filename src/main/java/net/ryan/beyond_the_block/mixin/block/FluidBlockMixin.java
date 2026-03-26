package net.ryan.beyond_the_block.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.feature.blockconversion.BlockConversionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin {
    @Inject(method = "neighborUpdate", at = @At("TAIL"))
    private void onNeighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if (!world.isClient && state.getFluidState().isOf(Fluids.LAVA)) {
            boolean isSource = state.getFluidState().isStill();
            if (isSource && world.getBlockState(pos).isOf(Blocks.SAND) || world.getBlockState(pos).isOf(Blocks.RED_SAND)) {
                BlockConversionHandler.queueAdjacentSand(world, pos.toImmutable());
                BeyondTheBlock.LOGGER.info("Queing sand to melt - mixin at pos " + pos + " - " + world.getBlockState(pos).getBlock());
            }
        }
    }
}
