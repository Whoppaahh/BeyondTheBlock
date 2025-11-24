package net.ryan.beyond_the_block.mixin.Client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmourFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {

    @Inject(method = "renderArmor", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;setAttributes(Lnet/minecraft/client/render/entity/model/BipedEntityModel;)V",
            shift = At.Shift.AFTER
    ))
    private void afterSetAttributes(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            T entity,
            EquipmentSlot slot,
            int light,
            A model,
            CallbackInfo ci
    ) {
        // Adjust pivots after setAttributes copied the base model's transforms
        switch (slot) {
            case LEGS, FEET -> {
                model.rightLeg.pivotX = -2.5F;
                model.leftLeg.pivotX = 2.5F;
            }
            case CHEST -> {
                model.rightArm.pivotX = -7F;
                model.leftArm.pivotX = 7F;
            }
        }
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void cancelHeldItemRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                                      LivingEntity entity, float limbAngle, float limbDistance, float tickDelta,
                                      float customAngle, float headYaw, float headPitch, CallbackInfo ci) {
        if (entity.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            ci.cancel();
        }
    }
}

