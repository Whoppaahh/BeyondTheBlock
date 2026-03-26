package net.ryan.beyond_the_block.system.tick;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class WorldTickRegistrar {
     ServerTickEvents.END_WORLD_TICK.register(END_WORLD_TICK_HANDLER);
}
