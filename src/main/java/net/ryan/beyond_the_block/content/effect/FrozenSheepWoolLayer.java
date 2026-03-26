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
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.render.entity.model.SheepWoolEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;

public class FrozenSheepWoolLayer extends FeatureRenderer<SheepEntity, SheepEntityModel<SheepEntity>> {

    public FrozenSheepWoolLayer(FeatureRendererContext<SheepEntity, SheepEntityModel<SheepEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers,
                       int light,
                       SheepEntity sheep,
                       float limbAngle,
                       float limbDistance,
                       float tickDelta,
                       float animationProgress,
                       float headYaw,
                       float headPitch) {

        if (!sheep.hasStatusEffect(ModEffects.FREEZE) || sheep.isSheared()) {
            return;
        }

        SheepWoolEntityModel<SheepEntity> woolModel =
                new SheepWoolEntityModel<>(
                        MinecraftClient.getInstance()
                                .getEntityModelLoader()
                                .getModelPart(EntityModelLayers.SHEEP_FUR)
                );

        this.getContextModel().copyStateTo(woolModel);
        woolModel.animateModel(sheep, limbAngle, limbDistance, tickDelta);
        woolModel.setAngles(sheep, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        VertexConsumer vertexConsumer =
                vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(FreezeRenderUtil.getFreezeTexture(sheep)));

        woolModel.render(
                matrices,
                vertexConsumer,
                light,
                OverlayTexture.DEFAULT_UV,
                1f, 1f, 1f, FreezeRenderUtil.getFreezeAlpha()
        );
    }
}