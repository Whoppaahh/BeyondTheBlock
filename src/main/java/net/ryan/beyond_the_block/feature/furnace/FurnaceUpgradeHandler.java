package net.ryan.beyond_the_block.feature.furnace;

import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.InfiFurnaceBlockEntity;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.utils.accessors.FurnaceAccessor;

public class FurnaceUpgradeHandler {
    public static ActionResult convertFurnace(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (!(state.getBlock() instanceof FurnaceBlock)) return ActionResult.PASS;
        if (world.isClient()) return ActionResult.PASS;

        ItemStack heldStack = player.getStackInHand(hand);

        // Check for specific item
        if (!(heldStack.isOf(ModItems.ECLIPSED_BLOOM) || heldStack.isOf(Items.NETHERRACK))) return ActionResult.PASS;
        if (!player.isSneaking()) return ActionResult.PASS;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof FurnaceBlockEntity furnaceBE)) return ActionResult.PASS;

        // Check the fuel slot (slot 1 in FurnaceBlockEntity)
        ItemStack fuelStack = furnaceBE.getStack(1);
        if (!(fuelStack.isOf(Items.LAVA_BUCKET))) return ActionResult.PASS;

        ItemStack inputStack = furnaceBE.getStack(0).copy();
        ItemStack outputStack = furnaceBE.getStack(2).copy();

        int cookTime = 0, cookTimeTotal = 0;
        if (furnaceBE instanceof FurnaceAccessor accessor) {
            cookTime = accessor.btb$getCookTime();
            cookTimeTotal = accessor.btb$getCookTimeTotal();
        }

        // Replace the block
        Direction facing = state.get(FurnaceBlock.FACING);
        BlockState newState = ModBlocks.INFI_FURNACE_BLOCK.getDefaultState().with(FurnaceBlock.FACING, facing);
        world.setBlockState(pos, newState, FurnaceBlock.NOTIFY_ALL);

        // Transfer block entity data
        BlockEntity newBE = world.getBlockEntity(pos);
        if (newBE instanceof InfiFurnaceBlockEntity customBE) {
            customBE.setStack(0, inputStack);
            customBE.setStack(1, outputStack);

            // Set cook times if your custom furnace supports it
            customBE.getPropertyDelegate().set(0, cookTime);
            customBE.getPropertyDelegate().set(1, cookTimeTotal);
            customBE.markDirty();
        }

        // Consume the item (optional)
        if (!player.isCreative()) {
            heldStack.decrement(1);
        }

        return ActionResult.SUCCESS;
    }
}
