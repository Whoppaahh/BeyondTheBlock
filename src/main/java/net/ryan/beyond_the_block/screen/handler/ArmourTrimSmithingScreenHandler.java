package net.ryan.beyond_the_block.screen.handler;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.ryan.beyond_the_block.content.registry.ModTrimRegistry;
import net.ryan.beyond_the_block.content.registry.family.ModArmourTrim;
import net.ryan.beyond_the_block.content.registry.family.ModTrimMaterial;
import net.ryan.beyond_the_block.content.registry.family.ModTrimPattern;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;

import java.util.Optional;

public class ArmourTrimSmithingScreenHandler extends ForgingScreenHandler {

    private final Inventory input = new SimpleInventory(3) {
        @Override
        public void markDirty() {
            super.markDirty();
            ArmourTrimSmithingScreenHandler.this.onContentChanged(this);
        }
    };

    private final CraftingResultInventory output = new CraftingResultInventory();

    public ArmourTrimSmithingScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.ARMOUR_TRIM_SMITHING, syncId, playerInventory, ScreenHandlerContext.EMPTY);

        // Template
        this.addSlot(new Slot(input, 0, 8, 48) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return ModTrimRegistry.getPatternFromTemplate(stack.getItem()) != null;
            }
        });

        // Base armor
        this.addSlot(new Slot(input, 1, 26, 48) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return isValidArmor(stack);
            }
        });

        // Material
        this.addSlot(new Slot(input, 2, 44, 48) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return ModTrimRegistry.getMaterialFromIngredient(stack.getItem()) != null;
            }
        });

        // Output
        this.addSlot(new Slot(output, 0, 98, 48) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return this.hasStack();
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                onCraft(stack);
                super.onTakeItem(player, stack);
            }
        });

        addPlayerSlots(playerInventory);
    }

    private void addPlayerSlots(PlayerInventory inventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }


    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return false;
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {

    }

    @Override
    protected boolean canUse(net.minecraft.block.BlockState state) {
        return true;
    }

    @Override
    public void updateResult() {
        ItemStack template = input.getStack(0);
        ItemStack base = input.getStack(1);
        ItemStack material = input.getStack(2);

        if (template.isEmpty() || base.isEmpty() || material.isEmpty()) {
            output.setStack(0, ItemStack.EMPTY);
            return;
        }

        ModTrimPattern pattern = ModTrimRegistry.getPatternFromTemplate(template.getItem());
        ModTrimMaterial mat = ModTrimRegistry.getMaterialFromIngredient(material.getItem());

        if (pattern == null || mat == null || !isValidArmor(base)) {
            output.setStack(0, ItemStack.EMPTY);
            return;
        }

        ItemStack result = base.copy();
        result.setCount(1);

        Optional<ModArmourTrim.Data> existing = ModArmourTrim.getTrim(result);
        if (existing.isPresent()
                && existing.get().patternId().equals(pattern.id())
                && existing.get().materialId().equals(mat.id())) {
            output.setStack(0, ItemStack.EMPTY);
            return;
        }

        ModArmourTrim.setTrim(result, pattern.id(), mat.id());

        output.setStack(0, result);
    }

    private void onCraft(ItemStack stack) {
        input.getStack(1).decrement(1); // armor
        input.getStack(2).decrement(1); // material

        context.run((world, pos) -> world.playSound(null, pos,
                SoundEvents.BLOCK_SMITHING_TABLE_USE,
                SoundCategory.BLOCKS, 1.0F, 1.0F));
    }

    private boolean isValidArmor(ItemStack stack) {
        if (!(stack.getItem() instanceof ArmorItem armor)) return false;
        EquipmentSlot slot = armor.getSlotType();
        return slot.getType() == EquipmentSlot.Type.ARMOR;
    }
}