package net.ryan.beyond_the_block.mixin.feature.enchantments.freeze;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.ai.control.LookControl;
import net.ryan.beyond_the_block.feature.status.handlers.FreezeStateHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LookControl.class)
public abstract class LookControlFreezeMixin {

    @Shadow @Final protected MobEntity entity;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$freezeLookControl(CallbackInfo ci) {
        if (FreezeStateHandler.isFrozen(entity)) {
            entity.headYaw = entity.bodyYaw;
            entity.prevHeadYaw = entity.bodyYaw;
            ci.cancel();
        }
    }
}