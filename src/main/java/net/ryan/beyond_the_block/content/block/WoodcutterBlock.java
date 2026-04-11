package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.ShelfBlockEntity;
import net.ryan.beyond_the_block.content.blockentity.WoodcutterBlockEntity;
import org.jetbrains.annotations.Nullable;

public class WoodcutterBlock extends BlockWithEntity {

    public WoodcutterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof WoodcutterBlockEntity woodcutter) {
                // Scatter everything except output slot
                for (int i = 0; i < woodcutter.size(); i++) {
                    if (i == 1) { // output slot
                        continue;
                    }

                    ItemStack stack = woodcutter.getStack(i);
                    if (!stack.isEmpty()) {
                        ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                    }
                }

                world.updateComparators(pos, this);
            }
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WoodcutterBlockEntity(pos, state);
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof WoodcutterBlockEntity woodcutterBlockEntity) {
            return woodcutterBlockEntity;
        }
        return null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
            if (factory != null) {
                player.openHandledScreen(factory);
            }
        }
        return ActionResult.SUCCESS;
    }
}