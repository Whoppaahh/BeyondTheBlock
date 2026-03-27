package net.ryan.beyond_the_block.network.packets.Server;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.network.PacketByteBuf;
import net.ryan.beyond_the_block.utils.helpers.BreedingSyncState;

public class SyncBreedingInfoS2CPacket {
    public static void receive(MinecraftClient client,
                               ClientPlayNetworkHandler handler,
                               PacketByteBuf buf,
                               PacketSender responseSender) {

        int entityId = buf.readVarInt();
        int age = buf.readInt();   // full server ticks

        client.execute(() -> {
            if (client.world == null) return;
            Entity e = client.world.getEntityById(entityId);

            if (e instanceof AnimalEntity animal) {
                BreedingSyncState.updateAge(animal.getId(), age);

                // Debug optional
                // BeyondTheBlock.LOGGER.info("[BTB] Sync age {} for {}", age, entityId);
            }
        });
    }
}
