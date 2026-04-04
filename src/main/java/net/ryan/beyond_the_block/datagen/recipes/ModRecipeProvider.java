package net.ryan.beyond_the_block.datagen.recipes;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.item.ModItems;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        generateSmeltingRecipes(exporter);
        generateBlastSmeltingRecipes(exporter);
        generateReversibleRecipes(exporter);
        generateMaterialFamilyRecipes(exporter);

        generateShelfRecipes(exporter);
        generateSpongeRecipes(exporter);
        generateUtilityBlockRecipes(exporter);
    }

    private void generateBlastSmeltingRecipes(Consumer<RecipeJsonProvider> exporter) {
        offerBlasting(exporter, List.of(ModBlocks.RAW_AZUROS_BLOCK), ModItems.AZUROS_ITEM, 3f, 3000, "azuros");
        offerBlasting(exporter, List.of(ModBlocks.RAW_MIRANITE_BLOCK), ModItems.MIRANITE_ITEM, 3f, 2550, "miranite");
        offerBlasting(exporter, List.of(ModBlocks.RAW_ROSETTE_BLOCK), ModItems.ROSETTE_ITEM, 4f, 3450, "rosette");

        offerBlasting(exporter, List.of(Blocks.RAW_COPPER_BLOCK), Items.COPPER_INGOT, 3f, 1000, "copper_ingot");
        offerBlasting(exporter, List.of(Blocks.RAW_IRON_BLOCK), Items.IRON_INGOT, 4f, 2000, "iron_ingot");
        offerBlasting(exporter, List.of(Blocks.RAW_GOLD_BLOCK), Items.GOLD_INGOT, 4.5f, 3000, "gold_ingot");
    }

    private void generateSmeltingRecipes(Consumer<RecipeJsonProvider> exporter) {
        offerSmelting(exporter, List.of(ModItems.RAW_MIRANITE_ITEM), ModItems.MIRANITE_ITEM, 3f, 300, "miranite");
        offerSmelting(exporter, List.of(ModItems.RAW_ROSETTE_ITEM), ModItems.ROSETTE_ITEM, 4f, 400, "rosette");
        offerSmelting(exporter, List.of(ModItems.RAW_NOCTURNITE_ITEM), ModItems.NOCTURNITE_ITEM, 15f, 1200, "nocturnite");
        offerSmelting(exporter, List.of(ModItems.RAW_INDIGRA_ITEM), ModItems.INDIGRA_ITEM, 7f, 600, "indigra");
        offerSmelting(exporter, List.of(ModItems.RAW_CHROMITE_ITEM), ModItems.CHROMITE_ITEM, 5f, 500, "chromite");
        offerSmelting(exporter, List.of(ModItems.RAW_AZUROS_ITEM), ModItems.AZUROS_ITEM, 3f, 350, "azuros");
        offerSmelting(exporter, List.of(ModItems.RAW_AMBERINE_ITEM), ModItems.AMBERINE_ITEM, 2f, 200, "amberine");
    }

    private void generateReversibleRecipes(Consumer<RecipeJsonProvider> exporter) {
        offerReversibleCompactingRecipes(exporter, ModItems.MIRANITE_ITEM, ModBlocks.MIRANITE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.ROSETTE_ITEM, ModBlocks.ROSETTE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.NOCTURNITE_ITEM, ModBlocks.NOCTURNITE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.INDIGRA_ITEM, ModBlocks.INDIGRA_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.CHROMITE_ITEM, ModBlocks.CHROMITE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.AZUROS_ITEM, ModBlocks.AZUROS_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.AMBERINE_ITEM, ModBlocks.AMBERINE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.XIRION_ITEM, ModBlocks.XIRION_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.RUBY_ITEM, ModBlocks.RUBY_BLOCK);

        offerReversibleCompactingRecipes(exporter, ModItems.RAW_MIRANITE_ITEM, ModBlocks.RAW_MIRANITE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.RAW_ROSETTE_ITEM, ModBlocks.RAW_ROSETTE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.RAW_AZUROS_ITEM, ModBlocks.RAW_AZUROS_BLOCK);
    }

    private void generateMaterialFamilyRecipes(Consumer<RecipeJsonProvider> exporter) {
        List<MaterialFamily> families = List.of(
                new MaterialFamily(ModItems.RUBY_ITEM, ModItems.RUBY_SWORD, ModItems.RUBY_AXE, ModItems.RUBY_PICKAXE, ModItems.RUBY_HOE, ModItems.RUBY_SHOVEL, ModItems.RUBY_HELMET, ModItems.RUBY_CHESTPLATE, ModItems.RUBY_LEGGINGS, ModItems.RUBY_BOOTS),
                new MaterialFamily(ModItems.AMBERINE_ITEM, ModItems.AMBERINE_SWORD, ModItems.AMBERINE_AXE, ModItems.AMBERINE_PICKAXE, ModItems.AMBERINE_HOE, ModItems.AMBERINE_SHOVEL, ModItems.AMBERINE_HELMET, ModItems.AMBERINE_CHESTPLATE, ModItems.AMBERINE_LEGGINGS, ModItems.AMBERINE_BOOTS),
                new MaterialFamily(ModItems.AZUROS_ITEM, ModItems.AZUROS_SWORD, ModItems.AZUROS_AXE, ModItems.AZUROS_PICKAXE, ModItems.AZUROS_HOE, ModItems.AZUROS_SHOVEL, ModItems.AZUROS_HELMET, ModItems.AZUROS_CHESTPLATE, ModItems.AZUROS_LEGGINGS, ModItems.AZUROS_BOOTS),
                new MaterialFamily(ModItems.CHROMITE_ITEM, ModItems.CHROMITE_SWORD, ModItems.CHROMITE_AXE, ModItems.CHROMITE_PICKAXE, ModItems.CHROMITE_HOE, ModItems.CHROMITE_SHOVEL, ModItems.CHROMITE_HELMET, ModItems.CHROMITE_CHESTPLATE, ModItems.CHROMITE_LEGGINGS, ModItems.CHROMITE_BOOTS),
                new MaterialFamily(ModItems.MIRANITE_ITEM, ModItems.MIRANITE_SWORD, ModItems.MIRANITE_AXE, ModItems.MIRANITE_PICKAXE, ModItems.MIRANITE_HOE, ModItems.MIRANITE_SHOVEL, ModItems.MIRANITE_HELMET, ModItems.MIRANITE_CHESTPLATE, ModItems.MIRANITE_LEGGINGS, ModItems.MIRANITE_BOOTS),
                new MaterialFamily(ModItems.ROSETTE_ITEM, ModItems.ROSETTE_SWORD, ModItems.ROSETTE_AXE, ModItems.ROSETTE_PICKAXE, ModItems.ROSETTE_HOE, ModItems.ROSETTE_SHOVEL, ModItems.ROSETTE_HELMET, ModItems.ROSETTE_CHESTPLATE, ModItems.ROSETTE_LEGGINGS, ModItems.ROSETTE_BOOTS),
                new MaterialFamily(ModItems.XIRION_ITEM, ModItems.XIRION_SWORD, ModItems.XIRION_AXE, ModItems.XIRION_PICKAXE, ModItems.XIRION_HOE, ModItems.XIRION_SHOVEL, ModItems.XIRION_HELMET, ModItems.XIRION_CHESTPLATE, ModItems.XIRION_LEGGINGS, ModItems.XIRION_BOOTS)
        );

        for (MaterialFamily family : families) {
            offerSwordRecipe(exporter, family.material(), family.sword());
            offerAxeRecipe(exporter, family.material(), family.axe());
            offerPickaxeRecipe(exporter, family.material(), family.pickaxe());
            offerHoeRecipe(exporter, family.material(), family.hoe());
            offerShovelRecipe(exporter, family.material(), family.shovel());

            offerHelmetRecipe(exporter, family.material(), family.helmet());
            offerChestplateRecipe(exporter, family.material(), family.chestplate());
            offerLeggingsRecipe(exporter, family.material(), family.leggings());
            offerBootsRecipe(exporter, family.material(), family.boots());
        }
    }

    private void generateShelfRecipes(Consumer<RecipeJsonProvider> exporter) {
        offerShelfRecipe(exporter, ModBlocks.OAK_SHELF_BLOCK, Items.OAK_PLANKS);
        offerShelfRecipe(exporter, ModBlocks.SPRUCE_SHELF_BLOCK, Items.SPRUCE_PLANKS);
        offerShelfRecipe(exporter, ModBlocks.BIRCH_SHELF_BLOCK, Items.BIRCH_PLANKS);
        offerShelfRecipe(exporter, ModBlocks.JUNGLE_SHELF_BLOCK, Items.JUNGLE_PLANKS);
        offerShelfRecipe(exporter, ModBlocks.ACACIA_SHELF_BLOCK, Items.ACACIA_PLANKS);
        offerShelfRecipe(exporter, ModBlocks.DARK_OAK_SHELF_BLOCK, Items.DARK_OAK_PLANKS);
        offerShelfRecipe(exporter, ModBlocks.MANGROVE_SHELF_BLOCK, Items.MANGROVE_PLANKS);
        //offerShelfRecipe(exporter, ModBlocks.BAMBOO_SHELF_BLOCK, Items.BAMBOO_PLANKS);
        offerShelfRecipe(exporter, ModBlocks.CRIMSON_SHELF_BLOCK, Items.CRIMSON_PLANKS);
        offerShelfRecipe(exporter, ModBlocks.WARPED_SHELF_BLOCK, Items.WARPED_PLANKS);
    }

    private void generateSpongeRecipes(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(ModBlocks.COMPRESSED_SPONGE)
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .input('S', Items.SPONGE)
                .criterion(hasItem(Items.SPONGE), conditionsFromItem(Items.SPONGE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(ModBlocks.DOUBLE_COMPRESSED_SPONGE)
                .pattern("CCC")
                .pattern("CCC")
                .pattern("CCC")
                .input('C', ModBlocks.COMPRESSED_SPONGE)
                .criterion(hasItem(ModBlocks.COMPRESSED_SPONGE), conditionsFromItem(ModBlocks.COMPRESSED_SPONGE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(ModBlocks.TRIPLE_COMPRESSED_SPONGE)
                .pattern("DDD")
                .pattern("DDD")
                .pattern("DDD")
                .input('D', ModBlocks.DOUBLE_COMPRESSED_SPONGE)
                .criterion(hasItem(ModBlocks.DOUBLE_COMPRESSED_SPONGE), conditionsFromItem(ModBlocks.DOUBLE_COMPRESSED_SPONGE))
                .offerTo(exporter);
    }

    private void generateUtilityBlockRecipes(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(ModBlocks.LAVA_LAMP_BLOCK)
                .pattern(" G ")
                .pattern("GLG")
                .pattern(" S ")
                .input('G', Items.GLASS)
                .input('L', Items.LAVA_BUCKET)
                .input('S', Items.SMOOTH_STONE)
                .criterion(hasItem(Items.LAVA_BUCKET), conditionsFromItem(Items.LAVA_BUCKET))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(ModBlocks.PEDESTAL_BLOCK)
                .pattern(" S ")
                .pattern(" C ")
                .pattern("SSS")
                .input('S', Items.STONE_BRICKS)
                .input('C', Items.CHISELED_STONE_BRICKS)
                .criterion(hasItem(Items.STONE_BRICKS), conditionsFromItem(Items.STONE_BRICKS))
                .offerTo(exporter);
    }

    private void offerShelfRecipe(Consumer<RecipeJsonProvider> exporter, Block result, Item plank) {
        ShapedRecipeJsonBuilder.create(result, 2)
                .pattern("PPP")
                .pattern("P P")
                .input('P', plank)
                .criterion(hasItem(plank), conditionsFromItem(plank))
                .offerTo(exporter);
    }

    private void offerSwordRecipe(Consumer<RecipeJsonProvider> exporter, Item material, Item result) {
        ShapedRecipeJsonBuilder.create(result)
                .pattern("  R")
                .pattern(" R ")
                .pattern("S  ")
                .input('R', material)
                .input('S', Items.STICK)
                .criterion(hasItem(material), conditionsFromItem(material))
                .offerTo(exporter);
    }

    private void offerAxeRecipe(Consumer<RecipeJsonProvider> exporter, Item material, Item result) {
        ShapedRecipeJsonBuilder.create(result)
                .pattern("RR")
                .pattern("RS")
                .pattern(" S")
                .input('R', material)
                .input('S', Items.STICK)
                .criterion(hasItem(material), conditionsFromItem(material))
                .offerTo(exporter);
    }

    private void offerPickaxeRecipe(Consumer<RecipeJsonProvider> exporter, Item material, Item result) {
        ShapedRecipeJsonBuilder.create(result)
                .pattern("RRR")
                .pattern(" S ")
                .pattern(" S ")
                .input('R', material)
                .input('S', Items.STICK)
                .criterion(hasItem(material), conditionsFromItem(material))
                .offerTo(exporter);
    }

    private void offerHoeRecipe(Consumer<RecipeJsonProvider> exporter, Item material, Item result) {
        ShapedRecipeJsonBuilder.create(result)
                .pattern("RR")
                .pattern(" S")
                .pattern(" S")
                .input('R', material)
                .input('S', Items.STICK)
                .criterion(hasItem(material), conditionsFromItem(material))
                .offerTo(exporter);
    }

    private void offerShovelRecipe(Consumer<RecipeJsonProvider> exporter, Item material, Item result) {
        ShapedRecipeJsonBuilder.create(result)
                .pattern("R")
                .pattern("S")
                .pattern("S")
                .input('R', material)
                .input('S', Items.STICK)
                .criterion(hasItem(material), conditionsFromItem(material))
                .offerTo(exporter);
    }

    private void offerHelmetRecipe(Consumer<RecipeJsonProvider> exporter, Item material, Item result) {
        ShapedRecipeJsonBuilder.create(result)
                .pattern("RRR")
                .pattern("R R")
                .input('R', material)
                .criterion(hasItem(material), conditionsFromItem(material))
                .offerTo(exporter);
    }

    private void offerChestplateRecipe(Consumer<RecipeJsonProvider> exporter, Item material, Item result) {
        ShapedRecipeJsonBuilder.create(result)
                .pattern("R R")
                .pattern("RRR")
                .pattern("RRR")
                .input('R', material)
                .criterion(hasItem(material), conditionsFromItem(material))
                .offerTo(exporter);
    }

    private void offerLeggingsRecipe(Consumer<RecipeJsonProvider> exporter, Item material, Item result) {
        ShapedRecipeJsonBuilder.create(result)
                .pattern("RRR")
                .pattern("R R")
                .pattern("R R")
                .input('R', material)
                .criterion(hasItem(material), conditionsFromItem(material))
                .offerTo(exporter);
    }

    private void offerBootsRecipe(Consumer<RecipeJsonProvider> exporter, Item material, Item result) {
        ShapedRecipeJsonBuilder.create(result)
                .pattern("R R")
                .pattern("R R")
                .input('R', material)
                .criterion(hasItem(material), conditionsFromItem(material))
                .offerTo(exporter);
    }

    private record MaterialFamily(
            Item material,
            Item sword,
            Item axe,
            Item pickaxe,
            Item hoe,
            Item shovel,
            Item helmet,
            Item chestplate,
            Item leggings,
            Item boots
    ) {
    }
}