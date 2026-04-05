package net.ryan.beyond_the_block.feature.combat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.ryan.beyond_the_block.feature.combat.handlers.EchoGuardHandler;
import net.ryan.beyond_the_block.feature.combat.handlers.LifeStealHandler;
import net.ryan.beyond_the_block.feature.combat.handlers.NightfallCleaveHandler;
import net.ryan.beyond_the_block.feature.combat.handlers.WardingGlyphHandler;

public final class EnchantmentCombatHooks {
    private EnchantmentCombatHooks() {}

    public static void onTick(LivingEntity entity) {
        WardingGlyphHandler.onTick(entity);
    }

    public static void onAttack(LivingEntity attacker, LivingEntity target) {
        NightfallCleaveHandler.onAttack(attacker, target);
    }

    public static void afterDamage(LivingEntity victim, DamageSource source, float amount) {
        LifeStealHandler.afterDamage(victim, source, amount);
        EchoGuardHandler.afterDamage(victim, source, amount);
    }
}