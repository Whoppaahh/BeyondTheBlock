package net.ryan.beyond_the_block.feature.paths.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.paths.PathPreviewState;
import net.ryan.beyond_the_block.feature.paths.PathToolHelper;

import java.util.List;

public class PathPreviewController {
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
        if (!PathToolHelper.withinMaxDistance(start, end, Configs.syncedServerConfig().pathsMaxDistance())) {
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
                .map(pos -> PathToolHelper.adjustToTerrain(client.world, pos, Configs.syncedServerConfig().pathsUseTerrainFollowing()))
                .toList();

        PathPreviewState.setPositions(adjusted);
    }
}
