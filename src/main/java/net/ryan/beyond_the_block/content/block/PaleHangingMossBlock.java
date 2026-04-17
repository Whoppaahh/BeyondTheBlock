package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class PaleHangingMossBlock extends Block implements Fertilizable {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public PaleHangingMossBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, net.minecraft.block.ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos abovePos = pos.up();
        BlockState aboveState = world.getBlockState(abovePos);
        return aboveState.isSideSolidFullSquare(world, abovePos, Direction.DOWN);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state,
                                                Direction direction,
                                                BlockState neighborState,
                                                WorldAccess world,
                                                BlockPos pos,
                                                BlockPos neighborPos) {
        if (!this.canPlaceAt(state, world, pos)) {
            return net.minecraft.block.Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        BlockPos below = pos.down();
        return world.getBlockState(below).isAir();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        BlockPos cursor = pos.down();

        while (world.getBlockState(cursor).isOf(this)) {
            cursor = cursor.down();
        }

        return world.getBlockState(cursor).isAir();
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos cursor = pos.down();

        while (world.getBlockState(cursor).isOf(this)) {
            cursor = cursor.down();
        }

        if (world.getBlockState(cursor).isAir()) {
            world.setBlockState(cursor, this.getDefaultState(), Block.NOTIFY_ALL);
        }
    }

}