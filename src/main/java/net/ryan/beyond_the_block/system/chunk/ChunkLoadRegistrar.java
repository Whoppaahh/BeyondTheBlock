package net.ryan.beyond_the_block.system.chunk;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;

public class ChunkLoadRegistrar {
     ServerChunkEvents.CHUNK_LOAD.register(CHUNK_LOAD_HANDLER);
}
