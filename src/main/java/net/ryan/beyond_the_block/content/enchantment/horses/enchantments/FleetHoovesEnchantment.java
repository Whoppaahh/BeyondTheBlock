package net.ryan.beyond_the_block.content.enchantment.horses.enchantments;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.ryan.beyond_the_block.content.enchantment.horses.HorseEnchantment;
import net.ryan.beyond_the_block.feature.horses.HorseEnchantHooks;

import java.util.UUID;

public class FleetHoovesEnchantment extends HorseEnchantment implements HorseEnchantHooks {

    private static final UUID MODIFIER_ID =
            UUID.fromString("e8b1a9f4-0c7c-4d36-9d0b-3e4e5d3b8a11");


    public FleetHoovesEnchantment() {
        super(Rarity.UNCOMMON);
    }

    public static void remove(AbstractHorseEntity horse) {
        var attr = horse.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (attr != null) {
            attr.removeModifier(MODIFIER_ID);
        }
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void onTick(AbstractHorseEntity horse, int level) {
        var attr = horse.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (attr == null) return;

        double amount = 0.05D * level;

        var existing = attr.getModifier(MODIFIER_ID);
        if (existing != null && existing.getValue() == amount) return;

        attr.removeModifier(MODIFIER_ID);

        attr.addPersistentModifier(new EntityAttributeModifier(
                MODIFIER_ID,
                "BTB horse speed",
                amount,
                EntityAttributeModifier.Operation.ADDITION
        ));
    }
}