package net.ryan.beyond_the_block.feature.theft;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.concurrent.ConcurrentLinkedQueue;

public class VillageContainerScannerManager {
    private static final ConcurrentLinkedQueue<ChunkPos> chunkScanQueue = new ConcurrentLinkedQueue<>();
    public record QueuedChunk(RegistryKey<World> worldKey, ChunkPos chunkPos) {}

    public static void queueChunkForScan(ServerWorld world, ChunkPos chunkPos) {

        chunkScanQueue.offer(chunkPos);
    }

    public static void tick(ServerWorld world) {
        ChunkPos pos;
        while ((pos = chunkScanQueue.poll()) != null) {
            VillageContainerScanner.scanChunk(world, pos);
        }
    }
}
