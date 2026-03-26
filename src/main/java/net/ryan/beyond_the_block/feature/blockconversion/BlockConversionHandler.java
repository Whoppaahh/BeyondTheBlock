package net.ryan.beyond_the_block.feature.blockconversion;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.utils.Helpers.SandToGlassManager;

public class BlockConversionHandler {
    public static ActionResult handleBlockConversion(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
        ActionResult result;

        result = convertDirtPath(player, world, hand, hit);
        if (result != ActionResult.PASS) return result;

        result = convertCraftingTable(player, world, hand, hit);
        if (result != ActionResult.PASS) return result;

        result = convertFurnace(player, world, hand, hit);
        return result;
    }

    private static ActionResult convertDirtPath(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult) {
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);
        BlockState stateBelow = world.getBlockState(pos.down());
        ItemStack stack = playerEntity.getStackInHand(hand);

        // 1. Placing fence/wall on top of path
        if (state.isAir() && stateBelow.isOf(Blocks.DIRT_PATH) && stack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof FenceBlock || block instanceof FenceGateBlock || block instanceof WallBlock) {
                world.setBlockState(pos, block.getDefaultState(), 3); // place fence/wall on top
                return ActionResult.SUCCESS;
            }
        }

        if (stack.getItem() instanceof ShovelItem) {
            // 2. Convert dirt/grass under existing fence/wall to path
            if (!world.isClient && (state.isOf(Blocks.DIRT) || state.isOf(Blocks.GRASS_BLOCK))) {
                BlockState above = world.getBlockState(pos.up());
                if (above.getBlock() instanceof FenceBlock || above.getBlock() instanceof FenceGateBlock || above.getBlock() instanceof WallBlock) {
                    world.setBlockState(pos, Blocks.DIRT_PATH.getDefaultState(), 3);
                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.PASS;
    }
    private static ActionResult convertCraftingTable(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (!world.isClient() && player.isSneaking()) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);

            if (state.getBlock() == Blocks.CRAFTING_TABLE) {
                int requiredXpLevels = 15; // change as needed
                if (player.experienceLevel >= requiredXpLevels) {
                    // Consume XP
                    player.addExperienceLevels(-requiredXpLevels);

                    // Replace with custom table
                    world.setBlockState(pos, ModBlocks.DECRAFTER_BLOCK.getDefaultState());

                    // Optional: play sound/particles
                    world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    ((ServerWorld) world).spawnParticles(ParticleTypes.ENCHANT,
                            pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                            20, 0.3, 0.5, 0.3, 0.1);

                    return ActionResult.SUCCESS;
                } else {
                    player.sendMessage(Text.literal("You need " + requiredXpLevels + " experience levels to upgrade this table.").formatted(Formatting.RED), true);
                    return ActionResult.FAIL;
                }
            }
        }
        return ActionResult.PASS;
    }

    public static void queueAdjacentSand(World world, BlockPos lavaPos) {
        if (!(world instanceof ServerWorld)) return;
        for (BlockPos neighbor : BlockPos.iterateOutwards(lavaPos, 1, 1, 1)) {
            BlockState state = world.getBlockState(neighbor);
            if (state.isOf(Blocks.SAND) || state.isOf(Blocks.RED_SAND)) {
                BeyondTheBlock.LOGGER.info("Queuing {} at {}", state.getBlock(), neighbor);
                SandToGlassManager.queueSand(neighbor.toImmutable(), state);
            }
        }
    }
}
