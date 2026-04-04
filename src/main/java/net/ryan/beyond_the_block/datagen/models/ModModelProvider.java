package net.ryan.beyond_the_block.datagen.models;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.item.ModItems;

import java.util.List;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        registerCubeAllBlocks(blockStateModelGenerator, List.of(
                // Ruby
                ModBlocks.RUBY_BLOCK,
                ModBlocks.RUBY_ORE,
                ModBlocks.DEEPSLATE_RUBY_ORE,

                // Miranite
                ModBlocks.MIRANITE_BLOCK,
                ModBlocks.RAW_MIRANITE_BLOCK,
                ModBlocks.MIRANITE_ORE,
                ModBlocks.DEEPSLATE_MIRANITE_ORE,
                ModBlocks.NETHER_MIRANITE_ORE,
                ModBlocks.END_MIRANITE_ORE,

                // Chromite
                ModBlocks.CHROMITE_BLOCK,
                ModBlocks.CHROMITE_ORE,
                ModBlocks.DEEPSLATE_CHROMITE_ORE,
                ModBlocks.NETHER_CHROMITE_ORE,
                ModBlocks.END_CHROMITE_ORE,

                // Nocturnite
                ModBlocks.NOCTURNITE_BLOCK,
                ModBlocks.NOCTURNITE_ORE,
                ModBlocks.DEEPSLATE_NOCTURNITE_ORE,
                ModBlocks.NETHER_NOCTURNITE_ORE,
                ModBlocks.END_NOCTURNITE_ORE,

                // Amberine
                ModBlocks.AMBERINE_BLOCK,
                ModBlocks.AMBERINE_ORE,
                ModBlocks.DEEPSLATE_AMBERINE_ORE,

                // Rosette
                ModBlocks.ROSETTE_BLOCK,
                ModBlocks.RAW_ROSETTE_BLOCK,
                ModBlocks.ROSETTE_ORE,
                ModBlocks.DEEPSLATE_ROSETTE_ORE,
                ModBlocks.NETHER_ROSETTE_ORE,
                ModBlocks.END_ROSETTE_ORE,

                // Azuros
                ModBlocks.AZUROS_BLOCK,
                ModBlocks.RAW_AZUROS_BLOCK,
                ModBlocks.AZUROS_ORE,
                ModBlocks.DEEPSLATE_AZUROS_ORE,
                ModBlocks.NETHER_AZUROS_ORE,
                ModBlocks.END_AZUROS_ORE,

                // Indigra
                ModBlocks.INDIGRA_BLOCK,
                ModBlocks.INDIGRA_ORE,
                ModBlocks.DEEPSLATE_INDIGRA_ORE,
                ModBlocks.NETHER_INDIGRA_ORE,
                ModBlocks.END_INDIGRA_ORE,

                // Xirion
                ModBlocks.XIRION_BLOCK,
                ModBlocks.XIRION_ORE,
                ModBlocks.DEEPSLATE_XIRION_ORE,
                ModBlocks.NETHER_XIRION_ORE,
                ModBlocks.END_XIRION_ORE
        ));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        registerGeneratedItems(itemModelGenerator, List.of(
                // Gems / materials
                ModItems.RUBY_ITEM,
                ModItems.MIRANITE_ITEM,
                ModItems.RAW_MIRANITE_ITEM,
                ModItems.CHROMITE_ITEM,
                ModItems.RAW_CHROMITE_ITEM,
                ModItems.NOCTURNITE_ITEM,
                ModItems.RAW_NOCTURNITE_ITEM,
                ModItems.ROSETTE_ITEM,
                ModItems.RAW_ROSETTE_ITEM,
                ModItems.AMBERINE_ITEM,
                ModItems.RAW_AMBERINE_ITEM,
                ModItems.AZUROS_ITEM,
                ModItems.RAW_AZUROS_ITEM,
                ModItems.INDIGRA_ITEM,
                ModItems.RAW_INDIGRA_ITEM,
                ModItems.XIRION_ITEM,

                // Armour
                ModItems.RUBY_HELMET,
                ModItems.RUBY_CHESTPLATE,
                ModItems.RUBY_LEGGINGS,
                ModItems.RUBY_BOOTS,

                ModItems.AMBERINE_HELMET,
                ModItems.AMBERINE_CHESTPLATE,
                ModItems.AMBERINE_LEGGINGS,
                ModItems.AMBERINE_BOOTS,

                ModItems.AZUROS_HELMET,
                ModItems.AZUROS_CHESTPLATE,
                ModItems.AZUROS_LEGGINGS,
                ModItems.AZUROS_BOOTS,

                ModItems.CHROMITE_HELMET,
                ModItems.CHROMITE_CHESTPLATE,
                ModItems.CHROMITE_LEGGINGS,
                ModItems.CHROMITE_BOOTS,

                ModItems.MIRANITE_HELMET,
                ModItems.MIRANITE_CHESTPLATE,
                ModItems.MIRANITE_LEGGINGS,
                ModItems.MIRANITE_BOOTS,

                ModItems.ROSETTE_HELMET,
                ModItems.ROSETTE_CHESTPLATE,
                ModItems.ROSETTE_LEGGINGS,
                ModItems.ROSETTE_BOOTS,

                ModItems.XIRION_HELMET,
                ModItems.XIRION_CHESTPLATE,
                ModItems.XIRION_LEGGINGS,
                ModItems.XIRION_BOOTS,

                // Fuels
                ModItems.ASTRACINDER,
                ModItems.ECLIPSED_BLOOM
        ));

        registerHandheldItems(itemModelGenerator, List.of(
                // Swords
                ModItems.RUBY_SWORD,
                ModItems.AMBERINE_SWORD,
                ModItems.AZUROS_SWORD,
                ModItems.CHROMITE_SWORD,
                ModItems.MIRANITE_SWORD,
                ModItems.ROSETTE_SWORD,
                ModItems.XIRION_SWORD,

                // Axes
                ModItems.RUBY_AXE,
                ModItems.AMBERINE_AXE,
                ModItems.AZUROS_AXE,
                ModItems.CHROMITE_AXE,
                ModItems.MIRANITE_AXE,
                ModItems.ROSETTE_AXE,
                ModItems.XIRION_AXE,

                // Pickaxes
                ModItems.RUBY_PICKAXE,
                ModItems.AMBERINE_PICKAXE,
                ModItems.AZUROS_PICKAXE,
                ModItems.CHROMITE_PICKAXE,
                ModItems.MIRANITE_PICKAXE,
                ModItems.ROSETTE_PICKAXE,
                ModItems.XIRION_PICKAXE,

                // Hoes
                ModItems.RUBY_HOE,
                ModItems.AMBERINE_HOE,
                ModItems.AZUROS_HOE,
                ModItems.CHROMITE_HOE,
                ModItems.MIRANITE_HOE,
                ModItems.ROSETTE_HOE,
                ModItems.XIRION_HOE,

                // Shovels
                ModItems.RUBY_SHOVEL,
                ModItems.AMBERINE_SHOVEL,
                ModItems.AZUROS_SHOVEL,
                ModItems.CHROMITE_SHOVEL,
                ModItems.MIRANITE_SHOVEL,
                ModItems.ROSETTE_SHOVEL,
                ModItems.XIRION_SHOVEL
        ));
    }

    private void registerCubeAllBlocks(BlockStateModelGenerator generator, List<Block> blocks) {
        for (Block block : blocks) {
            generator.registerCubeAllModelTexturePool(block);
        }
    }

    private void registerGeneratedItems(ItemModelGenerator generator, List<Item> items) {
        for (Item item : items) {
            generator.register(item, Models.GENERATED);
        }
    }

    private void registerHandheldItems(ItemModelGenerator generator, List<Item> items) {
        for (Item item : items) {
            generator.register(item, Models.HANDHELD);
        }
    }
}