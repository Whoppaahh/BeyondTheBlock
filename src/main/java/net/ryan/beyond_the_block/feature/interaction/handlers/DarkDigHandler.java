package net.ryan.beyond_the_block.feature.interaction.handlers;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.content.enchantment.MyEnchantmentHelper;
import net.ryan.beyond_the_block.feature.interaction.BreakContext;

import java.util.ArrayList;
import java.util.List;

public final class DarkDigHandler {

    private DarkDigHandler() {
    }

    public static void handle(ServerPlayerEntity player, ServerWorld world, BreakContext context) {
        int level = EnchantmentHelper.getLevel(ModEnchantments.DARK_DIG, context.getTool());
        if (level <= 0) {
            return;
        }

        if (!context.getState().isIn(BlockTags.SHOVEL_MINEABLE)) {
            return;
        }

        if (!BreakSuppressionHandler.isDarkArea(world, player)) {
            return;
        }

        List<ItemStack> drops = Block.getDroppedStacks(
                context.getState(),
                world,
                context.getPos(),
                context.getBlockEntity(),
                player,
                context.getTool()
        );

        List<ItemStack> multipliedDrops = new ArrayList<>(ShadowMiningHandler.multiplyDrops(drops, level));

        if (world.random.nextFloat() < 0.05f * level) {
            multipliedDrops.add(new ItemStack(Items.DIAMOND));
        }

        MyEnchantmentHelper.giveDropsWithMode(world, context.getPos(), player, multipliedDrops);
    }
}