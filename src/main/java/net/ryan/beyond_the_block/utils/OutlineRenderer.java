package net.ryan.beyond_the_block.utils;


import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.utils.Helpers.HighlightTracker;

public class OutlineRenderer {
    public static void init() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            MatrixStack matrices = context.matrixStack();
            Camera camera = context.camera();
            Vec3d cameraPos = camera.getPos();

            ClientWorld world = MinecraftClient.getInstance().world;
            if(world == null) return;

            VertexConsumerProvider.Immediate bufferSource = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            VertexConsumer buffer = bufferSource.getBuffer(RenderLayer.LINES);

            for (BlockPos pos : HighlightTracker.getHighlights()) {
                Block block = world.getBlockState(pos).getBlock();
                BlockState state = world.getBlockState(pos);
                if (state.isAir()) continue;
                float r, g, b;
                // Cycle through rainbow colors
                float cycleDuration = 4000f; // 4 seconds for full rainbow cycle
                float time = (System.currentTimeMillis() % (long)cycleDuration) / cycleDuration;
                int rgb = MathHelper.hsvToRgb(time, 1f, 1f);
                r = ((rgb >> 16) & 0xFF) / 255f;
                g = ((rgb >> 8) & 0xFF) / 255f;
                b = (rgb & 0xFF) / 255f;

                if (block.getDefaultState().isIn(ConventionalBlockTags.CHESTS)) {
                    r = 1f; g = 0.5f; b = 0f; // Orange
                } else if (block.getDefaultState().isIn(ConventionalBlockTags.ORES)) {
                    r = 0f; g = 1f; b = 1f; // Cyan
                }

                //float time = (System.currentTimeMillis() % 2000L) / 1000f; // [0, 2)
                float alpha = 0.5f + 0.5f * MathHelper.sin(time * (float)Math.PI); // Pulses between 0 and 1

                Box box = new Box(pos).offset(-cameraPos.x, -cameraPos.y, -cameraPos.z);
                WorldRenderer.drawBox(matrices, buffer, box, r, g, b, alpha);
            }


            bufferSource.draw();
        });
    }

}
