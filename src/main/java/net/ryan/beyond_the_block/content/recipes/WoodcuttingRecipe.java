package net.ryan.beyond_the_block.content.recipes;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.registry.ModRecipes;

public class WoodcuttingRecipe implements Recipe<Inventory> {

    private final Identifier id;
    private final Ingredient ingredient;
    private final int ingredientCount;
    private final ItemStack output;

    public WoodcuttingRecipe(Identifier id, Ingredient ingredient, int ingredientCount, ItemStack output) {
        this.id = id;
        this.ingredient = ingredient;
        this.ingredientCount = ingredientCount;
        this.output = output;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        if (inventory.isEmpty()) {
            return false;
        }

        ItemStack stack = inventory.getStack(0);
        return ingredient.test(stack) && stack.getCount() >= ingredientCount;
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getIngredientCount() {
        return ingredientCount;
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