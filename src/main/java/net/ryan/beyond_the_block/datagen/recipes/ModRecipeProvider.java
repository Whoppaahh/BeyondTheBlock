package net.ryan.beyond_the_block.datagen.recipes;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

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
        generateWoodcuttingRecipes(exporter);
    }

    public static void generateWoodcuttingRecipes(Consumer<RecipeJsonProvider> exporter) {
        offerStandardWoodSet(exporter,
                "oak",
                Items.OAK_LOG, Items.OAK_WOOD, Items.STRIPPED_OAK_LOG, Items.STRIPPED_OAK_WOOD,
                Items.OAK_PLANKS, Items.OAK_SLAB, Items.OAK_STAIRS, Items.OAK_FENCE, Items.OAK_FENCE_GATE,
                Items.OAK_DOOR, Items.OAK_TRAPDOOR, Items.OAK_BUTTON, Items.OAK_PRESSURE_PLATE,
                Items.OAK_SIGN
                //, Items.OAK_HANGING_SIGN, Items.OAK_BOAT, Items.OAK_CHEST_BOAT
        );

        offerStandardWoodSet(exporter,
                "spruce",
                Items.SPRUCE_LOG, Items.SPRUCE_WOOD, Items.STRIPPED_SPRUCE_LOG, Items.STRIPPED_SPRUCE_WOOD,
                Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB, Items.SPRUCE_STAIRS, Items.SPRUCE_FENCE, Items.SPRUCE_FENCE_GATE,
                Items.SPRUCE_DOOR, Items.SPRUCE_TRAPDOOR, Items.SPRUCE_BUTTON, Items.SPRUCE_PRESSURE_PLATE,
                Items.SPRUCE_SIGN
                //, Items.SPRUCE_HANGING_SIGN, Items.SPRUCE_BOAT, Items.SPRUCE_CHEST_BOAT
        );

        offerStandardWoodSet(exporter,
                "birch",
                Items.BIRCH_LOG, Items.BIRCH_WOOD, Items.STRIPPED_BIRCH_LOG, Items.STRIPPED_BIRCH_WOOD,
                Items.BIRCH_PLANKS, Items.BIRCH_SLAB, Items.BIRCH_STAIRS, Items.BIRCH_FENCE, Items.BIRCH_FENCE_GATE,
                Items.BIRCH_DOOR, Items.BIRCH_TRAPDOOR, Items.BIRCH_BUTTON, Items.BIRCH_PRESSURE_PLATE,
                Items.BIRCH_SIGN
                //, Items.BIRCH_HANGING_SIGN, Items.BIRCH_BOAT, Items.BIRCH_CHEST_BOAT
        );

        offerStandardWoodSet(exporter,
                "jungle",
                Items.JUNGLE_LOG, Items.JUNGLE_WOOD, Items.STRIPPED_JUNGLE_LOG, Items.STRIPPED_JUNGLE_WOOD,
                Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB, Items.JUNGLE_STAIRS, Items.JUNGLE_FENCE, Items.JUNGLE_FENCE_GATE,
                Items.JUNGLE_DOOR, Items.JUNGLE_TRAPDOOR, Items.JUNGLE_BUTTON, Items.JUNGLE_PRESSURE_PLATE,
                Items.JUNGLE_SIGN
                //, Items.JUNGLE_HANGING_SIGN, Items.JUNGLE_BOAT, Items.JUNGLE_CHEST_BOAT
        );

        offerStandardWoodSet(exporter,
                "acacia",
                Items.ACACIA_LOG, Items.ACACIA_WOOD, Items.STRIPPED_ACACIA_LOG, Items.STRIPPED_ACACIA_WOOD,
                Items.ACACIA_PLANKS, Items.ACACIA_SLAB, Items.ACACIA_STAIRS, Items.ACACIA_FENCE, Items.ACACIA_FENCE_GATE,
                Items.ACACIA_DOOR, Items.ACACIA_TRAPDOOR, Items.ACACIA_BUTTON, Items.ACACIA_PRESSURE_PLATE,
                Items.ACACIA_SIGN
                //, Items.ACACIA_HANGING_SIGN, Items.ACACIA_BOAT, Items.ACACIA_CHEST_BOAT
        );

        offerStandardWoodSet(exporter,
                "dark_oak",
                Items.DARK_OAK_LOG, Items.DARK_OAK_WOOD, Items.STRIPPED_DARK_OAK_LOG, Items.STRIPPED_DARK_OAK_WOOD,
                Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB, Items.DARK_OAK_STAIRS, Items.DARK_OAK_FENCE, Items.DARK_OAK_FENCE_GATE,
                Items.DARK_OAK_DOOR, Items.DARK_OAK_TRAPDOOR, Items.DARK_OAK_BUTTON, Items.DARK_OAK_PRESSURE_PLATE,
                Items.DARK_OAK_SIGN
                //, Items.DARK_OAK_HANGING_SIGN, Items.DARK_OAK_BOAT, Items.DARK_OAK_CHEST_BOAT
        );

        offerStandardWoodSet(exporter,
                "mangrove",
                Items.MANGROVE_LOG, Items.MANGROVE_WOOD, Items.STRIPPED_MANGROVE_LOG, Items.STRIPPED_MANGROVE_WOOD,
                Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB, Items.MANGROVE_STAIRS, Items.MANGROVE_FENCE, Items.MANGROVE_FENCE_GATE,
                Items.MANGROVE_DOOR, Items.MANGROVE_TRAPDOOR, Items.MANGROVE_BUTTON, Items.MANGROVE_PRESSURE_PLATE,
                Items.MANGROVE_SIGN
                //, Items.MANGROVE_HANGING_SIGN, Items.MANGROVE_BOAT, Items.MANGROVE_CHEST_BOAT
        );

        offerNetherWoodSet(exporter,
                "crimson",
                Items.CRIMSON_STEM, Items.CRIMSON_HYPHAE, Items.STRIPPED_CRIMSON_STEM, Items.STRIPPED_CRIMSON_HYPHAE,
                Items.CRIMSON_PLANKS, Items.CRIMSON_SLAB, Items.CRIMSON_STAIRS, Items.CRIMSON_FENCE, Items.CRIMSON_FENCE_GATE,
                Items.CRIMSON_DOOR, Items.CRIMSON_TRAPDOOR, Items.CRIMSON_BUTTON, Items.CRIMSON_PRESSURE_PLATE,
                Items.CRIMSON_SIGN
                //, Items.CRIMSON_HANGING_SIGN
        );

        offerNetherWoodSet(exporter,
                "warped",
                Items.WARPED_STEM, Items.WARPED_HYPHAE, Items.STRIPPED_WARPED_STEM, Items.STRIPPED_WARPED_HYPHAE,
                Items.WARPED_PLANKS, Items.WARPED_SLAB, Items.WARPED_STAIRS, Items.WARPED_FENCE, Items.WARPED_FENCE_GATE,
                Items.WARPED_DOOR, Items.WARPED_TRAPDOOR, Items.WARPED_BUTTON, Items.WARPED_PRESSURE_PLATE,
                Items.WARPED_SIGN
                //, Items.WARPED_HANGING_SIGN
        );

        //offerBambooSet(exporter);

        // Backported/custom sets:
        // offerStandardWoodSet(exporter, "cherry", ModItems.CHERRY_LOG, ...);
        // offerStandardWoodSet(exporter, "pale_oak", ModItems.PALE_OAK_LOG, ...);
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

    private static void offerStandardWoodSet(
            Consumer<RecipeJsonProvider> exporter,
            String name,
            Item log,
            Item wood,
            Item strippedLog,
            Item strippedWood,
            Item planks,
            Item slab,
            Item stairs,
            Item fence,
            Item fenceGate,
            Item door,
            Item trapdoor,
            Item button,
            Item pressurePlate,
            Item sign
//            Item hangingSign,
//            Item boat,
//            Item chestBoat
    ) {
        offerRawWoodToPlanks(exporter, name, log, wood, strippedLog, strippedWood, planks);
        offerCommonPlankRecipes(exporter, name, planks, slab, stairs, fence, fenceGate, door, trapdoor, button, pressurePlate, sign);
    }

    private static void offerNetherWoodSet(
            Consumer<RecipeJsonProvider> exporter,
            String name,
            Item stem,
            Item hyphae,
            Item strippedStem,
            Item strippedHyphae,
            Item planks,
            Item slab,
            Item stairs,
            Item fence,
            Item fenceGate,
            Item door,
            Item trapdoor,
            Item button,
            Item pressurePlate,
            Item sign //,
            //Item hangingSign
    ) {
        offerRawWoodToPlanks(exporter, name, stem, hyphae, strippedStem, strippedHyphae, planks);
        offerCommonPlankRecipes(exporter, name, planks, slab, stairs, fence, fenceGate, door, trapdoor, button, pressurePlate, sign);
    }

//    private static void offerBambooSet(Consumer<RecipeJsonProvider> exporter) {
//        Item planks = Items.BAMBOO_PLANKS;
//
//        offer(exporter, Items.BAMBOO_BLOCK, 1, planks, 4, "woodcutting/bamboo_planks_from_bamboo_block");
//        offer(exporter, Items.STRIPPED_BAMBOO_BLOCK, 1, planks, 4, "woodcutting/bamboo_planks_from_stripped_bamboo_block");
//
//        offerCommonPlankRecipes(exporter,
//                "bamboo",
//                planks,
//                Items.BAMBOO_SLAB,
//                Items.BAMBOO_STAIRS,
//                Items.BAMBOO_FENCE,
//                Items.BAMBOO_FENCE_GATE,
//                Items.BAMBOO_DOOR,
//                Items.BAMBOO_TRAPDOOR,
//                Items.BAMBOO_BUTTON,
//                Items.BAMBOO_PRESSURE_PLATE
//        );
//
//        offerUtilityPlankRecipes(exporter,
//                "bamboo",
//                planks,
//                Items.BAMBOO_SIGN,
//                Items.BAMBOO_HANGING_SIGN,
//                Items.BAMBOO_RAFT,
//                Items.BAMBOO_CHEST_RAFT
//        );
//    }

    private static void offerRawWoodToPlanks(
            Consumer<RecipeJsonProvider> exporter,
            String name,
            Item log,
            Item wood,
            Item strippedLog,
            Item strippedWood,
            Item planks
    ) {
        offer(exporter, log, 1, planks, 6, "woodcutting/" + name + "_planks_from_log");
        offer(exporter, wood, 1, planks, 6, "woodcutting/" + name + "_planks_from_wood");
        offer(exporter, strippedLog, 1, planks, 6, "woodcutting/" + name + "_planks_from_stripped_log");
        offer(exporter, strippedWood, 1, planks, 6, "woodcutting/" + name + "_planks_from_stripped_wood");
    }

    private static void offerCommonPlankRecipes(
            Consumer<RecipeJsonProvider> exporter,
            String name,
            Item planks,
            Item slab,
            Item stairs,
            Item fence,
            Item fenceGate,
            Item door,
            Item trapdoor,
            Item button,
            Item pressurePlate,
            Item sign
    ) {
        offer(exporter, planks, 1, slab, 3, "woodcutting/" + name + "_slab_from_planks");
        offer(exporter, planks, 1, stairs, 1, "woodcutting/" + name + "_stairs_from_planks");
        offer(exporter, planks, 1, fence, 1, "woodcutting/" + name + "_fence_from_planks");
        offer(exporter, planks, 1, fenceGate, 1, "woodcutting/" + name + "_fence_gate_from_planks");
        offer(exporter, planks, 1, door, 2, "woodcutting/" + name + "_door_from_planks");
        offer(exporter, planks, 1, trapdoor, 1, "woodcutting/" + name + "_trapdoor_from_planks");
        offer(exporter, planks, 1, button, 1, "woodcutting/" + name + "_button_from_planks");
        offer(exporter, planks, 1, pressurePlate, 1, "woodcutting/" + name + "_pressure_plate_from_planks");
        offer(exporter, planks, 1, sign, 3, "woodcutting/" + name + "_sign_from_planks");
    }

    private static void offer(
            Consumer<RecipeJsonProvider> exporter,
            Item ingredient,
            int ingredientCount,
            Item result,
            int resultCount,
            String path
    ) {
        WoodcuttingRecipeJsonBuilder.create(
                Ingredient.ofItems(ingredient),
                ingredientCount,
                result,
                resultCount,
                new Identifier(BeyondTheBlock.MOD_ID + ":" + path)
        ).offerTo(exporter);
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