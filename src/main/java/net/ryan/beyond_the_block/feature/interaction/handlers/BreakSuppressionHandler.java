package net.ryan.beyond_the_block.feature.interaction.handlers;

import net.minecraft.block.CropBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.PickaxeItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import net.ryan.beyond_the_block.feature.interaction.BreakContext;

public final class BreakSuppressionHandler {

    private BreakSuppressionHandler() {
    }

    public static void configureSuppression(ServerPlayerEntity player, ServerWorld world, BreakContext context) {
        context.setSuppressVanillaDrops(false);

        int shadowMiningLevel = EnchantmentHelper.getLevel(ModEnchantments.SHADOW_MINING, context.getTool());
        int darkDigLevel = EnchantmentHelper.getLevel(ModEnchantments.DARK_DIG, context.getTool());
        int bountyLevel = EnchantmentHelper.getLevel(ModEnchantments.GARDENS_BOUNTY, context.getTool());
        int nightLevel = EnchantmentHelper.getLevel(ModEnchantments.NIGHT_CULTIVATION, context.getTool());

        boolean isCrop = context.getState().getBlock() instanceof CropBlock;

        if (shadowMiningLevel > 0
                && context.getTool().getItem() instanceof PickaxeItem
                && context.getState().isIn(BlockTags.PICKAXE_MINEABLE)
                && isDarkArea(world, player)) {
            context.setSuppressVanillaDrops(true);
        }

        if (darkDigLevel > 0
                && context.getState().isIn(BlockTags.SHOVEL_MINEABLE)
                && isDarkArea(world, player)) {
            context.setSuppressVanillaDrops(true);
        }

        if (bountyLevel > 0 && isCrop) {
            context.setSuppressVanillaDrops(true);
        }

        if (nightLevel > 0 && isCrop && isDarkArea(world, player)) {
            context.setSuppressVanillaDrops(true);
        }
    }

    public static boolean isDarkArea(ServerWorld world, ServerPlayerEntity player) {
        return world.isNight()
                || player.getWorld().getLightLevel(player.getBlockPos()) < 5
                || !player.getWorld().getDimension().bedWorks();
    }
}