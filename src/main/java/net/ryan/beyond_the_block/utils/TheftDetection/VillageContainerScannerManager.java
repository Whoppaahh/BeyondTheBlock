package net.ryan.beyond_the_block.utils.TheftDetection;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import java.util.concurrent.ConcurrentLinkedQueue;

public class VillageContainerScannerManager {
    private static final ConcurrentLinkedQueue<ChunkPos> chunkScanQueue = new ConcurrentLinkedQueue<>();

    public static void queueChunkForScan(ChunkPos chunkPos) {
        chunkScanQueue.offer(chunkPos);
    }

    public static void tick(ServerWorld world) {
        ChunkPos pos;
        while ((pos = chunkScanQueue.poll()) != null) {
            VillageContainerScanner.scanChunk(world, pos);
        }
    }
}
