package net.ryan.beyond_the_block.content.enchantment.pets.enchantments;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantHooks;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantment;

public class DeflectingEnchantment extends PetEnchantment implements PetEnchantHooks {

    public DeflectingEnchantment() {
        super(Rarity.RARE);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean onDamage(LivingEntity pet, DamageSource source, float amount, int level) {
        if (!source.isProjectile()) {
            return false;
        }

        float chance = switch (level) {
            case 1 -> 0.25F;
            case 2 -> 0.50F;
            case 3 -> 0.75F;
            default -> 0.25F;
        };

        if (pet.getRandom().nextFloat() < chance) {
            // optional feedback (particles/sound later)
            return true;
        }

        return false;
    }
}