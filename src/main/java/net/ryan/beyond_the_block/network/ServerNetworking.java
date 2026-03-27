package net.ryan.beyond_the_block.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.network.packets.Client.GuardFollowC2SPacket;
import net.ryan.beyond_the_block.network.packets.Client.GuardPatrolC2SPacket;
import net.ryan.beyond_the_block.network.packets.Client.LeapOfFaithC2SPacket;
import net.ryan.beyond_the_block.network.packets.Client.TeleportWithStaffC2SPacket;
import net.ryan.beyond_the_block.network.sync.paths.PathWidthSyncHandler;

import static net.ryan.beyond_the_block.network.packets.PacketIDs.*;

public class ServerNetworking {

    public static ServerPlayerEntity serverPlayer;

    // Register the C2S packets
    public static void registerC2SPackets() {

        ServerPlayNetworking.registerGlobalReceiver(TELEPORT_WITH_STAFF_ID, TeleportWithStaffC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GUARD_FOLLOW_PACKET_ID, GuardFollowC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GUARD_PATROL_PACKET_ID, GuardPatrolC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(LEAP_OF_FAITH_PACKET_ID, LeapOfFaithC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SYNC_PATH_WIDTH_PACKET_ID, PathWidthSyncHandler::syncWidth);
    }

}
