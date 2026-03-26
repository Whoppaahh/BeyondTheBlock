package net.ryan.beyond_the_block.network.Packets.Server;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.ryan.beyond_the_block.content.village.GuardVillager.GuardEntity;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class SyncGuardStatsS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int entityId = buf.readVarInt();
        boolean hasPoison = buf.readBoolean();
        boolean hasWither = buf.readBoolean();
        boolean hasFreeze = buf.readBoolean();

        client.execute(() -> {
            Entity entity = client.world != null ? client.world.getEntityById(entityId) : null;
            if (entity instanceof GuardEntity guard) {
                guard.setClientSideEffectFlags(hasPoison, hasWither, hasFreeze);
                BeyondTheBlock.LOGGER.info("[Client] Guard status: Poison={}, Wither={}, Freeze={}",
                        hasPoison, hasWither, hasFreeze);
            }
        });
    }
}
