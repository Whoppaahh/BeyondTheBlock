package net.ryan.beyond_the_block.content.enchantment.pets;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.feature.pets.PetCollarAccessor;

import java.util.Map;

public final class PetEnchantDispatcher {

    private PetEnchantDispatcher() {
    }

    private static Map<Enchantment, Integer> getEnchantments(LivingEntity pet) {
        if (!(pet instanceof PetCollarAccessor accessor)) {
            return Map.of();
        }

        ItemStack collar = accessor.btb$getCollar();
        if (collar.isEmpty()) {
            return Map.of();
        }

        return EnchantmentHelper.get(collar);
    }

    public static void tick(LivingEntity pet) {
        for (Map.Entry<Enchantment, Integer> entry : getEnchantments(pet).entrySet()) {
            if (entry.getKey() instanceof PetEnchantHooks hooks) {
                hooks.onTick(pet, entry.getValue());
            }
        }
    }
    public static boolean damage(LivingEntity pet, DamageSource source, float amount) {
        for (var entry : getEnchantments(pet).entrySet()) {
            if (entry.getKey() instanceof PetEnchantHooks hooks) {
                if (hooks.onDamage(pet, source, amount, entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean statusEffectApplied(LivingEntity pet, StatusEffectInstance effect) {
        for (var entry : getEnchantments(pet).entrySet()) {
            if (entry.getKey() instanceof PetEnchantHooks hooks) {
                if (hooks.onStatusEffectApplied(pet, effect, entry.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }
    public static void ownerDistanceCheck(LivingEntity pet, PlayerEntity owner, double maxDistance) {
        if (pet.squaredDistanceTo(owner) <= maxDistance * maxDistance) {
            return;
        }

        for (var entry : getEnchantments(pet).entrySet()) {
            if (entry.getKey() instanceof PetEnchantHooks hooks) {
                hooks.onOwnerTooFar(pet, owner, entry.getValue());
            }
        }
    }
    public static boolean itemPickup(LivingEntity pet, PlayerEntity owner, ItemStack stack) {
        for (var entry : getEnchantments(pet).entrySet()) {
            if (entry.getKey() instanceof PetEnchantHooks hooks) {
                if (hooks.onItemPickup(pet, owner, stack, entry.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }
}