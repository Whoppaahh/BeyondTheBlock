package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class PaleMossCarpetSideBlock extends Block {
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.Type.HORIZONTAL);

    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D);
    private static final VoxelShape WEST_SHAPE  = Block.createCuboidShape(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_SHAPE  = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D);

    public PaleMossCarpetSideBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction facing = state.get(FACING);
        BlockPos supportPos = pos.offset(facing);
        BlockState supportState = world.getBlockState(supportPos);

        return supportState.isSideSolidFullSquare(world, supportPos, facing.getOpposite());
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            WorldAccess world,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if (!this.canPlaceAt(state, world, pos)) {
            return net.minecraft.block.Blocks.AIR.getDefaultState();
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        if (!this.canPlaceAt(state, world, pos)) {
            world.breakBlock(pos, false);
        }
    }
}
