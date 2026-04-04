package net.ryan.beyond_the_block.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.function.BiConsumer;

public class ModLootTableProvider extends SimpleFabricLootTableProvider {

    public ModLootTableProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator, LootContextTypes.BLOCK);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {

        // ===== MATERIAL FAMILIES =====
        generateOreFamily(exporter,
                "miranite",
                ModBlocks.MIRANITE_BLOCK,
                ModBlocks.RAW_MIRANITE_BLOCK,
                ModBlocks.MIRANITE_ORE,
                ModBlocks.DEEPSLATE_MIRANITE_ORE,
                ModBlocks.NETHER_MIRANITE_ORE,
                ModBlocks.END_MIRANITE_ORE,
                ModItems.MIRANITE_ITEM,
                ModItems.RAW_MIRANITE_ITEM
        );

        generateOreFamily(exporter,
                "chromite",
                ModBlocks.CHROMITE_BLOCK,
                null,
                ModBlocks.CHROMITE_ORE,
                ModBlocks.DEEPSLATE_CHROMITE_ORE,
                ModBlocks.NETHER_CHROMITE_ORE,
                ModBlocks.END_CHROMITE_ORE,
                ModItems.CHROMITE_ITEM,
                ModItems.RAW_CHROMITE_ITEM
        );

        generateOreFamily(exporter,
                "nocturnite",
                ModBlocks.NOCTURNITE_BLOCK,
                null,
                ModBlocks.NOCTURNITE_ORE,
                ModBlocks.DEEPSLATE_NOCTURNITE_ORE,
                ModBlocks.NETHER_NOCTURNITE_ORE,
                ModBlocks.END_NOCTURNITE_ORE,
                ModItems.NOCTURNITE_ITEM,
                ModItems.RAW_NOCTURNITE_ITEM
        );

        generateOreFamily(exporter,
                "amberine",
                ModBlocks.AMBERINE_BLOCK,
                null,
                ModBlocks.AMBERINE_ORE,
                ModBlocks.DEEPSLATE_AMBERINE_ORE,
                null,
                null,
                ModItems.AMBERINE_ITEM,
                ModItems.RAW_AMBERINE_ITEM
        );

        generateOreFamily(exporter,
                "rosette",
                ModBlocks.ROSETTE_BLOCK,
                ModBlocks.RAW_ROSETTE_BLOCK,
                ModBlocks.ROSETTE_ORE,
                ModBlocks.DEEPSLATE_ROSETTE_ORE,
                ModBlocks.NETHER_ROSETTE_ORE,
                ModBlocks.END_ROSETTE_ORE,
                ModItems.ROSETTE_ITEM,
                ModItems.RAW_ROSETTE_ITEM
        );

        generateOreFamily(exporter,
                "azuros",
                ModBlocks.AZUROS_BLOCK,
                ModBlocks.RAW_AZUROS_BLOCK,
                ModBlocks.AZUROS_ORE,
                ModBlocks.DEEPSLATE_AZUROS_ORE,
                ModBlocks.NETHER_AZUROS_ORE,
                ModBlocks.END_AZUROS_ORE,
                ModItems.AZUROS_ITEM,
                ModItems.RAW_AZUROS_ITEM
        );

        generateOreFamily(exporter,
                "indigra",
                ModBlocks.INDIGRA_BLOCK,
                null,
                ModBlocks.INDIGRA_ORE,
                ModBlocks.DEEPSLATE_INDIGRA_ORE,
                ModBlocks.NETHER_INDIGRA_ORE,
                ModBlocks.END_INDIGRA_ORE,
                ModItems.INDIGRA_ITEM,
                ModItems.RAW_INDIGRA_ITEM
        );

        generateOreFamily(exporter,
                "xirion",
                ModBlocks.XIRION_BLOCK,
                null,
                ModBlocks.XIRION_ORE,
                ModBlocks.DEEPSLATE_XIRION_ORE,
                ModBlocks.NETHER_XIRION_ORE,
                ModBlocks.END_XIRION_ORE,
                ModItems.XIRION_ITEM,
                null // direct drop
        );
    }

    // ===== FAMILY GENERATOR =====

    private void generateOreFamily(
            BiConsumer<Identifier, LootTable.Builder> exporter,
            String name,
            Block storageBlock,
            Block rawStorageBlock,
            Block overworldOre,
            Block deepslateOre,
            Block netherOre,
            Block endOre,
            Item refinedItem,
            Item rawItem
    ) {

        // Storage block -> drops itself
        exportDropSelf(exporter, storageBlock);

        // Raw storage block (if exists) -> drops raw item x9
        if (rawStorageBlock != null && rawItem != null) {
            exportRawStorage(exporter, rawStorageBlock, rawItem);
        }

        // Ore logic
        if (overworldOre != null) {
            exportOre(exporter, overworldOre, rawItem != null ? rawItem : refinedItem);
        }

        if (deepslateOre != null) {
            exportOre(exporter, deepslateOre, rawItem != null ? rawItem : refinedItem);
        }

        if (netherOre != null) {
            exportOre(exporter, netherOre, rawItem != null ? rawItem : refinedItem);
        }

        if (endOre != null) {
            exportOre(exporter, endOre, rawItem != null ? rawItem : refinedItem);
        }
    }

    // ===== EXPORT HELPERS =====

    private void exportDropSelf(BiConsumer<Identifier, LootTable.Builder> exporter, Block block) {
        exporter.accept(id(block),
                BlockLootTableGenerator.drops(block));
    }

    private void exportRawStorage(BiConsumer<Identifier, LootTable.Builder> exporter, Block block, Item rawItem) {
        exporter.accept(id(block),
                BlockLootTableGenerator.drops(rawItem)); // You can upgrade this to count=9 later if desired
    }

    private void exportOre(BiConsumer<Identifier, LootTable.Builder> exporter, Block ore, Item dropItem) {
        exporter.accept(id(ore),
                BlockLootTableGenerator.dropsWithSilkTouch(ore, ItemEntry.builder(dropItem)));
    }

    private Identifier id(Block block) {
        return new Identifier(BeyondTheBlock.MOD_ID,
                "blocks/" + block.getTranslationKey().replace("block." + BeyondTheBlock.MOD_ID + ".", ""));
    }
}