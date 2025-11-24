package net.ryan.beyond_the_block.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.ryan.beyond_the_block.block.ModBlocks;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateTags() {
        //region Mineable
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.RUBY_BLOCK)
                .add(ModBlocks.RUBY_ORE)
                .add(ModBlocks.DEEPSLATE_RUBY_ORE)

                .add(ModBlocks.MIRANITE_ORE)
                .add(ModBlocks.DEEPSLATE_MIRANITE_ORE)
                .add(ModBlocks.NETHER_MIRANITE_ORE)
                .add(ModBlocks.END_MIRANITE_ORE)
                .add(ModBlocks.MIRANITE_BLOCK)
                .add(ModBlocks.RAW_MIRANITE_BLOCK)

                .add(ModBlocks.CHROMITE_ORE)
                .add(ModBlocks.DEEPSLATE_CHROMITE_ORE)
                .add(ModBlocks.NETHER_CHROMITE_ORE)
                .add(ModBlocks.END_CHROMITE_ORE)
                .add(ModBlocks.CHROMITE_BLOCK)

                .add(ModBlocks.NOCTURNITE_ORE)
                .add(ModBlocks.DEEPSLATE_NOCTURNITE_ORE)
                .add(ModBlocks.NETHER_NOCTURNITE_ORE)
                .add(ModBlocks.END_NOCTURNITE_ORE)
                .add(ModBlocks.NOCTURNITE_BLOCK)

                .add(ModBlocks.AMBERINE_ORE)
                .add(ModBlocks.DEEPSLATE_AMBERINE_ORE)
                .add(ModBlocks.AMBERINE_BLOCK)

                .add(ModBlocks.ROSETTE_ORE)
                .add(ModBlocks.DEEPSLATE_ROSETTE_ORE)
                .add(ModBlocks.NETHER_ROSETTE_ORE)
                .add(ModBlocks.END_ROSETTE_ORE)
                .add(ModBlocks.ROSETTE_BLOCK)
                .add(ModBlocks.RAW_ROSETTE_BLOCK)

                .add(ModBlocks.AZUROS_ORE)
                .add(ModBlocks.DEEPSLATE_AZUROS_ORE)
                .add(ModBlocks.NETHER_AZUROS_ORE)
                .add(ModBlocks.END_AZUROS_ORE)
                .add(ModBlocks.AZUROS_BLOCK)
                .add(ModBlocks.RAW_AZUROS_BLOCK)

                .add(ModBlocks.INDIGRA_ORE)
                .add(ModBlocks.DEEPSLATE_INDIGRA_ORE)
                .add(ModBlocks.NETHER_INDIGRA_ORE)
                .add(ModBlocks.END_INDIGRA_ORE)
                .add(ModBlocks.INDIGRA_BLOCK)

                .add(ModBlocks.XIRION_ORE)
                .add(ModBlocks.DEEPSLATE_XIRION_ORE)
                .add(ModBlocks.NETHER_XIRION_ORE)
                .add(ModBlocks.END_XIRION_ORE)
                .add(ModBlocks.XIRION_BLOCK);
        //endregion
        //region Needs Stone Tool
        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.RUBY_BLOCK)
                .add(ModBlocks.AMBERINE_ORE)
                .add(ModBlocks.DEEPSLATE_AMBERINE_ORE)
                .add(ModBlocks.AMBERINE_BLOCK);
        //endregion
        //region Needs Iron Tool
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.RUBY_ORE)

                .add(ModBlocks.AZUROS_ORE)
                .add(ModBlocks.DEEPSLATE_AZUROS_ORE)
                .add(ModBlocks.NETHER_AZUROS_ORE)
                .add(ModBlocks.END_AZUROS_ORE)
                .add(ModBlocks.AZUROS_BLOCK)
                .add(ModBlocks.RAW_AZUROS_BLOCK)

                .add(ModBlocks.XIRION_ORE)
                .add(ModBlocks.DEEPSLATE_XIRION_ORE)
                .add(ModBlocks.NETHER_XIRION_ORE)
                .add(ModBlocks.END_XIRION_ORE)
                .add(ModBlocks.XIRION_BLOCK)

                .add(ModBlocks.INDIGRA_ORE)
                .add(ModBlocks.DEEPSLATE_INDIGRA_ORE)
                .add(ModBlocks.NETHER_INDIGRA_ORE)
                .add(ModBlocks.END_INDIGRA_ORE)
                .add(ModBlocks.INDIGRA_BLOCK)

                .add(ModBlocks.ROSETTE_ORE)
                .add(ModBlocks.DEEPSLATE_ROSETTE_ORE)
                .add(ModBlocks.NETHER_ROSETTE_ORE)
                .add(ModBlocks.END_ROSETTE_ORE)
                .add(ModBlocks.ROSETTE_BLOCK)
                .add(ModBlocks.RAW_ROSETTE_BLOCK)

                .add(ModBlocks.MIRANITE_ORE)
                .add(ModBlocks.DEEPSLATE_MIRANITE_ORE)
                .add(ModBlocks.NETHER_MIRANITE_ORE)
                .add(ModBlocks.END_MIRANITE_ORE)
                .add(ModBlocks.MIRANITE_BLOCK)
                .add(ModBlocks.RAW_MIRANITE_BLOCK)

                .add(ModBlocks.CHROMITE_ORE)
                .add(ModBlocks.DEEPSLATE_CHROMITE_ORE)
                .add(ModBlocks.NETHER_CHROMITE_ORE)
                .add(ModBlocks.END_CHROMITE_ORE)
                .add(ModBlocks.CHROMITE_BLOCK);
        //endregion
        //region Needs Diamond Tool
        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.DEEPSLATE_RUBY_ORE)

                .add(ModBlocks.NOCTURNITE_ORE)
                .add(ModBlocks.DEEPSLATE_NOCTURNITE_ORE)
                .add(ModBlocks.NETHER_NOCTURNITE_ORE)
                .add(ModBlocks.END_NOCTURNITE_ORE)
                .add(ModBlocks.NOCTURNITE_BLOCK);
        //endregion
        //region Ore Tags
        getOrCreateTagBuilder(ConventionalBlockTags.ORES)
                .add(ModBlocks.AMBERINE_ORE)
                .add(ModBlocks.DEEPSLATE_AMBERINE_ORE)

                .add(ModBlocks.RUBY_ORE)
                .add(ModBlocks.DEEPSLATE_RUBY_ORE)

                .add(ModBlocks.AZUROS_ORE)
                .add(ModBlocks.DEEPSLATE_AZUROS_ORE)
                .add(ModBlocks.NETHER_AZUROS_ORE)
                .add(ModBlocks.END_AZUROS_ORE)

                .add(ModBlocks.CHROMITE_ORE)
                .add(ModBlocks.DEEPSLATE_CHROMITE_ORE)
                .add(ModBlocks.NETHER_CHROMITE_ORE)
                .add(ModBlocks.END_CHROMITE_ORE)

                .add(ModBlocks.INDIGRA_ORE)
                .add(ModBlocks.DEEPSLATE_INDIGRA_ORE)
                .add(ModBlocks.NETHER_INDIGRA_ORE)
                .add(ModBlocks.END_INDIGRA_ORE)

                .add(ModBlocks.MIRANITE_ORE)
                .add(ModBlocks.DEEPSLATE_MIRANITE_ORE)
                .add(ModBlocks.NETHER_MIRANITE_ORE)
                .add(ModBlocks.END_MIRANITE_ORE)

                .add(ModBlocks.NOCTURNITE_ORE)
                .add(ModBlocks.DEEPSLATE_NOCTURNITE_ORE)
                .add(ModBlocks.NETHER_NOCTURNITE_ORE)
                .add(ModBlocks.END_NOCTURNITE_ORE)

                .add(ModBlocks.XIRION_ORE)
                .add(ModBlocks.DEEPSLATE_XIRION_ORE)
                .add(ModBlocks.NETHER_XIRION_ORE)
                .add(ModBlocks.END_XIRION_ORE)

                .add(ModBlocks.ROSETTE_ORE)
                .add(ModBlocks.DEEPSLATE_ROSETTE_ORE)
                .add(ModBlocks.NETHER_ROSETTE_ORE)
                .add(ModBlocks.END_ROSETTE_ORE);

        getOrCreateTagBuilder(ConventionalBlockTags.CHESTS)
                .add(Blocks.BARREL);

        //endregion
    }
}
