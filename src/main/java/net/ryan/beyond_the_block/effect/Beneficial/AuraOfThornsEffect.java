package net.ryan.beyond_the_block.effect.Beneficial;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.ryan.beyond_the_block.effect.ModEffects;

import java.util.Objects;

public class AuraOfThornsEffect extends StatusEffect {
    public AuraOfThornsEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public static void onDamaged(LivingEntity entity, DamageSource source) {
        if (entity.hasStatusEffect(ModEffects.AURA_OF_THORNS)) {
            if (source.getAttacker() instanceof LivingEntity attacker) {
                float thornsDamage = 2.0F + Objects.requireNonNull(entity.getStatusEffect(ModEffects.AURA_OF_THORNS)).getAmplifier();
                attacker.damage(DamageSource.MAGIC, thornsDamage);
            }
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        // This effect will be passive – we hook into onDamage logic elsewhere
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return false;
    }
}
