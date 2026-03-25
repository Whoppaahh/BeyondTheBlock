package net.ryan.beyond_the_block.mixin.Client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.utils.GlowManager;
import net.ryan.beyond_the_block.utils.InvisibleItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameEntityRenderer.class)
public abstract class ItemFrameRendererMixin {

    @Inject(
            method = "render(Lnet/minecraft/entity/decoration/ItemFrameEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD")
    )
    private void bt$renderInvisible(ItemFrameEntity entity,
                                    float yaw,
                                    float tickDelta,
                                    MatrixStack matrices,
                                    VertexConsumerProvider vertexConsumers,
                                    int light,
                                    CallbackInfo ci) {

        InvisibleItemFrame accessor = (InvisibleItemFrame) entity;

        entity.setInvisible(accessor.bt$isInvisibleItemFrame());
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/entity/decoration/ItemFrameEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0 // first int = light
    )
    private int injectGlowLight(int light, ItemFrameEntity entity) {
        ItemStack stack = entity.getHeldItemStack();

        if (GlowManager.shouldGlow(stack)) {
            return GlowManager.FULL_BRIGHT;
        }

        return light;
    }
}