package net.ryan.beyond_the_block.datagen.recipes;

import com.google.gson.JsonObject;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.content.recipes.ModRecipes;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class WoodcuttingRecipeJsonBuilder {
    private final Ingredient ingredient;
    private final int ingredientCount;
    private final Item result;
    private final int count;
    private final Identifier recipeId;

    public WoodcuttingRecipeJsonBuilder(Ingredient ingredient, int ingredientCount, Item result, int count, Identifier recipeId) {
        this.ingredient = ingredient;
        this.ingredientCount = ingredientCount;
        this.result = result;
        this.count = count;
        this.recipeId = recipeId;
    }

    public static WoodcuttingRecipeJsonBuilder create(Ingredient ingredient, int ingredientCount, Item result, int count, Identifier recipeId) {
        return new WoodcuttingRecipeJsonBuilder(ingredient, ingredientCount, result, count, recipeId);
    }

    public void offerTo(Consumer<RecipeJsonProvider> exporter) {
        exporter.accept(new RecipeJsonProvider() {
            @Override
            public void serialize(JsonObject json) {
                json.add("ingredient", ingredient.toJson());
                json.addProperty("ingredient_count", ingredientCount);
                json.addProperty("result", Registry.ITEM.getId(result).toString());
                json.addProperty("count", count);
            }

            @Override
            public Identifier getRecipeId() {
                return recipeId;
            }

            @Override
            public RecipeSerializer<?> getSerializer() {
                return ModRecipes.WOODCUTTING_RECIPE_SERIALISER;
            }

            @Nullable
            @Override
            public JsonObject toAdvancementJson() {
                return null;
            }

            @Nullable
            @Override
            public Identifier getAdvancementId() {
                return null;
            }
        });
    }
}