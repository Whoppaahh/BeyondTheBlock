package net.ryan.beyond_the_block.content.enchantment.pets.enchantments;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantHooks;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantment;

import java.util.UUID;

public class SpeedsterEnchantment extends PetEnchantment implements PetEnchantHooks {

    private static final UUID MODIFIER_ID =
            UUID.fromString("9c2820ef-7ec8-4d7d-a9d8-7d861bc4d8e1");

    public SpeedsterEnchantment() {
        super(Rarity.UNCOMMON);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void onTick(LivingEntity pet, int level) {
        var attr = pet.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (attr == null) return;

        double amount = 0.04D * level;

        EntityAttributeModifier existing = attr.getModifier(MODIFIER_ID);
        if (existing != null && existing.getValue() == amount) return;

        attr.removeModifier(MODIFIER_ID);
        attr.addPersistentModifier(new EntityAttributeModifier(
                MODIFIER_ID,
                "BTB pet speedster",
                amount,
                EntityAttributeModifier.Operation.ADDITION
        ));
    }
}