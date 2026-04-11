package net.ryan.beyond_the_block.client.render.blockentity;

import net.minecraft.client.model.*;

public final class HangingSignModels {
    private HangingSignModels() {
    }

    public enum AttachmentType {
        WALL,
        CEILING,
        CEILING_MIDDLE
    }

    public static TexturedModelData getTexturedModelData(AttachmentType attachmentType) {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        root.addChild(
                "board",
                ModelPartBuilder.create()
                        .uv(0, 12)
                        .cuboid(-7.0F, 0.0F, -1.0F, 14.0F, 10.0F, 2.0F),
                ModelTransform.NONE
        );

        if (attachmentType == AttachmentType.WALL) {
            root.addChild(
                    "plank",
                    ModelPartBuilder.create()
                            .uv(0, 0)
                            .cuboid(-8.0F, -6.0F, -2.0F, 16.0F, 2.0F, 4.0F),
                    ModelTransform.NONE
            );
        }

        if (attachmentType == AttachmentType.WALL || attachmentType == AttachmentType.CEILING) {
            ModelPartData normalChains = root.addChild("normalChains", ModelPartBuilder.create(), ModelTransform.NONE);

            normalChains.addChild(
                    "chainL1",
                    ModelPartBuilder.create().uv(0, 6).cuboid(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F),
                    ModelTransform.of(-5.0F, -6.0F, 0.0F, 0.0F, (-(float) Math.PI / 4F), 0.0F)
            );
            normalChains.addChild(
                    "chainL2",
                    ModelPartBuilder.create().uv(6, 6).cuboid(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F),
                    ModelTransform.of(-5.0F, -6.0F, 0.0F, 0.0F, ((float) Math.PI / 4F), 0.0F)
            );
            normalChains.addChild(
                    "chainR1",
                    ModelPartBuilder.create().uv(0, 6).cuboid(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F),
                    ModelTransform.of(5.0F, -6.0F, 0.0F, 0.0F, (-(float) Math.PI / 4F), 0.0F)
            );
            normalChains.addChild(
                    "chainR2",
                    ModelPartBuilder.create().uv(6, 6).cuboid(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F),
                    ModelTransform.of(5.0F, -6.0F, 0.0F, 0.0F, ((float) Math.PI / 4F), 0.0F)
            );
        }

        if (attachmentType == AttachmentType.CEILING_MIDDLE) {
            root.addChild(
                    "vChains",
                    ModelPartBuilder.create()
                            .uv(14, 6)
                            .cuboid(-6.0F, -6.0F, 0.0F, 12.0F, 6.0F, 0.0F),
                    ModelTransform.NONE
            );
        }

        return TexturedModelData.of(modelData, 64, 32);
    }
}