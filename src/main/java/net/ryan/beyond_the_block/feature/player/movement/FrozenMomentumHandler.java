package net.ryan.beyond_the_block.feature.player.movement;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;

public final class FrozenMomentumHandler {
    private FrozenMomentumHandler() {}

    public static Float getMovementSpeedBonus(PlayerEntity player) {
        int level = EnchantmentHelper.getLevel(
                ModEnchantments.FROZEN_MOMENTUM,
                player.getEquippedStack(EquipmentSlot.LEGS)
        );
        if (level <= 0) return null;

        if (!isOnFrozenSurface(player)) {
            return null;
        }

        if (level == 1) {
            return 0.03f;
        } else {
            return 0.06f;
        }
    }

    public static boolean isOnFrozenSurface(PlayerEntity player) {
        BlockState feetState = player.getWorld().getBlockState(player.getBlockPos());
        BlockState belowState = player.getWorld().getBlockState(player.getBlockPos().down());

        return isFrozenBlock(feetState) || isFrozenBlock(belowState);
    }

    private static boolean isFrozenBlock(BlockState state) {
        return state.isOf(Blocks.SNOW)
                || state.isOf(Blocks.SNOW_BLOCK)
                || state.isOf(Blocks.ICE)
                || state.isOf(Blocks.PACKED_ICE)
                || state.isOf(Blocks.BLUE_ICE);
    }
}