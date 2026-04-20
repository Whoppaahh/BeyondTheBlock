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
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
            builder.replaceBiome(BiomeKeys.FOREST, ModBiomes.CHERRY_GROVE);
            builder.replaceBiome(BiomeKeys.DARK_FOREST, ModBiomes.PALE_GARDEN);
        });

    }
}