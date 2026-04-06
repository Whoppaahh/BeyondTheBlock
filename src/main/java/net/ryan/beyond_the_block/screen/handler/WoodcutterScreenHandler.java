package net.ryan.beyond_the_block.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
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
    private final Property selectedRecipe = Property.create();
    private final List<WoodcuttingRecipe> recipes = new ArrayList<>();
    private final World world;

    public WoodcutterScreenHandler(int syncId, PlayerInventory inv, WoodcutterBlockEntity blockEntity) {
        super(ModScreenHandlers.WOODCUTTER, syncId);

        this.inventory = blockEntity;
        this.world = inv.player.world;

        addSlot(new Slot(inventory, 0, 20, 33));       // Input
        addSlot(new Slot(inventory, 1, 143, 33) {     // Output
            @Override
            public boolean canInsert(ItemStack stack) { return false; }
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                onCraft(player, stack);
                super.onTakeItem(player, stack);
            }
        });

        addProperties(selectedRecipe);

        // Player inventory
        for (int m = 0; m < 3; ++m)
            for (int l = 0; l < 9; ++l)
                addSlot(new Slot(inv, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));

        for (int n = 0; n < 9; ++n)
            addSlot(new Slot(inv, n, 8 + n * 18, 142));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        updateRecipes(inventory.getStack(0));
    }

    public void updateRecipes(ItemStack input) {
        recipes.clear();
        if (!input.isEmpty()) {
            recipes.addAll(world.getRecipeManager().listAllOfType(ModRecipes.WOODCUTTING_TYPE)
                    .stream().filter(r -> r.matches(new SingleStackRecipeInput(input), world))
                    .toList());
        }
    }

    public List<WoodcuttingRecipe> getRecipes() {
        return recipes;
    }

    public int getSelectedRecipe() {
        return selectedRecipe.get();
    }

    public void setSelectedRecipe(int index) {
        selectedRecipe.set(index);
        ItemStack result = ItemStack.EMPTY;

        if (index >= 0 && index < recipes.size()) {
            result = recipes.get(index).getOutput().copy();
        }

        inventory.setStack(1, result);
        sendContentUpdates();
    }
}
