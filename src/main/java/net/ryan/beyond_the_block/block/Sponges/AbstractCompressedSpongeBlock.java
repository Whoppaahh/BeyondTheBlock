package net.ryan.beyond_the_block.block.Sponges;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpongeBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.utils.Helpers.CompressedSpongeLogic;

import java.util.function.Supplier;

public abstract class AbstractCompressedSpongeBlock extends SpongeBlock {

    protected final int absorbRadius;
    protected final Supplier<Block> wetBlock;

    protected AbstractCompressedSpongeBlock(Settings settings,
                                            int absorbRadius,
                                            Supplier<Block> wetBlock) {
        super(settings);
        this.absorbRadius = absorbRadius;
        this.wetBlock = wetBlock;
    }

    @Override
    protected void update(World world, BlockPos pos) {
        tryAbsorb(world, pos);
    }

    @Override
    public void onBlockAdded(BlockState state, World world,
                             BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            tryAbsorb(world, pos);
        }
    }

    protected void tryAbsorb(World world, BlockPos pos) {
        if (!world.isClient &&
                CompressedSpongeLogic.absorb(world, pos, absorbRadius) > 0) {

            world.setBlockState(pos,
                    wetBlock.get().getDefaultState(),
                    Block.NOTIFY_ALL);
        }
    }
}
