package net.ryan.beyond_the_block.utils.Helpers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.block.entity.BeehiveBlockEntity;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HiveTracker {

    private static final Set<BeehiveBlockEntity> HIVES =
            Collections.synchronizedSet(new HashSet<>());

    public static void init() {

        // When a chunk loads → scan it for beehives
        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            for (var be : chunk.getBlockEntities().values()) {
                if (be instanceof BeehiveBlockEntity hive) {
                    HIVES.add(hive);
                }
            }
        });

        // When a chunk unloads → remove any beehives stored in it
        ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
            for (var be : chunk.getBlockEntities().values()) {
                if (be instanceof BeehiveBlockEntity hive) {
                    HIVES.remove(hive);
                }
            }
        });
    }

    public static Set<BeehiveBlockEntity> getHives() {
        return HIVES;
    }
}


