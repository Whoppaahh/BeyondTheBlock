package net.ryan.beyond_the_block.client.render.blockentity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.ShrineHeadsBlockEntity;

import java.util.Objects;

public class ShrineHeadsBlockEntityRenderer implements BlockEntityRenderer<ShrineHeadsBlockEntity> {
    ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

    public ShrineHeadsBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(ShrineHeadsBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        matrices.push();
        // Positioning the block entity center
        matrices.translate(0.5D, 0.5D, 0.5D); // Adjust the center position of the block

        for (int i = 0; i < 4; i++) {
            ItemStack headItem = entity.getItems().get(i);
            if (!headItem.isEmpty()) {
                Direction face = entity.getHeadDirectionForSlot(i);
                if (face != null && !headItem.isEmpty()) {
                    renderHeadOnFace(matrices, vertexConsumers, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos()), overlay, headItem, face);
                }
            }
        }


        matrices.pop();
    }

    // Helper method to render a head on a specific face
    private void renderHeadOnFace(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                  int light, int overlay, ItemStack headItem,
                                  Direction face) {
        matrices.push();
        // Half the block (0.5) - item depth (~0.0625)
        float scale = 1f; // Slightly bigger than before
        float depthOffset = 0.5f - (0.0625f * scale); // Push to just kiss the face

        switch (face) {
            case NORTH -> //  matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90)); // flip upside down
                    matrices.translate(0.0, 0.0, -depthOffset);
            case SOUTH -> {
                matrices.translate(0.0, 0.0, depthOffset);
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
            }
            case EAST -> {
                matrices.translate(depthOffset, 0.0, 0.0);
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90));
            }
            case WEST -> {
                matrices.translate(-depthOffset, 0.0, 0.0);
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
            }
        }

        // Scale down so the head isn’t huge
        matrices.scale(scale, scale, scale);
        itemRenderer.renderItem(headItem, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }

}

