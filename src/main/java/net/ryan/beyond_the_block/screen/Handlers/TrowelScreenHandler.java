package net.ryan.beyond_the_block.screen.Handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;
import net.ryan.beyond_the_block.utils.GUI.TrowelInventory;

public class TrowelScreenHandler extends ScreenHandler {

    private final TrowelInventory inventory;

    public TrowelScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ItemStack.EMPTY);
    }

    public TrowelScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack trowelStack) {
        super(ModScreenHandlers.TROWEL_SCREEN_HANDLER, syncId);

        this.inventory = new TrowelInventory(trowelStack);

        // Add 5 trowel slots
        int startX = 44;
        int startY = 20;
        for (int i = 0; i < 5; i++) {
            this.addSlot(new Slot(inventory, i, startX + i * 18, startY){
                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.getItem() instanceof BlockItem;
                }
            });
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public TrowelScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, buf.readItemStack());
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 51 + y * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        }
    }
}
