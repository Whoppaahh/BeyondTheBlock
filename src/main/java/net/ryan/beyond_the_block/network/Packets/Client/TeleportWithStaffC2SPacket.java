package net.ryan.beyond_the_block.network.Packets.Client;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.content.item.AnimatedItem;

public class TeleportWithStaffC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        server.execute(() -> {
            ItemStack heldStack = player.getMainHandStack();
            if (heldStack.getItem() instanceof AnimatedItem) {
                //EmeraldEmpire.LOGGER.info("Teleport triggered from air click");
                AnimatedItem.teleportWithStaff(player, heldStack);
            }
        });
    }
}
