package net.ryan.beyond_the_block.content.effect.Beneficial;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class CupidStatusEffect extends StatusEffect {

    public CupidStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return false; // no ticking
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        // no tick logic, purely a marker
    }
}