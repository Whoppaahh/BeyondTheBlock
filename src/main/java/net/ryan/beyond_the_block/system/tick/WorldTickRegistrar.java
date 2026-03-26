package net.ryan.beyond_the_block.system.tick;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.ryan.beyond_the_block.content.riddles.RiddleComponents;
import net.ryan.beyond_the_block.core.bootstrap.SystemRegistrar;
import net.ryan.beyond_the_block.feature.theft.VillageContainerScannerManager;

public class WorldTickRegistrar {
    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(END_WORLD_TICK_HANDLER);
    }
    private static final ServerTickEvents.EndWorldTick END_WORLD_TICK_HANDLER = world -> {
        if (world.isClient) return;
        var handler = RiddleComponents.get(world);
        if (handler != null) handler.tick(world);
        SystemRegistrar.BLEEDING_HANDLER.onWorldTick(world);
        VillageContainerScannerManager.tick(world);
    };
}
