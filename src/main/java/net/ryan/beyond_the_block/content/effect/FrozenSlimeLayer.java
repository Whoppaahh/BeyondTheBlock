package net.ryan.beyond_the_block.content.effect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;

public class FrozenSlimeLayer extends FeatureRenderer<SlimeEntity, SlimeEntityModel<SlimeEntity>> {

    public FrozenSlimeLayer(FeatureRendererContext<SlimeEntity, SlimeEntityModel<SlimeEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers,
                       int light,
                       SlimeEntity slime,
                       float limbAngle,
                       float limbDistance,
                       float tickDelta,
                       float animationProgress,
                       float headYaw,
                       float headPitch) {

        if (!slime.hasStatusEffect(ModEffects.FREEZE)) {
            return;
        }

        SlimeEntityModel<SlimeEntity> slimeModel =
                new SlimeEntityModel<>(
                        MinecraftClient.getInstance()
                                .getEntityModelLoader()
                                .getModelPart(EntityModelLayers.SLIME_OUTER)
                );

        this.getContextModel().copyStateTo(slimeModel);
        slimeModel.animateModel(slime, limbAngle, limbDistance, tickDelta);
        slimeModel.setAngles(slime, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        VertexConsumer vertexConsumer =
                vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(FreezeRenderUtil.getFreezeTexture(slime)));

        slimeModel.render(
                matrices,
                vertexConsumer,
                light,
                OverlayTexture.DEFAULT_UV,
                1f, 1f, 1f, FreezeRenderUtil.getFreezeAlpha()
        );
    }
}