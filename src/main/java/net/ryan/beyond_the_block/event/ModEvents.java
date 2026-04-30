package net.ryan.beyond_the_block.event;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.ryan.beyond_the_block.event.entity.EntityEventRegistrar;
import net.ryan.beyond_the_block.event.player.PlayerEventRegistrar;
import net.ryan.beyond_the_block.event.server.ServerLifecycleRegistrar;
import net.ryan.beyond_the_block.event.world.WorldEventRegistrar;
import net.ryan.beyond_the_block.utils.battle.BattleCommands;
import net.ryan.beyond_the_block.utils.helpers.HiveTracker;

public class ModEvents {

    public static void register() {
        PlayerEventRegistrar.register();
        WorldEventRegistrar.register();
        EntityEventRegistrar.register();
        ServerLifecycleRegistrar.register();
        HiveTracker.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BattleCommands.register(dispatcher);
        });
    }
}
