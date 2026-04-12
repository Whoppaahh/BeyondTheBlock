package net.ryan.beyond_the_block.screen.handler;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
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

public class ArmourTrimSmithingScreenHandler extends ScreenHandler {

    private final ScreenHandlerContext context;
    private final Inventory input;
    private final CraftingResultInventory output = new CraftingResultInventory();

    public ArmourTrimSmithingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }


    public ArmourTrimSmithingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.ARMOUR_TRIM_SMITHING, syncId);

        this.context = context;
        this.input = new SimpleInventory(3) {
            @Override
            public void markDirty() {
                super.markDirty();
                ArmourTrimSmithingScreenHandler.this.onContentChanged(this);
            }
        };

        // Template
        this.addSlot(new Slot(this.input, 0, 8, 48) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return isValidTemplate(stack);
            }
        });

        // Base armor
        this.addSlot(new Slot(this.input, 1, 26, 48) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return isValidBase(stack);
            }
        });

        // Material
        this.addSlot(new Slot(this.input, 2, 44, 48) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return ModTrimRegistry.getMaterialFromIngredient(stack.getItem()) != null
                        || stack.isOf(Items.NETHERITE_INGOT);
            }
        });

        // Output
        this.addSlot(new Slot(this.output, 0, 98, 48) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity player) {
                return this.hasStack();
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                onCraft(stack);
                super.onTakeItem(player, stack);
            }
        });

        addPlayerInventory(playerInventory);
        updateResult();
    }

    private boolean isUpgradeableDiamondGear(ItemStack stack) {
        return stack.isOf(Items.DIAMOND_HELMET)
                || stack.isOf(Items.DIAMOND_CHESTPLATE)
                || stack.isOf(Items.DIAMOND_LEGGINGS)
                || stack.isOf(Items.DIAMOND_BOOTS)
                || stack.isOf(Items.DIAMOND_SWORD)
                || stack.isOf(Items.DIAMOND_PICKAXE)
                || stack.isOf(Items.DIAMOND_AXE)
                || stack.isOf(Items.DIAMOND_SHOVEL)
                || stack.isOf(Items.DIAMOND_HOE);
    }

    private void addPlayerInventory(PlayerInventory inventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        updateResult();
    }

    public void updateResult() {
        ItemStack template = input.getStack(0);
        ItemStack base = input.getStack(1);
        ItemStack addition = input.getStack(2);

        if (template.isEmpty() || base.isEmpty() || addition.isEmpty()) {
            output.setStack(0, ItemStack.EMPTY);
            sendContentUpdates();
            return;
        }

        ItemStack result;

        if (ModTrimRegistry.isNetheriteUpgradeTemplate(template)) {
            result = buildUpgradeResult(base, addition);
        } else {
            result = buildTrimResult(template, base, addition);
        }

        output.setStack(0, result);
        sendContentUpdates();
    }

    private void onCraft(ItemStack stack) {
        input.getStack(0).decrement(1); // template
        input.getStack(1).decrement(1); // armor
        input.getStack(2).decrement(1); // material

        context.run((world, pos) -> world.playSound(
                null,
                pos,
                SoundEvents.BLOCK_SMITHING_TABLE_USE,
                SoundCategory.BLOCKS,
                1.0F,
                1.0F
        ));

        updateResult();
    }

    private ItemStack buildTrimResult(ItemStack template, ItemStack base, ItemStack addition) {
        ModTrimPattern pattern = ModTrimRegistry.getPatternFromTemplate(template.getItem());
        ModTrimMaterial mat = ModTrimRegistry.getMaterialFromIngredient(addition.getItem());

        if (pattern == null || mat == null || !isValidArmor(base)) {
            return ItemStack.EMPTY;
        }

        ItemStack result = base.copy();
        result.setCount(1);

        Optional<ModArmourTrim.Data> existing = ModArmourTrim.getTrim(result);
        if (existing.isPresent()
                && existing.get().patternId().equals(pattern.id())
                && existing.get().materialId().equals(mat.id())) {
            return ItemStack.EMPTY;
        }

        ModArmourTrim.setTrim(result, pattern.id(), mat.id());
        return result;
    }

    private ItemStack buildUpgradeResult(ItemStack base, ItemStack addition) {
        if (!addition.isOf(net.minecraft.item.Items.NETHERITE_INGOT)) {
            return ItemStack.EMPTY;
        }

        ItemStack upgraded = getNetheriteUpgradeResult(base);
        if (upgraded.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return upgraded;
    }

    private ItemStack getNetheriteUpgradeResult(ItemStack base) {
        ItemStack result = ItemStack.EMPTY;

        if (base.isOf(net.minecraft.item.Items.DIAMOND_HELMET)) {
            result = new ItemStack(net.minecraft.item.Items.NETHERITE_HELMET);
        } else if (base.isOf(net.minecraft.item.Items.DIAMOND_CHESTPLATE)) {
            result = new ItemStack(net.minecraft.item.Items.NETHERITE_CHESTPLATE);
        } else if (base.isOf(net.minecraft.item.Items.DIAMOND_LEGGINGS)) {
            result = new ItemStack(net.minecraft.item.Items.NETHERITE_LEGGINGS);
        } else if (base.isOf(net.minecraft.item.Items.DIAMOND_BOOTS)) {
            result = new ItemStack(net.minecraft.item.Items.NETHERITE_BOOTS);
        } else if (base.isOf(net.minecraft.item.Items.DIAMOND_SWORD)) {
            result = new ItemStack(net.minecraft.item.Items.NETHERITE_SWORD);
        } else if (base.isOf(net.minecraft.item.Items.DIAMOND_PICKAXE)) {
            result = new ItemStack(net.minecraft.item.Items.NETHERITE_PICKAXE);
        } else if (base.isOf(net.minecraft.item.Items.DIAMOND_AXE)) {
            result = new ItemStack(net.minecraft.item.Items.NETHERITE_AXE);
        } else if (base.isOf(net.minecraft.item.Items.DIAMOND_SHOVEL)) {
            result = new ItemStack(net.minecraft.item.Items.NETHERITE_SHOVEL);
        } else if (base.isOf(net.minecraft.item.Items.DIAMOND_HOE)) {
            result = new ItemStack(net.minecraft.item.Items.NETHERITE_HOE);
        }

        if (result.isEmpty()) {
            return ItemStack.EMPTY;
        }

        result.setNbt(base.getNbt() == null ? null : base.getNbt().copy());
        result.setDamage(base.getDamage());

        return result;
    }
    private boolean isValidArmor(ItemStack stack) {
        if (!(stack.getItem() instanceof ArmorItem armor)) return false;
        EquipmentSlot slot = armor.getSlotType();
        return slot.getType() == EquipmentSlot.Type.ARMOR;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack original = slot.getStack();
            newStack = original.copy();

            // Output slot
            if (index == 3) {
                if (!this.insertItem(original, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(original, newStack);
            }
            // Player inventory/hotbar
            else if (index >= 4) {
                if (isValidTemplate(original)) {
                    if (!this.insertItem(original, 0, 1, false)) return ItemStack.EMPTY;
                } else if (isValidBase(original)) {
                    if (!this.insertItem(original, 1, 2, false)) return ItemStack.EMPTY;
                } else if (ModTrimRegistry.getMaterialFromIngredient(original.getItem()) != null) {
                    if (!this.insertItem(original, 2, 3, false)) return ItemStack.EMPTY;
                } else if (index < 31) {
                    if (!this.insertItem(original, 31, 40, false)) return ItemStack.EMPTY;
                } else if (!this.insertItem(original, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // Input slots
            else if (!this.insertItem(original, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (original.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (original.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, original);
        }

        return newStack;
    }

    private boolean isValidBase(ItemStack stack) {
        return isValidArmor(stack) || isUpgradeableDiamondGear(stack);
    }

    private boolean isValidTemplate(ItemStack stack) {
        return ModTrimRegistry.getPatternFromTemplate(stack.getItem()) != null
                || ModTrimRegistry.isNetheriteUpgradeTemplate(stack);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.dropInventory(player, this.input);
    }
}