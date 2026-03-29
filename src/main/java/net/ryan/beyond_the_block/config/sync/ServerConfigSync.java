package net.ryan.beyond_the_block.config.sync;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.config.schema.ConfigServer;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.network.packets.PacketIDs;

public final class ServerConfigSync {
    private ServerConfigSync() {
    }

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(
                PacketIDs.REQUEST_SERVER_CONFIG_PACKET_ID,
                (server, player, handler, buf, responseSender) ->
                        server.execute(() -> sendToPlayer(player))
        );

        ServerPlayNetworking.registerGlobalReceiver(
                PacketIDs.UPDATE_SERVER_CONFIG_PACKET_ID,
                (server, player, handler, buf, responseSender) -> {
                    final SyncedServerConfig incoming;

                    try {
                        incoming = SyncedServerConfig.read(buf);
                    } catch (Exception e) {
                        BeyondTheBlock.LOGGER.error("Failed to read incoming server config update", e);
                        return;
                    }

                    server.execute(() -> {
                        if (!canEdit(player)) {
                            return;
                        }

                        ConfigServer serverConfig = AutoConfig.getConfigHolder(ConfigServer.class).getConfig();
                        incoming.applyToServerConfig(serverConfig);
                        AutoConfig.getConfigHolder(ConfigServer.class).save();

                        sendToAll(server);
                    });
                }
        );
    }

    public static void sendToPlayer(ServerPlayerEntity player) {
        SyncedServerConfig config = SyncedServerConfig.fromServerConfig(
                AutoConfig.getConfigHolder(ConfigServer.class).getConfig()
        );

        PacketByteBuf buf = PacketByteBufs.create();
        config.write(buf);
        ServerPlayNetworking.send(player, PacketIDs.SYNC_SERVER_CONFIG_PACKET_ID, buf);
    }

    public static void sendToAll(MinecraftServer server) {
        SyncedServerConfig config = SyncedServerConfig.fromServerConfig(
                AutoConfig.getConfigHolder(ConfigServer.class).getConfig()
        );

        for (ServerPlayerEntity player : PlayerLookup.all(server)) {
            PacketByteBuf buf = PacketByteBufs.create();
            config.write(buf);
            ServerPlayNetworking.send(player, PacketIDs.SYNC_SERVER_CONFIG_PACKET_ID, buf);
        }
    }

    private static boolean canEdit(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return false;
        }

        if (server.isSingleplayer()) {
            return true;
        }

        return player.hasPermissionLevel(4);
    }
}