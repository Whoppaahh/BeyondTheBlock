package net.ryan.beyond_the_block.content.enchantment.Armour.helmets;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;

public class ShadowsVeilEnchantment extends Enchantment {

    private static boolean effectApplied = false;

    public ShadowsVeilEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armour && armour.getSlotType() == EquipmentSlot.HEAD || stack.isOf(Items.BOOK);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }



    // Apply the effect to the player on each tick (or during certain conditions)
    public static void applyShadowsVeilEffect(LivingEntity entity) {
        // Check if the entity is wearing a helmet with Shadows Veil
        ItemStack helmet = entity.getEquippedStack(EquipmentSlot.HEAD);
        int level = EnchantmentHelper.getLevel(ModEnchantments.SHADOWS_VEIL, helmet); // Get the enchantment level

        // If the player is wearing a helmet with Shadows Veil enchantment and the level is greater than 0
        if (level > 0) {
            if (entity instanceof PlayerEntity player) {
                boolean shouldApplyInvisibility = isShouldApplyInvisibility(player, level);

                // Apply or remove the invisibility effect based on the condition
                if (shouldApplyInvisibility) {
                    // Apply invisibility effect (Duration of 100 ticks, and amplifier based on enchantment level)
                    if (!player.hasStatusEffect(StatusEffects.INVISIBILITY)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, Integer.MAX_VALUE, level - 1, false, false, false));
                        effectApplied = true;
                    }
                } else {
                    // Remove the invisibility effect if the conditions are no longer met
                    if(effectApplied) {
                        player.removeStatusEffect(StatusEffects.INVISIBILITY);
                        effectApplied = false;
                    }
                }
            }
        } else {
            // If no enchantment is found, ensure invisibility is removed
            if (entity instanceof PlayerEntity player) {
                if(effectApplied) {
                    player.removeStatusEffect(StatusEffects.INVISIBILITY);
                    effectApplied = false;
                }
            }
        }
    }

    private static boolean isShouldApplyInvisibility(PlayerEntity player, int level) {
        boolean shouldApplyInvisibility = false;

        // Apply invisibility based on the enchantment level
        if (level == 1) {
            // For level 1, apply invisibility if the player is sneaking or not moving

            shouldApplyInvisibility = player.isSneaking();
        } else if (level == 2) {
            // For level 2, always apply invisibility when the enchantment is equipped
            shouldApplyInvisibility = true;
        }
        return shouldApplyInvisibility;
    }


    public static void registerTickHandler(ServerWorld serverWorld) {
        for (ServerPlayerEntity player : serverWorld.getPlayers()) {
            applyShadowsVeilEffect(player);
        }
    }
}