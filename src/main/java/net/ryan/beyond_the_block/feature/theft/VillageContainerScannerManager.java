package net.ryan.beyond_the_block.feature.theft;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.concurrent.ConcurrentLinkedQueue;

public final class VillageContainerScannerManager {
    private static final int MAX_CHUNKS_PER_TICK = 8;

    public record QueuedChunk(RegistryKey<World> worldKey, ChunkPos chunkPos) {}

    private static final ConcurrentLinkedQueue<QueuedChunk> chunkScanQueue = new ConcurrentLinkedQueue<>();

    private VillageContainerScannerManager() {}

    public static void queueChunkForScan(ServerWorld world, ChunkPos chunkPos) {
        chunkScanQueue.offer(new QueuedChunk(world.getRegistryKey(), chunkPos));
    }

    public static void tick(ServerWorld world) {
        int processed = 0;
        int scannedOtherWorldEntries = 0;

        while (processed < MAX_CHUNKS_PER_TICK) {
            QueuedChunk queued = chunkScanQueue.poll();
            if (queued == null) {
                break;
            }

            // Re-queue chunks for other dimensions/worlds
            if (!queued.worldKey().equals(world.getRegistryKey())) {
                chunkScanQueue.offer(queued);
                scannedOtherWorldEntries++;

                // Avoid infinite spinning if the front of the queue belongs to other worlds
                if (scannedOtherWorldEntries >= chunkScanQueue.size() + 1) {
                    break;
                }
                continue;
            }

            VillageContainerScanner.scanChunk(world, queued.chunkPos());
            processed++;
        }
    }
}