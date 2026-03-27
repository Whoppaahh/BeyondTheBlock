package net.ryan.beyond_the_block.config.sync;

import org.jetbrains.annotations.Nullable;

public final class ClientSyncedConfigHolder {
    private static @Nullable SyncedServerConfig current;

    private ClientSyncedConfigHolder() {
    }

    public static void set(SyncedServerConfig config) {
        current = config;
    }

    public static void clear() {
        current = null;
    }

    public static @Nullable SyncedServerConfig get() {
        return current;
    }

    public static SyncedServerConfig getOrDefault() {
        return current != null ? current : SyncedServerConfig.defaults();
    }

    public static boolean hasConfig() {
        return current != null;
    }
}