package net.ryan.beyond_the_block.mixin.feature.enchantments.freeze;

import net.minecraft.entity.LivingEntity;
import net.ryan.beyond_the_block.feature.status.FreezeStateHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityFreezeMixin {

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$preventJumpWhileFrozen(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (FreezeStateHandler.isFrozen(entity)) {
            ci.cancel();
        }
    }

    @Inject(method = "updateLimbs", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$freezeLimbAnimation(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (FreezeStateHandler.isFrozen(entity)) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void beyond_the_block$hardResetFrozenAnimationState(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!FreezeStateHandler.isFrozen(entity)) {
            return;
        }

        entity.prevBodyYaw = entity.bodyYaw;
        entity.prevHeadYaw = entity.headYaw;
        entity.headYaw = entity.bodyYaw;

        entity.lastLimbDistance = 0.0F;
        entity.limbDistance = 0.0F;

        entity.lastHandSwingProgress = 0.0F;
        entity.handSwingProgress = 0.0F;
        entity.handSwingTicks = 0;
        entity.handSwinging = false;
    }
}