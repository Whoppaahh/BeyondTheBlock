package net.ryan.beyond_the_block.utils.Helpers;

import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

public class PathPreviewState {

    private static List<BlockPos> positions = Collections.emptyList();

    public static void setPositions(List<BlockPos> list) {
        positions = list;
    }

    public static List<BlockPos> getPositions() {
        return positions;
    }

    public static boolean hasPreview() {
        return !positions.isEmpty();
    }

    public static void clear() {
        positions = Collections.emptyList();
    }
}

