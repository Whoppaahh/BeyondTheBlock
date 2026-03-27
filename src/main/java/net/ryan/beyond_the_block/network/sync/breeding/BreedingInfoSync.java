package net.ryan.beyond_the_block.network.sync.breeding;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.ryan.beyond_the_block.network.packets.PacketIDs.SYNC_BREEDING_PACKET_ID;

public class BreedingInfoSync {
    public static void syncBreedingInfo(ServerPlayerEntity player, AnimalEntity animal, int age) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(animal.getId());
        buf.writeInt(age);
        ServerPlayNetworking.send(player, SYNC_BREEDING_PACKET_ID, buf);
    }
}
