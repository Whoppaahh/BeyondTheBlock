package net.ryan.beyond_the_block.network.Packets.Server;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SyncPlayerInventoryS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        UUID playerUUID = buf.readUuid();
        int inventorySize = buf.readInt();

        List<ItemStack> updatedInventory = new ArrayList<>();
        for (int i = 0; i < inventorySize; i++) {
            updatedInventory.add(buf.readItemStack());
        }

        // Execute on the client thread to update the inventory
        client.execute(() -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null && player.getUuid().equals(playerUUID)) {
                // Update the player's inventory with the new data
                for (int i = 0; i < updatedInventory.size(); i++) {
                    player.getInventory().setStack(i, updatedInventory.get(i));
                }
                // Optionally, refresh the UI if needed
                // player.inventoryListener.onInventoryChanged(player.inventory);
                //EmeraldEmpire.LOGGER.info("Player Inventory Synced with Client");
            }
        });
    }
}
