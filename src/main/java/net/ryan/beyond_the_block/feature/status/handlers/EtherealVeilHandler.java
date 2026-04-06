package net.ryan.beyond_the_block.feature.status.handlers;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.ryan.beyond_the_block.content.effect.ModEffects;

public final class EtherealVeilHandler {

    private EtherealVeilHandler() {
    }

    public static boolean shouldCancelIncomingDamage(LivingEntity victim, DamageSource source, float amount) {
        if (source.getAttacker() instanceof LivingEntity attacker) {
            return attacker.hasStatusEffect(ModEffects.ETHEREAL_VEIL);
        }

        return false;
    }
}