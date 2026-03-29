package net.ryan.beyond_the_block.feature.paths.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.config.sync.SyncedServerConfig;
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

        if (!PathToolHelper.hasStart(stack)) {
            PathPreviewState.clear();
            return;
        }

        if (!(client.crosshairTarget instanceof BlockHitResult hit)) {
            PathPreviewState.clear();
            return;
        }

        SyncedServerConfig synced = Configs.syncedServerConfig();

        BlockPos start = PathToolHelper.getStart(stack);
        BlockPos end = hit.getBlockPos();

        if (!PathToolHelper.withinMaxDistance(start, end, synced.pathsMaxDistance())) {
            PathPreviewState.clear();
            return;
        }

        List<BlockPos> affected = PathToolHelper.computeAffectedPositionsForPreview(
                client.world,
                stack,
                start,
                end,
                synced
        );

        PathPreviewState.setPositions(affected);
    }
}