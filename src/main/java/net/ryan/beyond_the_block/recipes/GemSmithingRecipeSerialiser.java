package net.ryan.beyond_the_block.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.BeyondTheBlock;

public class GemSmithingRecipeSerialiser implements RecipeSerializer<GemSmithingRecipe> {
    public static final Identifier ID = new Identifier(BeyondTheBlock.MOD_ID, "smithing_gem_upgrade");

    @Override
    public GemSmithingRecipe read(Identifier id, JsonObject json) {
        Ingredient base = Ingredient.fromJson(JsonHelper.getObject(json, "base"));
        Ingredient addition = Ingredient.fromJson(JsonHelper.getObject(json, "addition"));

        JsonObject resultObject = JsonHelper.getObject(json, "result");
        Item resultItem = Registry.ITEM.get(new Identifier(JsonHelper.getString(resultObject, "item")));
        int count = JsonHelper.getInt(resultObject, "count", 1);
        ItemStack resultStack = new ItemStack(resultItem, count);

        return new GemSmithingRecipe(id, base, addition, resultStack);
    }

    @Override
    public GemSmithingRecipe read(Identifier id, PacketByteBuf buf) {
        Ingredient base = Ingredient.fromPacket(buf);
        Ingredient addition = Ingredient.fromPacket(buf);
        ItemStack result = buf.readItemStack();

        return new GemSmithingRecipe(id, base, addition, result);
    }

    @Override
    public void write(PacketByteBuf buf, GemSmithingRecipe recipe) {
        recipe.getBase().write(buf);
        recipe.getAddition().write(buf);
        buf.writeItemStack(recipe.getOutput());
    }
}
