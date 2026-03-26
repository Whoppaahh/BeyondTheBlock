package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.ryan.beyond_the_block.client.visual.GlowManager;
import net.ryan.beyond_the_block.client.visual.Glowable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerBlockEntityRenderer.class)
public abstract class BannerBlockEntityRendererMixin {


    @Inject(method = "render(Lnet/minecraft/block/entity/BannerBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At("TAIL"))
    private void bt$renderGlow(BannerBlockEntity banner, float tickDelta,
                               MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                               int light, int overlay, CallbackInfo ci) {

        if (banner instanceof Glowable glowable && glowable.bt$isGlowing()) {
            MinecraftClient client = MinecraftClient.getInstance();

            // Render the banner again with FULL_BRIGHT lighting
            client.getBlockRenderManager().renderBlockAsEntity(
                    banner.getCachedState(),
                    matrices,
                    vertexConsumers,
                    GlowManager.FULL_BRIGHT, // fullbright
                    overlay
            );
        }
    }
}
