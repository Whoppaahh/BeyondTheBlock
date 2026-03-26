package net.ryan.beyond_the_block.content.block.Shrine.PlayerInputBlocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
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
import net.ryan.beyond_the_block.content.blockentity.ShrineHeadsBlockEntity;
import net.ryan.beyond_the_block.content.blockentity.SingleInputBlockEntity;
import net.ryan.beyond_the_block.content.riddles.RiddleDataManager;
import net.ryan.beyond_the_block.content.sound.ModSounds;
import net.ryan.beyond_the_block.core.bootstrap.ContentRegistrar;
import net.ryan.beyond_the_block.feature.shrines.ShrineHelper;

import java.util.UUID;

import static net.ryan.beyond_the_block.feature.shrines.ShrineHelper.createRewardShulkerBox;

public class SingleInputBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public SingleInputBlock(Settings settings) {
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
        return new SingleInputBlockEntity(pos, state);
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
        if (!(world.getBlockEntity(pos) instanceof SingleInputBlockEntity blockEntity)) return ActionResult.PASS;

        ItemStack heldStack = player.getStackInHand(hand);

        if (blockEntity.isEmpty() && !heldStack.isEmpty()) {
            return handleItemInsertion(state, world, pos, player, blockEntity, heldStack);
        } else if (heldStack.isEmpty() && !player.isSneaking()) {
            return handleItemRetrieval(world, pos, player, state, blockEntity);
        }

        return ActionResult.SUCCESS;
    }


    private ActionResult handleItemInsertion(BlockState state, World world, BlockPos pos, PlayerEntity player, SingleInputBlockEntity blockEntity, ItemStack heldStack) {
        Item requiredItem = blockEntity.getRequiredItem();

        if (requiredItem == null || heldStack.getItem() != requiredItem) {
            //EmeraldEmpire.LOGGER.info("This item is not what the shrine seeks");
            blockEntity.clear();
            ShrineHelper.applyRandomHarmfulEffect(player);
            return ActionResult.FAIL;
        }

        blockEntity.setStack(0, heldStack.split(1));
        world.playSound(null, pos, ModSounds.RIDDLE_COMPLETE, SoundCategory.BLOCKS, 1f, 1f);
       // world.playSound(null, pos, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 1f, 1.5f);

        if (!world.isClient) {
            triggerCompletionEffects((ServerWorld) world, pos);
            completeRiddle((ServerWorld) world, player, pos, state);
        }

        blockEntity.clear();
        blockEntity.markDirty();
        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);

        return ActionResult.SUCCESS;
    }

    private ActionResult handleItemRetrieval(World world, BlockPos pos, PlayerEntity player, BlockState state, SingleInputBlockEntity blockEntity) {
        ItemStack stack = blockEntity.getStack(0);
        if (!player.getInventory().insertStack(stack.copy())) {
            player.dropItem(stack.copy(), false);
        }
        world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
        blockEntity.clear();
        blockEntity.markDirty();
        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        return ActionResult.SUCCESS;
    }
    private void completeRiddle(ServerWorld world, PlayerEntity player, BlockPos pos, BlockState state) {
        UUID playerID = player.getUuid();
        RiddleDataManager handler = RiddleDataManager.get(world, ContentRegistrar.RIDDLE_COMPONENTS);
        handler.markCompleted(playerID, handler.getRiddle(playerID).getId());

        world.removeBlockEntity(pos);
        world.removeBlock(pos, false);
        world.markDirty(pos);
        //EmeraldEmpire.LOGGER.info("Riddle Complete");

        clearPlayerHead(world, pos, playerID);
        clearLecternBook(world, pos, state);
        if(!player.getInventory().insertStack(createRewardShulkerBox())){
            player.dropItem(createRewardShulkerBox(), true);
        }
    }
    private void clearPlayerHead(ServerWorld world, BlockPos pos, UUID playerID){
        ShrineHelper.findShrineCore(world, pos).ifPresent(corePos -> {
            BlockPos headPos = corePos.up().up();
            BlockEntity headEntity = world.getBlockEntity(headPos);
            if (headEntity instanceof ShrineHeadsBlockEntity head) {
                RiddleDataManager handler = RiddleDataManager.get(world, ContentRegistrar.RIDDLE_COMPONENTS);
                head.clearPlayerHead(playerID);
                head.markDirty();
                head.sync();
                world.updateListeners(head.getPos(), head.getCachedState(), head.getCachedState(), 3);
                world.getChunkManager().markForUpdate(head.getPos());

                head.updateLastSynced(handler.getLastUpdated());
            }
        });
    }
    private void clearLecternBook(ServerWorld world, BlockPos pos, BlockState state) {
        Direction facing = state.get(FACING);
        BlockPos lecternPos = pos.offset(facing).down();
        BlockState lecternState = world.getBlockState(lecternPos);
        BlockEntity entity = world.getBlockEntity(lecternPos);

        if (entity instanceof LecternBlockEntity lectern) {
            lectern.setBook(ItemStack.EMPTY);
            lectern.markDirty();
            world.setBlockState(lecternPos, lecternState.with(LecternBlock.HAS_BOOK, false), Block.NOTIFY_ALL);
            world.updateListeners(lecternPos, lecternState, lecternState, Block.NOTIFY_ALL);
            //EmeraldEmpire.LOGGER.info("Cleared riddle book at: {}", lecternPos);
        }
    }

    private void triggerCompletionEffects(ServerWorld world, BlockPos pos) {
        world.spawnParticles(ParticleTypes.FIREWORK,
                pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5,
                10, 0.25, 0.25, 0.25, 0.1
        );
    }

}

