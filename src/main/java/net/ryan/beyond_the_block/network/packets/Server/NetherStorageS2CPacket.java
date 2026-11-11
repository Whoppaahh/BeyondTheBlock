package net.ryan.beyond_the_block.network.packets.Server;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.ryan.beyond_the_block.utils.helpers.NetherLargeInventory;

import java.util.ArrayList;
import java.util.List;

public class NetherStorageS2CPacket {

    static ScreenHandler screenHandler;

    public static void receive(
            MinecraftClient client,
            ClientPlayNetworkHandler handler,
            PacketByteBuf buf,
            PacketSender responseSender
    ) {
        int syncId = buf.readVarInt();
        int count = buf.readVarInt();

        List<Integer> storageSlots = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            storageSlots.add(buf.readVarInt());
        }

        client.execute(() -> {
            if (client.player == null) {
                return;
            }

             screenHandler = client.player.currentScreenHandler;

            if (screenHandler.syncId != syncId) {
                return;
            }

            // Clear previous markers.
            for (Slot slot : screenHandler.slots) {
                Inventory inventory = slot.inventory;

                if (inventory instanceof NetherLargeInventory largeInventory) {
                    largeInventory.beyondTheBlock$setNetherStorage(false);
                }
            }

            // Mark the actual Nether storage slots.
            for (int slotIndex : storageSlots) {
                if (slotIndex < 0 || slotIndex >= screenHandler.slots.size()) {
                    continue;
                }

                Slot slot = screenHandler.getSlot(slotIndex);

                if (slot.inventory instanceof NetherLargeInventory largeInventory) {
                    largeInventory.beyondTheBlock$setNetherStorage(true);
                }
            }
        });
    }
}