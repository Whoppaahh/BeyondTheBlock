package net.ryan.beyond_the_block.feature.paths;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class PathUndoEntry {
    public final List<BlockChange> changes = new ArrayList<>();

    public static class BlockChange {
        public final BlockPos pos;
        public final BlockState before;
        public final BlockState after;

        public BlockChange(BlockPos pos, BlockState before, BlockState after) {
            this.pos = pos;
            this.before = before;
            this.after = after;
        }
    }
}

