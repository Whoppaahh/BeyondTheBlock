package net.ryan.beyond_the_block.effect;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.ryan.beyond_the_block.effect.Beneficial.SoulLinkEffect;

public class SoulLinkEffectRenderer {

    public static void renderSoulLinkEffect(LivingEntity entity) {
        StatusEffectInstance instance = entity.getStatusEffect(ModEffects.SOUL_LINK);

        if (instance != null && instance.getEffectType() instanceof SoulLinkEffect soulLinkEffect) {
            // Get the linked entity from a shared tracker (not the effect instance)
            LivingEntity linkedEntity = SoulLinkEffect.getLinkedEntityFor(entity); // you implement this method!

            if (linkedEntity != null && linkedEntity.isAlive()) {
                spawnSoulLinkParticles((ClientWorld) entity.getWorld(), entity, linkedEntity);
            }
        }
    }

    private static void spawnSoulLinkParticles(ClientWorld world, LivingEntity entity, LivingEntity linkedEntity) {
        // Create particles between the entities (simple line between them)
        double x1 = entity.getX();
        double y1 = entity.getY() + entity.getHeight() / 2;
        double z1 = entity.getZ();
        double x2 = linkedEntity.getX();
        double y2 = linkedEntity.getY() + linkedEntity.getHeight() / 2;
        double z2 = linkedEntity.getZ();

        // Add particles (simple example)
        for (int i = 0; i < 10; i++) {
            world.addParticle(ParticleTypes.CLOUD,
                    x1 + (x2 - x1) * i / 10.0,
                    y1 + (y2 - y1) * i / 10.0,
                    z1 + (z2 - z1) * i / 10.0,
                    0, 0, 0);
        }
    }
}
