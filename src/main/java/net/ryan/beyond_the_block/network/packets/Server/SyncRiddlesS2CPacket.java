package net.ryan.beyond_the_block.network.packets.Server;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.ryan.beyond_the_block.content.riddles.Riddle;
import net.ryan.beyond_the_block.content.riddles.RiddleClientCache;

import java.util.*;

public class SyncRiddlesS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int activeSize = buf.readInt();
        Map<UUID, Riddle> active = new HashMap<>();
        for (int i = 0; i < activeSize; i++) {
            UUID playerId = buf.readUuid();
            UUID riddleId = buf.readUuid();
            List <String> pages = buf.readList(PacketByteBuf::readString);
            List <String> itemIds = buf.readList(PacketByteBuf::readString);
            Riddle riddle = Riddle.fromStored(riddleId, pages, itemIds);
            active.put(playerId, riddle);
        }

        int completedSize = buf.readInt();
        Map<UUID, Set<UUID>> completed = new HashMap<>();
        for (int i = 0; i < completedSize; i++) {
            UUID playerId = buf.readUuid();
            int completedCount = buf.readInt();
            Set<UUID> completedRiddles = new HashSet<>();
            for (int j = 0; j < completedCount; j++) {
                completedRiddles.add(buf.readUuid());
            }
            completed.put(playerId, completedRiddles);
        }

        // Update the client-side cache
        client.execute(() -> RiddleClientCache.set(active, completed));
    }
}
