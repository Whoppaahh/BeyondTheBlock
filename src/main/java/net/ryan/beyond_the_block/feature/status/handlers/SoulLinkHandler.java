package net.ryan.beyond_the_block.feature.status.handlers;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import net.ryan.beyond_the_block.content.effect.beneficial.SoulLinkEffect;

public final class SoulLinkHandler {

    private SoulLinkHandler() {
    }

    public static void afterSuccessfulDamage(LivingEntity entity, DamageSource source, float amount) {
        if (!entity.hasStatusEffect(ModEffects.SOUL_LINK)) {
            return;
        }

        StatusEffectInstance effectInstance = entity.getStatusEffect(ModEffects.SOUL_LINK);
        if (effectInstance == null) {
            return;
        }

        if (!(effectInstance.getEffectType() instanceof SoulLinkEffect)) {
            return;
        }

        LivingEntity linked = SoulLinkEffect.getLinkedEntityFor(entity);
        if (linked == null || !linked.isAlive() || linked == entity) {
            return;
        }

        linked.damage(DamageSource.MAGIC, amount);

        entity.getWorld().playSound(
                null,
                entity.getBlockPos(),
                SoundEvents.BLOCK_END_PORTAL_SPAWN,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F
        );

        if (!entity.getWorld().isClient) {
            entity.getWorld().addParticle(
                    ParticleTypes.END_ROD,
                    linked.getX(),
                    linked.getY(),
                    linked.getZ(),
                    0.1,
                    0.1,
                    0.1
            );
        }
    }

    public static void onDeath(LivingEntity entity) {
        if (entity.hasStatusEffect(ModEffects.SOUL_LINK)) {
            SoulLinkEffect.clearLink(entity);
        }
    }
}