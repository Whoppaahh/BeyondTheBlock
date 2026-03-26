package net.ryan.beyond_the_block.core.bootstrap;

import net.ryan.beyond_the_block.feature.seasonal.AprilFoolsFeatures;
import net.ryan.beyond_the_block.feature.seasonal.ChristmasFeatures;
import net.ryan.beyond_the_block.feature.seasonal.HalloweenFeatures;
import net.ryan.beyond_the_block.feature.seasonal.ValentinesFeatures;
import net.ryan.beyond_the_block.utils.Helpers.BleedingParticleHandler;

public class SystemRegistrar {

    public static BleedingParticleHandler BLEEDING_HANDLER = new BleedingParticleHandler();

    public static void register() {

    }

    private static void registerSeasonalFeatures() {
        ValentinesFeatures.register();
        AprilFoolsFeatures.register();
        HalloweenFeatures.register();
        ChristmasFeatures.register();
    }
}
