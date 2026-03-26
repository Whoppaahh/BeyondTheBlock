package net.ryan.beyond_the_block.content.block.Shrine.PlayerInputBlocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.DoubleInputBlockEntity;
import net.ryan.beyond_the_block.content.blockentity.ShrineHeadsBlockEntity;
import net.ryan.beyond_the_block.content.riddles.RiddleDataManager;
import net.ryan.beyond_the_block.content.sound.ModSounds;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.feature.shrines.ShrineHelper;

import java.util.UUID;

import static net.ryan.beyond_the_block.feature.shrines.ShrineHelper.createRewardShulkerBox;

public class DoubleInputBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;


    public DoubleInputBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH)); // Default to facing north
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DoubleInputBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof Inventory inventory) {
                ItemScatterer.spawn(world, pos, inventory);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof DoubleInputBlockEntity blockEntity)) return ActionResult.PASS;
        if (!world.isClient) {
            ItemStack heldStack = player.getStackInHand(hand);

            if (!heldStack.isEmpty()) {
                handleItemInsertion(world, pos, player, hand, blockEntity, state);
            } else {
                handleItemRetrieval(world, pos, player, blockEntity);
            }
        }

        return ActionResult.SUCCESS;
    }

    // === INSERT ITEMS ===
    private void handleItemInsertion(World world, BlockPos pos, PlayerEntity player, Hand hand, DoubleInputBlockEntity blockEntity, BlockState state) {
        ItemStack heldStack = player.getStackInHand(hand);
        boolean inserted = false;

        if (blockEntity.getStack(0).isEmpty()) {
            blockEntity.setStack(0, heldStack.split(1));
            inserted = true;
        } else if (blockEntity.getStack(1).isEmpty()) {
            blockEntity.setStack(1, heldStack.split(1));
            inserted = true;
        }

        if (inserted) {
            world.playSound(null, pos, ModSounds.RIDDLE_COMPLETE, SoundCategory.BLOCKS, 1f, 1f);
        }

        if (!blockEntity.getStack(0).isEmpty() && !blockEntity.getStack(1).isEmpty()) {
            ((ServerWorld) world).spawnParticles(ParticleTypes.ENCHANT,
                    pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                    20, 0.3, 0.3, 0.3, 0.1
            );

            if (blockEntity.validateInputs()) {
                onCorrectInputs(world, pos, state, blockEntity, player);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.BLOCKS, 1f, 0.5f);
                //EmeraldEmpire.LOGGER.info("These are not the item(s) the shrine seeks");
                ShrineHelper.applyRandomHarmfulEffect(player);
                blockEntity.clear();
            }
        }
    }
    // === CORRECT ITEMS ===
    private void onCorrectInputs(World world, BlockPos pos, BlockState state, DoubleInputBlockEntity blockEntity, PlayerEntity player) {
       // EmeraldEmpire.LOGGER.info("Riddle Complete");

        ((ServerWorld) world).spawnParticles(ParticleTypes.FIREWORK,
                pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5,
                10, 0.25, 0.25, 0.25, 0.1
        );

        world.playSound(null, pos, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 1f, 1.5f);

        world.removeBlockEntity(pos);
        world.removeBlock(pos, false);
        world.markDirty(pos);
        if(!player.getInventory().insertStack(createRewardShulkerBox())){
            player.dropItem(createRewardShulkerBox(), true);
        }
        UUID playerID = player.getUuid();

        RiddleDataManager handler = RiddleDataManager.get((ServerWorld) world, BeyondTheBlock.RIDDLE_COMPONENTS);
        handler.markCompleted(playerID, handler.getRiddle(playerID).getId());

        ShrineHelper.findShrineCore(world, pos).ifPresent(corePos -> {
            BlockPos headPos = corePos.up().up();
            BlockEntity headEntity = world.getBlockEntity(headPos);
            if (headEntity instanceof ShrineHeadsBlockEntity head) {
                head.clearPlayerHead(playerID);
                head.markDirty();
                head.sync();
                world.updateListeners(head.getPos(), head.getCachedState(), head.getCachedState(), 3);
                ((ServerWorld) world).getChunkManager().markForUpdate(head.getPos());

            }
        });

        clearLecternBook(world, state, pos);
        blockEntity.clear();
        blockEntity.markDirty();
        world.updateListeners(pos, state, state, 3);
    }
    // === RETRIEVE ITEMS ===
    private void handleItemRetrieval(World world, BlockPos pos, PlayerEntity player, DoubleInputBlockEntity blockEntity) {
        if (player.isSneaking()) {
            boolean changed = false;
            for (int i = 0; i < 2; i++) {
                ItemStack stack = blockEntity.getStack(i);
                if (!stack.isEmpty()) {
                    if (!player.getInventory().insertStack(stack.copy())) {
                        player.dropItem(stack.copy(), false);
                    }
                    blockEntity.setStack(i, ItemStack.EMPTY);
                    changed = true;
                }
            }
            if (changed) {
                blockEntity.sync();
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
            }
        } else {
            for (int i = 1; i >= 0; i--) {
                ItemStack stack = blockEntity.getStack(i);
                if (!stack.isEmpty()) {
                    if (!player.getInventory().insertStack(stack.copy())) {
                        player.dropItem(stack.copy(), false);
                    }
                    blockEntity.setStack(i, ItemStack.EMPTY);
                    blockEntity.sync();
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                    break;
                }
            }
        }
    }
    // === CLEAR BOOK ===
    private void clearLecternBook(World world, BlockState state, BlockPos pos) {
        Direction facing = state.get(FACING);
        BlockPos lecternPos = pos.offset(facing).down();
        BlockState lecternState = world.getBlockState(lecternPos);
        BlockEntity entity = world.getBlockEntity(lecternPos);

        if (entity instanceof LecternBlockEntity lectern) {
            lectern.setBook(ItemStack.EMPTY);
            lectern.markDirty();
            world.setBlockState(lecternPos, lecternState.with(LecternBlock.HAS_BOOK, false), Block.NOTIFY_ALL);
            world.updateListeners(lecternPos, lecternState, lecternState, Block.NOTIFY_ALL);
            //EmeraldEmpire.LOGGER.info("Cleared riddle book at: " + lecternPos);
        }
    }

}

