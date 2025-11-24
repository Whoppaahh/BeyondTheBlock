package net.ryan.beyond_the_block.effect.Harmful;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class MindFogEffect extends StatusEffect {
    public MindFogEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {

    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return false;
    }
}
