package net.ryan.beyond_the_block.client.render.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.ryan.beyond_the_block.content.block.ShelfBlock;
import net.ryan.beyond_the_block.content.blockentity.ShelfBlockEntity;

public class ShelfBlockEntityRenderer implements BlockEntityRenderer<ShelfBlockEntity> {
    public ShelfBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(ShelfBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState state = entity.getCachedState();
        if (!(state.getBlock() instanceof ShelfBlock)) {
            return;
        }

        Direction facing = state.get(ShelfBlock.FACING);
        MinecraftClient client = MinecraftClient.getInstance();

        for (int i = 0; i < 3; i++) {
            ItemStack stack = entity.getStack(i);
            if (stack.isEmpty()) continue;

            matrices.push();

            // Center of block first
            matrices.translate(0.5, 0.5, 0.5);

            // Rotate so our local "front" always points out of the shelf face
            switch (facing) {
                case NORTH -> {
                }
                case SOUTH -> matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
                case WEST  -> matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0f));
                case EAST  -> matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0f));
            }

            // Three horizontal positions across the face
            float x = switch (i) {
                case 0 -> -0.31f;
                case 1 ->  0.00f;
                case 2 ->  0.31f;
                default -> 0.0f;
            };

            // Bring the item slightly in front of the shelf face to avoid z-fighting
            matrices.translate(x, 0f, 0.33f);

            // Slightly smaller than item frame display
            matrices.scale(0.5f, 0.5f, 0.5f);

            BakedModel model = client.getItemRenderer().getModel(
                    stack,
                    entity.getWorld(),
                    null,
                    0
            );

            client.getItemRenderer().renderItem(
                    stack,
                    ModelTransformation.Mode.FIXED,
                    false,
                    matrices,
                    vertexConsumers,
                    light,
                    OverlayTexture.DEFAULT_UV,
                    model
            );

            matrices.pop();
        }
    }


}
