package net.ryan.beyond_the_block.content.registry;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.content.recipes.*;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class ModRecipes {
    public static final RecipeSerializer<GemSmithingRecipe> GEM_SMITHING_SERIALISER =
            Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(BeyondTheBlock.MOD_ID, "smithing_gem_upgrade"), new GemSmithingRecipeSerialiser());

    public static final RecipeType<GemSmithingRecipe> GEM_SMITHING_TYPE =
            Registry.register(Registry.RECIPE_TYPE, new Identifier(BeyondTheBlock.MOD_ID, "smithing_gem_upgrade"), new RecipeType<>() {
                @Override
                public String toString() {
                    return BeyondTheBlock.MOD_ID + " smithing_gem_upgrade";
                }
            });

    public static final RecipeSerializer<CustomBlastingRecipe> CUSTOM_BLASTING_SERIALISER =
            Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(BeyondTheBlock.MOD_ID, "custom_blasting"), new CustomBlastingRecipeSerialiser());

    public static final RecipeSerializer<WoodcuttingRecipe> WOODCUTTING_RECIPE_SERIALISER =
            Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(BeyondTheBlock.MOD_ID, "woodcutting"), new WoodcuttingRecipeSerialiser());

    public static final RecipeType<WoodcuttingRecipe> WOODCUTTING_RECIPE_TYPE = Registry.register(
            Registry.RECIPE_TYPE,
            new Identifier(BeyondTheBlock.MOD_ID, "woodcutting"),
            new RecipeType<>() {
                @Override
                public String toString() {
                    return BeyondTheBlock.MOD_ID + ":woodcutting";
                }
            }
    );

    public static void registerModRecipes() {
        // Call in your main mod initializer
        BeyondTheBlock.LOGGER.info("Registering Mod Recipes for " + BeyondTheBlock.MOD_ID);
    }
}

