package net.ryan.beyond_the_block.feature.combat.handlers;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;

public final class WardingGlyphHandler {
    private WardingGlyphHandler() {}

    public static void onTick(LivingEntity entity) {
        int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.WARDING_GLYPH, entity);
        if (level <= 0) return;

        int cooldown = 100 * level;
        if (entity.age % cooldown != 0) return;

        reflectFirstNegativeEffect(entity);
    }

    private static void reflectFirstNegativeEffect(LivingEntity entity) {
        for (StatusEffectInstance effect : entity.getStatusEffects()) {
            if (effect.getEffectType().isBeneficial()) continue;

            LivingEntity attacker = entity.getAttacker();

            entity.removeStatusEffect(effect.getEffectType());

            if (attacker != null && attacker.isAlive() && attacker != entity) {
                attacker.addStatusEffect(new StatusEffectInstance(
                        effect.getEffectType(),
                        effect.getDuration(),
                        effect.getAmplifier()
                ));
            }

            entity.getWorld().playSound(
                    null,
                    entity.getBlockPos(),
                    SoundEvents.ENTITY_GENERIC_HURT,
                    SoundCategory.PLAYERS,
                    1.0f,
                    1.0f
            );

            if (!entity.getWorld().isClient) {
                entity.getWorld().addParticle(
                        ParticleTypes.SMOKE,
                        entity.getX(),
                        entity.getBodyY(0.5),
                        entity.getZ(),
                        0.0,
                        0.0,
                        0.0
                );
            }

            break;
        }
    }
}