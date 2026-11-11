package net.ryan.beyond_the_block.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.network.packets.PacketIDs;
import net.ryan.beyond_the_block.utils.helpers.NetherStorage;

import java.util.ArrayList;
import java.util.List;

public final class NetherStorageNetworking {

    private NetherStorageNetworking() {
    }

    public static void sendStorageSlots(
            ServerPlayerEntity player,
            ScreenHandler handler
    ) {
        List<Integer> storageSlots = new ArrayList<>();

        for (int i = 0; i < handler.slots.size(); i++) {
            Slot slot = handler.slots.get(i);

            if (NetherStorage.isNetherStorage(slot)) {
                storageSlots.add(i);
            }
        }

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeVarInt(handler.syncId);
        buf.writeVarInt(storageSlots.size());

        for (int slot : storageSlots) {
            buf.writeVarInt(slot);
        }

        ServerPlayNetworking.send(
                player,
                PacketIDs.NETHER_STORAGE_SLOTS_PACKET_ID,
                buf
        );
        for (int i = 0; i < handler.slots.size(); i++) {
            Slot slot = handler.slots.get(i);

            if (NetherStorage.isNetherStorage(slot)) {
                ItemStack stack = slot.getStack();

                System.out.println(
                        "[SERVER INITIAL SLOT] Slot: " + i
                                + " | Count: " + stack.getCount()
                                + " | Max: " + slot.getMaxItemCount(stack)
                                + " | Inventory Max: " + slot.inventory.getMaxCountPerStack()
                );
            }
        }
    }
}