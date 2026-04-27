package net.ryan.beyond_the_block.content.enchantment.pets.enchantments;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantHooks;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantment;

public class FireproofEnchantment extends PetEnchantment implements PetEnchantHooks {

    public FireproofEnchantment() {
        super(Rarity.RARE);
    }

    @Override
    public boolean onDamage(LivingEntity pet, DamageSource source, float amount, int level) {
        return source.isFire();
    }
}
