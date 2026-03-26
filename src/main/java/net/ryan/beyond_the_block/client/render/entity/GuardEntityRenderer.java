package net.ryan.beyond_the_block.client.render.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.village.GuardVillager.GuardEntity;
import net.ryan.beyond_the_block.content.village.ModVillagers;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.ryan.beyond_the_block.content.village.ModVillagers.GUARD_ENTITY_MODEL;


public class GuardEntityRenderer extends BipedEntityRenderer<GuardEntity, BipedEntityModel<GuardEntity>> {

    private final List<Identifier> GUARD_TEXTURES = new ArrayList<>(List.of(
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard.png"),
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard1.png"),
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard2.png"),
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard3.png"),
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard4.png"),
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard5.png"),
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard6.png"),
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard7.png"),
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard8.png"),
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard9.png"),
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard10.png"),
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/villager/guards/guard11.png")
    ));



    public GuardEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new GuardEntityModel(context.getPart(GUARD_ENTITY_MODEL)), 0.5F);
        this.model = this.getModel();
        this.addFeature(new ArmorFeatureRenderer<>(this,
                new GuardEntityArmourModel(context.getPart(ModVillagers.GUARD_ENTITY_ARMOUR_INNER)),
                new GuardEntityArmourModel(context.getPart(ModVillagers.GUARD_ENTITY_ARMOUR_OUTER))));
    }

    @Override
    public void render(GuardEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        this.setModelVisibilities(entityIn);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void scale(GuardEntity guardEntity, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Nullable
    @Override
    public Identifier getTexture(GuardEntity entity) {
        if(Configs.client().visuals.guards.variants) {
            int index = Math.abs(entity.getUuid().hashCode()) % GUARD_TEXTURES.size();
            return GUARD_TEXTURES.get(index);
        }
        return GUARD_TEXTURES.get(0);
    }
    private void setModelVisibilities(GuardEntity entityIn) {
        BipedEntityModel<GuardEntity> guardModel = this.getModel();
        ItemStack mainHandStack = entityIn.getMainHandStack();
        ItemStack offHandStack = entityIn.getOffHandStack();
        guardModel.setVisible(true);
        BipedEntityModel.ArmPose bipedmodel$armpose = this.getArmPose(entityIn, mainHandStack, offHandStack,
                Hand.MAIN_HAND);
        BipedEntityModel.ArmPose bipedmodel$armpose1 = this.getArmPose(entityIn, mainHandStack, offHandStack,
                Hand.OFF_HAND);
        guardModel.sneaking = entityIn.isSneaking();
        if (entityIn.getMainArm() == Arm.RIGHT) {
            guardModel.rightArmPose = bipedmodel$armpose;
            guardModel.leftArmPose = bipedmodel$armpose1;
        } else {
            guardModel.rightArmPose = bipedmodel$armpose1;
            guardModel.leftArmPose = bipedmodel$armpose;
        }
    }
    private BipedEntityModel.ArmPose getArmPose(GuardEntity entityIn, ItemStack itemStackMain, ItemStack itemStackOff, Hand handIn) {
        BipedEntityModel.ArmPose bipedmodel$armpose = BipedEntityModel.ArmPose.EMPTY;
        ItemStack itemstack = handIn == Hand.MAIN_HAND ? itemStackMain : itemStackOff;
        if (!itemstack.isEmpty()) {
            bipedmodel$armpose = BipedEntityModel.ArmPose.ITEM;
            if (entityIn.getItemUseTimeLeft() > 0) {
                UseAction useaction = itemstack.getUseAction();
                switch (useaction) {
                    case BLOCK:
                        bipedmodel$armpose = BipedEntityModel.ArmPose.BLOCK;
                        break;
                    case BOW:
                        bipedmodel$armpose = BipedEntityModel.ArmPose.BOW_AND_ARROW;
                        break;
                    case SPEAR:
                        bipedmodel$armpose = BipedEntityModel.ArmPose.THROW_SPEAR;
                        break;
                    case CROSSBOW:
                        if (handIn == entityIn.getActiveHand()) {
                            bipedmodel$armpose = BipedEntityModel.ArmPose.CROSSBOW_CHARGE;
                        }
                        break;
                    default:
                        bipedmodel$armpose = BipedEntityModel.ArmPose.EMPTY;
                        break;
                }
            } else {
                boolean flag1 = itemStackMain.getItem() instanceof CrossbowItem;
                boolean flag2 = itemStackOff.getItem() instanceof CrossbowItem;
                if (flag1 && entityIn.isAttacking()) {
                    bipedmodel$armpose = BipedEntityModel.ArmPose.CROSSBOW_HOLD;
                }

                if (flag2 && itemStackMain.getItem().getUseAction(itemStackMain) == UseAction.NONE
                        && entityIn.isAttacking()) {
                    bipedmodel$armpose = BipedEntityModel.ArmPose.CROSSBOW_HOLD;
                }
            }
        }
        return bipedmodel$armpose;
    }
}