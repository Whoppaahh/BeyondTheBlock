package net.ryan.beyond_the_block.system.tick;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ServerTickRegistrar {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(END_SERVER_TICK_HANDLER);
    }
}
