package net.ryan.beyond_the_block.feature.combat;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import org.jetbrains.annotations.Nullable;

public final class CombatEnchantmentHandler {
    private CombatEnchantmentHandler() {
    }

    public static ActionResult onEntityAttacked(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hit) {
        if (!(entity instanceof LivingEntity target)) {
            return ActionResult.PASS;
        }

        ItemStack stack = player.getStackInHand(hand);

        int flameSweepLevel = EnchantmentHelper.getLevel(ModEnchantments.FLAME_SWEEP, stack);

        if (flameSweepLevel > 0) {
            FlameSweepHandler.apply(player, world, target, stack, flameSweepLevel);
        }

        // Let vanilla attack damage continue either way.
        return ActionResult.PASS;
    }
}