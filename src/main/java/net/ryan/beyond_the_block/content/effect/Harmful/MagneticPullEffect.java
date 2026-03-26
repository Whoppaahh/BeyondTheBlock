package net.ryan.beyond_the_block.content.effect.Harmful;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class MagneticPullEffect extends StatusEffect {

    public MagneticPullEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.world.isClient()) {
            // Example: pull the entity toward the nearest player
            PlayerEntity nearestPlayer = pLivingEntity.world.getClosestPlayer(pLivingEntity, 16);
            if (nearestPlayer != null) {
                Vec3d direction = nearestPlayer.getPos().subtract(pLivingEntity.getPos()).normalize();
                double strength = 0.1D + pAmplifier * 0.05D;
                pLivingEntity.addVelocity(direction.x * strength, direction.y * strength, direction.z * strength);
            }
        }
        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}
