package net.ryan.beyond_the_block.config.sync;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.network.Packets.PacketIDs;

public final class ServerConfigSync {
    private ServerConfigSync() {
    }

    public static void sendToPlayer(ServerPlayerEntity player) {
        SyncedServerConfig config = SyncedServerConfigFactory.fromServerConfig();
        PacketByteBuf buf = PacketByteBufs.create();
        config.write(buf);
        ServerPlayNetworking.send(player, PacketIDs.SYNC_SERVER_CONFIG_PACKET_ID, buf);
    }

    public static void sendToAll(MinecraftServer server) {
        SyncedServerConfig config = SyncedServerConfigFactory.fromServerConfig();

        for (ServerPlayerEntity player : PlayerLookup.all(server)) {
            PacketByteBuf buf = PacketByteBufs.create();
            config.write(buf);
            ServerPlayNetworking.send(player, PacketIDs.SYNC_SERVER_CONFIG_PACKET_ID, buf);
        }
    }
}