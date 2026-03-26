package net.ryan.beyond_the_block.event.server;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.ryan.beyond_the_block.feature.deathcounter.DeathCounterDatapackInstaller;

public class ServerLifecycleRegistrar {
    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(DeathCounterDatapackInstaller::installIfMissing);
    }
}
