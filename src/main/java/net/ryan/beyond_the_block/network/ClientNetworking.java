package net.ryan.beyond_the_block.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.network.packets.Server.*;
import net.ryan.beyond_the_block.utils.helpers.RestoreManager;

import static net.ryan.beyond_the_block.network.packets.PacketIDs.*;

public class ClientNetworking {

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_INVENTORY_PACKET_ID, SyncPlayerInventoryS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SYNC_RIDDLES_ID, SyncRiddlesS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(GUARD_STATS_SYNC_PACKET_ID, SyncGuardStatsS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(RIDDLE_TIME_SYNC_PACKET_ID, SyncRiddleTimeS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SYNC_BREEDING_PACKET_ID, SyncBreedingInfoS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SYNC_RESTORE_PACKET_ID, ClientNetworking::syncRestoreBlocks);
    }

    private static void syncRestoreBlocks(MinecraftClient client, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf buf, PacketSender packetSender) {
        BlockPos pos = buf.readBlockPos();
        boolean add = buf.readBoolean();

        client.execute(() -> {
            if (add) {
                RestoreManager.CLIENT_PROTECTED.add(pos);
            } else {
                RestoreManager.CLIENT_PROTECTED.remove(pos);
            }
        });
    }

    public static void sendInventoryUpdate(PlayerEntity player) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            // Create and send packet for withdrawal request
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeUuid(player.getUuid());
            ClientPlayNetworking.send(SYNC_INVENTORY_PACKET_ID, buf);
        }
    }
    public static void sendWidthUpdate(int width) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(width);
            ClientPlayNetworking.send(SYNC_PATH_WIDTH_PACKET_ID, buf);
        }
}
