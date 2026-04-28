package net.ryan.beyond_the_block.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.entity.WolfArmourHolder;
import net.ryan.beyond_the_block.content.item.armour.WolfArmourItem;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.utils.ModTags;

@Environment(EnvType.CLIENT)
public class WolfArmourFeatureRenderer extends FeatureRenderer<WolfEntity, WolfEntityModel<WolfEntity>> {
    private static final Identifier WOLF_ARMOUR_TEXTURE =
            new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/wolf/wolf_armor.png");

    public WolfArmourFeatureRenderer(FeatureRendererContext<WolfEntity, WolfEntityModel<WolfEntity>> context) {
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
        WolfArmourHolder holder = (WolfArmourHolder) wolf;
        ItemStack armor = holder.btb$getWolfArmour();
        if (armor.isEmpty() || !armor.isIn(ModTags.Items.MOD_ARMOURS)) {
            return;
        }

        WolfEntityModel<WolfEntity> model = this.getContextModel();
        model.copyStateTo(model);

        int color = 0xFFFFFF;

        if (armor.getItem() instanceof WolfArmourItem wolfArmorItem) {
            color = wolfArmorItem.getTintColor();
        }

        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(WOLF_ARMOUR_TEXTURE));
        model.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1.0F);
    }
}
