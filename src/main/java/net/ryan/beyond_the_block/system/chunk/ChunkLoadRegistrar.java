package net.ryan.beyond_the_block.system.chunk;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import net.ryan.beyond_the_block.feature.theft.VillageContainerScannerManager;
import net.ryan.beyond_the_block.utils.CampfireProtectionManager;

public class ChunkLoadRegistrar {
    public static void register() {
        ServerChunkEvents.CHUNK_LOAD.register(CHUNK_LOAD_HANDLER);
        ServerChunkEvents.CHUNK_LOAD.register(CampfireProtectionManager::onChunkLoad);
        ServerChunkEvents.CHUNK_UNLOAD.register(CampfireProtectionManager::onChunkUnload);
    }
    private static final ServerChunkEvents.Load CHUNK_LOAD_HANDLER =
            (ServerWorld world, WorldChunk chunk) -> VillageContainerScannerManager.queueChunkForScan(world, chunk.getPos());

}
