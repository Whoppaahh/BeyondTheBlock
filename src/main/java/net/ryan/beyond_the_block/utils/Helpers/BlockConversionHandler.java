package net.ryan.beyond_the_block.utils.Helpers;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.block.Entity.InfiFurnaceBlockEntity;
import net.ryan.beyond_the_block.block.ModBlocks;
import net.ryan.beyond_the_block.block.PlayerVaultBlock;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.enchantment.MyEnchantmentHelper;
import net.ryan.beyond_the_block.item.ModItems;
import net.ryan.beyond_the_block.utils.Accessors.FurnaceAccessor;

import java.util.function.BiFunction;

public class BlockConversionHandler {

    /**
     * Registers all block-related event callbacks.
     */
    public static void register() {
        UseBlockCallback.EVENT.register(BlockConversionHandler::onBlockUse);
        PlayerBlockBreakEvents.BEFORE.register(PlayerVaultBlock::handleBreak);
    }

    /**
     * Handles all block-use interactions.
     * Dispatches logic to specialized converters only when conditions match.
     */
    private static ActionResult onBlockUse(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.PASS;

        BlockPos pos = hit.getBlockPos();
        BlockState state = world.getBlockState(pos);
        ItemStack held = player.getStackInHand(hand);

        if (state.getBlock() instanceof DoorBlock
                || state.getBlock() instanceof TrapdoorBlock
                || state.getBlock() instanceof FenceGateBlock) {
            return ActionResult.PASS;
        }

        // Only run block conversions when the player is sneaking or holding a special item
        if (!player.isSneaking() && !(held.getItem() instanceof BlockItem) && !hasRelevantEnchant(held))
            return ActionResult.PASS;

        // Run conversions in short-circuit order for speed
        return tryConvert(player, world, held, state, pos,
                BlockConversionHandler::convertFurnace,
                BlockConversionHandler::convertCraftingTable,
                BlockConversionHandler::convertDirtPath,
                BlockConversionHandler::convertByEnchantment
        );
    }

    /**
     * Runs conversion handlers in order until one succeeds.
     */
    @SafeVarargs
    private static ActionResult tryConvert(
            PlayerEntity player, World world, ItemStack held, BlockState state, BlockPos pos,
            BiFunction<PlayerEntity, ConversionContext, ActionResult>... converters) {

        ConversionContext ctx = new ConversionContext(world, pos, state, held);
        for (BiFunction<PlayerEntity, ConversionContext, ActionResult> handler : converters) {
            ActionResult result = handler.apply(player, ctx);
            if (result != ActionResult.PASS) return result;
        }
        return ActionResult.PASS;
    }

    // -----------------------------
    // Conversion logic
    // -----------------------------

    private static ActionResult convertFurnace(PlayerEntity player, ConversionContext ctx) {
        if (!(ctx.state.getBlock() instanceof FurnaceBlock)) return ActionResult.PASS;
        if (!ctx.held.isOf(ModItems.ECLIPSED_BLOOM) || !player.isSneaking()) return ActionResult.PASS;

        BlockEntity be = ctx.world.getBlockEntity(ctx.pos);
        if (!(be instanceof FurnaceBlockEntity furnaceBE)) return ActionResult.PASS;
        if (!furnaceBE.getStack(1).isOf(Items.LAVA_BUCKET)) return ActionResult.PASS;

        // Cache data before replacing
        ItemStack input = furnaceBE.getStack(0).copy();
        ItemStack output = furnaceBE.getStack(2).copy();
        int cook = (be instanceof FurnaceAccessor a) ? a.btb$getCookTime() : 0;
        int total = (be instanceof FurnaceAccessor a) ? a.btb$getCookTimeTotal() : 0;

        Direction facing = ctx.state.get(FurnaceBlock.FACING);
        ctx.world.setBlockState(ctx.pos, ModBlocks.INFI_FURNACE_BLOCK.getDefaultState().with(FurnaceBlock.FACING, facing), Block.NOTIFY_ALL);

        BlockEntity newBe = ctx.world.getBlockEntity(ctx.pos);
        if (newBe instanceof InfiFurnaceBlockEntity infi) {
            infi.setStack(0, input);
            infi.setStack(1, output);
            infi.getPropertyDelegate().set(0, cook);
            infi.getPropertyDelegate().set(1, total);
            infi.markDirty();
        }

        if (!player.isCreative()) ctx.held.decrement(1);
        return ActionResult.SUCCESS;
    }

    private static ActionResult convertCraftingTable(PlayerEntity player, ConversionContext ctx) {
        if (!ctx.state.isOf(Blocks.CRAFTING_TABLE) || !player.isSneaking()) return ActionResult.PASS;

        final int xpCost = 15;
        if (player.experienceLevel < xpCost) {
            player.sendMessage(Text.literal("Requires " + xpCost + " levels").formatted(Formatting.RED), true);
            return ActionResult.FAIL;
        }

        player.addExperienceLevels(-xpCost);
        ctx.world.setBlockState(ctx.pos, ModBlocks.DECRAFTER_BLOCK.getDefaultState());
        ctx.world.playSound(null, ctx.pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);

        if (ctx.world instanceof ServerWorld sw) {
            sw.spawnParticles(ParticleTypes.ENCHANT,
                    ctx.pos.getX() + 0.5, ctx.pos.getY() + 1, ctx.pos.getZ() + 0.5,
                    20, 0.3, 0.5, 0.3, 0.1);
        }
        return ActionResult.SUCCESS;
    }

    private static ActionResult convertDirtPath(PlayerEntity player, ConversionContext ctx) {
        Item item = ctx.held.getItem();
        BlockState below = ctx.world.getBlockState(ctx.pos.down());

        // Place fence/wall/gate on path
        if (ctx.state.isAir() && below.isOf(Blocks.DIRT_PATH) && item instanceof BlockItem bi && isFenceType(bi.getBlock())) {
            ctx.world.setBlockState(ctx.pos, bi.getBlock().getDefaultState(), 3);
            return ActionResult.SUCCESS;
        }

        // Convert dirt under fence
        if (item instanceof ShovelItem && (ctx.state.isOf(Blocks.DIRT) || ctx.state.isOf(Blocks.GRASS_BLOCK))) {
            Block above = ctx.world.getBlockState(ctx.pos.up()).getBlock();
            if (isFenceType(above)) {
                ctx.world.setBlockState(ctx.pos, Blocks.DIRT_PATH.getDefaultState(), 3);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    private static ActionResult convertByEnchantment(PlayerEntity player, ConversionContext ctx) {
        int till = EnchantmentHelper.getLevel(ModEnchantments.DEEP_TILL, ctx.held);
        int bark = EnchantmentHelper.getLevel(ModEnchantments.BARKSKIN, ctx.held);

        if (till > 0) {
            MyEnchantmentHelper.tillArea(ctx.world, ctx.pos, till);
            return ActionResult.SUCCESS;
        }
        if (bark > 0) {
            MyEnchantmentHelper.handleTreeStripping(player, ctx.held, ctx.pos, ctx.world);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    // -----------------------------
    // Utility / helpers
    // -----------------------------

    private static boolean isFenceType(Block block) {
        return block instanceof FenceBlock || block instanceof FenceGateBlock || block instanceof WallBlock;
    }

    private static boolean hasRelevantEnchant(ItemStack stack) {
        return EnchantmentHelper.getLevel(ModEnchantments.DEEP_TILL, stack) > 0
                || EnchantmentHelper.getLevel(ModEnchantments.BARKSKIN, stack) > 0;
    }

    private record ConversionContext(World world, BlockPos pos, BlockState state, ItemStack held) {}
}
