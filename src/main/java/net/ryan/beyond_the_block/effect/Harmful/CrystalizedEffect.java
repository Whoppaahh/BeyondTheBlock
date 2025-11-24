package net.ryan.beyond_the_block.effect.Harmful;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.UUID;

public class CrystalizedEffect extends StatusEffect {
    private static final UUID ARMOR_REDUCTION_UUID = UUID.fromString("aa5d54e7-f30a-4563-989e-1234abcdef12");

    public CrystalizedEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
        addAttributeModifier(EntityAttributes.GENERIC_ARMOR, ARMOR_REDUCTION_UUID.toString(), -4.0D, EntityAttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.world.isClient()) {
            pLivingEntity.setVelocity(0, 0, 0);
            pLivingEntity.velocityModified = true;

            pLivingEntity.world.addParticle(
                    net.minecraft.particle.ParticleTypes.END_ROD,
                    pLivingEntity.getX(), pLivingEntity.getY() + 1.0, pLivingEntity.getZ(),
                    0.0, 0.01, 0.0
            );
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}
