package net.ryan.beyond_the_block.utils.helpers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerContext {
    private static MinecraftServer currentServer;

    public static void init() {
        // Store server instance when it starts
        ServerLifecycleEvents.SERVER_STARTED.register(server -> currentServer = server);

        // Clear when it stops
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> currentServer = null);
    }

    public static MinecraftServer getServer() {
        return currentServer;
    }
}

