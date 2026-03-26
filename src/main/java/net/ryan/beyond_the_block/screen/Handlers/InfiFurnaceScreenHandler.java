package net.ryan.beyond_the_block.screen.Handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.content.block.Entity.InfiFurnaceBlockEntity;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;

public class InfiFurnaceScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    // These values will be synced from the BlockEntity via PropertyDelegate
    private final PropertyDelegate propertyDelegate;

    public InfiFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf){
        super(ModScreenHandlers.INFI_SCREEN_HANDLER, syncId);
        BlockPos pos = buf.readBlockPos();
        InfiFurnaceBlockEntity blockEntity = (InfiFurnaceBlockEntity) playerInventory.player.world.getBlockEntity(pos);
        this.inventory = blockEntity;
        this.propertyDelegate = blockEntity.getPropertyDelegate(); // Make sure this exists

        // Furnace slots
        this.addSlot(new Slot(inventory, 0, 56, 35));  // input
        this.addSlot(new FurnaceOutputSlot(playerInventory.player, inventory, 1, 116, 35));  // output

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        // Player hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        addProperties(this.propertyDelegate);
        // Now call the main constructor:
        new InfiFurnaceScreenHandler(syncId, playerInventory, blockEntity, blockEntity.getPropertyDelegate());
    }


    public InfiFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate) {
        super(ModScreenHandlers.INFI_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.propertyDelegate = delegate;

        checkSize(inventory, 2);
        inventory.onOpen(playerInventory.player);

        // Furnace slots
        this.addSlot(new Slot(inventory, 0, 56, 17));  // input
        this.addSlot(new FurnaceOutputSlot(playerInventory.player, inventory, 1, 116, 35));  // output

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        // Player hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        addProperties(this.propertyDelegate);
    }

    public int getCookTime() {
        return propertyDelegate.get(0);
    }

    public int getCookTimeTotal() {
        return propertyDelegate.get(1);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            int inventorySize = inventory.size();

            if (index < inventorySize) { // From furnace to player inventory
                if (!insertItem(originalStack, inventorySize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else { // From player to furnace input slot
                if (!insertItem(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, originalStack);
        }

        return newStack;
    }
}

