package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.ryan.beyond_the_block.content.blockentity.HangingSignBlockEntity;

public class HangingSignBlock extends AbstractSignBlock {
    public static final IntProperty ROTATION = Properties.ROTATION;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty ATTACHED = BooleanProperty.of("attached");

    private static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D);

    public HangingSignBlock(Settings settings, SignType type) {
        super(settings, type);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(ROTATION, 0)
                .with(WATERLOGGED, false)
                .with(ATTACHED, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());

        // Match standing-sign style rotation logic
        int rotation = Math.floorMod((int) Math.floor((180.0F + ctx.getPlayerYaw()) * 16.0F / 360.0F + 0.5D), 16);

        boolean attached = isAttachedVariant(ctx.getWorld(), ctx.getBlockPos().up());

        return this.getDefaultState()
                .with(ROTATION, rotation)
                .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER)
                .with(ATTACHED, attached);
    }

    private boolean isAttachedVariant(WorldAccess world, BlockPos supportPos) {
        BlockState supportState = world.getBlockState(supportPos);
        Block supportBlock = supportState.getBlock();

        // This can be expanded later to mimic vanilla more closely.
        // For now, treat narrow supports as "attached" chain style.
        return supportBlock instanceof FenceBlock
                || supportBlock instanceof WallBlock
                || supportBlock instanceof PaneBlock;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos supportPos = pos.up();
        BlockState supportState = world.getBlockState(supportPos);
        return Block.sideCoversSmallSquare(world, supportPos, Direction.DOWN) || supportState.isSideSolidFullSquare(world, supportPos, Direction.DOWN);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && !this.canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

//        if (state.get(WATERLOGGED)) {
//            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
//        }

        if (direction == Direction.UP) {
            state = state.with(ATTACHED, isAttachedVariant(world, pos.up()));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(ROTATION, rotation.rotate(state.get(ROTATION), 16));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(ROTATION, mirror.mirror(state.get(ROTATION), 16));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HangingSignBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof HangingSignBlockEntity signBlockEntity) {
            if (!world.isClient) {
                signBlockEntity.setEditor(player.getUuid());
                player.openEditSignScreen(signBlockEntity);
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return super.getPickStack(world, pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROTATION, WATERLOGGED, ATTACHED);
    }
}