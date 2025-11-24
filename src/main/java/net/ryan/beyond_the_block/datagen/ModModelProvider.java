package net.ryan.beyond_the_block.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.ryan.beyond_the_block.block.ModBlocks;
import net.ryan.beyond_the_block.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        //region Modded Ores
        //region Miranite
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.MIRANITE_BLOCK);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.RAW_MIRANITE_BLOCK);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.MIRANITE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DEEPSLATE_MIRANITE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.NETHER_MIRANITE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.END_MIRANITE_ORE);
        //endregion
        //region Chromite
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.CHROMITE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DEEPSLATE_CHROMITE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.NETHER_CHROMITE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.END_CHROMITE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.CHROMITE_BLOCK);
        //endregion
        //region Nocturnite
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.NOCTURNITE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DEEPSLATE_NOCTURNITE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.NETHER_NOCTURNITE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.END_NOCTURNITE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.NOCTURNITE_BLOCK);
        //endregion
        //region Amberine
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.AMBERINE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DEEPSLATE_AMBERINE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.AMBERINE_BLOCK);
        //endregion
        //region Rosette
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.ROSETTE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DEEPSLATE_ROSETTE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.NETHER_ROSETTE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.END_ROSETTE_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.ROSETTE_BLOCK);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.RAW_ROSETTE_BLOCK);
        //endregion
        //region Azuros
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.AZUROS_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DEEPSLATE_AZUROS_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.NETHER_AZUROS_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.END_AZUROS_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.AZUROS_BLOCK);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.RAW_AZUROS_BLOCK);
        //endregion
        //region Indigra
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.INDIGRA_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DEEPSLATE_INDIGRA_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.NETHER_INDIGRA_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.END_INDIGRA_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.INDIGRA_BLOCK);
        //endregion
        //region Xirion
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.XIRION_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DEEPSLATE_XIRION_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.NETHER_XIRION_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.END_XIRION_ORE);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.XIRION_BLOCK);
        //endregion
        //endregion
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.RUBY_ITEM, Models.GENERATED);

        //region Modded Ores
        //region Miranite
        itemModelGenerator.register(ModItems.MIRANITE_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_MIRANITE_ITEM, Models.GENERATED);
        //endregion
        //region Chromite
        itemModelGenerator.register(ModItems.CHROMITE_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_CHROMITE_ITEM, Models.GENERATED);
        //endregion
        //region Nocturnite
        itemModelGenerator.register(ModItems.NOCTURNITE_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_NOCTURNITE_ITEM, Models.GENERATED);
        //endregion
        //region Rosette
        itemModelGenerator.register(ModItems.ROSETTE_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_ROSETTE_ITEM, Models.GENERATED);
        //endregion
        //region Amberine
        itemModelGenerator.register(ModItems.AMBERINE_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_AMBERINE_ITEM, Models.GENERATED);
        //endregion
        //region Azuros
        itemModelGenerator.register(ModItems.AZUROS_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_AZUROS_ITEM, Models.GENERATED);
        //endregion
        //region Indigra
        itemModelGenerator.register(ModItems.INDIGRA_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_INDIGRA_ITEM, Models.GENERATED);
        //endregion
        //region Xirion
        itemModelGenerator.register(ModItems.XIRION_ITEM, Models.GENERATED);
        //endregion
        //endregion
        //region Modded Tools
        //region Swords
        itemModelGenerator.register(ModItems.RUBY_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.AMBERINE_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.AZUROS_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.CHROMITE_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.MIRANITE_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.ROSETTE_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.XIRION_SWORD, Models.HANDHELD);
        //endregion
        //region Axes
        itemModelGenerator.register(ModItems.RUBY_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.AMBERINE_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.AZUROS_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.CHROMITE_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.MIRANITE_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.ROSETTE_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.XIRION_AXE, Models.HANDHELD);
        //endregion
        //region Pickaxes
        itemModelGenerator.register(ModItems.RUBY_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.AMBERINE_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.AZUROS_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.CHROMITE_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.MIRANITE_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.ROSETTE_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.XIRION_PICKAXE, Models.HANDHELD);
        //endregion
        //region Hoes
        itemModelGenerator.register(ModItems.RUBY_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.AMBERINE_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.AZUROS_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.CHROMITE_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.MIRANITE_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.ROSETTE_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.XIRION_HOE, Models.HANDHELD);
        //endregion
        //region Shovels
        itemModelGenerator.register(ModItems.RUBY_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.AMBERINE_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.AZUROS_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.CHROMITE_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.MIRANITE_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.ROSETTE_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.XIRION_SHOVEL, Models.HANDHELD);
        //endregion
        //endregion
        //region Modded Armour
        //region Ruby
        itemModelGenerator.register(ModItems.RUBY_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.RUBY_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.RUBY_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.RUBY_BOOTS, Models.GENERATED);
        //endregion
        //region Azuros
        itemModelGenerator.register(ModItems.AZUROS_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.AZUROS_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.AZUROS_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.AZUROS_BOOTS, Models.GENERATED);
        //endregion
        //region Amberine
        itemModelGenerator.register(ModItems.AMBERINE_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.AMBERINE_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.AMBERINE_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.AMBERINE_BOOTS, Models.GENERATED);
        //endregion
        //region Chromite
        itemModelGenerator.register(ModItems.CHROMITE_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.CHROMITE_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.CHROMITE_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.CHROMITE_BOOTS, Models.GENERATED);
        //endregion
        //region Miranite
        itemModelGenerator.register(ModItems.MIRANITE_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.MIRANITE_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.MIRANITE_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.MIRANITE_BOOTS, Models.GENERATED);
        //endregion
        //region Rosette
        itemModelGenerator.register(ModItems.ROSETTE_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.ROSETTE_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ROSETTE_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.ROSETTE_BOOTS, Models.GENERATED);
        //endregion
        //region Xirion
        itemModelGenerator.register(ModItems.XIRION_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.XIRION_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.XIRION_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.XIRION_BOOTS, Models.GENERATED);
        //endregion
        //endregion
        //region Modded Fuels
        itemModelGenerator.register(ModItems.ASTRACINDER, Models.GENERATED);
        itemModelGenerator.register(ModItems.ECLIPSED_BLOOM, Models.GENERATED);
        //endregion
    }
}