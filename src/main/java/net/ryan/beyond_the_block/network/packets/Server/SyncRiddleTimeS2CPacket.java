package net.ryan.beyond_the_block.network.packets.Server;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.ryan.beyond_the_block.content.riddles.RiddleDataManager;

public class SyncRiddleTimeS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        long timeOfDay = buf.readLong();
        client.execute(() -> RiddleDataManager.updateTime(timeOfDay));
    }
}