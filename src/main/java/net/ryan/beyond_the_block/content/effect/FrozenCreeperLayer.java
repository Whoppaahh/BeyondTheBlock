package net.ryan.beyond_the_block.content.effect;

import net.minecraft.client.render.entity.feature.EnergySwirlOverlayFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.registry.ModEffects;

public class FrozenCreeperLayer extends EnergySwirlOverlayFeatureRenderer<CreeperEntity, CreeperEntityModel<CreeperEntity>> {

    private static final Identifier FREEZE_TEXTURE =
            new Identifier("minecraft", "textures/block/frosted_ice_0.png");

    private final CreeperEntityModel<CreeperEntity> model;

    public FrozenCreeperLayer(FeatureRendererContext<CreeperEntity, CreeperEntityModel<CreeperEntity>> context,
                              EntityModelLoader loader) {
        super(context);
        this.model = new CreeperEntityModel<>(loader.getModelPart(EntityModelLayers.CREEPER));
    }

    @Override
    protected float getEnergySwirlX(float partialAge) {
        return partialAge * 0.01F;
    }

    @Override
    protected Identifier getEnergySwirlTexture() {
        return FREEZE_TEXTURE;
    }

    @Override
    protected CreeperEntityModel<CreeperEntity> getEnergySwirlModel() {
        return this.model;
    }

    @Override
    public void render(net.minecraft.client.util.math.MatrixStack matrices,
                       net.minecraft.client.render.VertexConsumerProvider vertexConsumers,
                       int light,
                       CreeperEntity creeper,
                       float limbAngle,
                       float limbDistance,
                       float tickDelta,
                       float animationProgress,
                       float headYaw,
                       float headPitch) {
        if (!creeper.hasStatusEffect(ModEffects.FREEZE)) {
            return;
        }
        super.render(matrices, vertexConsumers, light, creeper, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
    }
}