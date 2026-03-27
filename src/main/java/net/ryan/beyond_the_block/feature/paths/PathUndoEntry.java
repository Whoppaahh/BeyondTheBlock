package net.ryan.beyond_the_block.feature.paths;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class PathUndoEntry {
    public final List<BlockChange> changes = new ArrayList<>();

    public record BlockChange(BlockPos pos, BlockState before, BlockState after) {
    }
}

