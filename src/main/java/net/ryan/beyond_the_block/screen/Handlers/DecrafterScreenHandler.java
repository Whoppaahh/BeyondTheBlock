package net.ryan.beyond_the_block.screen.Handlers;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.ryan.beyond_the_block.content.block.Entity.DecrafterBlockEntity;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;

import java.util.List;

public class DecrafterScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final DecrafterBlockEntity decrafterEntity;

    private static final int INPUT_SLOT_INDEX = 0;
    private static final int INGREDIENT_SLOT_START = 1;
    private static final int INGREDIENT_SLOT_COUNT = 9;

    private int decraftedUnitsTaken = 0;
    private final int outputPerInput = 9; // Default, will be set dynamically


    public DecrafterScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

    public DecrafterScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.DECRAFTER_SCREEN_HANDLER, syncId);
        this.inventory = (Inventory) blockEntity;
        this.decrafterEntity = (DecrafterBlockEntity) blockEntity;

        this.addSlot(new Slot(inventory, INPUT_SLOT_INDEX, 36, 35) {
            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                onInputChanged(stack);
            }
            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                // Only allow taking input if all outputs from it have been taken
                return decraftedUnitsTaken % outputPerInput == 0
                        || areOutputSlotsEmpty();
            }
            private boolean areOutputSlotsEmpty() {
                for (int i = 0; i < INGREDIENT_SLOT_COUNT; i++) {
                    if (!inventory.getStack(INGREDIENT_SLOT_START + i).isEmpty()) {
                        return false;
                    }
                }
                return true;
            }
        });

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                int slotIndex = INGREDIENT_SLOT_START + col + row * 3;
                this.addSlot(new Slot(inventory, slotIndex, 98 + col * 18, 17 + row * 18) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return false;
                    }

                    @Override
                    public boolean canTakeItems(PlayerEntity playerEntity) {
                        return true;
                    }

                    @Override
                    public void onTakeItem(PlayerEntity player, ItemStack stack) {
                        super.onTakeItem(player, stack);
//                        inventory.setStack(INPUT_SLOT_INDEX, ItemStack.EMPTY);
//                        inventory.markDirty();
//                        sendContentUpdates();

                        // Count how many outputs were taken
                        decraftedUnitsTaken += stack.getCount();
                        // Recalculate how many input items are still bound to unclaimed outputs
                        int fullSetsTaken = decraftedUnitsTaken / outputPerInput;
                        int totalInput = inventory.getStack(INPUT_SLOT_INDEX).getCount();
                        int newInputCount = totalInput - fullSetsTaken;

                        // Clamp to avoid negative
                        newInputCount = Math.max(newInputCount, 0);

                        inventory.setStack(INPUT_SLOT_INDEX, new ItemStack(inventory.getStack(INPUT_SLOT_INDEX).getItem(), newInputCount));
                        inventory.markDirty();

                        // Refill output for remaining input
                       // onInputChanged(inventory.getStack(INPUT_SLOT_INDEX));
                    }
                });
            }
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private void onInputChanged(ItemStack inputStack) {
        decraftedUnitsTaken = 0; // Reset

        for (int i = 0; i < INGREDIENT_SLOT_COUNT; i++) {
            inventory.setStack(INGREDIENT_SLOT_START + i, ItemStack.EMPTY);
        }

        if (inputStack.isEmpty()) return;

        List<CraftingRecipe> matches = decrafterEntity.getWorld()
                .getRecipeManager().listAllOfType(RecipeType.CRAFTING).stream()
                .filter(r -> {
                    ItemStack simulated = simulateCraftingResult(r);
                    return ItemStack.canCombine(simulated, inputStack)
                            && simulated.getCount() <= inputStack.getCount();
                })
                .toList();

        if (matches.isEmpty()) return;

        CraftingRecipe recipe = matches.get(0);
        ItemStack output = simulateCraftingResult(recipe);
        int setsAvailable = inputStack.getCount() / output.getCount();

        if (recipe instanceof ShapedRecipe shaped) {
            List<Ingredient> ingredients = shaped.getIngredients();
            int width = shaped.getWidth();
            int height = shaped.getHeight();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int shapedIndex = x + y * width;
                    int gridIndex = x + y * 3; // center-aligned
                    if (shapedIndex >= ingredients.size()) continue;

                    Ingredient ing = ingredients.get(shapedIndex);
                    if (!ing.isEmpty()) {
                        ItemStack stack = ing.getMatchingStacks()[0].copy();
                        stack.setCount(setsAvailable);
                        inventory.setStack(INGREDIENT_SLOT_START + gridIndex, stack);
                    }
                }
            }
        } else {
            // Treat as shapeless
            List<Ingredient> ingredients = recipe.getIngredients();
            for (int i = 0; i < ingredients.size() && i < INGREDIENT_SLOT_COUNT; i++) {
                Ingredient ing = ingredients.get(i);
                if (!ing.isEmpty()) {
                    ItemStack stack = ing.getMatchingStacks()[0].copy();
                    stack.setCount(setsAvailable);
                    inventory.setStack(INGREDIENT_SLOT_START + i, stack);
                }
            }
        }
    }

    private ItemStack simulateCraftingResult(CraftingRecipe recipe) {
        CraftingInventory dummy = new CraftingInventory(new ScreenHandler(null, -1) {
            @Override
            public ItemStack transferSlot(PlayerEntity player, int index) {
                return ItemStack.EMPTY;
            }

            @Override
            public boolean canUse(PlayerEntity player) {
                return false;
            }
        }, 3, 3);

        List<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < ingredients.size() && i < dummy.size(); i++) {
            Ingredient ing = ingredients.get(i);
            if (!ing.isEmpty()) {
                ItemStack[] options = ing.getMatchingStacks();
                if (options.length > 0) {
                    dummy.setStack(i, options[0].copy());
                }
            }
        }

        return recipe.craft(dummy);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                    return ItemStack.EMPTY;
                }
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
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
