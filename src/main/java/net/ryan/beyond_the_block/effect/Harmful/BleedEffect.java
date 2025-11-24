package net.ryan.beyond_the_block.effect.Harmful;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.damage_sources.ModDamageSources;

public class BleedEffect extends StatusEffect {
    public BleedEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        World world = pLivingEntity.getWorld();
        if (!world.isClient) {
            float damage = 3.0F + pAmplifier * 0.5F;
            pLivingEntity.damage(ModDamageSources.BLEED, damage);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        // Only apply every 20 ticks (1 second) or faster if amplifier increases
        int interval = Math.max(10, 40 - (pAmplifier * 5));
        return pDuration % interval == 0;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        // Optional: play sound or spawn particles

        super.onApplied(entity, attributes, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        // Optional: clean up or effects
        super.onRemoved(entity, attributes, amplifier);
    }
}
