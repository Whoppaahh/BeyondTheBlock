package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class PinkPetalsBlock extends PlantBlock implements Fertilizable {
    public static final IntProperty FLOWER_AMOUNT = IntProperty.of("flower_amount", 1, 4);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    protected static final VoxelShape NORTH_WEST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 3.0D, 8.0D);
    protected static final VoxelShape NORTH_EAST_SHAPE = Block.createCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 3.0D, 8.0D);
    protected static final VoxelShape SOUTH_EAST_SHAPE = Block.createCuboidShape(8.0D, 0.0D, 8.0D, 16.0D, 3.0D, 16.0D);
    protected static final VoxelShape SOUTH_WEST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 8.0D, 8.0D, 3.0D, 16.0D);

    public PinkPetalsBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FLOWER_AMOUNT, 1)
                .with(FACING, Direction.NORTH));
    }

    private VoxelShape getSegmentShape(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH_WEST_SHAPE;
            case EAST -> NORTH_EAST_SHAPE;
            case SOUTH -> SOUTH_EAST_SHAPE;
            case WEST -> SOUTH_WEST_SHAPE;
            default -> NORTH_WEST_SHAPE;
        };
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int amount = state.get(FLOWER_AMOUNT);
        if (amount < 4) {
            world.setBlockState(pos, state.with(FLOWER_AMOUNT, amount + 1), Block.NOTIFY_LISTENERS);
        } else {
            dropStack(world, pos, new ItemStack(this));
        }
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        ItemStack stack = context.getStack();
        return (stack.isOf(this.asItem()) && state.get(FLOWER_AMOUNT) < 4) || super.canReplace(state, context);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState currentState = context.getWorld().getBlockState(context.getBlockPos());

        if (currentState.isOf(this)) {
            int amount = currentState.get(FLOWER_AMOUNT);
            return currentState.with(FLOWER_AMOUNT, Math.min(4, amount + 1));
        }

        Direction facing = context.getPlayerFacing().getOpposite();
        return this.getDefaultState()
                .with(FLOWER_AMOUNT, 1)
                .with(FACING, facing);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return super.canPlantOnTop(floor, world, pos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, net.minecraft.block.ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        Direction direction = state.get(FACING);
        int amount = state.get(FLOWER_AMOUNT);

        for (int i = 0; i < amount; i++) {
            shape = VoxelShapes.union(shape, getSegmentShape(direction));
            direction = direction.rotateYCounterclockwise();
        }

        return shape;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FLOWER_AMOUNT, FACING);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return type == NavigationType.AIR && !this.collidable;
    }
}
