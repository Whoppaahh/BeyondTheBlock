package net.ryan.beyond_the_block.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.effect.ModEffects;
import net.ryan.beyond_the_block.network.Packets.Client.GuardFollowC2SPacket;
import net.ryan.beyond_the_block.network.Packets.Client.GuardPatrolC2SPacket;
import net.ryan.beyond_the_block.network.Packets.Client.LeapOfFaithC2SPacket;
import net.ryan.beyond_the_block.network.Packets.Client.TeleportWithStaffC2SPacket;
import net.ryan.beyond_the_block.village.GuardVillager.GuardEntity;

import static net.ryan.beyond_the_block.network.PacketIDs.*;

public class ServerNetworking {

    public static ServerPlayerEntity serverPlayer;

    // Register the C2S packets
    public static void registerC2SPackets() {

        ServerPlayNetworking.registerGlobalReceiver(TELEPORT_WITH_STAFF_ID, TeleportWithStaffC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GUARD_FOLLOW_PACKET_ID, GuardFollowC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GUARD_PATROL_PACKET_ID, GuardPatrolC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(LEAP_OF_FAITH_PACKET_ID, LeapOfFaithC2SPacket::receive);
    }

    public static void syncGuardStatus(ServerPlayerEntity player, GuardEntity guard) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(guard.getId());
        buf.writeBoolean(guard.hasStatusEffect(StatusEffects.POISON));
        buf.writeBoolean(guard.hasStatusEffect(StatusEffects.WITHER));
        buf.writeBoolean(guard.hasStatusEffect(ModEffects.FREEZE) || guard.isFrozen());

        //EmeraldEmpire.LOGGER.info("Syncing Guard Stats");
        ServerPlayNetworking.send(player, GUARD_STATS_SYNC_PACKET_ID, buf);
    }

    public static void syncBreedingInfo(ServerPlayerEntity player, AnimalEntity animal, int age) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(animal.getId());
        buf.writeInt(age);
        ServerPlayNetworking.send(player, SYNC_BREEDING_PACKET_ID, buf);
    }


    public static void syncPlayerInventoryWithClient(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(player.getUuid());
        buf.writeInt(player.getInventory().size());
        // Add player's inventory to the packet (serialize items, etc.)
        for (int i = 0; i < player.getInventory().size(); i++) {
            buf.writeItemStack(player.getInventory().getStack(i));
        }

        ServerPlayNetworking.send(player, SYNC_INVENTORY_PACKET_ID, buf);
    }

    public static void syncRiddleTime(ServerPlayerEntity player, long timeOfDay) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeLong(timeOfDay);
        ServerPlayNetworking.send(player, RIDDLE_TIME_SYNC_PACKET_ID, buf);
    }

}
