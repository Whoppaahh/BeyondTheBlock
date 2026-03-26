package net.ryan.beyond_the_block.screen.handler;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;

public class StaffScreenHandler extends ScreenHandler {
    private final Inventory inventory;


    public StaffScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.STAFF_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(1);

        this.addSlot(new Slot(inventory, 0, 80, 35) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }
            @Override
            public boolean canInsert(ItemStack stack){
                return stack.isIn(ConventionalItemTags.AXES) || stack.isIn(ConventionalItemTags.PICKAXES);
            }
            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                long time = System.currentTimeMillis() / 2000; // flickers every 500ms
                boolean flicker = (time % 2) == 0;

                Identifier texture = flicker
                        ? new Identifier(BeyondTheBlock.MOD_ID, "gui/slot/axe")
                        : new Identifier(BeyondTheBlock.MOD_ID, "gui/slot/pickaxe");

                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, texture);
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public StaffScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack staffStack) {
        super(ModScreenHandlers.STAFF_SCREEN_HANDLER, syncId);

        this.inventory = new SimpleInventory(1) {
            @Override
            public void markDirty() {
                super.markDirty();
                ItemStack toolStack = getStack(0);
                if (!toolStack.isEmpty()) {
                    staffStack.getOrCreateNbt().put("StoredTool", toolStack.writeNbt(new NbtCompound()));
                } else {
                    staffStack.getOrCreateNbt().remove("StoredTool");
                }
            }
        };

        // Restore stored item if it exists
        if (staffStack.getNbt() != null && staffStack.getNbt().contains("StoredTool")) {
            ItemStack stored = ItemStack.fromNbt(staffStack.getNbt().getCompound("StoredTool"));
            this.inventory.setStack(0, stored);
        }

        this.addSlot(new Slot(this.inventory, 0, 80, 35) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(ConventionalItemTags.AXES) || stack.isIn(ConventionalItemTags.PICKAXES);
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
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
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
