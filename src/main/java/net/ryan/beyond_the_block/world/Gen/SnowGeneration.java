package net.ryan.beyond_the_block.world.Gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.utils.Helpers.SnowHelper;

import static net.ryan.beyond_the_block.world.Feature.ModPlacedFeatures.SNOW_PLACED;

public class SnowGeneration extends Feature<DefaultFeatureConfig>{

    // Register the feature
    public static final Feature<DefaultFeatureConfig> SNOW_FEATURE = Registry.register(
            Registry.FEATURE,
            new Identifier(BeyondTheBlock.MOD_ID, "snow_under_blocks"),
            new SnowGeneration());


    public SnowGeneration() {
        super(DefaultFeatureConfig.CODEC);
    }

    public static void addToBiomes(){
        // Add the feature to snowy biomes only
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld().and(ctx ->
                        ctx.getBiome().getPrecipitation() == Biome.Precipitation.SNOW
                ),
                GenerationStep.Feature.TOP_LAYER_MODIFICATION,
                SNOW_PLACED.getKey().get()
        );
    }


    private static void placeSnowUnderTopBlock(World world, BlockPos topPos) {
        BlockPos pos = topPos.down(); // start below the top block

        while (pos.getY() > 0) {
            BlockState state = world.getBlockState(pos);

            // If this is a valid "ground" block, place snow **on top** of it
            if (state.isOf(Blocks.GRASS_BLOCK) || state.isOf(Blocks.DIRT) || state.isOf(Blocks.SAND)) {
                BlockPos snowPos = pos.up(); // place snow above the ground
                BlockState above = world.getBlockState(snowPos);

                // Only place snow if the space is empty or replaceable
                if (above.isAir() || above.getMaterial().isReplaceable()) {
                    if(world.getBlockState(snowPos.up()).isIn(SnowHelper.SNOW_CAN_COVER)) {
                        world.setBlockState(snowPos, Blocks.SNOW.getDefaultState(), 3);
                    }else{
                        world.setBlockState(snowPos.down(), Blocks.SNOW_BLOCK.getDefaultState(), 3);
                    }
                }
                break; // stop scanning — only one snow block per column
            }

            pos = pos.down(); // keep scanning down
        }
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        //BeyondTheBlock.LOGGER.info("SnowGeneration feature triggered at {}", context.getOrigin());

        WorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();

        int x = origin.getX();
        int z = origin.getZ();

        for (int xPos = 0; xPos < 16; xPos++) {
            for(int zPos = 0; zPos < 16; zPos++) {
                int realX = x + xPos;
                int realZ = z + zPos;
                int realY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, realX, realZ);
                BlockPos pos = new BlockPos(realX, realY, realZ);


                while (pos.getY() > 0) {
                    BlockState state = world.getBlockState(pos);

                    if (state.isOf(Blocks.GRASS_BLOCK) || state.isOf(Blocks.DIRT) || state.isOf(Blocks.SAND)) {
                        // call your helper
                        if (world instanceof World serverWorld) {
                            placeSnowUnderTopBlock(serverWorld, pos);
                        }
                        break;
                    }

                    pos = pos.down();
                }
            }
        }

        return true;
    }
}

