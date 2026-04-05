package net.ryan.beyond_the_block.feature.player.movement;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;

public final class FrozenMomentumHandler {
    private FrozenMomentumHandler() {}

    public static Float getMovementSpeedOverride(PlayerEntity player) {
        int level = EnchantmentHelper.getLevel(
                ModEnchantments.FROZEN_MOMENTUM,
                player.getEquippedStack(EquipmentSlot.LEGS)
        );
        if (level <= 0) return null;

        BlockState state = player.getWorld().getBlockState(player.getBlockPos().down());
        if (!(state.isOf(Blocks.SNOW) || state.isOf(Blocks.ICE) || state.isOf(Blocks.PACKED_ICE))) {
            return null;
        }

        float speed = 0.1f;
        if (level == 1) speed += 0.1f;
        else if (level >= 2) speed += 0.2f;

        return speed;
    }

    public static void onTravel(PlayerEntity player, Vec3d movementInput) {
        int level = EnchantmentHelper.getLevel(
                ModEnchantments.FROZEN_MOMENTUM,
                player.getEquippedStack(EquipmentSlot.LEGS)
        );
        if (level <= 0) return;

        BlockState state = player.getWorld().getBlockState(player.getBlockPos().down());

        double boost;
        if (state.isOf(Blocks.ICE) || state.isOf(Blocks.PACKED_ICE)) {
            boost = 0.03;
        } else if (state.isOf(Blocks.SNOW)) {
            boost = 0.015;
        } else {
            return;
        }

        Vec3d input = new Vec3d(movementInput.x, 0, movementInput.z);
        if (input.lengthSquared() < 1.0E-6) return;

        Vec3d normalized = input.normalize().multiply(boost);
        player.addVelocity(normalized.x, 0.0, normalized.z);
    }
}