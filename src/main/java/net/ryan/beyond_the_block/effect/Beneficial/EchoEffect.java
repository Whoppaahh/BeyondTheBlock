package net.ryan.beyond_the_block.effect.Beneficial;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;

public class EchoEffect extends StatusEffect {
    public EchoEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.world.isClient() && pLivingEntity.getRecentDamageSource() != null) {
            DamageSource lastHit = pLivingEntity.getRecentDamageSource();
            if (lastHit.getAttacker() instanceof LivingEntity attacker) {
                ServerWorld serverWorld = (ServerWorld) pLivingEntity.world;
                // Delay echo hit by 20 ticks (1 second)
                serverWorld.getServer().execute(() -> {
                    if (pLivingEntity.isAlive()) {
                        pLivingEntity.damage(DamageSource.magic(attacker, attacker), 1.0F + pAmplifier);
                    }
                });
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return pDuration % 40 == 0; // Echo hits every 2 seconds
    }
}
