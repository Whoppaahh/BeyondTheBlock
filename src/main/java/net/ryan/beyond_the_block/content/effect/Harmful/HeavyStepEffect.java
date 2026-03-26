package net.ryan.beyond_the_block.content.effect.Harmful;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.UUID;

public class HeavyStepEffect extends StatusEffect {

    private static final UUID KNOCKBACK_RESISTANCE_ID = UUID.fromString("7f8e5f3b-1bc4-41cf-91b1-1e9cf63c6f9e");
    private static final UUID MOVEMENT_SLOWDOWN_ID = UUID.fromString("a5cb4450-5f29-4c8b-8e01-f91e0ddea444");

    public HeavyStepEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
        this.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_ID.toString(), 1.0D, EntityAttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, MOVEMENT_SLOWDOWN_ID.toString(), -0.2D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        // Optional: play heavy step sound or spawn particles
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        // Optional: cleanup or effect end visuals
    }
}
