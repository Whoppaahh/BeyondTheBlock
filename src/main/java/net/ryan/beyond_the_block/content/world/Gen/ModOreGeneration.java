package net.ryan.beyond_the_block.content.world.Gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tag.BiomeTags;
import net.minecraft.world.gen.GenerationStep;
import net.ryan.beyond_the_block.content.world.Feature.ModPlacedFeatures;

public class ModOreGeneration {

    public static void generateModOres(){

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.XP_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_XP_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.END_XP_ORE_PLACED.getKey().get());

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.RUBY_ORE_PLACED.getKey().get());

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.MIRANITE_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_MIRANITE_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.END_MIRANITE_ORE_PLACED.getKey().get());

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.IS_MOUNTAIN)),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.CHROMITE_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_CHROMITE_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.END_CHROMITE_ORE_PLACED.getKey().get());

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.IS_FOREST).or(BiomeSelectors.tag(BiomeTags.IS_TAIGA))),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.NOCTURNITE_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_NOCTURNITE_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.END_NOCTURNITE_ORE_PLACED.getKey().get());

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.IS_SAVANNA).or(BiomeSelectors.tag(BiomeTags.IS_HILL))),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ROSETTE_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_ROSETTE_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.END_ROSETTE_ORE_PLACED.getKey().get());

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.IS_OCEAN).or(BiomeSelectors.tag(BiomeTags.IS_TAIGA))),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.AZUROS_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_AZUROS_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.END_AZUROS_ORE_PLACED.getKey().get());

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.INDIGRA_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_INDIGRA_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.END_INDIGRA_ORE_PLACED.getKey().get());

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.XIRION_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_XIRION_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.END_XIRION_ORE_PLACED.getKey().get());

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.IS_BADLANDS).or(BiomeSelectors.tag(BiomeTags.IS_BEACH))),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.AMBERINE_ORE_PLACED.getKey().get());
    }

}
