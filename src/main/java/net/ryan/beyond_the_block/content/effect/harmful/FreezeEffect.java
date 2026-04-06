package net.ryan.beyond_the_block.content.effect.harmful;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.effect.FreezeEffectLayer;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import net.ryan.beyond_the_block.feature.status.handlers.FreezeStateHandler;
import net.ryan.beyond_the_block.utils.TickTimeUtil;

import java.util.UUID;

public class FreezeEffect extends StatusEffect {

    private static final UUID FREEZE_MODIFIER_ID =
            UUID.fromString("d7a4d2a0-1a3b-4e4a-9e11-9f55f338a541");

    public FreezeEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);

        this.addAttributeModifier(
                EntityAttributes.GENERIC_MOVEMENT_SPEED,
                FREEZE_MODIFIER_ID.toString(),
                -1.0,
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();
        if (world.isClient) {
            return;
        }

        if (entity.age % TickTimeUtil.randomTicks(1, 20) == 0) {
            entity.damage(DamageSource.FREEZE, 1.0F);
        }

        spawnFreezeParticles(entity);
        FreezeStateHandler.applyPerTickMotionControl(entity);
    }

    private void spawnFreezeParticles(LivingEntity entity) {
        Vec3d pos = entity.getPos();

        entity.getWorld().addParticle(
                new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.FROSTED_ICE.getDefaultState()),
                pos.x + (entity.getRandom().nextDouble() - 0.5) * entity.getWidth(),
                pos.y + entity.getHeight() + entity.getRandom().nextDouble(),
                pos.z + (entity.getRandom().nextDouble() - 0.5) * entity.getWidth(),
                0.03,
                0.01,
                0.03
        );
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);

        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            var instance = entity.getStatusEffect(ModEffects.FREEZE);
            if (instance != null) {
                serverWorld.getChunkManager().sendToOtherNearbyPlayers(
                        entity,
                        new EntityStatusEffectS2CPacket(entity.getId(), instance)
                );
            }
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);

        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.getChunkManager().sendToOtherNearbyPlayers(
                    entity,
                    new RemoveEntityStatusEffectS2CPacket(entity.getId(), ModEffects.FREEZE)
            );
            FreezeEffectLayer.shatterOnThaw(entity);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}