package net.ryan.beyond_the_block.utils.helpers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BreedingSyncState {
    private static final Map<Integer, Integer> AGE_MAP = new ConcurrentHashMap<>();

    public static void updateAge(int entityId, int age) {
        AGE_MAP.put(entityId, age);
    }

    public static int getAge(int entityId) {
        return AGE_MAP.getOrDefault(entityId, 0);
    }
}
