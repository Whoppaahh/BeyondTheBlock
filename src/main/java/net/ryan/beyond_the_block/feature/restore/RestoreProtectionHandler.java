package net.ryan.beyond_the_block.feature.restore;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.block.Cauldrons.DyedWaterCauldronBlock;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.blockentity.DyedWaterCauldronBlockEntity;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.content.enchantment.MyEnchantmentHelper;
import net.ryan.beyond_the_block.feature.cauldrons.ModdedFluidCauldronHandler;
import net.ryan.beyond_the_block.utils.Helpers.RestoreManager;

public class RestoreProtectionHandler {
    private static TypedActionResult<ItemStack> onItemUsed(
            PlayerEntity player,
            World world,
            Hand hand
    ) {
        ItemStack stack = player.getStackInHand(hand);

        // Only care about block items
        if (!(stack.getItem() instanceof BlockItem)) {
            return TypedActionResult.pass(stack);
        }

        // Raycast MUST run on both sides
        BlockHitResult hit = (BlockHitResult) player.raycast(5.0D, 0.0F, false);
        if (hit == null) {
            return TypedActionResult.pass(stack);
        }

        BlockPos placePos = hit.getBlockPos().offset(hit.getSide());

        // CLIENT-SIDE BLOCK (prevents prediction)
        if (world.isClient) {
            if (world instanceof ClientWorld clientWorld
                    && RestoreManager.isProtectedClient(placePos)) {
                return TypedActionResult.fail(stack);
            }
            return TypedActionResult.pass(stack);
        }

        // SERVER-SIDE BLOCK (authoritative)
        if (world instanceof ServerWorld serverWorld) {
            if (RestoreManager.isProtected(serverWorld, placePos)) {
                player.sendMessage(
                        Text.literal("This area is restoring.")
                                .formatted(Formatting.GRAY),
                        true
                );
                return TypedActionResult.fail(stack);
            }
        }

        return TypedActionResult.pass(stack);
    }
    private static ActionResult onBlockUsed(
            PlayerEntity player,
            World world,
            Hand hand,
            BlockHitResult hit
    ) {

        BlockState blockState = world.getBlockState(hit.getBlockPos());
        if (blockState.contains(Properties.OPEN)) {
            return ActionResult.PASS;
        }

        BlockPos placePos = hit.getBlockPos().offset(hit.getSide());

        // =========================
        // CLIENT: stop prediction
        // =========================
        if (world.isClient) {
            if (RestoreManager.isProtectedClient(placePos)) {
                player.sendMessage(
                        Text.literal("This area is restoring.")
                                .formatted(Formatting.GRAY),
                        true
                );
                return ActionResult.FAIL; // hard stop
            }
            return ActionResult.PASS; // allow prediction
        }

        // =========================
        // SERVER: stop authority
        // =========================
        if (world instanceof ServerWorld serverWorld) {
            if (RestoreManager.isProtected(serverWorld, placePos)) {
                player.sendMessage(
                        Text.literal("This area is restoring.")
                                .formatted(Formatting.GRAY),
                        true
                );
                return ActionResult.FAIL; // hard stop
            }
        }

        // =========================
        // NORMAL LOGIC CONTINUES
        // =========================

        // Lava → sand logic
        if (world instanceof ServerWorld && world.getFluidState(placePos).isOf(Fluids.LAVA)
                && world.getFluidState(placePos).isStill()) {
            queueAdjacentSand(world, placePos.toImmutable());
        }

        BlockPos pos = hit.getBlockPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = player.getStackInHand(hand);

        // Water cauldron dye logic
        if (state.isOf(Blocks.WATER_CAULDRON) && stack.getItem() instanceof DyeItem dyeItem) {
            int rgb = DyedWaterCauldronBlock.toRgb(dyeItem.getColor());

            world.setBlockState(
                    pos,
                    ModBlocks.DYED_WATER_CAULDRON_BLOCK.getDefaultState()
                            .with(LeveledCauldronBlock.LEVEL,
                                    state.get(LeveledCauldronBlock.LEVEL)),
                    3
            );

            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof DyedWaterCauldronBlockEntity dyed) {
                dyed.mixDye(rgb);
            }

            if (!player.isCreative()) stack.decrement(1);

            world.playSound(null, pos,
                    SoundEvents.ITEM_DYE_USE,
                    SoundCategory.BLOCKS, 1, 1.1f);

            return ActionResult.SUCCESS;
        }

        ActionResult cauldronResult =
                ModdedFluidCauldronHandler.handleCauldronUse(player, world, hand, hit);
        if (cauldronResult != ActionResult.PASS) {
            return cauldronResult;
        }

        int tillLevel = EnchantmentHelper.getLevel(ModEnchantments.DEEP_TILL, stack);
        if (tillLevel > 0) {
            MyEnchantmentHelper.tillArea(world, pos, tillLevel);
            return ActionResult.SUCCESS;
        }

        int barkskinLevel = EnchantmentHelper.getLevel(ModEnchantments.BARKSKIN, stack);
        if (barkskinLevel > 0) {
            MyEnchantmentHelper.handleTreeStripping(player, stack, pos, world);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
