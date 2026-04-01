package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.ShelfBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;

public class ShelfBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final EnumProperty<ShelfSideChainPart> SIDE_CHAIN =
            EnumProperty.of("side_chain", ShelfSideChainPart.class);

    // Matches the 1.21 shelf shape much more closely
    private static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0.0, 12.0, 11.0, 16.0, 16.0, 13.0),
            Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 11.0, 16.0, 4.0, 13.0)
    );

    private static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0.0, 12.0, 3.0, 16.0, 16.0, 5.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0),
            Block.createCuboidShape(0.0, 0.0, 3.0, 16.0, 4.0, 5.0)
    );

    private static final VoxelShape WEST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(11.0, 12.0, 0.0, 13.0, 16.0, 16.0),
            Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(11.0, 0.0, 0.0, 13.0, 4.0, 16.0)
    );

    private static final VoxelShape EAST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(3.0, 12.0, 0.0, 5.0, 16.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0),
            Block.createCuboidShape(3.0, 0.0, 0.0, 5.0, 4.0, 16.0)
    );

    public ShelfBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(POWERED, false)
                .with(SIDE_CHAIN, ShelfSideChainPart.UNCONNECTED));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, SIDE_CHAIN);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        boolean powered = ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos());
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite())
                .with(POWERED, powered)
                .with(SIDE_CHAIN, ShelfSideChainPart.UNCONNECTED);
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

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ShelfBlockEntity(pos, state);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient) {
            updateSideChain(world, pos, state);
            updateNeighborShelves(world, pos, state);
        }
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean powered = world.isReceivingRedstonePower(pos);
            BlockState newState = state;

            if (powered != state.get(POWERED)) {
                newState = newState.with(POWERED, powered);
                if (!powered) {
                    newState = newState.with(SIDE_CHAIN, ShelfSideChainPart.UNCONNECTED);
                }
            }

            world.setBlockState(pos, newState, Block.NOTIFY_ALL);
            updateSideChain(world, pos, newState);
            updateNeighborShelves(world, pos, newState);
        }

        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

    private void updateNeighborShelves(World world, BlockPos pos, BlockState state) {
        Direction facing = state.get(FACING);
        Direction left = facing.rotateYCounterclockwise();
        Direction right = facing.rotateYClockwise();

        tryUpdateShelf(world, pos.offset(left));
        tryUpdateShelf(world, pos.offset(right));
        tryUpdateShelf(world, pos.offset(left, 2));
        tryUpdateShelf(world, pos.offset(right, 2));
    }

    private void tryUpdateShelf(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof ShelfBlock) {
            updateSideChain(world, pos, state);
        }
    }

    private void updateSideChain(World world, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof ShelfBlock)) {
            return;
        }

        if (!state.get(POWERED)) {
            if (state.get(SIDE_CHAIN) != ShelfSideChainPart.UNCONNECTED) {
                world.setBlockState(pos, state.with(SIDE_CHAIN, ShelfSideChainPart.UNCONNECTED), Block.NOTIFY_ALL);
            }
            return;
        }

        Direction facing = state.get(FACING);
        Direction leftDir = facing.rotateYCounterclockwise();
        Direction rightDir = facing.rotateYClockwise();

        boolean left = isCompatibleShelf(world, pos.offset(leftDir), facing);
        boolean right = isCompatibleShelf(world, pos.offset(rightDir), facing);

        ShelfSideChainPart part;
        if (left && right) {
            part = ShelfSideChainPart.CENTER;
        } else if (left) {
            part = ShelfSideChainPart.RIGHT;
        } else if (right) {
            part = ShelfSideChainPart.LEFT;
        } else {
            part = ShelfSideChainPart.UNCONNECTED;
        }

        if (state.get(SIDE_CHAIN) != part) {
            world.setBlockState(pos, state.with(SIDE_CHAIN, part), Block.NOTIFY_ALL);
        }
    }

    private boolean isCompatibleShelf(World world, BlockPos pos, Direction facing) {
        BlockState other = world.getBlockState(pos);
        return other.getBlock() instanceof ShelfBlock
                && other.get(FACING) == facing
                && other.get(POWERED);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (hand == Hand.OFF_HAND) return ActionResult.PASS;
        if (hit.getSide() != state.get(FACING)) return ActionResult.PASS;

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof ShelfBlockEntity shelf)) return ActionResult.PASS;

        OptionalInt hitSlot = getHitSlot(hit, state.get(FACING));
        if (hitSlot.isEmpty()) return ActionResult.PASS;

        PlayerInventory playerInventory = player.getInventory();

        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        if (!state.get(POWERED)) {
            int slot = hitSlot.getAsInt();
            ItemStack selected = playerInventory.getMainHandStack();
            ItemStack old = shelf.swapStackNoMarkDirty(slot, selected.copy());

            if (player.isCreative() && old.isEmpty() && !selected.isEmpty()) {
                old = selected.copy();
            }

            playerInventory.setStack(playerInventory.selectedSlot, old);
            playerInventory.markDirty();
            shelf.markDirtyAndSync();

            return ActionResult.SUCCESS;
        }

        boolean swapped = swapAllStacks(world, pos, playerInventory);
        return swapped ? ActionResult.SUCCESS : ActionResult.CONSUME;
    }

    private boolean swapAllStacks(World world, BlockPos pos, PlayerInventory playerInventory) {
        List<BlockPos> chain = getPositionsInChain(world, pos);
        if (chain.isEmpty()) {
            return false;
        }

        boolean changed = false;

        for (int i = 0; i < chain.size(); i++) {
            BlockEntity be = world.getBlockEntity(chain.get(i));
            if (!(be instanceof ShelfBlockEntity shelf)) {
                continue;
            }

            for (int j = 0; j < shelf.size(); j++) {
                int hotbarIndex = 9 - (chain.size() - i) * shelf.size() + j;
                if (hotbarIndex < 0 || hotbarIndex >= playerInventory.size()) {
                    continue;
                }

                ItemStack playerStack = playerInventory.removeStack(hotbarIndex);
                ItemStack shelfStack = shelf.swapStackNoMarkDirty(j, playerStack);

                if (!playerStack.isEmpty() || !shelfStack.isEmpty()) {
                    playerInventory.setStack(hotbarIndex, shelfStack);
                    changed = true;
                }
            }

            shelf.markDirtyAndSync();
        }

        playerInventory.markDirty();
        return changed;
    }

    private List<BlockPos> getPositionsInChain(World world, BlockPos origin) {
        BlockState originState = world.getBlockState(origin);
        if (!(originState.getBlock() instanceof ShelfBlock) || !originState.get(POWERED)) {
            return List.of(origin);
        }

        Direction facing = originState.get(FACING);
        Direction leftDir = facing.rotateYCounterclockwise();
        Direction rightDir = facing.rotateYClockwise();

        List<BlockPos> positions = new ArrayList<>();
        positions.add(origin);

        BlockPos cursor = origin;
        for (int i = 0; i < 2; i++) {
            cursor = cursor.offset(leftDir);
            if (isCompatibleShelf(world, cursor, facing)) {
                positions.add(cursor);
            } else {
                break;
            }
        }

        cursor = origin;
        for (int i = 0; i < 2; i++) {
            cursor = cursor.offset(rightDir);
            if (isCompatibleShelf(world, cursor, facing)) {
                positions.add(cursor);
            } else {
                break;
            }
        }

        positions.sort(Comparator.comparingInt(pos -> projectAlong(pos, rightDir)));
        if (positions.size() > 3) {
            return positions.subList(0, 3);
        }
        return positions;
    }

    private int projectAlong(BlockPos pos, Direction direction) {
        return switch (direction.getAxis()) {
            case X -> pos.getX();
            case Y -> pos.getY();
            case Z -> pos.getZ();
        };
    }

    private OptionalInt getHitSlot(BlockHitResult hit, Direction facing) {
        BlockPos pos = hit.getBlockPos();

        double fx = hit.getPos().x - pos.getX();
        double fy = hit.getPos().y - pos.getY();
        double fz = hit.getPos().z - pos.getZ();

        // Only the visible middle band
        if (fy < 4.0 / 16.0 || fy > 12.0 / 16.0) {
            return OptionalInt.empty();
        }

        // Only the actual front face
        if (hit.getSide() != facing) {
            return OptionalInt.empty();
        }

        // Horizontal coordinate across the visible front, left -> right
        double u = switch (facing) {
            case NORTH -> fx;
            case SOUTH -> 1.0 - fx;
            case WEST  -> 1.0 - fz;
            case EAST  -> fz;
            default -> fx;
        };

        // Trim off the outer frame a bit
        double min = 1.0 / 16.0;
        double max = 15.0 / 16.0;
        if (u < min || u > max) {
            return OptionalInt.empty();
        }

        double normalized = (u - min) / (max - min);

        if (normalized < 1.0 / 3.0) return OptionalInt.of(0);
        if (normalized < 2.0 / 3.0) return OptionalInt.of(1);
        return OptionalInt.of(2);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof ShelfBlockEntity shelf) {
                ItemScatterer.spawn(world, pos, shelf);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
}