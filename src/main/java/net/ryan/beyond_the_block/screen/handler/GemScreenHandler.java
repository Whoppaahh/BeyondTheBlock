package net.ryan.beyond_the_block.screen.handler;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.blockentity.GemBlockEntity;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;
import net.ryan.beyond_the_block.utils.ModTags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GemScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final GemBlockEntity gemEntity;
    private final PropertyDelegate propertyDelegate;

    private List<Identifier> GEM_TEXTURES = new ArrayList<>(List.of(
            new Identifier(BeyondTheBlock.MOD_ID, "gui/slot/emerald"),
            new Identifier(BeyondTheBlock.MOD_ID, "gui/slot/diamond"),
            new Identifier(BeyondTheBlock.MOD_ID, "gui/slot/redstone_dust"),
            new Identifier(BeyondTheBlock.MOD_ID, "gui/slot/ingot")
    ));



    public GemScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }
    public GemScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        this(syncId, playerInventory, blockEntity, ((GemBlockEntity) blockEntity).getPropertyDelegate());
    }

    public GemScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate delegate) {
        super(ModScreenHandlers.GEM_SCREEN_HANDLER, syncId);
        this.inventory = ((Inventory) blockEntity);
        gemEntity = (GemBlockEntity) blockEntity;

        this.propertyDelegate = delegate;
        this.addProperties(delegate);
        Collections.shuffle(GEM_TEXTURES);

        this.addSlot(new Slot(inventory, 0, 26, 35) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                if(!gemEntity.hasChanged){
                    inventory.setStack(1, ItemStack.EMPTY);
                    inventory.setStack(2, ItemStack.EMPTY);
                    inventory.setStack(3, ItemStack.EMPTY);
                }
                gemEntity.hasLoaded = false;
                gemEntity.hasChanged = false;
                inventory.setStack(4, ItemStack.EMPTY);
            }
            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return !gemEntity.hasLoaded || !gemEntity.hasChanged;
            }
            @Override //Tool to upgrade
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(ModTags.Items.GEM_UPGRADABLE_TOOLS);
            }
            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(BeyondTheBlock.MOD_ID, "gui/slot/sword"));
            }
        });
        this.addSlot(new Slot(inventory, 1, 62, 35) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }
            @Override //Gem #1
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(ModTags.Items.MOD_CUT_GEMS);
            }
            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                long currentTime = System.currentTimeMillis();
                double cycleDuration = 4000.0;
                double timeInCycle = ((currentTime) % cycleDuration) / cycleDuration;
                int index = (int)(timeInCycle * GEM_TEXTURES.size());

                Identifier texture = GEM_TEXTURES.get(index);

                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, texture);
            }
        });
        this.addSlot(new Slot(inventory, 2, 80, 35) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }
            @Override //Gem #2
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(ModTags.Items.MOD_CUT_GEMS);
            }
            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                long currentTime = System.currentTimeMillis();
                long offset = this.getIndex() * 500L;
                double cycleDuration = 4000.0;
                double timeInCycle = ((currentTime + offset) % cycleDuration) / cycleDuration;
                int index = (int)(timeInCycle * GEM_TEXTURES.size());

                Identifier texture = GEM_TEXTURES.get(index);

                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, texture);
            }
        });
        this.addSlot(new Slot(inventory, 3, 98, 35) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }
            @Override //Gem #3
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(ModTags.Items.MOD_CUT_GEMS);
            }
            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                long currentTime = System.currentTimeMillis();
                long offset = this.getIndex() * 1200L;
                double cycleDuration = 4000.0;
                double timeInCycle = ((currentTime + offset) % cycleDuration) / cycleDuration;
                int index = (int)(timeInCycle * GEM_TEXTURES.size());

                Identifier texture = GEM_TEXTURES.get(index);

                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, texture);
            }
        });
        this.addSlot(new Slot(inventory, 4, 134, 35) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                inventory.setStack(0, ItemStack.EMPTY);
                inventory.setStack(1, ItemStack.EMPTY);
                inventory.setStack(2, ItemStack.EMPTY);
                inventory.setStack(3, ItemStack.EMPTY);
                gemEntity.hasLoaded = false;
                gemEntity.hasChanged = false;
            }
            @Override //Result
            public boolean canInsert(ItemStack stack) {
                return false;
            }
            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(BeyondTheBlock.MOD_ID, "gui/slot/sword"));
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
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }
    public boolean isInputLocked() {
        return getPropertyDelegate().get(0) == 1 && getPropertyDelegate().get(1) == 1;
    }

}
