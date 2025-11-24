package net.ryan.beyond_the_block.mixin.Client;

import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.GhastEntity;
import net.ryan.beyond_the_block.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GhastEntityModel.class)
public abstract class GhastEntityModelMixin<T extends Entity> {
    @Inject(method = "setAngles", at = @At("HEAD"), cancellable = true)
    private void cancelSetAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof GhastEntity g && g.hasStatusEffect(ModEffects.FREEZE)) {
            ci.cancel();
        }
    }
}


