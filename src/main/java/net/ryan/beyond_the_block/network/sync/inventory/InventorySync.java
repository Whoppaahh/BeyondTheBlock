package net.ryan.beyond_the_block.network.sync.inventory;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.ryan.beyond_the_block.network.packets.PacketIDs.SYNC_INVENTORY_PACKET_ID;

public class InventorySync {
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
}
