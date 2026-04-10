package net.ryan.beyond_the_block.content.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.HangingSignBlockEntity;

public class HangingSignItem extends Item {
    private final Block hangingSignBlock;
    private final Block wallHangingSignBlock;

    public HangingSignItem(Settings settings, Block hangingSignBlock, Block wallHangingSignBlock) {
        super(settings);
        this.hangingSignBlock = hangingSignBlock;
        this.wallHangingSignBlock = wallHangingSignBlock;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Direction side = context.getSide();
        World world = context.getWorld();
        BlockPos clickedPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();

        if (side == Direction.UP) {
            return ActionResult.FAIL;
        }

        BlockPos placePos = clickedPos.offset(side);

        BlockState placementState;
        if (side == Direction.DOWN) {
            placementState = this.hangingSignBlock.getDefaultState();
            placementState = this.hangingSignBlock.getPlacementState(new ItemPlacementContext(context));
        } else {
            placementState = this.wallHangingSignBlock.getDefaultState();
            placementState = this.wallHangingSignBlock.getPlacementState(new ItemPlacementContext(context));
        }

        if (placementState == null) {
            return ActionResult.FAIL;
        }

        if (!world.setBlockState(placePos, placementState, Block.NOTIFY_ALL)) {
            return ActionResult.FAIL;
        }

        if (!world.isClient) {
            if (player != null && world.getBlockEntity(placePos) instanceof HangingSignBlockEntity signBlockEntity) {
                signBlockEntity.setEditor(player.getUuid());
                player.openEditSignScreen(signBlockEntity);

                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
        }

        return ActionResult.success(world.isClient);
    }
}