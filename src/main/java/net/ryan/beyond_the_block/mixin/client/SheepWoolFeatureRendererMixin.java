package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.SheepWoolEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.client.visual.Glowable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepWoolFeatureRenderer.class)
public abstract class SheepWoolFeatureRendererMixin {

    @Shadow
    @Final
    private SheepWoolEntityModel<SheepEntity> model;

    @Unique
    private static final Identifier WOOL_TEXTURE =
            new Identifier("textures/entity/sheep/sheep_fur.png");

    @Inject(
            method = "render*",
            at = @At("TAIL")
    )
    private void bt$renderEmissive(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            SheepEntity sheep,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        Glowable glow = (Glowable) sheep;
        if (!glow.bt$isGlowing()) return;
        if (sheep.isSheared()) return;
        if (sheep.isInvisible()) return;

        // FULLBRIGHT LIGHT
        int fullBright = 0xF000F0;

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(WOOL_TEXTURE)
        );

        // Handle rainbow jeb_ sheep
        float r, g, b;
        if (sheep.hasCustomName() && "jeb_".equals(sheep.getName().getString())) {
            int n = sheep.age / 25 + sheep.getId();
            int o = DyeColor.values().length;
            int p = n % o;
            int q = (n + 1) % o;
            float blend = ((float)(sheep.age % 25) + tickDelta) / 25.0F;
            float[] c1 = SheepEntity.getRgbColor(DyeColor.byId(p));
            float[] c2 = SheepEntity.getRgbColor(DyeColor.byId(q));
            r = c1[0] * (1.0F - blend) + c2[0] * blend;
            g = c1[1] * (1.0F - blend) + c2[1] * blend;
            b = c1[2] * (1.0F - blend) + c2[2] * blend;
        } else {
            float[] c = SheepEntity.getRgbColor(sheep.getColor());
            r = c[0]; g = c[1]; b = c[2];
        }
        // Compute brightness
        float brightness = 0.2126F*r + 0.7152F*g + 0.0722F*b;
        float alpha = 1.0f;
        // --- DARK WOOL PATH ---
        if (brightness < 0.25F) {
            // Subtle glow overlay (do NOT modify base colour)
             alpha = 0.4F; // tweak: 0.4–0.7 looks good
        }

        // Render emissive pass
        model.render(
                matrices,
                vertexConsumer,
                fullBright,                      // 👈 full brightness
                LivingEntityRenderer.getOverlay(sheep, 0.0f),
                r, g, b, alpha
        );
    }
}