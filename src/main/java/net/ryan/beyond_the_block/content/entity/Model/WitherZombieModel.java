package net.ryan.beyond_the_block.content.entity.Model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.mob.HostileEntity;
import net.ryan.beyond_the_block.content.entity.WitherZombie;

public class WitherZombieModel<T extends HostileEntity> extends BipedEntityModel<T> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public WitherZombieModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0F);
        return TexturedModelData.of(modelData, 64, 64); // 64x64 texture
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        // If attacking, raise the right arm (for bow-pulling animation)
        if (entity.isAttacking() && entity instanceof WitherZombie zombie
                && zombie.usesBow) {
            this.rightArm.pitch = -1.6F;  // Change pitch to simulate arm raising
            this.rightArm.yaw = -0.2f;
            this.rightArm.roll = 0.0f;

            this.leftArm.pitch = -1.2F;
            this.leftArm.yaw = 0.6F;
            this.leftArm.roll = 0.0F;
        }else{
            super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        }
    }

}
