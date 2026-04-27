package net.ryan.beyond_the_block.content.enchantment.pets.enchantments;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantHooks;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantment;

import java.util.UUID;

public class HealthBoostEnchantment extends PetEnchantment implements PetEnchantHooks {

    private static final UUID MODIFIER_ID =
            UUID.fromString("e6eacaa1-9c0a-4a3f-9f32-6e2f02a6d3a9");

    public HealthBoostEnchantment() {
        super(Rarity.UNCOMMON);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void onTick(LivingEntity pet, int level) {
        var attr = pet.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attr == null) {
            return;
        }

        double amount = 4.0D * level;

        EntityAttributeModifier existing = attr.getModifier(MODIFIER_ID);
        if (existing != null && existing.getValue() == amount) {
            return;
        }

        float oldMax = pet.getMaxHealth();

        attr.removeModifier(MODIFIER_ID);
        attr.addPersistentModifier(new EntityAttributeModifier(
                MODIFIER_ID,
                "BTB pet health boost",
                amount,
                EntityAttributeModifier.Operation.ADDITION
        ));

        float newMax = pet.getMaxHealth();

        if (pet.getHealth() < newMax && oldMax < newMax) {
            pet.heal(newMax - oldMax);
        }
    }
}