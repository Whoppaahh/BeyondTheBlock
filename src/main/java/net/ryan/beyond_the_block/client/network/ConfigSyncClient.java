package net.ryan.beyond_the_block.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.ryan.beyond_the_block.config.sync.ClientSyncedConfigHolder;
import net.ryan.beyond_the_block.config.sync.SyncedServerConfig;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.network.packets.PacketIDs;

public final class ConfigSyncClient {
    private ConfigSyncClient() {
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(
                PacketIDs.SYNC_SERVER_CONFIG_PACKET_ID,
                (client, handler, buf, responseSender) -> {
                    final SyncedServerConfig config;

                    try {
                        config = SyncedServerConfig.read(buf);
                    } catch (Exception e) {
                        BeyondTheBlock.LOGGER.error("Failed to read synced server config packet", e);
                        return;
                    }

                    client.execute(() -> ClientSyncedConfigHolder.set(config));
                }
        );

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ClientSyncedConfigHolder.clear());
    }
}