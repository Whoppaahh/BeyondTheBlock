package net.ryan.beyond_the_block.screen.handler;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;

import java.util.UUID;

public class RiddleCoreScreenHandler extends ScreenHandler {

    private final PlayerEntity player;
    private final ServerPlayerEntity serverPlayer;
    private final UUID playerUUID;
    private final BlockEntity blockEntity;

    public RiddleCoreScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

    public RiddleCoreScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.RIDDLE_CORE_SCREEN_HANDLER, syncId);

        this.player = playerInventory.player;
        this.serverPlayer = this.player instanceof ServerPlayerEntity sp ? sp : null;
        this.playerUUID = this.player.getUuid();
        this.blockEntity = blockEntity;
    }

    public UUID getPlayerID() {
        return playerUUID;
    }

    public String getPlayerNameFromUUID(UUID playerUUID) {
        if (serverPlayer != null && serverPlayer.getUuid().equals(playerUUID)) {
            return serverPlayer.getName().getString();
        }

        return playerUUID.toString();
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public ServerPlayerEntity getServerPlayer() {
        return serverPlayer;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}