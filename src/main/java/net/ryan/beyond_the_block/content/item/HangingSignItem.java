package net.ryan.beyond_the_block.content.item;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.block.HangingSignBlock;
import net.ryan.beyond_the_block.content.block.WallHangingSignBlock;
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

        // Do not allow replacing into an occupied non-replaceable block
        BlockState existingState = world.getBlockState(placePos);
        if (!existingState.getMaterial().isReplaceable()) {
            return ActionResult.FAIL;
        }

        BlockState placementState;

        if (side == Direction.DOWN) {
            // Ceiling hanging sign
            if (!(this.hangingSignBlock instanceof HangingSignBlock hangingBlock)) {
                return ActionResult.FAIL;
            }

            int rotation = Math.floorMod(
                    (int) Math.floor((180.0F + context.getPlayerYaw()) * 16.0F / 360.0F + 0.5D),
                    16
            );

            boolean waterlogged = world.getFluidState(placePos).getFluid() == Fluids.WATER;
            boolean attached = isAttachedVariant(world, placePos.up());

            placementState = hangingBlock.getDefaultState()
                    .with(HangingSignBlock.ROTATION, rotation)
                    .with(HangingSignBlock.WATERLOGGED, waterlogged)
                    .with(HangingSignBlock.ATTACHED, attached);

            if (!placementState.canPlaceAt(world, placePos)) {
                return ActionResult.FAIL;
            }

        } else {
            // Wall hanging sign
            if (!(this.wallHangingSignBlock instanceof WallHangingSignBlock wallBlock)) {
                return ActionResult.FAIL;
            }

            Direction facing = side.getOpposite();
            boolean waterlogged = world.getFluidState(placePos).getFluid() == Fluids.WATER;

            placementState = wallBlock.getDefaultState()
                    .with(WallHangingSignBlock.FACING, facing)
                    .with(WallHangingSignBlock.WATERLOGGED, waterlogged);

            if (!placementState.canPlaceAt(world, placePos)) {
                return ActionResult.FAIL;
            }
        }

        if (!world.setBlockState(placePos, placementState, Block.NOTIFY_ALL)) {
            return ActionResult.FAIL;
        }

        if (!world.isClient && player != null) {
            if (world.getBlockEntity(placePos) instanceof HangingSignBlockEntity signBlockEntity) {
                signBlockEntity.setEditor(player.getUuid());
                player.openEditSignScreen(signBlockEntity);
            }

            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }

        return ActionResult.success(world.isClient);
    }

    private boolean isAttachedVariant(World world, BlockPos supportPos) {
        BlockState supportState = world.getBlockState(supportPos);
        Block supportBlock = supportState.getBlock();

        return supportBlock instanceof FenceBlock
                || supportBlock instanceof WallBlock
                || supportBlock instanceof PaneBlock;
    }
}