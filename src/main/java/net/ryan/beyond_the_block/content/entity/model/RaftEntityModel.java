package net.ryan.beyond_the_block.content.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.MathHelper;

public class RaftEntityModel extends CompositeEntityModel<BoatEntity> {
    protected final ModelPart bottom;
    protected final ModelPart leftPaddle;
    protected final ModelPart rightPaddle;
    protected final ImmutableList<ModelPart> parts;

    public RaftEntityModel(ModelPart root) {
        this.bottom = root.getChild("bottom");
        this.leftPaddle = root.getChild("left_paddle");
        this.rightPaddle = root.getChild("right_paddle");
        this.parts = ImmutableList.of(this.bottom, this.leftPaddle, this.rightPaddle);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        addParts(root);

        return TexturedModelData.of(modelData, 128, 64);
    }

    protected static void addParts(ModelPartData root) {
        root.addChild(
                "bottom",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-14.0F, -11.0F, -4.0F, 28.0F, 20.0F, 4.0F)
                        .uv(0, 0)
                        .cuboid(-14.0F, -9.0F, -8.0F, 28.0F, 16.0F, 4.0F),
                ModelTransform.of(0.0F, -2.1F, 1.0F, 1.5708F, 0.0F, 0.0F)
        );

        root.addChild(
                "left_paddle",
                ModelPartBuilder.create()
                        .uv(0, 24)
                        .cuboid(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F)
                        .cuboid(-1.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F),
                ModelTransform.of(3.0F, -4.0F, 9.0F, 0.0F, 0.0F, 0.19634955F)
        );

        root.addChild(
                "right_paddle",
                ModelPartBuilder.create()
                        .uv(40, 24)
                        .cuboid(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F)
                        .cuboid(0.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F),
                ModelTransform.of(3.0F, -4.0F, -9.0F, 0.0F, (float) Math.PI, 0.19634955F)
        );
    }

    @Override
    public void setAngles(BoatEntity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        setPaddleAngle(entity, 0, this.leftPaddle, limbSwing);
        setPaddleAngle(entity, 1, this.rightPaddle, limbSwing);
    }

    protected static void setPaddleAngle(BoatEntity entity, int sigma, ModelPart part, float angle) {
        float phase = entity.interpolatePaddlePhase(sigma, angle);
        part.pitch = MathHelper.clampedLerp((float) (-Math.PI / 3.0), (float) (-Math.PI / 12.0), (MathHelper.sin(-phase) + 1.0F) / 2.0F);
        part.yaw = MathHelper.clampedLerp((float) (-Math.PI / 4.0), (float) (Math.PI / 4.0), (MathHelper.sin(-phase + 1.0F) + 1.0F) / 2.0F);

        if (sigma == 1) {
            part.yaw = (float) Math.PI - part.yaw;
        }
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return this.parts;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        for (ModelPart part : this.getParts()) {
            part.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }
}