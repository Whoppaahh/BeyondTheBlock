package net.ryan.beyond_the_block.feature.interaction.handlers;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.content.enchantment.MyEnchantmentHelper;
import net.ryan.beyond_the_block.feature.interaction.BreakContext;

import java.util.ArrayList;
import java.util.List;

public final class ShadowMiningHandler {

    private ShadowMiningHandler() {
    }

    public static void handle(ServerPlayerEntity player, ServerWorld world, BreakContext context) {
        int level = EnchantmentHelper.getLevel(ModEnchantments.SHADOW_MINING, context.getTool());
        if (level <= 0) {
            return;
        }

        if (!(context.getTool().getItem() instanceof PickaxeItem)) {
            return;
        }

        if (!context.getState().isIn(BlockTags.PICKAXE_MINEABLE)) {
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

        List<ItemStack> multipliedDrops = multiplyDrops(drops, level);
        MyEnchantmentHelper.giveDropsWithMode(world, context.getPos(), player, multipliedDrops);
    }

    static List<ItemStack> multiplyDrops(List<ItemStack> drops, int level) {
        List<ItemStack> multipliedDrops = new ArrayList<>(drops);

        for (ItemStack drop : drops) {
            if (drop.isEmpty()) {
                continue;
            }

            int totalCount = drop.getCount() * level;
            int maxCount = drop.getMaxCount();

            while (totalCount > 0) {
                int splitCount = Math.min(totalCount, maxCount);
                ItemStack split = drop.copy();
                split.setCount(splitCount);
                multipliedDrops.add(split);
                totalCount -= splitCount;
            }
        }

        return multipliedDrops;
    }
}