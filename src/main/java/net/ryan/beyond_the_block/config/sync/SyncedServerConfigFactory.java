package net.ryan.beyond_the_block.config.sync;

import me.shedaniel.autoconfig.AutoConfig;
import net.ryan.beyond_the_block.config.schema.ConfigServer;

public final class SyncedServerConfigFactory {
    private SyncedServerConfigFactory() {
    }

    public static SyncedServerConfig fromServerConfig() {
        ConfigServer cfg = AutoConfig.getConfigHolder(ConfigServer.class).getConfig();

        return new SyncedServerConfig(
                // Horses
                cfg.features.horses.enableSwimming,
                cfg.features.horses.undeadCanSwim,
                cfg.features.horses.preventWandering,
                cfg.features.horses.stayRadius,
                cfg.features.horses.removeMiningPenalty,
                cfg.features.horses.increaseStepHeight,

                // Paths
                cfg.features.paths.enabled,
                cfg.features.paths.maxDistance,
                cfg.features.paths.useTerrainFollowing,
                cfg.features.paths.preserveDurability,
                cfg.features.paths.defaultPathBlockId,
                cfg.features.paths.allowedStartingBlocks,
                cfg.features.paths.allowedEndingBlocks,
                cfg.features.paths.minWidth,
                cfg.features.paths.maxWidth
        );
    }
}