package net.ryan.beyond_the_block.system.tick;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.ryan.beyond_the_block.content.riddles.RiddleDataManager;
import net.ryan.beyond_the_block.core.bootstrap.ContentRegistrar;
import net.ryan.beyond_the_block.feature.theft.VillageContainerScannerManager;
import net.ryan.beyond_the_block.utils.helpers.BleedingParticleHandler;

public class WorldTickRegistrar {
    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(END_WORLD_TICK_HANDLER);
    }

    private static final ServerTickEvents.EndWorldTick END_WORLD_TICK_HANDLER = world -> {
        if (world.isClient()) return;

        RiddleDataManager handler = RiddleDataManager.get(world, ContentRegistrar.RIDDLE_COMPONENTS);
        handler.tick(world);

        BleedingParticleHandler.INSTANCE.onWorldTick(world);
        VillageContainerScannerManager.tick(world);
    };
}