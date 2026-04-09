package net.ryan.beyond_the_block.feature.combat.handlers;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;

public final class EchoGuardHandler {
    private EchoGuardHandler() {}

    public static void afterDamage(LivingEntity victim, DamageSource source, float amount) {
        int level = EnchantmentHelper.getLevel(
                ModEnchantments.ECHO_GUARD,
                victim.getEquippedStack(EquipmentSlot.CHEST)
        );
        if (level <= 0) return;

        if (!(source.getAttacker() instanceof LivingEntity attacker)) return;
        if (attacker == victim) return;

        float reflected = amount * 0.25f;
        if (reflected <= 0.0f) return;

        attacker.damage(DamageSource.magic(victim, victim), reflected);

        victim.getWorld().playSound(
                null,
                victim.getBlockPos(),
                SoundEvents.ENTITY_PLAYER_HURT,
                SoundCategory.PLAYERS,
                1.0f,
                1.0f
        );

        if (victim.getWorld().isClient) return;

        victim.getWorld().addParticle(
                ParticleTypes.EXPLOSION,
                victim.getX(),
                victim.getBodyY(0.5),
                victim.getZ(),
                0.0,
                0.0,
                0.0
        );
    }
}