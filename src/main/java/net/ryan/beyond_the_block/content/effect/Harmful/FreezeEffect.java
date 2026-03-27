package net.ryan.beyond_the_block.content.effect.Harmful;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.effect.FreezeEffectLayer;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import net.ryan.beyond_the_block.utils.TickTimeUtil;

import java.util.UUID;

public class FreezeEffect extends StatusEffect {

    // A constant UUID for your modifier (make sure it’s unique for this effect)
    private static final UUID FREEZE_MODIFIER_ID = UUID.fromString("d7a4d2a0-1a3b-4e4a-9e11-9f55f338a541");

    public FreezeEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
        // This modifier sets the movement speed to 0.
        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, FREEZE_MODIFIER_ID.toString(), -1.0, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        World world = pLivingEntity.getWorld();
        if (world.isClient) return;
        // Deal damage every X ticks, like fire
        if (pLivingEntity.age % TickTimeUtil.randomTicks(1, 20) == 0) { // every second (20 ticks)
            pLivingEntity.damage(DamageSource.FREEZE, 1.0F); // 1 damage per second, adjust as needed
        }
        Vec3d pos = pLivingEntity.getPos();
        pLivingEntity.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.FROSTED_ICE.getDefaultState()),
                pos.x + (pLivingEntity.getRandom().nextDouble() - 0.5) * pLivingEntity.getWidth(),
                pos.y + pLivingEntity.getHeight() + pLivingEntity.getRandom().nextDouble(),
                pos.z + (pLivingEntity.getRandom().nextDouble() - 0.5) * pLivingEntity.getWidth(),
                0.03, 0.01, 0.03);
        if(pLivingEntity instanceof FlyingEntity fe){
            fe.setVelocity(0.0F, Math.max(-0.08, fe.getVelocity().y - 0.08), 0.0);
        }else if(pLivingEntity instanceof MobEntity mob){
            mob.getNavigation().stop();
            mob.setTarget(null);
        }else{
            pLivingEntity.setVelocity(0.0F, 0.0, 0.0);
        }

        pLivingEntity.velocityModified = true;
        pLivingEntity.setOnFire(false);

    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        // Optional: play sound or spawn particles
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
        // Optional: clean up or effects
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
        return true; // ensures the effect ticks every tick
    }
}
