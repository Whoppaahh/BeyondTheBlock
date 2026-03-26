package net.ryan.beyond_the_block.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.item.ModItems;

import java.util.function.BiConsumer;

public class ModLootTableProvider extends SimpleFabricLootTableProvider {
    public ModLootTableProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator, LootContextTypes.BLOCK);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> identifierBuilderBiConsumer) {
        //region Miranite
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/miranite_block"),
                BlockLootTableGenerator.drops(ModBlocks.MIRANITE_BLOCK));
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/raw_miranite_block"),
                BlockLootTableGenerator.drops(ModItems.MIRANITE_ITEM));

        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/miranite_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_MIRANITE_ITEM));
        BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.MIRANITE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/deepslate_miranite_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_MIRANITE_ITEM));
        BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.DEEPSLATE_MIRANITE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/nether_miranite_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_MIRANITE_ITEM));
        BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.NETHER_MIRANITE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/end_miranite_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_MIRANITE_ITEM));
        BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.END_MIRANITE_ORE);
        //endregion
        //region Chromite
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/chromite_block"),
                BlockLootTableGenerator.drops(ModBlocks.CHROMITE_BLOCK));

        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/chromite_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_CHROMITE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.CHROMITE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/deepslate_chromite_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_CHROMITE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.DEEPSLATE_CHROMITE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/nether_chromite_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_CHROMITE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.NETHER_CHROMITE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/end_chromite_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_CHROMITE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.END_CHROMITE_ORE);
        //endregion
        //region Nocturnite
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/nocturnite_block"),
                BlockLootTableGenerator.drops(ModBlocks.NOCTURNITE_BLOCK));

        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/nocturnite_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_NOCTURNITE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.NOCTURNITE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/deepslate_nocturnite_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_NOCTURNITE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.DEEPSLATE_NOCTURNITE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/nether_nocturnite_ore"),
                BlockLootTableGenerator.drops(ModItems.NOCTURNITE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.NETHER_NOCTURNITE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/end_nocturnite_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_NOCTURNITE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.END_NOCTURNITE_ORE);
        //endregion
        //region Amberine
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/amberine_block"),
                BlockLootTableGenerator.drops(ModBlocks.AMBERINE_BLOCK));

        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/amberine_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_AMBERINE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.AMBERINE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/deepslate_amberine_ore"),
                BlockLootTableGenerator.drops(ModItems.AMBERINE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.DEEPSLATE_AMBERINE_ORE);
        //endregion
        //region Rosette
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/rosette_block"),
                BlockLootTableGenerator.drops(ModBlocks.ROSETTE_BLOCK));
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/raw_rosette_block"),
                BlockLootTableGenerator.drops(ModItems.ROSETTE_ITEM));

        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/rosette_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_ROSETTE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.ROSETTE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/deepslate_rosette_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_ROSETTE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.DEEPSLATE_ROSETTE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/nether_rosette_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_ROSETTE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.NETHER_ROSETTE_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/end_rosette_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_ROSETTE_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.END_ROSETTE_ORE);
        //endregion
        //region Azuros
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/azuros_block"),
                BlockLootTableGenerator.drops(ModBlocks.AZUROS_BLOCK));
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/raw_azuros_block"),
                BlockLootTableGenerator.drops(ModItems.AZUROS_ITEM));

        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/azuros_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_AZUROS_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.AZUROS_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/deepslate_azuros_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_AZUROS_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.DEEPSLATE_AZUROS_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/nether_azuros_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_AZUROS_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.NETHER_AZUROS_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/end_azuros_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_AZUROS_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.END_AZUROS_ORE);
        //endregion
        //region Indigra
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/indigra_block"),
                BlockLootTableGenerator.drops(ModBlocks.INDIGRA_BLOCK));

        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/indigra_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_INDIGRA_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.INDIGRA_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/deepslate_indigra_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_INDIGRA_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.DEEPSLATE_INDIGRA_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/nether_indigra_ore"),
                BlockLootTableGenerator.drops(ModItems.INDIGRA_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.NETHER_INDIGRA_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/end_indigra_ore"),
                BlockLootTableGenerator.drops(ModItems.RAW_INDIGRA_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.END_INDIGRA_ORE);
        //endregion
        //region Xirion
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/xirion_block"),
                BlockLootTableGenerator.drops(ModBlocks.XIRION_BLOCK));

        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/xirion_ore"),
                BlockLootTableGenerator.drops(ModItems.XIRION_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.XIRION_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/deepslate_xirion_ore"),
                BlockLootTableGenerator.drops(ModItems.XIRION_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.DEEPSLATE_XIRION_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/nether_xirion_ore"),
                BlockLootTableGenerator.drops(ModItems.XIRION_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.NETHER_XIRION_ORE);
        identifierBuilderBiConsumer.accept(new Identifier(BeyondTheBlock.MOD_ID, "blocks/end_xirion_ore"),
                BlockLootTableGenerator.drops(ModItems.XIRION_ITEM));
                BlockLootTableGenerator.dropsWithSilkTouch(ModBlocks.END_XIRION_ORE);
        //endregion
    }
}
