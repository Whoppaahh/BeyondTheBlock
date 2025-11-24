package net.ryan.beyond_the_block.mixin.Client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.ryan.beyond_the_block.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Unique
    private float alpha = 1.0F;

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"))
    private void onRenderHead(T entity, float yaw, float tickDelta, MatrixStack matrices,
                              VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity.hasStatusEffect(ModEffects.ETHEREAL_VEIL)) {
            alpha = 0.3F; // 30% transparency when effect is active
        } else {
            alpha = 1.0F; // fully opaque
        }
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"
            ),
            index = 7
    )
    private float modifyAlpha(float originalAlpha) {
        return alpha;
    }

//    @Inject(method = "<init>", at = @At("RETURN"))
//    @SuppressWarnings("unchecked")
//    private void addFreezeLayer(EntityRendererFactory.Context context, M model, float shadowRadius, CallbackInfo ci) {
//        ((LivingEntityRendererAccessor<T, M>) this).getFeatures()
//                .add(new FreezeEffectLayer<>((LivingEntityRenderer<T, M>) (Object) this, model));
//    } //Works but breaks vanilla rendering due to clothes

}



