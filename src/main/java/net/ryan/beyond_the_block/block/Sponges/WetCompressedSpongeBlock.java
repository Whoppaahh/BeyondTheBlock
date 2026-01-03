package net.ryan.beyond_the_block.block.Sponges;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WetSpongeBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class WetCompressedSpongeBlock extends WetSpongeBlock {

    private final Supplier<Block> dryBlock;

    public WetCompressedSpongeBlock(Settings settings, Supplier<Block> dryBlock) {
        super(settings);
        this.dryBlock = dryBlock;
    }

    @Override
    public void onBlockAdded(BlockState state, World world,
                             BlockPos pos, BlockState oldState, boolean notify) {

        super.onBlockAdded(state, world, pos, oldState, notify);

        if (!world.isClient && world.getRegistryKey() == World.NETHER) {
            world.setBlockState(pos,
                    dryBlock.get().getDefaultState(),
                    Block.NOTIFY_ALL);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world,
                              BlockPos pos, Random random) {

        world.setBlockState(pos,
                dryBlock.get().getDefaultState(),
                Block.NOTIFY_ALL);
    }
}
