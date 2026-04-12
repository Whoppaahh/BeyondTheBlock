package net.ryan.beyond_the_block.client.render.trim;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public final class ArmourTrimRenderer {

    private ArmourTrimRenderer() {
    }

    public static <T extends LivingEntity> void renderTrim(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            BipedEntityModel<T> model,
            Identifier texture,
            boolean glint
    ) {
        VertexConsumer consumer = ItemRenderer.getArmorGlintConsumer(
                vertexConsumers,
                RenderLayer.getArmorCutoutNoCull(texture),
                false,
                glint
        );

        model.render(
                matrices,
                consumer,
                light,
                OverlayTexture.DEFAULT_UV,
                1.0F,
                1.0F,
                1.0F,
                1.0F
        );
    }
}