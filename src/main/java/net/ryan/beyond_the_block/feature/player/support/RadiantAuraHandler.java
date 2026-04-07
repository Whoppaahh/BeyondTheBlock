package net.ryan.beyond_the_block.feature.player.support;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;

public final class RadiantAuraHandler {
    private RadiantAuraHandler() {}

    public static void onTick(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (player.age % 20 != 0) return; // once per second

        int level = EnchantmentHelper.getLevel(
                ModEnchantments.RADIANT_AURA,
                player.getEquippedStack(EquipmentSlot.HEAD)
        );
        if (level <= 0) return;

        double radius = (level == 1) ? 10.0 : 15.0;
        float heal = (level == 1) ? 1.0f : 2.0f;

        Box box = player.getBoundingBox().expand(radius);

        player.getWorld().getEntitiesByClass(PlayerEntity.class, box, other -> other != player)
                .forEach(other -> other.heal(heal));
    }
}