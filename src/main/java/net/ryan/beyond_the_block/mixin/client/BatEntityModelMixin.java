package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.client.render.entity.model.BatEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BatEntity;
import net.ryan.beyond_the_block.content.registry.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BatEntityModel.class)
public abstract class BatEntityModelMixin <T extends Entity>{
    @Inject(method = "setAngles(Lnet/minecraft/entity/passive/BatEntity;FFFFF)V", at = @At("HEAD"), cancellable = true)
    private void cancelSetAngles(BatEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (entity.hasStatusEffect(ModEffects.FREEZE)) {
            ci.cancel();
        }
    }
}

