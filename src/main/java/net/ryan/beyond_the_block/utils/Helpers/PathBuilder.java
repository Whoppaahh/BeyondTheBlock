package net.ryan.beyond_the_block.utils.Helpers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.ModConfig;
import net.ryan.beyond_the_block.utils.PathUndoEntry;

import java.util.List;
import java.util.Set;

public final class PathBuilder {

    private PathBuilder() {}

    public static void buildPath(World world, PlayerEntity player, ItemStack stack,
                                 BlockPos start, BlockPos end,
                                 ModConfig config) {

        if (!(world instanceof ServerWorld serverWorld)) return;

        // Config shortcuts
        var pc = config.pathConfig;

        // Width: sneaking on second click -> configured width, else 1
        int width = PathToolHelper.getWidth(stack, config);
        if (width < 1) width = 1;

        // Limits
        if (!PathToolHelper.withinMaxDistance(start, end, pc.maxDistance)) {
            return;
        }

        Set<Block> allowedEnd = PathToolHelper.resolveBlockList(pc.allowedEndingBlocks);
        Set<Block> allowedStart = PathToolHelper.resolveBlockList(pc.allowedStartingBlocks);

        // Ensure start and end are allowed as per config
        if (!allowedStart.contains(world.getBlockState(start).getBlock())) return;
        if (!allowedEnd.contains(world.getBlockState(end).getBlock())) return;

        // Compute center line and then widen it
        List<BlockPos> center = PathToolHelper.computeLine2D(start, end);
        var primaryDir = PathToolHelper.getPrimaryDirection(start, end);
        List<BlockPos> full = PathToolHelper.widenLine(center, width, primaryDir);

        int blocksChanged = 0;

        PathUndoEntry undo = new PathUndoEntry();

        for (BlockPos pos : full) {
            BlockPos adjusted = PathToolHelper.adjustToTerrain(world, pos, pc.useTerrainFollowing);
            BlockState current = world.getBlockState(adjusted);

            if (!allowedEnd.contains(current.getBlock())) continue;

            BlockState target = PathToolHelper.resolvePathBlockFor(world, player, adjusted, config);

            if (!current.isOf(target.getBlock())) {
                // store undo data
                undo.changes.add(new PathUndoEntry.BlockChange(adjusted.toImmutable(), current, target));

                world.setBlockState(adjusted, target, Block.NOTIFY_ALL);
                blocksChanged++;
            }
        }

// push undo entry
        if (!undo.changes.isEmpty()) {
            PathUndoManager.push(player, undo);
        }


        if (!pc.preserveDurability && blocksChanged > 0) {
            stack.damage(blocksChanged, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
        }

        PathToolHelper.clearStart(stack);
    }
}
