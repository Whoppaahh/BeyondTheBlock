package net.ryan.beyond_the_block.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.WoodcutterBlockEntity;
import net.ryan.beyond_the_block.content.recipes.ModRecipes;
import net.ryan.beyond_the_block.content.recipes.WoodcuttingRecipe;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;

import java.util.ArrayList;
import java.util.List;

public class WoodcutterScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final World world;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        private final int[] data = new int[1];

        @Override
        public int get(int index) {
            return data[index];
        }

        @Override
        public void set(int index, int value) {
            data[index] = value;
        }

        @Override
        public int size() {
            return 1;
        }
    };

    private final List<WoodcuttingRecipe> recipes = new ArrayList<>();

    public WoodcutterScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(2));
    }

    public WoodcutterScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.WOODCUTTER_SCREEN_HANDLER, syncId);

        checkSize(inventory, 2);
        this.inventory = inventory;
        this.world = playerInventory.player.world;

        this.addProperties(propertyDelegate);

        // Input slot
        this.addSlot(new Slot(this.inventory, 0, 20, 33) {
            @Override
            public void markDirty() {
                super.markDirty();
                WoodcutterScreenHandler.this.onContentChanged(this.inventory);
            }
        });

        // Output slot
        this.addSlot(new Slot(this.inventory, 1, 143, 33) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraft(player.world, player, stack.getCount());

                int selected = WoodcutterScreenHandler.this.getSelectedRecipe();
                if (selected >= 0 && selected < WoodcutterScreenHandler.this.recipes.size()) {
                    WoodcuttingRecipe recipe = WoodcutterScreenHandler.this.recipes.get(selected);
                    int ingredientCount = recipe.getIngredientCount();

                    ItemStack inputStack = WoodcutterScreenHandler.this.inventory.getStack(0);
                    if (!inputStack.isEmpty()) {
                        inputStack.decrement(ingredientCount);
                        if (inputStack.isEmpty()) {
                            WoodcutterScreenHandler.this.inventory.setStack(0, ItemStack.EMPTY);
                        }
                    }
                }

                WoodcutterScreenHandler.this.updateAvailableRecipes();
                WoodcutterScreenHandler.this.updateResult();

                super.onTakeItem(player, stack);
            }
        });

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        // Hotbar
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, 8 + column * 18, 142));
        }

        this.onContentChanged(this.inventory);
    }

    public WoodcutterScreenHandler(int syncId, PlayerInventory playerInventory, WoodcutterBlockEntity blockEntity) {
        this(syncId, playerInventory, (Inventory) blockEntity);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        updateAvailableRecipes();
        updateResult();
        sendContentUpdates();
    }

    private void updateAvailableRecipes() {
        ItemStack input = this.inventory.getStack(0);
        this.recipes.clear();

        if (!input.isEmpty()) {
            SimpleInventory recipeInput = new SimpleInventory(input.copy());
            this.recipes.addAll(
                    this.world.getRecipeManager().getAllMatches(
                            ModRecipes.WOODCUTTING_RECIPE_TYPE,
                            recipeInput,
                            this.world
                    )
            );
        }

        if (this.recipes.isEmpty()) {
            this.propertyDelegate.set(0, -1);
        } else {
            int selected = this.propertyDelegate.get(0);
            if (selected < 0 || selected >= this.recipes.size()) {
                this.propertyDelegate.set(0, 0);
            }
        }
    }

    private void updateResult() {
        int selected = this.propertyDelegate.get(0);

        if (selected >= 0 && selected < this.recipes.size()) {
            this.inventory.setStack(1, this.recipes.get(selected).getOutput().copy());
        } else {
            this.inventory.setStack(1, ItemStack.EMPTY);
        }
    }

    public boolean hasRecipes() {
        return !this.recipes.isEmpty();
    }

    public int getRecipeCount() {
        return this.recipes.size();
    }

    public int getSelectedRecipe() {
        return this.propertyDelegate.get(0);
    }

    public ItemStack getOutputStack(int index) {
        if (index >= 0 && index < this.recipes.size()) {
            return this.recipes.get(index).getOutput().copy();
        }
        return ItemStack.EMPTY;
    }

    public void setSelectedRecipe(int index) {
        if (index >= 0 && index < this.recipes.size()) {
            this.propertyDelegate.set(0, index);
            this.updateResult();
            this.sendContentUpdates();
        }
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id >= 0 && id < this.recipes.size()) {
            this.setSelectedRecipe(id);
            return true;
        }
        return false;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot == null || !slot.hasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack slotStack = slot.getStack();
        result = slotStack.copy();

        // Output slot shift-click: craft repeatedly
        if (index == 1) {
            int selected = this.getSelectedRecipe();
            if (selected < 0 || selected >= this.recipes.size()) {
                return ItemStack.EMPTY;
            }

            WoodcuttingRecipe recipe = this.recipes.get(selected);

            while (true) {
                ItemStack inputStack = this.inventory.getStack(0);
                if (inputStack.isEmpty()) {
                    break;
                }

                if (!recipe.getIngredient().test(inputStack)) {
                    break;
                }

                if (inputStack.getCount() < recipe.getIngredientCount()) {
                    break;
                }

                ItemStack crafted = recipe.getOutput().copy();
                if (crafted.isEmpty()) {
                    break;
                }

                if (!this.insertItem(crafted, 2, 38, true)) {
                    break;
                }

                inputStack.decrement(recipe.getIngredientCount());
                if (inputStack.isEmpty()) {
                    this.inventory.setStack(0, ItemStack.EMPTY);
                } else {
                    this.inventory.setStack(0, inputStack);
                }

                crafted.getItem().onCraft(crafted, player.world, player);
            }

            this.updateAvailableRecipes();
            this.updateResult();
            this.sendContentUpdates();
            return result;
        }

        // Input slot -> player inventory
        if (index == 0) {
            if (!this.insertItem(slotStack, 2, 38, false)) {
                return ItemStack.EMPTY;
            }
        }
        // Player inventory / hotbar
        else {
            SimpleInventory recipeInput = new SimpleInventory(slotStack.copy());
            boolean isWoodcuttable = !this.world.getRecipeManager()
                    .getAllMatches(ModRecipes.WOODCUTTING_RECIPE_TYPE, recipeInput, this.world)
                    .isEmpty();

            if (isWoodcuttable) {
                if (!this.insertItem(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 2 && index < 29) {
                if (!this.insertItem(slotStack, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 29 && index < 38) {
                if (!this.insertItem(slotStack, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (slotStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }

        if (slotStack.getCount() == result.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTakeItem(player, slotStack);
        this.onContentChanged(this.inventory);
        return result;
    }
}