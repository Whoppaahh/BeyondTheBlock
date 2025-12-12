package net.ryan.beyond_the_block.utils.Helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.utils.PathUndoEntry;

import java.util.*;

public class PathUndoManager {
    private static final Map<UUID, Deque<PathUndoEntry>> UNDO = new HashMap<>();

    public static void push(PlayerEntity player, PathUndoEntry entry) {
        UNDO.computeIfAbsent(player.getUuid(), k -> new ArrayDeque<>()).push(entry);
    }

    public static PathUndoEntry pop(PlayerEntity player) {
        Deque<PathUndoEntry> stack = UNDO.get(player.getUuid());
        if (stack == null || stack.isEmpty()) return null;
        return stack.pop();
    }

    public static boolean hasUndo(PlayerEntity player) {
        Deque<PathUndoEntry> stack = UNDO.get(player.getUuid());
        return stack != null && !stack.isEmpty();
    }
}

