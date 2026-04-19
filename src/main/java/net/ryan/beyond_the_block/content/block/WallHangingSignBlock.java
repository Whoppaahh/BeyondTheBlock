package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
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
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.utils.HangingSignTextureProvider;

public class WallHangingSignBlock extends AbstractSignBlock implements HangingSignTextureProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(1.0D, 1.0D, 14.0D, 15.0D, 15.0D, 16.0D);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(1.0D, 1.0D, 0.0D, 15.0D, 15.0D, 2.0D);
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(14.0D, 1.0D, 1.0D, 16.0D, 15.0D, 15.0D);
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0D, 1.0D, 1.0D, 2.0D, 15.0D, 15.0D);

    private final Identifier guiTexture;

    public WallHangingSignBlock(Settings settings, SignType type, Identifier guiTexture) {
        super(settings, type);
        this.guiTexture = guiTexture;
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false));
    }

    @Override
    public Identifier beyond_the_block$getGuiTexture() {
        return this.guiTexture;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        WorldAccess world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        FluidState fluidState = world.getFluidState(pos);

        Direction side = ctx.getSide();
        if (!side.getAxis().isHorizontal()) {
            return null;
        }

        BlockState state = this.getDefaultState()
                .with(FACING, side)
                .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

        return state.canPlaceAt(world, pos) ? state : null;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        Item item = ModBlocks.HANGING_SIGN_ITEMS.get(this.getSignType());
        return item != null ? new ItemStack(item) : ItemStack.EMPTY;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction facing = state.get(FACING);
        BlockPos supportPos = pos.offset(facing.getOpposite());
        BlockState supportState = world.getBlockState(supportPos);
        return supportState.isSideSolidFullSquare(world, supportPos, facing);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.get(FACING) && !this.canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

//        if (state.get(WATERLOGGED)) {
//            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
//        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HangingSignBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (!(blockEntity instanceof HangingSignBlockEntity signBlockEntity)) {
            return ActionResult.PASS;
        }

        // Glow ink sac
        if (stack.isOf(Items.GLOW_INK_SAC)) {
            if (!signBlockEntity.isGlowingText()) {
                if (!world.isClient) {
                    signBlockEntity.setGlowingText(true);
                    signBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);

                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                }
                return ActionResult.success(world.isClient);
            }
            return ActionResult.CONSUME;
        }

        // Normal ink sac removes glow
        if (stack.isOf(Items.INK_SAC)) {
            if (signBlockEntity.isGlowingText()) {
                if (!world.isClient) {
                    signBlockEntity.setGlowingText(false);
                    signBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);

                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                }
                return ActionResult.success(world.isClient);
            }
            return ActionResult.CONSUME;
        }

        // Dyes
        if (stack.getItem() instanceof DyeItem dyeItem) {
            DyeColor newColor = dyeItem.getColor();
            if (signBlockEntity.getTextColor() != newColor) {
                if (!world.isClient) {
                    signBlockEntity.setTextColor(newColor);
                    signBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);

                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                }
                return ActionResult.success(world.isClient);
            }
            return ActionResult.CONSUME;
        }

        // Otherwise open editor
        if (!world.isClient) {
            signBlockEntity.setEditor(player.getUuid());
            player.openEditSignScreen(signBlockEntity);
        }

        return ActionResult.success(world.isClient);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }
}