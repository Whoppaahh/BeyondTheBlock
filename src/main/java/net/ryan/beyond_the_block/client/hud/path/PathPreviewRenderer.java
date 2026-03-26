package net.ryan.beyond_the_block.client.hud.path;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.feature.paths.PathPreviewState;

public class PathPreviewRenderer {
    public static void renderPathPreview(MatrixStack matrices) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();

        VertexConsumerProvider.Immediate provider = client.getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer consumer = provider.getBuffer(RenderLayer.getLines());

        for (BlockPos pos : PathPreviewState.getPositions()) {
            double x = pos.getX() - camPos.x;
            double y = pos.getY() - camPos.y;
            double z = pos.getZ() - camPos.z;

            Box box = new Box(x, y, z, x + 1, y + 1, z + 1);

            WorldRenderer.drawBox(
                    matrices,
                    consumer,
                    box,
                    0f, 1f, 0f, 1f // RGBA - bright green
            );
        }

        provider.draw(); // flush to GPU
    }
}
