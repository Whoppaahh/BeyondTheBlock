package net.ryan.beyond_the_block.content.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WoodcuttingRecipeSerialiser implements RecipeSerializer<WoodcuttingRecipe> {

    public static final WoodcuttingRecipeSerialiser INSTANCE = new WoodcuttingRecipeSerialiser();

    @Override
    public WoodcuttingRecipe read(Identifier id, JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
        Identifier outId = new Identifier(json.get("result").getAsString());
        int count = json.has("count") ? json.get("count").getAsInt() : 1;

        return new WoodcuttingRecipe(id, ingredient, new ItemStack(Registry.ITEM.get(outId), count));
    }

    @Override
    public WoodcuttingRecipe read(Identifier id, PacketByteBuf buf) {
        return null;
    }

    @Override
    public void write(PacketByteBuf buf, WoodcuttingRecipe recipe) {

    }
}
