package net.ryan.beyond_the_block.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.paths.PathPreviewState;
import net.ryan.beyond_the_block.feature.paths.PathToolHelper;

import java.util.List;

public class ClientHudRegistrar {
    public static void register(){
        FloatingXPManager.register();
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



    public static void updatePathPreview(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            PathPreviewState.clear();
            return;
        }

        ItemStack stack = client.player.getMainHandStack();
        if (!(stack.getItem() instanceof ShovelItem)) {
            PathPreviewState.clear();
            return;
        }

        // Must have a starting point set
        if (!PathToolHelper.hasStart(stack)) {
            PathPreviewState.clear();
            return;
        }

        // Must be looking at a block
        if (!(client.crosshairTarget instanceof BlockHitResult hit)) {
            PathPreviewState.clear();
            return;
        }

        BlockPos start = PathToolHelper.getStart(stack);
        BlockPos end = hit.getBlockPos();

        // Too far? No preview
        if (!PathToolHelper.withinMaxDistance(start, end,  Configs.server().features.paths.maxDistance)) {
            PathPreviewState.clear();
            return;
        }

        // Compute width
        int width = PathToolHelper.getWidth(stack, Configs.server());

        // Compute line & widened area
        List<BlockPos> centerLine = PathToolHelper.computeLine2D(start, end);
        var direction = PathToolHelper.getPrimaryDirection(start, end);
        List<BlockPos> full = PathToolHelper.widenLine(centerLine, width, direction);

        // Terrain-follow for preview
        List<BlockPos> adjusted = full.stream()
                .map(pos -> PathToolHelper.adjustToTerrain(client.world, pos,  Configs.server().features.paths.useTerrainFollowing))
                .toList();

        PathPreviewState.setPositions(adjusted);
    }
}
