package net.ryan.beyond_the_block.client.render.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.entity.villager.guard.GuardEntity;

public class GuardEntityModel extends BipedEntityModel<GuardEntity> {
    private final ModelPart head;
	private final ModelPart hat;
    private final ModelPart quiver;
	private final ModelPart rightShoulder;
	private final ModelPart leftShoulder;
	private final ModelPart jacket;

	public GuardEntityModel(ModelPart root) {
		super(root);
        ModelPart body = root.getChild("body");
		this.quiver = body.getChild("quiver");
		this.head = root.getChild("head");
		this.hat = root.getChild("hat");
		this.jacket = body.getChild("jacket");
        ModelPart nose = this.head.getChild("nose");
        ModelPart rightArm = root.getChild("right_arm");
		this.rightShoulder = rightArm.getChild("right_shoulder");
        ModelPart leftArm = root.getChild("left_arm");
		this.leftShoulder = leftArm.getChild("left_shoulder");
        ModelPart rightLeg = root.getChild("right_leg");
        ModelPart leftLeg = root.getChild("left_leg");

		this.setRotateAngle(quiver, 0.0F, 0.0F, 0.2617993877991494F);
		this.setRotateAngle(rightShoulder, 0.0F, 0.0F, -0.3490658503988659F);
		this.setRotateAngle(leftShoulder, 0.0F, 0.0F, 0.3490658503988659F);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();
		ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(16, 20).cuboid(-4.0F, 0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		body.addChild("jacket", ModelPartBuilder.create().uv(0, 38).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, new Dilation(0.5F)), ModelTransform.NONE);
		body.addChild("quiver", ModelPartBuilder.create().uv(37, 51).cuboid(-0.3F, -0.5F, -0.475F, 3.0F, 7.0F, 2.75F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.48F));

		ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		root.addChild("hat", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.5F))
				.uv(34, 1).cuboid(-6.52F, -9.0F, -4.0F, 2.0F, 1.1F, 8.0F, new Dilation(0.0F))
				.uv(36, 3).cuboid(-7.32F, -8.0F, -3.5F, 2.8F, 1.1F, 7.0F, new Dilation(0.0F))
				.uv(36, 3).cuboid(-7.82F, -7.0F, -3.0F, 3.3F, 1.1F, 6.0F, new Dilation(0.0F))
				.uv(38, 5).cuboid(-7.8F, -6.0F, -2.5F, 1.0F, 1.1F, 5.0F, new Dilation(0.0F))
				.uv(38, 5).cuboid(-5.52F, -10.0F, -2.5F, 1.0F, 1.1F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		head.addChild("nose", ModelPartBuilder.create().uv(24, 0).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 0.0F));
		ModelPartData rightArm = root.addChild("right_arm", ModelPartBuilder.create().uv(45, 22).mirrored().cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
		rightArm.addChild("right_shoulder", ModelPartBuilder.create().uv(34, 41).cuboid(-2.5F, -1.5F, -3.0F, 5.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-1.5524F, -2.0499F, 0.0F, 0.0F, 0.0F, -0.1309F));
		ModelPartData leftArm = root.addChild("left_arm", ModelPartBuilder.create().uv(45, 22).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
		leftArm.addChild("left_shoulder", ModelPartBuilder.create().uv(34, 41).mirrored().cuboid(-2.5F, -1.5F, -3.0F, 5.0F, 3.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(1.5524F, -2.0499F, 0.0F, 0.0F, 0.0F, 0.1309F));
		root.addChild("right_leg", ModelPartBuilder.create().uv(0, 22).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 12.0F, 0.0F));
		root.addChild("left_leg", ModelPartBuilder.create().uv(0, 22).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, 12.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(GuardEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netbipedHeadYaw, float bipedHeadPitch) {
		super.setAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netbipedHeadYaw, bipedHeadPitch);
		ItemStack itemstack = entityIn.getStackInHand(Hand.MAIN_HAND);
		this.quiver.visible = itemstack.getItem() instanceof RangedWeaponItem;
		boolean hasChestplate = entityIn.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ArmorItem;
		boolean hasHelmet = entityIn.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof ArmorItem;

		this.leftShoulder.visible = !hasChestplate && Configs.client().visuals.guards.shoulderPads;
		this.rightShoulder.visible = !hasChestplate && Configs.client().visuals.guards.shoulderPads;
		this.jacket.visible = !hasChestplate;
		this.hat.visible = !hasHelmet && Configs.client().visuals.guards.berets;

		if (entityIn.getKickTicks() > 0) {
			float f1 = 1.0F - (float) MathHelper.abs(10 - 2 * entityIn.getKickTicks()) / 10.0F;
			this.rightLeg.pitch = MathHelper.lerp(f1, this.rightLeg.pitch, -1.40F);
		}
		if (entityIn.getMainArm() == Arm.RIGHT) {
			this.eatingAnimationRightHand(Hand.MAIN_HAND, entityIn, ageInTicks);
			this.eatingAnimationLeftHand(Hand.OFF_HAND, entityIn, ageInTicks);
		} else {
			this.eatingAnimationRightHand(Hand.OFF_HAND, entityIn, ageInTicks);
			this.eatingAnimationLeftHand(Hand.MAIN_HAND, entityIn, ageInTicks);
		}
	}
	public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
		ModelRenderer.pitch = x;
		ModelRenderer.yaw = y;
		ModelRenderer.roll = z;
	}
	public void eatingAnimationRightHand(Hand hand, GuardEntity entity, float ageInTicks) {
		ItemStack itemstack = entity.getStackInHand(hand);
		boolean drinkingOrEating = itemstack.getUseAction() == UseAction.EAT
				|| itemstack.getUseAction() == UseAction.DRINK;
		if (entity.isEating() && drinkingOrEating
				|| entity.getItemUseTimeLeft() > 0 && drinkingOrEating && entity.getActiveHand() == hand) {
			this.rightArm.yaw = -0.5F;
			this.rightArm.pitch = -1.3F;
			this.rightArm.roll = MathHelper.cos(ageInTicks) * 0.1F;
			this.head.pitch = MathHelper.cos(ageInTicks) * 0.2F;
			this.head.yaw = 0.0F;
			this.hat.copyTransform(head);
		}
	}

	public void eatingAnimationLeftHand(Hand hand, GuardEntity entity, float ageInTicks) {
		ItemStack itemstack = entity.getStackInHand(hand);
		boolean drinkingOrEating = itemstack.getUseAction() == UseAction.EAT
				|| itemstack.getUseAction() == UseAction.DRINK;
		if (entity.isEating() && drinkingOrEating
				|| entity.getItemUseTimeLeft() > 0 && drinkingOrEating && entity.getActiveHand() == hand) {
			this.leftArm.yaw = 0.5F;
			this.leftArm.pitch = -1.3F;
			this.leftArm.roll = MathHelper.cos(ageInTicks) * 0.1F;
			this.head.pitch = MathHelper.cos(ageInTicks) * 0.2F;
			this.head.yaw = 0.0F;
			this.hat.copyTransform(head);
		}
	}
}