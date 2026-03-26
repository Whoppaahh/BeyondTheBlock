package net.ryan.beyond_the_block.system.tick;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.ryan.beyond_the_block.feature.xp_orbs.HomingXPManager;

public class ServerTickRegistrar {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(END_SERVER_TICK_HANDLER);
    }
    private static final ServerTickEvents.EndTick END_SERVER_TICK_HANDLER =
            server -> server.getWorlds().forEach(HomingXPManager::tick);


}
