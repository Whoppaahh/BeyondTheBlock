package net.ryan.beyond_the_block.content.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.WallShape;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Type;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Map;
import java.util.function.BooleanSupplier;

public class PaleMossCarpetBlock extends Block implements Fertilizable {
    public static final BooleanProperty BOTTOM = Properties.BOTTOM;
    public static final EnumProperty<WallShape> NORTH = Properties.NORTH_WALL_SHAPE;
    public static final EnumProperty<WallShape> EAST = Properties.EAST_WALL_SHAPE;
    public static final EnumProperty<WallShape> SOUTH = Properties.SOUTH_WALL_SHAPE;
    public static final EnumProperty<WallShape> WEST = Properties.WEST_WALL_SHAPE;

    public static final Map<Direction, EnumProperty<WallShape>> WALL_SHAPE_PROPERTIES_BY_DIRECTION =
            ImmutableMap.copyOf(Maps.newEnumMap(Map.of(
                    Direction.NORTH, NORTH,
                    Direction.EAST, EAST,
                    Direction.SOUTH, SOUTH,
                    Direction.WEST, WEST
            )));

    private static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    private static final VoxelShape LOW_NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 14.0D, 16.0D, 10.0D, 16.0D);
    private static final VoxelShape LOW_SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 2.0D);
    private static final VoxelShape LOW_WEST_SHAPE  = Block.createCuboidShape(14.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D);
    private static final VoxelShape LOW_EAST_SHAPE  = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 2.0D, 10.0D, 16.0D);

    private static final VoxelShape TALL_NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape TALL_SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D);
    private static final VoxelShape TALL_WEST_SHAPE  = Block.createCuboidShape(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape TALL_EAST_SHAPE  = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D);

    public PaleMossCarpetBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(BOTTOM, true)
                .with(NORTH, WallShape.NONE)
                .with(EAST, WallShape.NONE)
                .with(SOUTH, WallShape.NONE)
                .with(WEST, WallShape.NONE));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = state.get(BOTTOM) ? BOTTOM_SHAPE : VoxelShapes.empty();

        shape = addWallShape(shape, state.get(NORTH), LOW_NORTH_SHAPE, TALL_NORTH_SHAPE);
        shape = addWallShape(shape, state.get(SOUTH), LOW_SOUTH_SHAPE, TALL_SOUTH_SHAPE);
        shape = addWallShape(shape, state.get(EAST), LOW_EAST_SHAPE, TALL_EAST_SHAPE);
        shape = addWallShape(shape, state.get(WEST), LOW_WEST_SHAPE, TALL_WEST_SHAPE);

        return shape.isEmpty() ? VoxelShapes.fullCube() : shape;
    }

    private static VoxelShape addWallShape(VoxelShape base, WallShape wallShape, VoxelShape lowShape, VoxelShape tallShape) {
        return switch (wallShape) {
            case LOW -> VoxelShapes.union(base, lowShape);
            case TALL -> VoxelShapes.union(base, tallShape);
            default -> base;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(BOTTOM) ? BOTTOM_SHAPE : VoxelShapes.empty();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.down());
        if (state.get(BOTTOM)) {
            return !below.isAir();
        } else {
            return below.isOf(this) && below.get(BOTTOM);
        }
    }

    private static boolean hasAnyShape(BlockState state) {
        if (state.get(BOTTOM)) {
            return true;
        }

        for (EnumProperty<WallShape> property : WALL_SHAPE_PROPERTIES_BY_DIRECTION.values()) {
            if (state.get(property) != WallShape.NONE) {
                return true;
            }
        }

        return false;
    }

    private static boolean canGrowOnFace(BlockView world, BlockPos pos, Direction direction) {
        if (direction == Direction.UP) {
            return false;
        }

        BlockPos supportPos = pos.offset(direction);
        BlockState supportState = world.getBlockState(supportPos);
        return supportState.isSideSolidFullSquare(world, supportPos, direction.getOpposite());
    }

    private BlockState updateState(BlockState state, BlockView world, BlockPos pos, boolean includeBottom) {
        BlockState aboveState = null;
        BlockState belowState = null;

        includeBottom |= state.get(BOTTOM);

        for (Direction direction : Type.HORIZONTAL) {
            EnumProperty<WallShape> property = getWallShape(direction);
            WallShape wallShape = canGrowOnFace(world, pos, direction)
                    ? (includeBottom ? WallShape.LOW : state.get(property))
                    : WallShape.NONE;

            if (wallShape == WallShape.LOW) {
                if (aboveState == null) {
                    aboveState = world.getBlockState(pos.up());
                }

                if (aboveState.isOf(this) && aboveState.get(property) != WallShape.NONE && !aboveState.get(BOTTOM)) {
                    wallShape = WallShape.TALL;
                }

                if (!state.get(BOTTOM)) {
                    if (belowState == null) {
                        belowState = world.getBlockState(pos.down());
                    }

                    if (belowState.isOf(this) && belowState.get(property) == WallShape.NONE) {
                        wallShape = WallShape.NONE;
                    }
                }
            }

            state = state.with(property, wallShape);
        }

        return state;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return updateState(this.getDefaultState(), ctx.getWorld(), ctx.getBlockPos(), true);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!world.isClient) {
            BlockState upperState = createUpperState(world, pos, world.getRandom()::nextBoolean);
            if (!upperState.isAir()) {
                world.setBlockState(pos.up(), upperState, Block.NOTIFY_ALL);
            }
        }
    }

    private BlockState createUpperState(BlockView world, BlockPos pos, BooleanSupplier booleanSupplier) {
        BlockPos upPos = pos.up();
        BlockState currentUp = world.getBlockState(upPos);
        boolean isSameBlock = currentUp.isOf(this);

        if ((!isSameBlock || !currentUp.get(BOTTOM)) &&
                (isSameBlock || currentUp.isAir() || currentUp.getMaterial().isReplaceable())) {

            BlockState upper = this.getDefaultState().with(BOTTOM, false);
            BlockState updated = updateState(upper, world, upPos, true);

            for (Direction direction : Type.HORIZONTAL) {
                EnumProperty<WallShape> property = getWallShape(direction);
                if (updated.get(property) != WallShape.NONE && !booleanSupplier.getAsBoolean()) {
                    updated = updated.with(property, WallShape.NONE);
                }
            }

            if (hasAnyShape(updated) && !updated.equals(currentUp)) {
                return updated;
            }
        }

        return Blocks.AIR.getDefaultState();
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
        if (!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

        BlockState updated = updateState(state, world, pos, false);
        return !hasAnyShape(updated) ? Blocks.AIR.getDefaultState() : updated;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BOTTOM, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 -> state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
            case COUNTERCLOCKWISE_90 -> state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
            case CLOCKWISE_90 -> state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
            default -> state;
        };
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
            case FRONT_BACK -> state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
            default -> super.mirror(state, mirror);
        };
    }

    public static EnumProperty<WallShape> getWallShape(Direction face) {
        return WALL_SHAPE_PROPERTIES_BY_DIRECTION.get(face);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(BOTTOM) && !createUpperState(world, pos, () -> true).isAir();
    }

    @Override
    public boolean canGrow(World world, net.minecraft.util.math.random.Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, net.minecraft.util.math.random.Random random, BlockPos pos, BlockState state) {
        BlockState upperState = createUpperState(world, pos, () -> true);
        if (!upperState.isAir()) {
            world.setBlockState(pos.up(), upperState, Block.NOTIFY_ALL);
        }
    }
}