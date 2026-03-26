package net.ryan.beyond_the_block.system.chunk;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import net.ryan.beyond_the_block.feature.theft.VillageContainerScannerManager;

public class ChunkLoadRegistrar {
    public static void register() {
        ServerChunkEvents.CHUNK_LOAD.register(CHUNK_LOAD_HANDLER);
    }
    private static final ServerChunkEvents.Load CHUNK_LOAD_HANDLER =
            (ServerWorld world, WorldChunk chunk) -> VillageContainerScannerManager.queueChunkForScan(world, chunk.getPos());

}
