package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.ryan.beyond_the_block.content.blockentity.ChiseledBookshelfBlockEntity;

public class ChiseledBookshelfBlock extends Block implements BlockEntityProvider, Waterloggable {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty SLOT_0_OCCUPIED = BooleanProperty.of("slot_0_occupied");
    public static final BooleanProperty SLOT_1_OCCUPIED = BooleanProperty.of("slot_1_occupied");
    public static final BooleanProperty SLOT_2_OCCUPIED = BooleanProperty.of("slot_2_occupied");
    public static final BooleanProperty SLOT_3_OCCUPIED = BooleanProperty.of("slot_3_occupied");
    public static final BooleanProperty SLOT_4_OCCUPIED = BooleanProperty.of("slot_4_occupied");
    public static final BooleanProperty SLOT_5_OCCUPIED = BooleanProperty.of("slot_5_occupied");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final BooleanProperty[] SLOT_PROPERTIES = new BooleanProperty[]{
            SLOT_0_OCCUPIED, SLOT_1_OCCUPIED, SLOT_2_OCCUPIED,
            SLOT_3_OCCUPIED, SLOT_4_OCCUPIED, SLOT_5_OCCUPIED
    };

    private static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public ChiseledBookshelfBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false)
                .with(SLOT_0_OCCUPIED, false)
                .with(SLOT_1_OCCUPIED, false)
                .with(SLOT_2_OCCUPIED, false)
                .with(SLOT_3_OCCUPIED, false)
                .with(SLOT_4_OCCUPIED, false)
                .with(SLOT_5_OCCUPIED, false));
    }

    public static BooleanProperty getSlotProperty(int slot) {
        return SLOT_PROPERTIES[slot];
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChiseledBookshelfBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED,
                SLOT_0_OCCUPIED, SLOT_1_OCCUPIED, SLOT_2_OCCUPIED,
                SLOT_3_OCCUPIED, SLOT_4_OCCUPIED, SLOT_5_OCCUPIED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite())
                .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, net.minecraft.util.BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, net.minecraft.util.BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ChiseledBookshelfBlockEntity shelf)) {
            return ActionResult.PASS;
        }

        int slot = getHitSlot(state, hit);
        if (slot < 0 || slot >= ChiseledBookshelfBlockEntity.SIZE) {
            return ActionResult.PASS;
        }

        ItemStack heldStack = player.getStackInHand(hand);
        ItemStack slotStack = shelf.getStack(slot);

        if (!slotStack.isEmpty()) {
            if (!world.isClient) {
                ItemStack removed = shelf.removeStack(slot, 1);
                if (!player.getAbilities().creativeMode) {
                    if (!player.getInventory().insertStack(removed.copy())) {
                        player.dropItem(removed.copy(), false);
                    }
                }

                world.playSound(null, pos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }

            return ActionResult.success(world.isClient);
        }

        if (!isValidBook(heldStack)) {
            return ActionResult.CONSUME;
        }

        if (!world.isClient) {
            ItemStack singleBook = heldStack.copy();
            singleBook.setCount(1);
            shelf.setStack(slot, singleBook);

            if (!player.getAbilities().creativeMode) {
                heldStack.decrement(1);
            }

            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        return ActionResult.success(world.isClient);
    }

    public static boolean isValidBook(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        return item == Items.BOOK
                || item == Items.ENCHANTED_BOOK
                || item == Items.WRITABLE_BOOK
                || item == Items.WRITTEN_BOOK
                || item == Items.KNOWLEDGE_BOOK;
    }

    /**
     * Maps the clicked front face into a 3x2 slot grid:
     *
     * top row:    0 1 2
     * bottom row: 3 4 5
     */
    public static int getHitSlot(BlockState state, BlockHitResult hit) {
        Direction facing = state.get(FACING);
        Vec3d hitPos = hit.getPos();
        BlockPos blockPos = hit.getBlockPos();

        double localX = hitPos.x - blockPos.getX();
        double localY = hitPos.y - blockPos.getY();
        double localZ = hitPos.z - blockPos.getZ();

        double horizontal;
        switch (facing) {
            case NORTH -> horizontal = 1.0D - localX;
            case SOUTH -> horizontal = localX;
            case WEST  -> horizontal = localZ;
            case EAST  -> horizontal = 1.0D - localZ;
            default -> horizontal = localX;
        }

        int column = Math.min(2, Math.max(0, (int)(horizontal * 3.0D)));
        int row = localY >= 0.5D ? 0 : 1;

        return row * 3 + column;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ChiseledBookshelfBlockEntity shelf) {
            return shelf.getComparatorOutput();
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            super.onStateReplaced(state, world, pos, newState, moved);
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof Inventory inventory) {
            ItemScatterer.spawn(world, pos, inventory);
            world.updateComparators(pos, this);
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

}