package net.ryan.beyond_the_block.core.bootstrap;

import net.ryan.beyond_the_block.feature.seasonal.AprilFoolsFeatures;
import net.ryan.beyond_the_block.feature.seasonal.ChristmasFeatures;
import net.ryan.beyond_the_block.feature.seasonal.HalloweenFeatures;
import net.ryan.beyond_the_block.feature.seasonal.ValentinesFeatures;
import net.ryan.beyond_the_block.system.chunk.ChunkLoadRegistrar;
import net.ryan.beyond_the_block.system.tick.ServerTickRegistrar;
import net.ryan.beyond_the_block.system.tick.WorldTickRegistrar;

public class SystemRegistrar {

    public static void register() {
        WorldTickRegistrar.register();
        ServerTickRegistrar.register();
        ChunkLoadRegistrar.register();
        registerSeasonalFeatures();
    }

    private static void registerSeasonalFeatures() {
        ValentinesFeatures.register();
        AprilFoolsFeatures.register();
        HalloweenFeatures.register();
        ChristmasFeatures.register();
    }
}
