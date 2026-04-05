package net.ryan.beyond_the_block.mixin.feature.enchantments.freeze;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.ryan.beyond_the_block.feature.status.FreezeStateHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class SkeletonFreezeAttackMixin {

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$preventFrozenSkeletonAttack(LivingEntity target, float pullProgress, CallbackInfo ci) {
        AbstractSkeletonEntity self = (AbstractSkeletonEntity) (Object) this;
        if (FreezeStateHandler.isFrozen(self)) {
            ci.cancel();
        }
    }
}