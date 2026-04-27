package net.ryan.beyond_the_block.content.enchantment.pets.enchantments;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantHooks;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantment;

public class PoisonResistanceEnchantment extends PetEnchantment implements PetEnchantHooks {

    public PoisonResistanceEnchantment() {
        super(Rarity.UNCOMMON);
    }

    @Override
    public boolean onStatusEffectApplied(LivingEntity pet, StatusEffectInstance effect, int level) {
        return effect.getEffectType() == StatusEffects.POISON;
    }
}