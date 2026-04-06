package net.ryan.beyond_the_block.feature.interaction.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.content.enchantment.MyEnchantmentHelper;
import net.ryan.beyond_the_block.feature.interaction.BreakContext;

import java.util.ArrayList;
import java.util.List;

public final class CropDropHandler {

    private CropDropHandler() {
    }

    public static void handle(ServerPlayerEntity player, ServerWorld world, BreakContext context) {
        if (!(context.getState().getBlock() instanceof CropBlock)) {
            return;
        }

        int bountyLevel = EnchantmentHelper.getLevel(ModEnchantments.GARDENS_BOUNTY, context.getTool());
        int nightLevel = EnchantmentHelper.getLevel(ModEnchantments.NIGHT_CULTIVATION, context.getTool());

        if (bountyLevel > 0) {
            enhanceCropDrops(world, context, player, bountyLevel, true);
        }

        if (nightLevel > 0 && BreakSuppressionHandler.isDarkArea(world, player)) {
            enhanceCropDrops(world, context, player, nightLevel, false);
        }
    }

    private static void enhanceCropDrops(ServerWorld world, BreakContext context, ServerPlayerEntity player, int level, boolean isBounty) {
        List<ItemStack> drops = Block.getDroppedStacks(
                context.getState(),
                world,
                context.getPos(),
                null,
                player,
                context.getTool()
        );

        List<ItemStack> enhanced = new ArrayList<>(drops);

        for (int i = 0; i < level; i++) {
            for (ItemStack drop : drops) {
                enhanced.add(drop.copy());
            }
        }

        if (level >= 2 && isBounty) {
            enhanced.add(new ItemStack(world.random.nextBoolean() ? Items.GOLDEN_CARROT : Items.GOLDEN_APPLE));
        }

        MyEnchantmentHelper.giveDropsWithMode(world, context.getPos(), player, enhanced);
    }
}