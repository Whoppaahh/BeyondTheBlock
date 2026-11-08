package net.ryan.beyond_the_block.content.effect.harmful;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import net.ryan.beyond_the_block.damage_sources.ModDamageSources;

import java.util.UUID;

public class HeavyStepEffect extends StatusEffect {
    private static final UUID KNOCKBACK_RESISTANCE_ID = UUID.fromString("7f8e5f3b-1bc4-41cf-91b1-1e9cf63c6f9e");
    private static final UUID MOVEMENT_SLOWDOWN_ID = UUID.fromString("a5cb4450-5f29-4c8b-8e01-f91e0ddea444");

    public HeavyStepEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
        this.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_ID.toString(), 1.0D, EntityAttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, MOVEMENT_SLOWDOWN_ID.toString(), -0.2D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.world.isClient && entity.isOnGround() && (entity.getX() != entity.prevX || entity.getZ() != entity.prevZ)) {
            if (entity.age % 5 == 0 && entity.world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                serverWorld.spawnParticles(
                        new net.minecraft.particle.BlockStateParticleEffect(net.minecraft.particle.ParticleTypes.BLOCK, entity.getSteppingBlockState()),
                        entity.getX(), entity.getY(), entity.getZ(),
                        2, 0.1, 0.1, 0.1, 0.05
                );
            }

            // Damage nearby entities
            entity.world.getOtherEntities(entity, entity.getBoundingBox().expand(0.5, 0, 0.5), e -> e instanceof LivingEntity)
                    .forEach(e -> ((LivingEntity)e).damage(ModDamageSources.STOMP, 1.0F + amplifier));
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
