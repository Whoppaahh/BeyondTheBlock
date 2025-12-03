package net.ryan.beyond_the_block.utils.Snow;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class SnowDebugRenderer {

    public static boolean enabled = false; // Toggle via keybind or config

    public static void init() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(SnowDebugRenderer::render);
    }

    private static void render(WorldRenderContext ctx) {
        if (!enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        World world = client.world;
        if (world == null) return;

        Camera camera = ctx.camera();
        double camX = camera.getPos().x;
        double camY = camera.getPos().y;
        double camZ = camera.getPos().z;

        MatrixStack matrices = ctx.matrixStack();
        VertexConsumerProvider.Immediate consumers = (VertexConsumerProvider.Immediate) ctx.consumers();
        VertexConsumer buffer = consumers.getBuffer(RenderLayer.LINES);

        BlockPos playerPos = client.player.getBlockPos();
        int radius = 16; // 32x32 block area around player

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int x = playerPos.getX() + dx;
                int z = playerPos.getZ() + dz;

                BlockPos top = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, new BlockPos(x, 0, z));
                BlockState topState = world.getBlockState(top);

                boolean inTag = topState.isIn(SnowHelper.SNOW_CAN_COVER);

                // Only show relevant columns: tagged blocks or open sky
                if (!inTag && !topState.isAir()) continue;

                // Box for top position
                Color topColor = inTag ? Color.RED : Color.WHITE; // RED = tag, WHITE = open-sky snow layer
                drawWireBox(matrices, buffer, camX, camY, camZ, top, topColor);

                // If in tag, also show ground where full snow block will form
                if (inTag) {
                    BlockPos ground = SnowHelper.findRealGround(world, top);
                    if (ground != null) {
                        drawWireBox(matrices, buffer, camX, camY, camZ, ground, Color.GREEN); // ground
                        drawWireBox(matrices, buffer, camX, camY, camZ, ground, Color.CYAN);  // snow block
                    }
                }
            }
        }

        consumers.draw();
    }

    private static void drawWireBox(
            MatrixStack matrices,
            VertexConsumer buffer,
            double camX, double camY, double camZ,
            BlockPos pos,
            Color color
    ) {
        matrices.push();

        matrices.translate(
                pos.getX() - camX,
                pos.getY() - camY,
                pos.getZ() - camZ
        );

        Matrix4f mat = matrices.peek().getPositionMatrix();
        float r = color.r;
        float g = color.g;
        float b = color.b;
        float a = 1.0f;

        Box box = new Box(0, 0, 0, 1, 1, 1);
        WorldRenderer.drawBox(
                matrices, buffer,
                box.minX, box.minY, box.minZ,
                box.maxX, box.maxY, box.maxZ,
                r, g, b, a
        );

        matrices.pop();
    }

    private static class Color {
        final float r, g, b;

        static final Color RED   = new Color(1f, 0f, 0f);
        static final Color GREEN = new Color(0f, 1f, 0f);
        static final Color CYAN  = new Color(0f, 1f, 1f);
        static final Color WHITE = new Color(1f, 1f, 1f);

        Color(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
}

