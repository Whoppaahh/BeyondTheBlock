package net.ryan.beyond_the_block.client.render.effect;

import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.registry.ModEffects;

public class FreezeEffectLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {


    public FreezeEffectLayer(FeatureRendererContext<T, M> context, M baseModel) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       T entity, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress,
                       float headYaw, float headPitch) {

        // Only render if the entity has the freeze effect
        if (!entity.hasStatusEffect(ModEffects.FREEZE)) return;

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(FreezeRenderUtil.getFreezeTexture(entity)));

        // Render the copied model (ice overlay)
        this.getContextModel().animateModel(entity, limbAngle, limbDistance, tickDelta);
        this.getContextModel().setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        matrices.push();
        this.getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, FreezeRenderUtil.getFreezeAlpha());
        matrices.pop();
    }
}
