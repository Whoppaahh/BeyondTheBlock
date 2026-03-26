package net.ryan.beyond_the_block.core.bootstrap;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import net.ryan.beyond_the_block.content.riddles.RiddleComponents;
import net.ryan.beyond_the_block.feature.seasonal.AprilFoolsFeatures;
import net.ryan.beyond_the_block.feature.seasonal.ChristmasFeatures;
import net.ryan.beyond_the_block.feature.seasonal.HalloweenFeatures;
import net.ryan.beyond_the_block.feature.seasonal.ValentinesFeatures;
import net.ryan.beyond_the_block.feature.theft.VillageContainerScannerManager;
import net.ryan.beyond_the_block.feature.xp_orbs.HomingXPManager;
import net.ryan.beyond_the_block.utils.Helpers.BleedingParticleHandler;

public class SystemRegistrar {

    public static BleedingParticleHandler BLEEDING_HANDLER = new BleedingParticleHandler();

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(END_WORLD_TICK_HANDLER);
        ServerTickEvents.END_SERVER_TICK.register(END_SERVER_TICK_HANDLER);
        ServerChunkEvents.CHUNK_LOAD.register(CHUNK_LOAD_HANDLER);
        registerSeasonalFeatures();
    }

    private static final ServerTickEvents.EndWorldTick END_WORLD_TICK_HANDLER = world -> {
        if (world.isClient) return;
        var handler = RiddleComponents.get(world);
        if (handler != null) handler.tick(world);
        BLEEDING_HANDLER.onWorldTick(world);
        VillageContainerScannerManager.tick(world);
    };

    private static final ServerTickEvents.EndTick END_SERVER_TICK_HANDLER =
            server -> server.getWorlds().forEach(HomingXPManager::tick);

    private static final ServerChunkEvents.Load CHUNK_LOAD_HANDLER =
            (ServerWorld world, WorldChunk chunk) -> VillageContainerScannerManager.queueChunkForScan(world, chunk.getPos());


    private static void registerSeasonalFeatures() {
        ValentinesFeatures.register();
        AprilFoolsFeatures.register();
        HalloweenFeatures.register();
        ChristmasFeatures.register();
    }
}
