package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.PedestalBlockEntity;

public class PedestalBlock extends BlockWithEntity implements BlockEntityProvider {

    private static final VoxelShape SHAPE =
            Block.createCuboidShape(2, 0, 2, 14, 13, 14);

    public PedestalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PedestalBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof PedestalBlockEntity) {
                ItemScatterer.spawn(world, pos, ((PedestalBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
            if (pedestalBlockEntity.isEmpty() && !player.getStackInHand(hand).isEmpty()) {
                pedestalBlockEntity.setStack(0, player.getStackInHand(hand).copy());
                world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 2f);
                player.getStackInHand(hand).decrement(1);

                pedestalBlockEntity.markDirty();
                world.updateListeners(pos, state, state, 0);
            } else if (player.getStackInHand(hand).isEmpty() && !player.isSneaking()) {
                ItemStack stackOnPedestal = pedestalBlockEntity.getStack(0);
                player.setStackInHand(Hand.MAIN_HAND, stackOnPedestal);
                world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                pedestalBlockEntity.clear();

                pedestalBlockEntity.markDirty();
                world.updateListeners(pos, state, state, 0);
            }
        }

        return ActionResult.SUCCESS;
    }

}

