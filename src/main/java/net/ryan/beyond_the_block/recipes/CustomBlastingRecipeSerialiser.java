package net.ryan.beyond_the_block.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.BeyondTheBlock;

public class CustomBlastingRecipeSerialiser implements RecipeSerializer<CustomBlastingRecipe> {
    public static final Identifier ID = new Identifier(BeyondTheBlock.MOD_ID, "custom_blasting");


    @Override
    public CustomBlastingRecipe read(Identifier id, JsonObject json) {
        String group = JsonHelper.getString(json, "group", "");
        Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));

        JsonObject resultJson = JsonHelper.getObject(json, "result");
        Identifier resultId = new Identifier(JsonHelper.getString(resultJson, "item"));
        int count = JsonHelper.getInt(resultJson, "count", 1);
        ItemStack result = new ItemStack(Registry.ITEM.get((resultId)), count);

        float experience = JsonHelper.getFloat(json, "experience", 0.0F);
        int cookingTime = JsonHelper.getInt(json, "cookingtime", 100);

        return new CustomBlastingRecipe(id, group, ingredient, result, experience, cookingTime);

    }

    @Override
    public CustomBlastingRecipe read(Identifier id, PacketByteBuf buf) {
        String group = buf.readString();
        Ingredient ingredient = Ingredient.fromPacket(buf);
        ItemStack result = buf.readItemStack();
        float experience = buf.readFloat();
        int cookingTime = buf.readVarInt();
        return new CustomBlastingRecipe(id, group, ingredient, result, experience, cookingTime);

    }

    @Override
    public void write(PacketByteBuf buf, CustomBlastingRecipe recipe) {
        buf.writeString(recipe.getGroup());
        recipe.getIngredients().get(0).write(buf);
        buf.writeItemStack(recipe.getOutput());
        buf.writeFloat(recipe.getExperience());
        buf.writeVarInt(recipe.getCookTime());
    }
}

