package net.ryan.beyond_the_block.feature.interaction.handlers;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;

public final class FrozenMomentumInteractionHandler {

    private FrozenMomentumInteractionHandler() {
    }

    public static ActionResult handle(ServerPlayerEntity player, World world) {
        int level = EnchantmentHelper.getLevel(
                ModEnchantments.FROZEN_MOMENTUM,
                player.getEquippedStack(EquipmentSlot.LEGS)
        );

        if (level <= 0) {
            return ActionResult.PASS;
        }

        if (world.getBlockState(player.getBlockPos().down()).isOf(Blocks.POWDER_SNOW)) {
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }
}