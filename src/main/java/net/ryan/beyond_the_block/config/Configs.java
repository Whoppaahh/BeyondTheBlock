package net.ryan.beyond_the_block.config;

import me.shedaniel.autoconfig.AutoConfig;

public final class Configs {

    private Configs() {}

    public static ConfigClient client() {
        return AutoConfig.getConfigHolder(ConfigClient.class).getConfig();
    }

    public static ConfigServer server() {
        return AutoConfig.getConfigHolder(ConfigServer.class).getConfig();
    }
}