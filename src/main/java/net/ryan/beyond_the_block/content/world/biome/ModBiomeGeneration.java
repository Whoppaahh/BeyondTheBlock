package net.ryan.beyond_the_block.content.world.biome;

public class ModBiomeGeneration {

    public static void generateBiomes() {
        // Lower weight = rarer biome
        OverworldBiomes.addContinentalBiome(ModBiomes.CHERRY_GROVE, OverworldBiomes.ContinentalBiomePlacement.PEAK, 3.0D);
    }
}
