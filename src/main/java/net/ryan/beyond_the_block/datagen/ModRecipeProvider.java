package net.ryan.beyond_the_block.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.ryan.beyond_the_block.block.ModBlocks;
import net.ryan.beyond_the_block.item.ModItems;

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
        generateShapedRecipes(exporter);
    }
    private void generateBlastSmeltingRecipes(Consumer<RecipeJsonProvider> exporter){
        offerBlasting(exporter, List.of(ModBlocks.RAW_AZUROS_BLOCK), ModItems.AZUROS_ITEM, 3f, 3000, "azuros");
        offerBlasting(exporter, List.of(ModBlocks.RAW_MIRANITE_BLOCK), ModItems.MIRANITE_ITEM, 3f, 2550, "miranite");
        offerBlasting(exporter, List.of(ModBlocks.RAW_ROSETTE_BLOCK), ModItems.ROSETTE_ITEM, 4f, 3450, "rosette");

        offerBlasting(exporter, List.of(Blocks.RAW_COPPER_BLOCK), Items.COPPER_INGOT, 3f, 1000, "copper_ingot");
        offerBlasting(exporter, List.of(Blocks.RAW_IRON_BLOCK), Items.IRON_INGOT, 4f, 2000, "iron_ingot");
        offerBlasting(exporter, List.of(Blocks.RAW_GOLD_BLOCK), Items.GOLD_INGOT, 4.5f, 3000, "gold_ingot");
    }
    private void generateSmeltingRecipes(Consumer<RecipeJsonProvider> exporter){
        //region Raw Item to Cut Item
        offerSmelting(exporter, List.of(ModItems.RAW_MIRANITE_ITEM), ModItems.MIRANITE_ITEM,
                3f, 300, "miranite");
        offerSmelting(exporter, List.of(ModItems.RAW_ROSETTE_ITEM), ModItems.ROSETTE_ITEM,
                4f, 400, "rosette");
        offerSmelting(exporter, List.of(ModItems.RAW_NOCTURNITE_ITEM), ModItems.NOCTURNITE_ITEM,
                15f, 1200, "nocturnite");
        offerSmelting(exporter, List.of(ModItems.RAW_INDIGRA_ITEM), ModItems.INDIGRA_ITEM,
                7f, 600, "indigra");
        offerSmelting(exporter, List.of(ModItems.RAW_CHROMITE_ITEM), ModItems.CHROMITE_ITEM,
                5f, 500, "chromite");
        offerSmelting(exporter, List.of(ModItems.RAW_AZUROS_ITEM), ModItems.AZUROS_ITEM,
                3f, 350, "azuros");
        offerSmelting(exporter, List.of(ModItems.RAW_AMBERINE_ITEM), ModItems.AMBERINE_ITEM,
                2f, 200, "amberine");
        //endregion
    }
    private void generateReversibleRecipes(Consumer<RecipeJsonProvider> exporter){
        offerReversibleCompactingRecipes(exporter, ModItems.MIRANITE_ITEM, ModBlocks.MIRANITE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.ROSETTE_ITEM, ModBlocks.ROSETTE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.NOCTURNITE_ITEM, ModBlocks.NOCTURNITE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.INDIGRA_ITEM, ModBlocks.INDIGRA_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.CHROMITE_ITEM, ModBlocks.CHROMITE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.AZUROS_ITEM, ModBlocks.AZUROS_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.AMBERINE_ITEM, ModBlocks.AMBERINE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.XIRION_ITEM, ModBlocks.XIRION_BLOCK);

        offerReversibleCompactingRecipes(exporter, ModItems.RAW_MIRANITE_ITEM, ModBlocks.RAW_MIRANITE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.RAW_ROSETTE_ITEM, ModBlocks.RAW_ROSETTE_BLOCK);
        offerReversibleCompactingRecipes(exporter, ModItems.RAW_AZUROS_ITEM, ModBlocks.RAW_AZUROS_BLOCK);
    }

