package net.ryan.beyond_the_block.client.render.blockentity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.PedestalBlockEntity;

import java.util.Objects;

public class PedestalBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {

    public PedestalBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}


    @Override
    public void render(PedestalBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack stack = entity.getStack(0);

        matrices.push();
        matrices.translate(0.5f, 1f, 0.5f);
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(new Quaternion(Vec3f.POSITIVE_Y, entity.getRenderingRotation(), true));

        BakedModel model = itemRenderer.getModel(stack, entity.getWorld(), null, 0); // Get the item model
        itemRenderer.renderItem(stack, ModelTransformation.Mode.GUI, false, matrices, vertexConsumers,
                getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos()),
                OverlayTexture.DEFAULT_UV, model);

        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }

}

