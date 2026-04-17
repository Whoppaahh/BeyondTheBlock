package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class ResinClumpBlock extends Block {
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty DOWN = Properties.DOWN;
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty WEST = Properties.WEST;

    protected static final VoxelShape UP_SHAPE = Block.createCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);

    public ResinClumpBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(UP, false)
                .with(DOWN, false)
                .with(NORTH, false)
                .with(SOUTH, false)
                .with(EAST, false)
                .with(WEST, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();

        if (state.get(UP)) shape = VoxelShapes.union(shape, UP_SHAPE);
        if (state.get(DOWN)) shape = VoxelShapes.union(shape, DOWN_SHAPE);
        if (state.get(NORTH)) shape = VoxelShapes.union(shape, NORTH_SHAPE);
        if (state.get(SOUTH)) shape = VoxelShapes.union(shape, SOUTH_SHAPE);
        if (state.get(EAST)) shape = VoxelShapes.union(shape, EAST_SHAPE);
        if (state.get(WEST)) shape = VoxelShapes.union(shape, WEST_SHAPE);

        return shape.isEmpty() ? VoxelShapes.empty() : shape;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos pos = context.getBlockPos();
        BlockView world = context.getWorld();
        BlockState existing = world.getBlockState(pos);
        Direction face = context.getSide().getOpposite();

        if (existing.isOf(this)) {
            BlockState updated = withFaceIfSupported(existing, world, pos, face);
            return hasAnyFace(updated) ? updated : null;
        }

        BlockState state = withFaceIfSupported(this.getDefaultState(), world, pos, face);
        return hasAnyFace(state) ? state : null;
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return context.getStack().isOf(this.asItem()) || super.canReplace(state, context);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state,
                                                Direction direction,
                                                BlockState neighborState,
                                                WorldAccess world,
                                                BlockPos pos,
                                                BlockPos neighborPos) {
        state = removeUnsupportedFaces(state, world, pos);
        return hasAnyFace(state) ? state : net.minecraft.block.Blocks.AIR.getDefaultState();
    }

    private BlockState withFaceIfSupported(BlockState state, BlockView world, BlockPos pos, Direction face) {
        if (canAttachTo(world, pos, face)) {
            return state.with(getProperty(face), true);
        }
        return state;
    }

    private BlockState removeUnsupportedFaces(BlockState state, BlockView world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BooleanProperty property = getProperty(direction);
            if (state.get(property) && !canAttachTo(world, pos, direction)) {
                state = state.with(property, false);
            }
        }
        return state;
    }

    private boolean canAttachTo(BlockView world, BlockPos pos, Direction face) {
        BlockPos supportPos = pos.offset(face);
        BlockState supportState = world.getBlockState(supportPos);
        return supportState.isSideSolidFullSquare(world, supportPos, face.getOpposite());
    }

    private boolean hasAnyFace(BlockState state) {
        return state.get(UP) || state.get(DOWN) || state.get(NORTH)
                || state.get(SOUTH) || state.get(EAST) || state.get(WEST);
    }

    private BooleanProperty getProperty(Direction direction) {
        return switch (direction) {
            case UP -> UP;
            case DOWN -> DOWN;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
        };
    }
}