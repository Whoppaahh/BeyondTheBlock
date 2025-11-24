package net.ryan.beyond_the_block.riddles;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RiddleClientCache {
    public static Map<UUID, Riddle> activeRiddles = new HashMap<>();
    public static Map<UUID, Set<UUID>> completedRiddles = new HashMap<>();

    public static void set(Map<UUID, Riddle> active, Map<UUID, Set<UUID>> completed) {
        activeRiddles = active;
        completedRiddles = completed;
    }
}
