package net.ryan.beyond_the_block.content.world.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class ModBiomeGeneration extends Region {

    public ModBiomeGeneration() {
        super(new Identifier(BeyondTheBlock.MOD_ID, "overworld"), RegionType.OVERWORLD, 2);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
        // Cherry Grove:
        // Mild / humid / inland / surface-ish forest-like climate
        addSurfaceBiome(mapper,
                ModBiomes.CHERRY_GROVE,
                MultiNoiseUtil.ParameterRange.of(0.1F, 0.6F),   // temperature
                MultiNoiseUtil.ParameterRange.of(0.2F, 0.8F),   // humidity
                MultiNoiseUtil.ParameterRange.of(-0.11F, 0.55F),// continentalness
                MultiNoiseUtil.ParameterRange.of(-0.78F, 0.05F),// erosion
                MultiNoiseUtil.ParameterRange.of(-0.35F, 0.35F) // weirdness
        );

        // Pale Garden:
        // Cooler / humid / inland / rougher dark-forest-like climate
        addSurfaceBiome(mapper,
                ModBiomes.PALE_GARDEN,
                MultiNoiseUtil.ParameterRange.of(-0.15F, 0.35F), // temperature
                MultiNoiseUtil.ParameterRange.of(0.35F, 1.0F),   // humidity
                MultiNoiseUtil.ParameterRange.of(-0.19F, 0.45F), // continentalness
                MultiNoiseUtil.ParameterRange.of(-0.78F, 0.1F),  // erosion
                MultiNoiseUtil.ParameterRange.of(-0.45F, 0.2F)   // weirdness
        );

    }

    private static void addSurfaceBiome(
            Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper,
            RegistryKey<Biome> biome,
            MultiNoiseUtil.ParameterRange temperature,
            MultiNoiseUtil.ParameterRange humidity,
            MultiNoiseUtil.ParameterRange continentalness,
            MultiNoiseUtil.ParameterRange erosion,
            MultiNoiseUtil.ParameterRange weirdness
    ) {
        mapper.accept(Pair.of(
                MultiNoiseUtil.createNoiseHypercube(
                        temperature,
                        humidity,
                        continentalness,
                        erosion,
                        MultiNoiseUtil.ParameterRange.of(0.0F), // depth
                        weirdness,
                        0.0F
                ),
                biome
        ));
    }
}