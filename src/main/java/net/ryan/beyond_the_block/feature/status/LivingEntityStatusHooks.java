package net.ryan.beyond_the_block.feature.status;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.ryan.beyond_the_block.feature.status.handlers.EtherealVeilHandler;
import net.ryan.beyond_the_block.feature.status.handlers.MindWardHandler;
import net.ryan.beyond_the_block.feature.status.handlers.SoulLinkHandler;

public final class LivingEntityStatusHooks {

    private LivingEntityStatusHooks() {
    }

    public static boolean shouldCancelDamage(LivingEntity victim, DamageSource source, float amount) {
        return EtherealVeilHandler.shouldCancelIncomingDamage(victim, source, amount);
    }

    public static void afterSuccessfulDamage(LivingEntity victim, DamageSource source, float amount) {
        SoulLinkHandler.afterSuccessfulDamage(victim, source, amount);
    }

    public static void onDeath(LivingEntity entity, DamageSource source) {
        SoulLinkHandler.onDeath(entity);
    }

    public static void onTick(LivingEntity entity) {
        MindWardHandler.onTick(entity);
    }
}