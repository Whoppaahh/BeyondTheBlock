package net.ryan.beyond_the_block.config.access;

import me.shedaniel.autoconfig.AutoConfig;
import net.ryan.beyond_the_block.config.schema.ConfigClient;
import net.ryan.beyond_the_block.config.schema.ConfigServer;
import net.ryan.beyond_the_block.config.sync.ClientSyncedConfigHolder;
import net.ryan.beyond_the_block.config.sync.SyncedServerConfig;

public final class Configs {

    private Configs() {}

    public static ConfigClient client() {
        return AutoConfig.getConfigHolder(ConfigClient.class).getConfig();
    }

    public static ConfigServer server() {
        return AutoConfig.getConfigHolder(ConfigServer.class).getConfig();
    }

    public static SyncedServerConfig syncedServerConfig() {
        return ClientSyncedConfigHolder.getOrDefault();
    }
}