package net.ryan.beyond_the_block.feature.paths;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.schema.ConfigServer;

import java.util.List;
import java.util.Set;

public final class PathBuilder {

    private PathBuilder() {}

    public static void buildPath(World world, PlayerEntity player, ItemStack stack,
                                 BlockPos start, BlockPos end,
                                 ConfigServer config) {

        if (world.isClient) return;

        int width = PathToolHelper.getWidth(stack, config);
        if (width < 1) width = 1;

        if (!PathToolHelper.withinMaxDistance(start, end, config.features.paths.maxDistance)) {
            return;
        }

        Set<Block> allowedStart = PathToolHelper.resolveBlockList(config.features.paths.allowedStartingBlocks);
        Set<Block> allowedEnd = PathToolHelper.resolveBlockList(config.features.paths.allowedEndingBlocks);

        List<BlockPos> full = PathToolHelper.computeAffectedPositions(
                world,
                start,
                end,
                width,
                config.features.paths.useTerrainFollowing,
                allowedStart,
                allowedEnd
        );

        if (full.isEmpty()) {
            return;
        }

        int blocksChanged = 0;
        PathUndoEntry undo = new PathUndoEntry();

        for (BlockPos adjusted : full) {
            BlockState current = world.getBlockState(adjusted);
            BlockState target = PathToolHelper.resolvePathBlockFor(world, player, adjusted, config);

            if (!current.isOf(target.getBlock())) {
                undo.changes.add(new PathUndoEntry.BlockChange(adjusted.toImmutable(), current, target));
                world.setBlockState(adjusted, target, Block.NOTIFY_ALL);
                blocksChanged++;
            }
        }

        if (!undo.changes.isEmpty()) {
            PathUndoManager.push(player, undo);
        }

        if (!config.features.paths.preserveDurability && blocksChanged > 0) {
            stack.damage(blocksChanged, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
        }

        PathToolHelper.clearStart(stack);
    }
}