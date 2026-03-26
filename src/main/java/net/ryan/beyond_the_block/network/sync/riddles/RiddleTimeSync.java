package net.ryan.beyond_the_block.network.sync.riddles;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.ryan.beyond_the_block.network.Packets.PacketIDs.RIDDLE_TIME_SYNC_PACKET_ID;

public class RiddleTimeSync {
    public static void syncRiddleTime(ServerPlayerEntity player, long timeOfDay) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeLong(timeOfDay);
        ServerPlayNetworking.send(player, RIDDLE_TIME_SYNC_PACKET_ID, buf);
    }
}
