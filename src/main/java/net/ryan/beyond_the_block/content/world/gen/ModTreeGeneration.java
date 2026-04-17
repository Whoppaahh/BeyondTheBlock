package net.ryan.beyond_the_block.content.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tag.BiomeTags;
import net.minecraft.world.gen.GenerationStep;
import net.ryan.beyond_the_block.content.world.feature.ModPlacedFeatures;

public class ModTreeGeneration {

    public static void generateModTrees() {

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.IS_FOREST)),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.CHERRY_TREE_PLACED.getKey().get()
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.IS_TAIGA).or(BiomeSelectors.tag(BiomeTags.IS_FOREST))),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.PALE_OAK_TREE_PLACED.getKey().get()
        );
    }
}