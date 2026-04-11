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
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.content.registry.family.BambooWoodSet;
import net.ryan.beyond_the_block.content.registry.family.HangingSignDropFamily;
import net.ryan.beyond_the_block.content.registry.family.WoodSet;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.datagen.common.ModDatagenFamilies;

import java.util.function.BiConsumer;

import static net.ryan.beyond_the_block.content.registry.ModBlocks.HANGING_SIGN_ITEMS;

public class ModLootTableProvider extends SimpleFabricLootTableProvider {

    public ModLootTableProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator, LootContextTypes.BLOCK);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        generateStandardWoodLoot(exporter, ModBlocks.CHERRY_SET);
        generateStandardWoodLoot(exporter, ModBlocks.PALE_OAK_SET);
        generateBambooWoodLoot(exporter, ModBlocks.BAMBOO_WOOD_SET);

        generateOreFamily(exporter,
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
                ModBlocks.XIRION_BLOCK,
                null,
                ModBlocks.XIRION_ORE,
                ModBlocks.DEEPSLATE_XIRION_ORE,
                ModBlocks.NETHER_XIRION_ORE,
                ModBlocks.END_XIRION_ORE,
                ModItems.XIRION_ITEM,
                null
        );

        exportDropSelf(exporter, ModBlocks.RUBY_BLOCK);
        exportOre(exporter, ModBlocks.RUBY_ORE, ModItems.RUBY_ITEM);
        exportOre(exporter, ModBlocks.DEEPSLATE_RUBY_ORE, ModItems.RUBY_ITEM);

        generateSelfDropFamily(exporter, ModDatagenFamilies.SHELVES);
        generateSelfDropFamily(exporter, ModDatagenFamilies.SPONGES);
        generateSelfDropFamily(exporter, ModDatagenFamilies.SHRINE_BLOCKS);
        generateSelfDropFamily(exporter, ModDatagenFamilies.SIMPLE_UTILITY_BLOCKS);
        generateSelfDropFamily(exporter, ModDatagenFamilies.MACHINE_STYLE_BLOCKS);
        generateSelfDropFamily(exporter, ModDatagenFamilies.RAIL_STYLE_BLOCKS);

        for (HangingSignDropFamily family : ModDatagenFamilies.HANGING_SIGNS) {
            generateHangingSignDropFamily(exporter, family);
        }

        exportDropSelf(exporter, ModBlocks.CHISELED_BOOKSHELF);
        exportDropSelf(exporter, ModBlocks.WATER_TORCH_BLOCK);
    }

    private void generateHangingSignDropFamily(BiConsumer<Identifier, LootTable.Builder> exporter, HangingSignDropFamily family) {
        exporter.accept(id(family.hangingSign()), BlockLootTableGenerator.drops(family.dropItem()));
        exporter.accept(id(family.wallHangingSign()), BlockLootTableGenerator.drops(family.dropItem()));
    }

    private void generateStandardWoodLoot(BiConsumer<Identifier, LootTable.Builder> exporter, WoodSet set) {
        exportDropSelf(exporter, set.log());
        exportDropSelf(exporter, set.wood());
        exportDropSelf(exporter, set.strippedLog());
        exportDropSelf(exporter, set.strippedWood());
        exportDropSelf(exporter, set.planks());
        exportDropSelf(exporter, set.slab());
        exportDropSelf(exporter, set.stairs());
        exportDropSelf(exporter, set.fence());
        exportDropSelf(exporter, set.fenceGate());
        exportDropSelf(exporter, set.door());
        exportDropSelf(exporter, set.trapdoor());
        exportDropSelf(exporter, set.button());
        exportDropSelf(exporter, set.pressurePlate());
    }

    private void generateBambooWoodLoot(BiConsumer<Identifier, LootTable.Builder> exporter, BambooWoodSet set) {
        exportDropSelf(exporter, set.bambooBlock());
        exportDropSelf(exporter, set.strippedBambooBlock());
        exportDropSelf(exporter, set.planks());
        exportDropSelf(exporter, set.slab());
        exportDropSelf(exporter, set.stairs());
        exportDropSelf(exporter, set.fence());
        exportDropSelf(exporter, set.fenceGate());
        exportDropSelf(exporter, set.door());
        exportDropSelf(exporter, set.trapdoor());
        exportDropSelf(exporter, set.button());
        exportDropSelf(exporter, set.pressurePlate());
        exportDropSelf(exporter, set.mosaic());
        exportDropSelf(exporter, set.mosaicSlab());
        exportDropSelf(exporter, set.mosaicStairs());
    }

    private void generateOreFamily(
            BiConsumer<Identifier, LootTable.Builder> exporter,
            Block storageBlock,
            Block rawStorageBlock,
            Block overworldOre,
            Block deepslateOre,
            Block netherOre,
            Block endOre,
            Item refinedItem,
            Item rawItem
    ) {
        exportDropSelf(exporter, storageBlock);

        if (rawStorageBlock != null && rawItem != null) {
            exportRawStorage(exporter, rawStorageBlock, rawItem);
        }

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

    private void generateSelfDropFamily(BiConsumer<Identifier, LootTable.Builder> exporter, Iterable<Block> blocks) {
        for (Block block : blocks) {
            exportDropSelf(exporter, block);
        }
    }

    private void exportDropSelf(BiConsumer<Identifier, LootTable.Builder> exporter, Block block) {
        exporter.accept(id(block), BlockLootTableGenerator.drops(block));
    }

    private void exportRawStorage(BiConsumer<Identifier, LootTable.Builder> exporter, Block block, Item rawItem) {
        exporter.accept(id(block), BlockLootTableGenerator.drops(rawItem));
    }

    private void exportOre(BiConsumer<Identifier, LootTable.Builder> exporter, Block ore, Item dropItem) {
        exporter.accept(id(ore), BlockLootTableGenerator.dropsWithSilkTouch(ore, ItemEntry.builder(dropItem)));
    }

    private Identifier id(Block block) {
        return new Identifier(
                BeyondTheBlock.MOD_ID,
                "blocks/" + block.getTranslationKey().replace("block." + BeyondTheBlock.MOD_ID + ".", "")
        );
    }
}