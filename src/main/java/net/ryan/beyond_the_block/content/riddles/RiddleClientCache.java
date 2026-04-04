package net.ryan.beyond_the_block.content.riddles;

import net.ryan.beyond_the_block.config.access.Configs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class RiddleClientCache {
    private static Map<UUID, Riddle> activeRiddles = Map.of();
    private static Map<UUID, Set<UUID>> completedRiddles = Map.of();

    private static long syncedTimeOfDay = 0L;

    private RiddleClientCache() {
    }

    public static void set(Map<UUID, Riddle> active, Map<UUID, Set<UUID>> completed) {
        Map<UUID, Riddle> activeCopy = new HashMap<>(active);

        Map<UUID, Set<UUID>> completedCopy = new HashMap<>();
        for (Map.Entry<UUID, Set<UUID>> entry : completed.entrySet()) {
            completedCopy.put(entry.getKey(), Set.copyOf(entry.getValue()));
        }

        activeRiddles = Collections.unmodifiableMap(activeCopy);
        completedRiddles = Collections.unmodifiableMap(completedCopy);
    }

    public static Map<UUID, Riddle> getActiveRiddles() {
        return activeRiddles;
    }

    public static Map<UUID, Set<UUID>> getCompletedRiddles() {
        return completedRiddles;
    }

    public static Riddle getActiveRiddle(UUID playerId) {
        return activeRiddles.get(playerId);
    }

    public static boolean hasCompleted(UUID playerId, UUID riddleId) {
        return completedRiddles.getOrDefault(playerId, Set.of()).contains(riddleId);
    }

    public static void updateTime(long timeOfDay) {
        syncedTimeOfDay = timeOfDay;
    }

    public static long getSyncedTimeOfDay() {
        return syncedTimeOfDay;
    }

    public static long getSecondsUntilNextDay() {
        long interval = Configs.server().features.shrines.generationInterval;
        long ticksRemaining = interval - syncedTimeOfDay;
        return ticksRemaining / 20L;
    }

    public static void clear() {
        activeRiddles = Map.of();
        completedRiddles = Map.of();
        syncedTimeOfDay = 0L;
    }
}