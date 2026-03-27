package net.ryan.beyond_the_block.network.packets.Client;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.content.enchantment.armour.boots.LeapOfFaithEnchantment;

public class LeapOfFaithC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){

        server.execute(() -> LeapOfFaithEnchantment.tryPerformDoubleJump(player));
    }
}
