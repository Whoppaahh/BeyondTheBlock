package net.ryan.beyond_the_block.network.packets.Client;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.content.entity.villager.guard.GuardEntity;

public class GuardPatrolC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        int entityId = buf.readInt();
        boolean pressed = buf.readBoolean();
        server.execute(() -> {
            Entity entity = player.world.getEntityById(entityId);
            if(entity instanceof GuardEntity guardEntity){
                BlockPos pos = guardEntity.getBlockPos();
                if (guardEntity.getBlockPos() != null){
                    guardEntity.setPatrolPos(pos);
                }
                guardEntity.setPatrolling(pressed);
            }

        });
    }
}
