package net.ryan.beyond_the_block.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.datagen.common.ModDatagenFamilies;

import java.util.List;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateTags() {
        generateMiningTags();
        generateFamilyMiningTags();
        generateToolRequirementTags();
        generateOreTags();
        generateMiscTags();
    }

    private void generateMiningTags() {
        FabricTagProvider<Block>.FabricTagBuilder<Block> builder = getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE);

        addAll(builder, allOreBlocks());
        addAll(builder, allStorageBlocks());
        addAll(builder, allRawStorageBlocks());
    }

    private void generateFamilyMiningTags() {
        FabricTagProvider<Block>.FabricTagBuilder<Block> axeMineable = getOrCreateTagBuilder(BlockTags.AXE_MINEABLE);
        addAll(axeMineable, ModDatagenFamilies.SHELVES);
        axeMineable.add(ModBlocks.CHISELED_BOOKSHELF);

        FabricTagProvider<Block>.FabricTagBuilder<Block> hoeMineable = getOrCreateTagBuilder(BlockTags.HOE_MINEABLE);
        addAll(hoeMineable, ModDatagenFamilies.SPONGES);

        FabricTagProvider<Block>.FabricTagBuilder<Block> pickaxeMineable = getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE);
        addAll(pickaxeMineable, ModDatagenFamilies.SHRINE_BLOCKS);
        addAll(pickaxeMineable, ModDatagenFamilies.SIMPLE_UTILITY_BLOCKS);
        addAll(pickaxeMineable, ModDatagenFamilies.MACHINE_STYLE_BLOCKS);
        addAll(pickaxeMineable, ModDatagenFamilies.RAIL_STYLE_BLOCKS);
    }

    private void generateToolRequirementTags() {
        FabricTagProvider<Block>.FabricTagBuilder<Block> stoneTool = getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL);
        addAll(stoneTool, stoneTierBlocks());

        FabricTagProvider<Block>.FabricTagBuilder<Block> ironTool = getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL);
        addAll(ironTool, ironTierBlocks());

        FabricTagProvider<Block>.FabricTagBuilder<Block> diamondTool = getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL);
        addAll(diamondTool, diamondTierBlocks());
    }

    private void generateOreTags() {
        FabricTagProvider<Block>.FabricTagBuilder<Block> ores = getOrCreateTagBuilder(ConventionalBlockTags.ORES);
        addAll(ores, allOreBlocks());
    }

    private void generateMiscTags() {
        getOrCreateTagBuilder(ConventionalBlockTags.CHESTS)
                .add(Blocks.BARREL);
    }

    private <T> void addAll(FabricTagBuilder<T> builder, Iterable<T> entries) {
        for (T entry : entries) {
            builder.add(entry);
        }
    }

    private List<Block> allOreBlocks() {
        return List.of(
                ModBlocks.RUBY_ORE,
                ModBlocks.DEEPSLATE_RUBY_ORE,

                ModBlocks.MIRANITE_ORE,
                ModBlocks.DEEPSLATE_MIRANITE_ORE,
                ModBlocks.NETHER_MIRANITE_ORE,
                ModBlocks.END_MIRANITE_ORE,

                ModBlocks.CHROMITE_ORE,
                ModBlocks.DEEPSLATE_CHROMITE_ORE,
                ModBlocks.NETHER_CHROMITE_ORE,
                ModBlocks.END_CHROMITE_ORE,

                ModBlocks.NOCTURNITE_ORE,
                ModBlocks.DEEPSLATE_NOCTURNITE_ORE,
                ModBlocks.NETHER_NOCTURNITE_ORE,
                ModBlocks.END_NOCTURNITE_ORE,

                ModBlocks.AMBERINE_ORE,
                ModBlocks.DEEPSLATE_AMBERINE_ORE,

                ModBlocks.ROSETTE_ORE,
                ModBlocks.DEEPSLATE_ROSETTE_ORE,
                ModBlocks.NETHER_ROSETTE_ORE,
                ModBlocks.END_ROSETTE_ORE,

                ModBlocks.AZUROS_ORE,
                ModBlocks.DEEPSLATE_AZUROS_ORE,
                ModBlocks.NETHER_AZUROS_ORE,
                ModBlocks.END_AZUROS_ORE,

                ModBlocks.INDIGRA_ORE,
                ModBlocks.DEEPSLATE_INDIGRA_ORE,
                ModBlocks.NETHER_INDIGRA_ORE,
                ModBlocks.END_INDIGRA_ORE,

                ModBlocks.XIRION_ORE,
                ModBlocks.DEEPSLATE_XIRION_ORE,
                ModBlocks.NETHER_XIRION_ORE,
                ModBlocks.END_XIRION_ORE
        );
    }

    private List<Block> allStorageBlocks() {
        return List.of(
                ModBlocks.RUBY_BLOCK,
                ModBlocks.MIRANITE_BLOCK,
                ModBlocks.CHROMITE_BLOCK,
                ModBlocks.NOCTURNITE_BLOCK,
                ModBlocks.AMBERINE_BLOCK,
                ModBlocks.ROSETTE_BLOCK,
                ModBlocks.AZUROS_BLOCK,
                ModBlocks.INDIGRA_BLOCK,
                ModBlocks.XIRION_BLOCK
        );
    }

    private List<Block> allRawStorageBlocks() {
        return List.of(
                ModBlocks.RAW_MIRANITE_BLOCK,
                ModBlocks.RAW_ROSETTE_BLOCK,
                ModBlocks.RAW_AZUROS_BLOCK
        );
    }

    private List<Block> stoneTierBlocks() {
        return List.of(
                ModBlocks.RUBY_BLOCK,
                ModBlocks.AMBERINE_ORE,
                ModBlocks.DEEPSLATE_AMBERINE_ORE,
                ModBlocks.AMBERINE_BLOCK
        );
    }

    private List<Block> ironTierBlocks() {
        return List.of(
                ModBlocks.RUBY_ORE,

                ModBlocks.AZUROS_ORE,
                ModBlocks.DEEPSLATE_AZUROS_ORE,
                ModBlocks.NETHER_AZUROS_ORE,
                ModBlocks.END_AZUROS_ORE,
                ModBlocks.AZUROS_BLOCK,
                ModBlocks.RAW_AZUROS_BLOCK,

                ModBlocks.XIRION_ORE,
                ModBlocks.DEEPSLATE_XIRION_ORE,
                ModBlocks.NETHER_XIRION_ORE,
                ModBlocks.END_XIRION_ORE,
                ModBlocks.XIRION_BLOCK,

                ModBlocks.INDIGRA_ORE,
                ModBlocks.DEEPSLATE_INDIGRA_ORE,
                ModBlocks.NETHER_INDIGRA_ORE,
                ModBlocks.END_INDIGRA_ORE,
                ModBlocks.INDIGRA_BLOCK,

                ModBlocks.ROSETTE_ORE,
                ModBlocks.DEEPSLATE_ROSETTE_ORE,
                ModBlocks.NETHER_ROSETTE_ORE,
                ModBlocks.END_ROSETTE_ORE,
                ModBlocks.ROSETTE_BLOCK,
                ModBlocks.RAW_ROSETTE_BLOCK,

                ModBlocks.MIRANITE_ORE,
                ModBlocks.DEEPSLATE_MIRANITE_ORE,
                ModBlocks.NETHER_MIRANITE_ORE,
                ModBlocks.END_MIRANITE_ORE,
                ModBlocks.MIRANITE_BLOCK,
                ModBlocks.RAW_MIRANITE_BLOCK,

                ModBlocks.CHROMITE_ORE,
                ModBlocks.DEEPSLATE_CHROMITE_ORE,
                ModBlocks.NETHER_CHROMITE_ORE,
                ModBlocks.END_CHROMITE_ORE,
                ModBlocks.CHROMITE_BLOCK
        );
    }

    private List<Block> diamondTierBlocks() {
        return List.of(
                ModBlocks.DEEPSLATE_RUBY_ORE,

                ModBlocks.NOCTURNITE_ORE,
                ModBlocks.DEEPSLATE_NOCTURNITE_ORE,
                ModBlocks.NETHER_NOCTURNITE_ORE,
                ModBlocks.END_NOCTURNITE_ORE,
                ModBlocks.NOCTURNITE_BLOCK
        );
    }
}