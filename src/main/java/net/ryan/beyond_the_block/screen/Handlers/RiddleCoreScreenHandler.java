package net.ryan.beyond_the_block.screen.Handlers;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.network.ServerNetworking;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;

import java.util.UUID;

public class RiddleCoreScreenHandler extends ScreenHandler {

    public final PlayerEntity player;
    public ServerPlayerEntity serverPlayer = null;
    public static UUID playerUUID;

    public RiddleCoreScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

    public RiddleCoreScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.RIDDLE_CORE_SCREEN_HANDLER, syncId);
        player = playerInventory.player;
        if (!player.getWorld().isClient) {
            this.serverPlayer = (ServerPlayerEntity) player;
            ServerNetworking.serverPlayer = serverPlayer;
            playerUUID = getPlayerID();
        }
    }
    public UUID getPlayerID() {
        if (playerUUID != null) {
            return playerUUID;
        }

        if (!player.getWorld().isClient && player instanceof ServerPlayerEntity) {
            return player.getUuid();
        } else {
            BeyondTheBlock.LOGGER.info("This is client-side, returning null.");
            return null;
        }
    }

    public String getPlayerNameFromUUID(UUID playerUUID) {
        return (ServerNetworking.serverPlayer != null) ? ServerNetworking.serverPlayer.getName().getString() : player.getUuid().toString();
        // Return the UUID if player is not online
    }

    public PlayerEntity getPlayer() {
        return player;
    }


    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

}
