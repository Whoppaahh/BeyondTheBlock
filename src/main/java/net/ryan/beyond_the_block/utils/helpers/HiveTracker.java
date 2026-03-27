package net.ryan.beyond_the_block.utils.helpers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HiveTracker {

    private static final Set<BlockPos> HIVE_POSITIONS =
            Collections.synchronizedSet(new HashSet<>());

    public static void init() {
        ServerChunkEvents.CHUNK_LOAD.register(HiveTracker::onChunkLoad);
        ServerChunkEvents.CHUNK_UNLOAD.register(HiveTracker::onChunkUnload);
    }

    private static void onChunkLoad(ServerWorld world, WorldChunk chunk) {
        chunk.getBlockEntities().forEach((pos, be) -> {
            if (be instanceof BeehiveBlockEntity) {
                HIVE_POSITIONS.add(pos.toImmutable());
            }
        });
    }

    private static void onChunkUnload(ServerWorld world, WorldChunk chunk) {
        chunk.getBlockEntities().forEach((pos, be) -> {
            if (be instanceof BeehiveBlockEntity) {
                HIVE_POSITIONS.remove(pos);
            }
        });
    }

    public static Set<BlockPos> getHives() {
        return HIVE_POSITIONS;
    }

    public static void remove(BlockPos pos) {
        HIVE_POSITIONS.remove(pos);
    }
}
