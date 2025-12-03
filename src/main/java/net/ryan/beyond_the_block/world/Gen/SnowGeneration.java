package net.ryan.beyond_the_block.world.Gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.utils.Snow.SnowHelper;

import static net.ryan.beyond_the_block.world.Feature.ModPlacedFeatures.SNOW_PLACED;

public class SnowGeneration extends Feature<DefaultFeatureConfig> {

    public static final Feature<DefaultFeatureConfig> SNOW_FEATURE = Registry.register(
            Registry.FEATURE,
            new Identifier(BeyondTheBlock.MOD_ID, "snow_under_blocks"),
            new SnowGeneration()
    );

    public SnowGeneration() {
        super(DefaultFeatureConfig.CODEC);
    }

    public static void addToBiomes() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld().and(ctx ->
                        ctx.getBiome().getPrecipitation() == net.minecraft.world.biome.Biome.Precipitation.SNOW
                ),
                GenerationStep.Feature.TOP_LAYER_MODIFICATION,
                SNOW_PLACED.getKey().get()
        );
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx) {
        WorldAccess world = ctx.getWorld();
        BlockPos origin = ctx.getOrigin();

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {

                int x = origin.getX() + dx;
                int z = origin.getZ() + dz;

                BlockPos top = SnowHelper.getTrueColumnTop(world, x, z);
                if (top == null) continue;

                // Leaf-skipping downward scan
                BlockPos tagged = SnowHelper.findTaggedBelow(world, top);
                if (tagged != null) {
                    BlockPos ground = SnowHelper.findRealGround(world, tagged.up());
                    if (ground != null) {
                        SnowHelper.placeFullSnowBlock(world, ground);
                    }
                    continue;
                }

                // fallback: snow layer
                BlockState s = world.getBlockState(top);
                if (s.isAir()) {
                    world.setBlockState(top, Blocks.SNOW.getDefaultState(), 2);
                }
            }
        }

        return true;
    }
}
