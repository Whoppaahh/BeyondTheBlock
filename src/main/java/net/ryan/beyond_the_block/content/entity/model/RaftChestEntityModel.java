package net.ryan.beyond_the_block.content.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;

public class RaftChestEntityModel extends RaftEntityModel {
    private final ModelPart chestBottom;
    private final ModelPart chestLid;
    private final ModelPart chestLock;
    private final ImmutableList<ModelPart> allParts;

    public RaftChestEntityModel(ModelPart root) {
        super(root);
        this.chestBottom = root.getChild("chest_bottom");
        this.chestLid = root.getChild("chest_lid");
        this.chestLock = root.getChild("chest_lock");
        this.allParts = ImmutableList.of(this.bottom, this.leftPaddle, this.rightPaddle, this.chestBottom, this.chestLid, this.chestLock);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        addParts(root);

        root.addChild(
                "chest_bottom",
                ModelPartBuilder.create()
                        .uv(0, 76)
                        .cuboid(0.0F, 0.0F, 0.0F, 12.0F, 8.0F, 12.0F),
                ModelTransform.of(-2.0F, -10.1F, -6.0F, 0.0F, (-(float) Math.PI / 2.0F), 0.0F)
        );

        root.addChild(
                "chest_lid",
                ModelPartBuilder.create()
                        .uv(0, 59)
                        .cuboid(0.0F, 0.0F, 0.0F, 12.0F, 4.0F, 12.0F),
                ModelTransform.of(-2.0F, -14.1F, -6.0F, 0.0F, (-(float) Math.PI / 2.0F), 0.0F)
        );

        root.addChild(
                "chest_lock",
                ModelPartBuilder.create()
                        .uv(0, 59)
                        .cuboid(0.0F, 0.0F, 0.0F, 2.0F, 4.0F, 1.0F),
                ModelTransform.of(-1.0F, -11.1F, -1.0F, 0.0F, (-(float) Math.PI / 2.0F), 0.0F)
        );

        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return this.allParts;
    }
}