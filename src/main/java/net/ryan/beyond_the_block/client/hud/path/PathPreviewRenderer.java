package net.ryan.beyond_the_block.client.hud.path;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.feature.paths.PathPreviewState;

public class PathPreviewRenderer {

    private static final double Y_OFFSET = 0.03D;
    private static final double THICKNESS = 0.001D;
    private static final double INSET = 0.002D;

    private PathPreviewRenderer() {
    }

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

            Box box = new Box(
                    x + INSET,
                    y + 1.0D + Y_OFFSET,
                    z + INSET,
                    x + 1.0D - INSET,
                    y + 1.0D + Y_OFFSET + THICKNESS,
                    z + 1.0D - INSET
            );

            WorldRenderer.drawBox(
                    matrices,
                    consumer,
                    box,
                    0.0F, 1.0F, 0.0F, 1.0F
            );
        }

        provider.draw();
    }
}