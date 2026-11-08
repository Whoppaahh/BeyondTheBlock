package net.ryan.beyond_the_block.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class CampfireProtectionManager {

    private static final int RADIUS = 30;
    private static final int RADIUS_SQUARED = RADIUS * RADIUS;
    private static final int VERTICAL_RANGE = 8;

    /*
     * Each world has its own map.
     * Each chunk contains the positions of its lit campfires.
     */
    private static final Map<ServerWorld, Map<ChunkPos, Set<BlockPos>>> WORLDS =
            new WeakHashMap<>();

    private CampfireProtectionManager() {
    }

    private static boolean isLitCampfire(BlockState state) {
        return state.getBlock() instanceof CampfireBlock
                && state.get(CampfireBlock.LIT);
    }

    public static void onChunkLoad(ServerWorld world, WorldChunk chunk) {
        Map<ChunkPos, Set<BlockPos>> worldIndex =
                WORLDS.computeIfAbsent(world, ignored -> new HashMap<>());

        Set<BlockPos> campfires = new HashSet<>();

        /*
         * Campfires have block entities, so this avoids scanning
         * every block in the chunk.
         */
        for (BlockPos pos : chunk.getBlockEntities().keySet()) {
            BlockState state = chunk.getBlockState(pos);

            if (isLitCampfire(state)) {
                campfires.add(pos.toImmutable());
            }
        }

        ChunkPos chunkPos = chunk.getPos();

        if (campfires.isEmpty()) {
            worldIndex.remove(chunkPos);
        } else {
            worldIndex.put(chunkPos, campfires);
        }
    }

    public static void onChunkUnload(ServerWorld world, WorldChunk chunk) {
        Map<ChunkPos, Set<BlockPos>> worldIndex = WORLDS.get(world);

        if (worldIndex == null) {
            return;
        }

        worldIndex.remove(chunk.getPos());

        if (worldIndex.isEmpty()) {
            WORLDS.remove(world);
        }
    }

    public static void onBlockStateChanged(
            ServerWorld world,
            BlockPos pos,
            BlockState oldState,
            BlockState newState
    ) {
        boolean wasLitCampfire = isLitCampfire(oldState);
        boolean isLitCampfire = isLitCampfire(newState);

        if (wasLitCampfire == isLitCampfire) {
            return;
        }

        Map<ChunkPos, Set<BlockPos>> worldIndex =
                WORLDS.computeIfAbsent(world, ignored -> new HashMap<>());

        ChunkPos chunkPos = new ChunkPos(pos);

        if (isLitCampfire) {
            worldIndex.computeIfAbsent(chunkPos, ignored -> new HashSet<>())
                    .add(pos.toImmutable());
        } else {
            Set<BlockPos> campfires = worldIndex.get(chunkPos);

            if (campfires != null) {
                campfires.remove(pos);

                if (campfires.isEmpty()) {
                    worldIndex.remove(chunkPos);
                }
            }
        }
    }

    public static boolean isProtected(ServerWorld world, BlockPos spawnPos) {
        Map<ChunkPos, Set<BlockPos>> worldIndex = WORLDS.get(world);

        if (worldIndex == null || worldIndex.isEmpty()) {
            return false;
        }

        int minChunkX = Math.floorDiv(spawnPos.getX() - RADIUS, 16);
        int maxChunkX = Math.floorDiv(spawnPos.getX() + RADIUS, 16);
        int minChunkZ = Math.floorDiv(spawnPos.getZ() - RADIUS, 16);
        int maxChunkZ = Math.floorDiv(spawnPos.getZ() + RADIUS, 16);

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                Set<BlockPos> campfires =
                        worldIndex.get(new ChunkPos(chunkX, chunkZ));

                if (campfires == null) {
                    continue;
                }

                for (BlockPos campfirePos : campfires) {
                    int dx = campfirePos.getX() - spawnPos.getX();
                    int dz = campfirePos.getZ() - spawnPos.getZ();
                    int dy = Math.abs(campfirePos.getY() - spawnPos.getY());

                    if (dy <= VERTICAL_RANGE
                            && dx * dx + dz * dz <= RADIUS_SQUARED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}