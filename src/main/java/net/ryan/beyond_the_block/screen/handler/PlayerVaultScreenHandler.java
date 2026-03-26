package net.ryan.beyond_the_block.screen.handler;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.content.blockentity.PlayerVaultBlockEntity;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;
import net.ryan.beyond_the_block.utils.GUI.BigSlot;

public class PlayerVaultScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    int k = (6 - 4) * 18;
    private final Text title;

    public PlayerVaultScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                Text.translatable("gui.beyond_the_block.player_vault", buf.readString()));
    }

    public PlayerVaultScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity entity, Text title) {
        super(ModScreenHandlers.PLAYER_VAULT_SCREEN_HANDLER, syncId);
        if (!(entity instanceof PlayerVaultBlockEntity pbe)) {
            throw new IllegalStateException("Expected PlayerVaultBlockEntity");
        }

        this.inventory = pbe; // Keep direct reference for identity match
        pbe.onOpen(playerInventory.player);
        this.title = title;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new BigSlot(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public Text getTitle() {
        return this.title;
    }


    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 104 + i * 18 + k));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 162 + k));
        }
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < 6 * 9) {
                if (!this.insertItem(itemStack2, 6 * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, 6 * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }

    public Inventory getInventory() {
        return this.inventory;
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}

