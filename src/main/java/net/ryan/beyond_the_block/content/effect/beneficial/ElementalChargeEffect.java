package net.ryan.beyond_the_block.content.effect.beneficial;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

import java.util.UUID;

public class ElementalChargeEffect extends StatusEffect {
    private static final UUID DAMAGE_UUID = UUID.fromString("1f6ffb6d-d889-4e1f-aaf1-ff7642d12a99");


    public ElementalChargeEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, DAMAGE_UUID.toString(), 2.0D, EntityAttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.world.isClient()) {
            ServerWorld serverWorld = (ServerWorld) pLivingEntity.world;
            BlockPos pos = pLivingEntity.getBlockPos();
            Biome biome = serverWorld.getBiome(pos).value();

            // You can conditionally spawn different particles or buffs depending on biome type
            // Example: Fire particles in desert
            if (biome.getTemperature() > 1.5F) {
                serverWorld.spawnParticles(
                        net.minecraft.particle.ParticleTypes.FLAME,
                        pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(),
                        2, 0.2, 0.2, 0.2, 0.01
                );
            } else if (biome.getPrecipitation() == Biome.Precipitation.SNOW) {
                serverWorld.spawnParticles(
                        net.minecraft.particle.ParticleTypes.SNOWFLAKE,
                        pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(),
                        2, 0.2, 0.2, 0.2, 0.01
                );
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}
