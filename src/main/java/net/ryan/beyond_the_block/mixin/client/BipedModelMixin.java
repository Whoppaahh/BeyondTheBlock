package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.ryan.beyond_the_block.content.registry.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedModelMixin<T extends LivingEntity> {
    @Inject(method = "setAngles*", at = @At("HEAD"), cancellable = true)
    private void cancelSetAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (entity.hasStatusEffect(ModEffects.FREEZE)) {
            ci.cancel();
        }
    }

    @Inject(method = "animateModel*", at = @At("HEAD"), cancellable = true)
    private void cancelAnimateModel(T entity, float limbAngle, float limbDistance, float tickDelta, CallbackInfo ci) {
        if (entity.hasStatusEffect(ModEffects.FREEZE)) {
            ci.cancel();
        }
    }
}

