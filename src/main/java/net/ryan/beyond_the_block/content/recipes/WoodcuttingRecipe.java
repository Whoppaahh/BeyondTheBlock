package net.ryan.beyond_the_block.content.recipes;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class WoodcuttingRecipe extends StonecuttingRecipe {

    private final Identifier id;
    private final Ingredient ingredient;
    private final ItemStack output;

    public WoodcuttingRecipe(Identifier id, Ingredient ingredient, ItemStack output) {
        super();
        this.id = id;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean matches(SingleStackRecipeInput input, World world) {
        return ingredient.test(input.getStack());
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input) {
        return output.copy();
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return ingredient.test(input.getStack());
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WOODCUTTING_RECIPE_SERIALISER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WOODCUTTING_RECIPE_TYPE;
    }
}