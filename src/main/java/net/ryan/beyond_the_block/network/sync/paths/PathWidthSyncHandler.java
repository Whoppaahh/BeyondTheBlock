package net.ryan.beyond_the_block.network.sync.paths;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.feature.paths.PathToolHelper;

public class PathWidthSyncHandler {
    private static void syncWidth(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender packetSender) {
        int width = buf.readInt();
        server.execute(() -> {
            ItemStack stack = player.getMainHandStack();
            if(stack.getItem() instanceof ShovelItem){
                PathToolHelper.setWidth(stack, width);
                player.getInventory().markDirty();
            }
        });
    }
}
