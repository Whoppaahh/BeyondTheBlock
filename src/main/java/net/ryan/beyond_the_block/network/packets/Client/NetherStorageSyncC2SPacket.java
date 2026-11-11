package net.ryan.beyond_the_block.network.packets.Client;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.network.packets.PacketIDs;

public class NetherStorageSyncC2SPacket {

    public static void receive(MinecraftServer minecraftServer,
                               ServerPlayerEntity player,
                               ServerPlayNetworkHandler serverPlayNetworkHandler,
                               PacketByteBuf buf,
                               PacketSender packetSender
    ) {
        int syncId = buf.readVarInt();

        if (player.currentScreenHandler.syncId != syncId) {
            return;
        }

        player.currentScreenHandler.syncState();
    }


}