//    private void generateShapelessRecipes(Consumer<RecipeJsonProvider> exporter){
//        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RAW_PINK_GARNET, 9)
//                .input(ModBlocks.RAW_PINK_GARNET_BLOCK)
//                .criterion(hasItem(ModBlocks.RAW_PINK_GARNET_BLOCK), conditionsFromItem(ModBlocks.RAW_PINK_GARNET_BLOCK))
//                .offerTo(exporter);
//    }
    private void generateShapedRecipes(Consumer<RecipeJsonProvider> exporter){
        //region Tools
        //region Swords
        ShapedRecipeJsonBuilder.create(ModItems.RUBY_SWORD)
                .pattern("  R")
                .pattern(" R ")
                .pattern("S  ")
                .input('R', ModItems.RUBY_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.RUBY_ITEM), conditionsFromItem(ModItems.RUBY_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AMBERINE_SWORD)
                .pattern("  R")
                .pattern(" R ")
                .pattern("S  ")
                .input('R', ModItems.AMBERINE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.AMBERINE_ITEM), conditionsFromItem(ModItems.AMBERINE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AZUROS_SWORD)
                .pattern("  R")
                .pattern(" R ")
                .pattern("S  ")
                .input('R', ModItems.AZUROS_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.AZUROS_ITEM), conditionsFromItem(ModItems.AZUROS_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.CHROMITE_SWORD)
                .pattern("  R")
                .pattern(" R ")
                .pattern("S  ")
                .input('R', ModItems.CHROMITE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.CHROMITE_ITEM), conditionsFromItem(ModItems.CHROMITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.MIRANITE_SWORD)
                .pattern("  R")
                .pattern(" R ")
                .pattern("S  ")
                .input('R', ModItems.MIRANITE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.MIRANITE_ITEM), conditionsFromItem(ModItems.MIRANITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.ROSETTE_SWORD)
                .pattern("  R")
                .pattern(" R ")
                .pattern("S  ")
                .input('R', ModItems.ROSETTE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.ROSETTE_ITEM), conditionsFromItem(ModItems.ROSETTE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.XIRION_SWORD)
                .pattern("  R")
                .pattern(" R ")
                .pattern("S  ")
                .input('R', ModItems.XIRION_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.XIRION_ITEM), conditionsFromItem(ModItems.XIRION_ITEM))
                .offerTo(exporter);
        //endregion
        //region Axes
        ShapedRecipeJsonBuilder.create(ModItems.RUBY_AXE)
                .pattern("RR")
                .pattern("RS")
                .pattern(" S")
                .input('R', ModItems.RUBY_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.RUBY_ITEM), conditionsFromItem(ModItems.RUBY_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AMBERINE_AXE)
                .pattern("RR")
                .pattern("RS")
                .pattern(" S")
                .input('R', ModItems.AMBERINE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.AMBERINE_ITEM), conditionsFromItem(ModItems.AMBERINE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AZUROS_AXE)
                .pattern("RR")
                .pattern("RS")
                .pattern(" S")
                .input('R', ModItems.AZUROS_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.AZUROS_ITEM), conditionsFromItem(ModItems.AZUROS_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.CHROMITE_AXE)
                .pattern("RR")
                .pattern("RS")
                .pattern(" S")
                .input('R', ModItems.CHROMITE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.CHROMITE_ITEM), conditionsFromItem(ModItems.CHROMITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.MIRANITE_AXE)
                .pattern("RR")
                .pattern("RS")
                .pattern(" S")
                .input('R', ModItems.MIRANITE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.MIRANITE_ITEM), conditionsFromItem(ModItems.MIRANITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.ROSETTE_AXE)
                .pattern("RR")
                .pattern("RS")
                .pattern(" S")
                .input('R', ModItems.ROSETTE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.ROSETTE_ITEM), conditionsFromItem(ModItems.ROSETTE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.XIRION_AXE)
                .pattern("RR")
                .pattern("RS")
                .pattern(" S")
                .input('R', ModItems.XIRION_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.XIRION_ITEM), conditionsFromItem(ModItems.XIRION_ITEM))
                .offerTo(exporter);
        //endregion
        //region Shovels
        ShapedRecipeJsonBuilder.create(ModItems.RUBY_SHOVEL)
                .pattern("R")
                .pattern("S")
                .pattern("S")
                .input('R', ModItems.RUBY_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.RUBY_ITEM), conditionsFromItem(ModItems.RUBY_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AMBERINE_SHOVEL)
                .pattern("R")
                .pattern("S")
                .pattern("S")
                .input('R', ModItems.AMBERINE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.AMBERINE_ITEM), conditionsFromItem(ModItems.AMBERINE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AZUROS_SHOVEL)
                .pattern("R")
                .pattern("S")
                .pattern("S")
                .input('R', ModItems.AZUROS_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.AZUROS_ITEM), conditionsFromItem(ModItems.AZUROS_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.CHROMITE_SHOVEL)
                .pattern("R")
                .pattern("S")
                .pattern("S")
                .input('R', ModItems.CHROMITE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.CHROMITE_ITEM), conditionsFromItem(ModItems.CHROMITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.MIRANITE_SHOVEL)
                .pattern("R")
                .pattern("S")
                .pattern("S")
                .input('R', ModItems.MIRANITE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.MIRANITE_ITEM), conditionsFromItem(ModItems.MIRANITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.ROSETTE_SHOVEL)
                .pattern("R")
                .pattern("S")
                .pattern("S")
                .input('R', ModItems.ROSETTE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.ROSETTE_ITEM), conditionsFromItem(ModItems.ROSETTE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.XIRION_SHOVEL)
                .pattern("R")
                .pattern("S")
                .pattern("S")
                .input('R', ModItems.XIRION_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.XIRION_ITEM), conditionsFromItem(ModItems.XIRION_ITEM))
                .offerTo(exporter);
        //endregion
        //region Pickaxe
        ShapedRecipeJsonBuilder.create(ModItems.RUBY_PICKAXE)
                .pattern("RRR")
                .pattern(" S ")
                .pattern(" S ")
                .input('R', ModItems.RUBY_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.RUBY_ITEM), conditionsFromItem(ModItems.RUBY_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AMBERINE_PICKAXE)
                .pattern("RRR")
                .pattern(" S ")
                .pattern(" S ")
                .input('R', ModItems.AMBERINE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.AMBERINE_ITEM), conditionsFromItem(ModItems.AMBERINE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AZUROS_PICKAXE)
                .pattern("RRR")
                .pattern(" S ")
                .pattern(" S ")
                .input('R', ModItems.AZUROS_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.AZUROS_ITEM), conditionsFromItem(ModItems.AZUROS_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.CHROMITE_PICKAXE)
                .pattern("RRR")
                .pattern(" S ")
                .pattern(" S ")
                .input('R', ModItems.CHROMITE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.CHROMITE_ITEM), conditionsFromItem(ModItems.CHROMITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.MIRANITE_PICKAXE)
                .pattern("RRR")
                .pattern(" S ")
                .pattern(" S ")
                .input('R', ModItems.MIRANITE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.MIRANITE_ITEM), conditionsFromItem(ModItems.MIRANITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.ROSETTE_PICKAXE)
                .pattern("RRR")
                .pattern(" S ")
                .pattern(" S ")
                .input('R', ModItems.ROSETTE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.ROSETTE_ITEM), conditionsFromItem(ModItems.ROSETTE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.XIRION_PICKAXE)
                .pattern("RRR")
                .pattern(" S ")
                .pattern(" S ")
                .input('R', ModItems.XIRION_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.XIRION_ITEM), conditionsFromItem(ModItems.XIRION_ITEM))
                .offerTo(exporter);
        //endregion
        //region Hoe
        ShapedRecipeJsonBuilder.create(ModItems.RUBY_HOE)
                .pattern("RR")
                .pattern(" S")
                .pattern(" S")
                .input('R', ModItems.RUBY_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.RUBY_ITEM), conditionsFromItem(ModItems.RUBY_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AMBERINE_HOE)
                .pattern("RR")
                .pattern(" S")
                .pattern(" S")
                .input('R', ModItems.AMBERINE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.AMBERINE_ITEM), conditionsFromItem(ModItems.AMBERINE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AZUROS_HOE)
                .pattern("RR")
                .pattern(" S")
                .pattern(" S")
                .input('R', ModItems.AZUROS_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.AZUROS_ITEM), conditionsFromItem(ModItems.AZUROS_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.CHROMITE_HOE)
                .pattern("RR")
                .pattern(" S")
                .pattern(" S")
                .input('R', ModItems.CHROMITE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.CHROMITE_ITEM), conditionsFromItem(ModItems.CHROMITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.MIRANITE_HOE)
                .pattern("RR")
                .pattern(" S")
                .pattern(" S")
                .input('R', ModItems.MIRANITE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.MIRANITE_ITEM), conditionsFromItem(ModItems.MIRANITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.ROSETTE_HOE)
                .pattern("RR")
                .pattern(" S")
                .pattern(" S")
                .input('R', ModItems.ROSETTE_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.ROSETTE_ITEM), conditionsFromItem(ModItems.ROSETTE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.XIRION_HOE)
                .pattern("RR")
                .pattern(" S")
                .pattern(" S")
                .input('R', ModItems.XIRION_ITEM)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.XIRION_ITEM), conditionsFromItem(ModItems.XIRION_ITEM))
                .offerTo(exporter);
        //endregion
        //endregion
        //region Armour
        //region Helmets
        ShapedRecipeJsonBuilder.create(ModItems.RUBY_HELMET)
                .pattern("RRR")
                .pattern("R R")
                .input('R', ModItems.RUBY_ITEM)
                .criterion(hasItem(ModItems.RUBY_ITEM), conditionsFromItem(ModItems.RUBY_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AMBERINE_HELMET)
                .pattern("RRR")
                .pattern("R R")
                .input('R', ModItems.AMBERINE_ITEM)
                .criterion(hasItem(ModItems.AMBERINE_ITEM), conditionsFromItem(ModItems.AMBERINE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AZUROS_HELMET)
                .pattern("RRR")
                .pattern("R R")
                .input('R', ModItems.AZUROS_ITEM)
                .criterion(hasItem(ModItems.AZUROS_ITEM), conditionsFromItem(ModItems.AZUROS_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.CHROMITE_HELMET)
                .pattern("RRR")
                .pattern("R R")
                .input('R', ModItems.CHROMITE_ITEM)
                .criterion(hasItem(ModItems.CHROMITE_ITEM), conditionsFromItem(ModItems.CHROMITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.MIRANITE_HELMET)
                .pattern("RRR")
                .pattern("R R")
                .input('R', ModItems.MIRANITE_ITEM)
                .criterion(hasItem(ModItems.MIRANITE_ITEM), conditionsFromItem(ModItems.MIRANITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.XIRION_HELMET)
                .pattern("RRR")
                .pattern("R R")
                .input('R', ModItems.XIRION_ITEM)
                .criterion(hasItem(ModItems.XIRION_ITEM), conditionsFromItem(ModItems.XIRION_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.ROSETTE_HELMET)
                .pattern("RRR")
                .pattern("R R")
                .input('R', ModItems.ROSETTE_ITEM)
                .criterion(hasItem(ModItems.ROSETTE_ITEM), conditionsFromItem(ModItems.ROSETTE_ITEM))
                .offerTo(exporter);
        //endregion
        //region Chestplate
        ShapedRecipeJsonBuilder.create(ModItems.RUBY_CHESTPLATE)
                .pattern("R R")
                .pattern("RRR")
                .pattern("RRR")
                .input('R', ModItems.RUBY_ITEM)
                .criterion(hasItem(ModItems.RUBY_ITEM), conditionsFromItem(ModItems.RUBY_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AMBERINE_CHESTPLATE)
                .pattern("R R")
                .pattern("RRR")
                .pattern("RRR")
                .input('R', ModItems.AMBERINE_ITEM)
                .criterion(hasItem(ModItems.AMBERINE_ITEM), conditionsFromItem(ModItems.AMBERINE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AZUROS_CHESTPLATE)
                .pattern("R R")
                .pattern("RRR")
                .pattern("RRR")
                .input('R', ModItems.AZUROS_ITEM)
                .criterion(hasItem(ModItems.AZUROS_ITEM), conditionsFromItem(ModItems.AZUROS_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.CHROMITE_CHESTPLATE)
                .pattern("R R")
                .pattern("RRR")
                .pattern("RRR")
                .input('R', ModItems.CHROMITE_ITEM)
                .criterion(hasItem(ModItems.CHROMITE_ITEM), conditionsFromItem(ModItems.CHROMITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.MIRANITE_CHESTPLATE)
                .pattern("R R")
                .pattern("RRR")
                .pattern("RRR")
                .input('R', ModItems.MIRANITE_ITEM)
                .criterion(hasItem(ModItems.MIRANITE_ITEM), conditionsFromItem(ModItems.MIRANITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.XIRION_CHESTPLATE)
                .pattern("R R")
                .pattern("RRR")
                .pattern("RRR")
                .input('R', ModItems.XIRION_ITEM)
                .criterion(hasItem(ModItems.XIRION_ITEM), conditionsFromItem(ModItems.XIRION_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.ROSETTE_CHESTPLATE)
                .pattern("R R")
                .pattern("RRR")
                .pattern("RRR")
                .input('R', ModItems.ROSETTE_ITEM)
                .criterion(hasItem(ModItems.ROSETTE_ITEM), conditionsFromItem(ModItems.ROSETTE_ITEM))
                .offerTo(exporter);
        //endregion
        //region Leggings
        ShapedRecipeJsonBuilder.create(ModItems.RUBY_LEGGINGS)
                .pattern("RRR")
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.RUBY_ITEM)
                .criterion(hasItem(ModItems.RUBY_ITEM), conditionsFromItem(ModItems.RUBY_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AMBERINE_LEGGINGS)
                .pattern("RRR")
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.AMBERINE_ITEM)
                .criterion(hasItem(ModItems.AMBERINE_ITEM), conditionsFromItem(ModItems.AMBERINE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AZUROS_LEGGINGS)
                .pattern("RRR")
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.AZUROS_ITEM)
                .criterion(hasItem(ModItems.AZUROS_ITEM), conditionsFromItem(ModItems.AZUROS_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.CHROMITE_LEGGINGS)
                .pattern("RRR")
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.CHROMITE_ITEM)
                .criterion(hasItem(ModItems.CHROMITE_ITEM), conditionsFromItem(ModItems.CHROMITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.MIRANITE_LEGGINGS)
                .pattern("RRR")
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.MIRANITE_ITEM)
                .criterion(hasItem(ModItems.MIRANITE_ITEM), conditionsFromItem(ModItems.MIRANITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.XIRION_LEGGINGS)
                .pattern("RRR")
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.XIRION_ITEM)
                .criterion(hasItem(ModItems.XIRION_ITEM), conditionsFromItem(ModItems.XIRION_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.ROSETTE_LEGGINGS)
                .pattern("RRR")
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.ROSETTE_ITEM)
                .criterion(hasItem(ModItems.ROSETTE_ITEM), conditionsFromItem(ModItems.ROSETTE_ITEM))
                .offerTo(exporter);
        //endregion
        //region Boots
        ShapedRecipeJsonBuilder.create(ModItems.RUBY_BOOTS)
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.RUBY_ITEM)
                .criterion(hasItem(ModItems.RUBY_ITEM), conditionsFromItem(ModItems.RUBY_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AMBERINE_BOOTS)
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.AMBERINE_ITEM)
                .criterion(hasItem(ModItems.AMBERINE_ITEM), conditionsFromItem(ModItems.AMBERINE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.AZUROS_BOOTS)
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.AZUROS_ITEM)
                .criterion(hasItem(ModItems.AZUROS_ITEM), conditionsFromItem(ModItems.AZUROS_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.CHROMITE_BOOTS)
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.CHROMITE_ITEM)
                .criterion(hasItem(ModItems.CHROMITE_ITEM), conditionsFromItem(ModItems.CHROMITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.MIRANITE_BOOTS)
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.MIRANITE_ITEM)
                .criterion(hasItem(ModItems.MIRANITE_ITEM), conditionsFromItem(ModItems.MIRANITE_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.XIRION_BOOTS)
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.XIRION_ITEM)
                .criterion(hasItem(ModItems.XIRION_ITEM), conditionsFromItem(ModItems.XIRION_ITEM))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(ModItems.ROSETTE_BOOTS)
                .pattern("R R")
                .pattern("R R")
                .input('R', ModItems.ROSETTE_ITEM)
                .criterion(hasItem(ModItems.ROSETTE_ITEM), conditionsFromItem(ModItems.ROSETTE_ITEM))
                .offerTo(exporter);
        //endregion
        //endregion
    }
}