package net.ryan.beyond_the_block.content.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class WoodcuttingRecipeSerialiser implements RecipeSerializer<WoodcuttingRecipe> {

    public static final WoodcuttingRecipeSerialiser INSTANCE = new WoodcuttingRecipeSerialiser();

    @Override
    public WoodcuttingRecipe read(Identifier id, JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
        int ingredientCount = JsonHelper.getInt(json, "ingredient_count", 1);

        Identifier resultId = new Identifier(JsonHelper.getString(json, "result"));
        int count = JsonHelper.getInt(json, "count", 1);

        Item resultItem = Registry.ITEM.get(resultId);
        ItemStack output = new ItemStack(resultItem, count);

        return new WoodcuttingRecipe(id, ingredient, ingredientCount, output);
    }

    @Override
    public WoodcuttingRecipe read(Identifier id, PacketByteBuf buf) {
        Ingredient ingredient = Ingredient.fromPacket(buf);
        int ingredientCount = buf.readVarInt();
        ItemStack output = buf.readItemStack();
        return new WoodcuttingRecipe(id, ingredient, ingredientCount, output);
    }

    @Override
    public void write(PacketByteBuf buf, WoodcuttingRecipe recipe) {
        recipe.getIngredient().write(buf);
        buf.writeVarInt(recipe.getIngredientCount());
        buf.writeItemStack(recipe.getOutput());
    }
}