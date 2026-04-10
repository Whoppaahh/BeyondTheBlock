package net.ryan.beyond_the_block.client.render.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.ryan.beyond_the_block.content.entity.BoatEntity;
import net.ryan.beyond_the_block.content.entity.ChestBoatEntity;
import net.ryan.beyond_the_block.content.registry.family.ModBoatVariant;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class ModBoatRenderer<T extends net.minecraft.entity.vehicle.BoatEntity> extends EntityRenderer<T> {
    private final BoatEntityModel model;
    private final boolean chest;

    public ModBoatRenderer(EntityRendererFactory.Context ctx, boolean chest) {
        super(ctx);
        this.chest = chest;

        ModelPart modelPart = ctx.getPart(
                chest
                        ? EntityModelLayers.createChestBoat(net.minecraft.entity.vehicle.BoatEntity.Type.OAK)
                        : EntityModelLayers.createBoat(net.minecraft.entity.vehicle.BoatEntity.Type.OAK)
        );

        this.model = new BoatEntityModel(modelPart, chest);
        this.shadowRadius = 0.8F;
    }

    @Override
    public Identifier getTexture(T boat) {
        ModBoatVariant variant = getVariant(boat);

        return new Identifier(
                BeyondTheBlock.MOD_ID,
                chest
                        ? "textures/entity/chest_boat/" + variant.asString() + ".png"
                        : "textures/entity/boat/" + variant.asString() + ".png"
        );
    }

    private ModBoatVariant getVariant(T boat) {
        if (boat instanceof BoatEntity modBoat) {
            return modBoat.getVariant();
        }
        if (boat instanceof ChestBoatEntity modChestBoat) {
            return modChestBoat.getVariant();
        }
        return ModBoatVariant.CHERRY;
    }

    @Override
    public void render(
            T boat,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light
    ) {
        matrices.push();
        matrices.translate(0.0D, 0.375D, 0.0D);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - yaw));

        float wobbleTicks = (float) boat.getDamageWobbleTicks() - tickDelta;
        float wobbleStrength = boat.getDamageWobbleStrength() - tickDelta;
        if (wobbleStrength < 0.0F) {
            wobbleStrength = 0.0F;
        }

        if (wobbleTicks > 0.0F) {
            matrices.multiply(
                    Vec3f.POSITIVE_X.getDegreesQuaternion(
                            MathHelper.sin(wobbleTicks) * wobbleTicks * wobbleStrength / 10.0F * (float) boat.getDamageWobbleSide()
                    )
            );
        }

        float bubbleWobble = boat.interpolateBubbleWobble(tickDelta);
        if (!MathHelper.approximatelyEquals(bubbleWobble, 0.0F)) {
            matrices.multiply(
                    Vec3f.POSITIVE_X.getDegreesQuaternion(bubbleWobble)
            );
        }

        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

        this.model.setAngles(boat, tickDelta, 0.0F, -0.1F, 0.0F, 0.0F);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(boat)));
        this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        if (!boat.isSubmergedInWater()) {
            VertexConsumer waterMask = vertexConsumers.getBuffer(RenderLayer.getWaterMask());
            this.model.getWaterPatch().render(matrices, waterMask, light, OverlayTexture.DEFAULT_UV);
        }

        matrices.pop();
        super.render(boat, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}