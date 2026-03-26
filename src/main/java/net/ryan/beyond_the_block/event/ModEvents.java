package net.ryan.beyond_the_block.event;

import net.ryan.beyond_the_block.event.entity.EntityEventRegistrar;
import net.ryan.beyond_the_block.event.player.PlayerEventRegistrar;
import net.ryan.beyond_the_block.event.server.ServerLifecycleRegistrar;
import net.ryan.beyond_the_block.event.world.WorldEventRegistrar;
import net.ryan.beyond_the_block.utils.Helpers.HiveTracker;

public class ModEvents {

    public static void register() {
        PlayerEventRegistrar.register();
        WorldEventRegistrar.register();
        EntityEventRegistrar.register();
        ServerLifecycleRegistrar.register();
        HiveTracker.init();
    }
}
