package net.ryan.beyond_the_block.content.effect.Harmful;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.world.ServerWorld;

public class SplitSoulEffect extends StatusEffect {

    public SplitSoulEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.world.isClient()) {
            ServerWorld serverWorld = (ServerWorld) pLivingEntity.world;
            ZombieEntity clone = new ZombieEntity(serverWorld);
            clone.setPosition(pLivingEntity.getX() + 1, pLivingEntity.getY(), pLivingEntity.getZ());
            clone.setCustomName(pLivingEntity.getDisplayName());
            clone.setCustomNameVisible(true);
            clone.setTarget(pLivingEntity);
            clone.setInvisible(true);
            clone.setSilent(true);

            serverWorld.spawnEntity(clone);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return pDuration == 40; // Only spawn once after 1 second
    }
}
