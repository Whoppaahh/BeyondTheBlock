package net.ryan.beyond_the_block.client.render.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.feature.pets.PetCollarAccessor;

public class WolfCollarTagFeatureRenderer
        extends FeatureRenderer<WolfEntity, WolfEntityModel<WolfEntity>> {

    public WolfCollarTagFeatureRenderer(
            FeatureRendererContext<WolfEntity, WolfEntityModel<WolfEntity>> context
    ) {
        super(context);
    }

    @Override
    public void render(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            WolfEntity wolf,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        if (!wolf.isTamed()) return;
        if (!(wolf instanceof PetCollarAccessor accessor)) return;
        if (accessor.btb$getCollar().isEmpty()) return;

        matrices.push();

        matrices.translate(0.0D, 1.1D, -0.38D);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180.0F));


        Identifier texture = getOwnerSkin(wolf);
        if (texture == null) {
            texture = new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/wolf/wolf_collar.png");
            matrices.scale(0.5F, 0.4F, 0.5F);
            VertexConsumer consumer = vertexConsumers.getBuffer(
                    RenderLayer.getEntityCutoutNoCull(texture));

            renderQuad(matrices, consumer, light);
        }else {
            matrices.scale(0.3F, 0.3F, 0.3F);
            VertexConsumer consumer = vertexConsumers.getBuffer(
                    RenderLayer.getEntityCutoutNoCull(texture));

            renderHeadQuad(matrices, consumer, light);
        }

        matrices.pop();
    }

    private static void renderHeadQuad(MatrixStack matrices, VertexConsumer consumer, int light) {
        MatrixStack.Entry entry = matrices.peek();

        float minX = -0.25F;
        float maxX = 0.25F;
        float minY = -0.25F;
        float maxY = 0.25F;
        float z = 0.0F;

        float u1 = 8F / 64F;
        float v1 = 8F / 64F;
        float u2 = 16F / 64F;
        float v2 = 16F / 64F;

        consumer.vertex(entry.getPositionMatrix(), minX, minY, z)
                .color(255, 255, 255, 255)
                .texture(u1, v2)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry.getNormalMatrix(), 0.0F, 0.0F, 1.0F)
                .next();

        consumer.vertex(entry.getPositionMatrix(), maxX, minY, z)
                .color(255, 255, 255, 255)
                .texture(u2, v2)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry.getNormalMatrix(), 0.0F, 0.0F, 1.0F)
                .next();

        consumer.vertex(entry.getPositionMatrix(), maxX, maxY, z)
                .color(255, 255, 255, 255)
                .texture(u2, v1)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry.getNormalMatrix(), 0.0F, 0.0F, 1.0F)
                .next();

        consumer.vertex(entry.getPositionMatrix(), minX, maxY, z)
                .color(255, 255, 255, 255)
                .texture(u1, v1)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry.getNormalMatrix(), 0.0F, 0.0F, 1.0F)
                .next();
    }

    private static Identifier getOwnerSkin(WolfEntity wolf) {
        if (wolf.getOwnerUuid() == null) {
            return null;
        }

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.getNetworkHandler() == null) {
            return null;
        }

        PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(wolf.getOwnerUuid());

        if (entry == null) {
            return null;
        }

        return entry.getSkinTexture();
    }

    private static void renderQuad(MatrixStack matrices, VertexConsumer consumer, int light) {
        MatrixStack.Entry entry = matrices.peek();

        float minX = -0.25F;
        float maxX = 0.25F;
        float minY = -0.35F;
        float maxY = 0.35F;
        float z = 0.0F;

        consumer.vertex(entry.getPositionMatrix(), minX, minY, z)
                .color(255, 255, 255, 255)
                .texture(0.0F, 1.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry.getNormalMatrix(), 0.0F, 0.0F, 1.0F)
                .next();

        consumer.vertex(entry.getPositionMatrix(), maxX, minY, z)
                .color(255, 255, 255, 255)
                .texture(1.0F, 1.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry.getNormalMatrix(), 0.0F, 0.0F, 1.0F)
                .next();

        consumer.vertex(entry.getPositionMatrix(), maxX, maxY, z)
                .color(255, 255, 255, 255)
                .texture(1.0F, 0.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry.getNormalMatrix(), 0.0F, 0.0F, 1.0F)
                .next();

        consumer.vertex(entry.getPositionMatrix(), minX, maxY, z)
                .color(255, 255, 255, 255)
                .texture(0.0F, 0.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry.getNormalMatrix(), 0.0F, 0.0F, 1.0F)
                .next();
    }
}