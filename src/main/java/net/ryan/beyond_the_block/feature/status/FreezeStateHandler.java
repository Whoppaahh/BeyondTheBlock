package net.ryan.beyond_the_block.feature.status;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.ai.control.LookControl;
import net.ryan.beyond_the_block.content.effect.ModEffects;

public final class FreezeStateHandler {

    private FreezeStateHandler() {
    }

    public static boolean isFrozen(LivingEntity entity) {
        return entity.hasStatusEffect(ModEffects.FREEZE);
    }

    public static void applyPerTickMotionControl(LivingEntity entity) {
        if (!isFrozen(entity)) {
            return;
        }

        entity.setOnFire(false);

        // Lock body/head rotations to current body yaw
        entity.prevYaw = entity.getYaw();
        entity.prevPitch = entity.getPitch();
        entity.prevBodyYaw = entity.bodyYaw;
        entity.prevHeadYaw = entity.headYaw;

        entity.setPitch(0.0F);
        entity.headYaw = entity.bodyYaw;

        if (entity instanceof FlyingEntity flying) {
            flying.setVelocity(0.0D, Math.max(-0.08D, flying.getVelocity().y - 0.08D), 0.0D);
        } else {
            entity.setVelocity(0.0D, 0.0D, 0.0D);
        }

        if (entity instanceof MobEntity mob) {
            mob.getNavigation().stop();
            mob.setTarget(null);

            LookControl look = mob.getLookControl();
            if (look != null) {
                look.lookAt(mob.getX(), mob.getEyeY(), mob.getZ());
            }
        }

        entity.velocityModified = true;
    }
}