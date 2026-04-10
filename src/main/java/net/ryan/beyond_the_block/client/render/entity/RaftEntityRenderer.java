package net.ryan.beyond_the_block.client.render.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.ryan.beyond_the_block.content.entity.RaftEntity;
import net.ryan.beyond_the_block.content.entity.RaftChestEntity;
import net.ryan.beyond_the_block.content.entity.model.RaftChestEntityModel;
import net.ryan.beyond_the_block.content.entity.model.RaftEntityModel;
import net.ryan.beyond_the_block.content.registry.ModEntities;
import net.ryan.beyond_the_block.content.registry.family.ModBoatVariant;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class RaftEntityRenderer<T extends BoatEntity> extends EntityRenderer<T> {
    private final boolean chest;
    private final RaftEntityModel model;

    public RaftEntityRenderer(EntityRendererFactory.Context ctx, boolean chest) {
        super(ctx);
        this.chest = chest;
        this.model = chest
                ? new RaftChestEntityModel(ctx.getPart(ModEntities.CHEST_RAFT))
                : new RaftEntityModel(ctx.getPart(ModEntities.RAFT));
        this.shadowRadius = 0.8F;
    }

    @Override
    public Identifier getTexture(T entity) {
        ModBoatVariant variant = getVariant(entity);
        return new Identifier(
                BeyondTheBlock.MOD_ID,
                chest
                        ? "textures/entity/chest_boat/" + variant.asString() + ".png"
                        : "textures/entity/boat/" + variant.asString() + ".png"
        );
    }

    private ModBoatVariant getVariant(T entity) {
        if (entity instanceof RaftEntity raft) {
            return raft.getVariant();
        }
        if (entity instanceof RaftChestEntity chestRaft) {
            return chestRaft.getVariant();
        }
        return ModBoatVariant.BAMBOO;
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(0.0D, 0.375D, 0.0D);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - yaw));

        float wobbleTicks = entity.getDamageWobbleTicks() - tickDelta;
        float wobbleStrength = entity.getDamageWobbleStrength() - tickDelta;
        if (wobbleStrength < 0.0F) wobbleStrength = 0.0F;

        if (wobbleTicks > 0.0F) {
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(
                    MathHelper.sin(wobbleTicks) * wobbleTicks * wobbleStrength / 10.0F * entity.getDamageWobbleSide()
            ));
        }

        float bubbleWobble = entity.interpolateBubbleWobble(tickDelta);
        if (!MathHelper.approximatelyEquals(bubbleWobble, 0.0F)) {
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(bubbleWobble));
        }

        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

        this.model.setAngles(entity, tickDelta, 0.0F, -0.1F, 0.0F, 0.0F);

        VertexConsumer consumer = vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(entity)));
        this.model.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}