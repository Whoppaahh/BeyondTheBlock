package net.ryan.beyond_the_block.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class CustomBlastingRecipe extends BlastingRecipe {
    private final ItemStack output;

    public CustomBlastingRecipe(Identifier id, String group, Ingredient input, ItemStack output, float experience, int cookingTime) {
        super(id, group, input, output, experience, cookingTime);
        this.output = output;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return false; // Ensures it shows up
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CUSTOM_BLASTING_SERIALISER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.BLASTING;
    }

}

