package net.ryan.beyond_the_block.content.enchantment.pets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface PetEnchantHooks {
    default void onTick(LivingEntity pet, int level) {
    }
    default boolean onDamage(
            LivingEntity pet,
            DamageSource source,
            float amount,
            int level
    ) {
        return false;
    }
    default boolean onStatusEffectApplied(LivingEntity pet, StatusEffectInstance effect, int level) {
        return false;
    }
    default void onOwnerTooFar(LivingEntity pet, PlayerEntity owner, int level) {
    }
    default boolean onItemPickup(LivingEntity pet, PlayerEntity owner, ItemStack stack, int level) {
        return false;
    }
}