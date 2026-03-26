package net.ryan.beyond_the_block.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import net.ryan.beyond_the_block.content.village.GuardVillager.GuardEntity;
import net.ryan.beyond_the_block.feature.paths.PathToolHelper;
import net.ryan.beyond_the_block.network.Packets.Client.GuardFollowC2SPacket;
import net.ryan.beyond_the_block.network.Packets.Client.GuardPatrolC2SPacket;
import net.ryan.beyond_the_block.network.Packets.Client.LeapOfFaithC2SPacket;
import net.ryan.beyond_the_block.network.Packets.Client.TeleportWithStaffC2SPacket;

import static net.ryan.beyond_the_block.network.Packets.PacketIDs.*;

public class ServerNetworking {

    public static ServerPlayerEntity serverPlayer;

    // Register the C2S packets
    public static void registerC2SPackets() {

        ServerPlayNetworking.registerGlobalReceiver(TELEPORT_WITH_STAFF_ID, TeleportWithStaffC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GUARD_FOLLOW_PACKET_ID, GuardFollowC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GUARD_PATROL_PACKET_ID, GuardPatrolC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(LEAP_OF_FAITH_PACKET_ID, LeapOfFaithC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SYNC_PATH_WIDTH_PACKET_ID, ServerNetworking::syncWidth);
    }

}
