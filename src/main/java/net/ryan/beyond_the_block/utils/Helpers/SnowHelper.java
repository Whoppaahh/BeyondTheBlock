package net.ryan.beyond_the_block.utils.Helpers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.ryan.beyond_the_block.BeyondTheBlock;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;

public class SnowHelper {
    public static final TagKey<Block> SNOW_CAN_COVER =
            TagKey.of(Registry.BLOCK_KEY, new Identifier(BeyondTheBlock.MOD_ID, "snow_can_cover"));

    /**
     * Snow accumulation during snowfall on empty/replaceable blocks
     */

    // Vanilla-style temperature noise (similar to Biome.TEMPERATURE_NOISE)
    private static final OctaveSimplexNoiseSampler TEMPERATURE_NOISE =
            new OctaveSimplexNoiseSampler(new ChunkRandom(new CheckedRandom(1234L)), singletonList(0));

    // Per-chunk bitmap: chunk -> 16x16 X,Z columns
    private static final Map<ChunkPos, short[]> CHUNK_SNOW_MASK = new HashMap<>();


    public static boolean canSnowAt(World world, BlockPos pos) {
        Biome biome = world.getBiome(pos).value();
        float temperature = biome.getTemperature();

        // Approximate altitude & noise adjustments like vanilla
        if (pos.getY() > 80) {
            float noise = (float) (TEMPERATURE_NOISE.sample(pos.getX() / 8.0, pos.getZ() / 8.0, false) * 8.0);
            temperature -= (noise + pos.getY() - 80) * 0.05F / 40.0F;
        }
        // Vanilla-style snow check
        return biome.getPrecipitation() == Biome.Precipitation.SNOW || temperature <= 0.15F && world.isRaining();
    }


    public static void accumulateSnowOnGround(World world) {
        if (world.isClient) return;

        for (int i = 0; i < 16; i++) {
            BlockPos pos = world.getRandomPosInChunk(0, world.getTopY(), 0, 0);

            // Only accumulate if snow can form at this position
            if (!canSnowAt(world, pos)) continue;

            BlockState state = world.getBlockState(pos);

            if (state.isAir() || state.getMaterial().isReplaceable()) {
                BlockPos below = pos.down();
                BlockState ground = world.getBlockState(below);

                if (!ground.isAir()) {
                    world.setBlockState(pos, Blocks.SNOW.getDefaultState(), 3);
                }
            } else if (state.isOf(Blocks.SNOW)) {
                int layers = state.get(SnowBlock.LAYERS);
                if (layers < 8) {
                    world.setBlockState(pos, state.with(SnowBlock.LAYERS, layers + 1), 3);
                }
            }
        }
    }


    public static void tickSnow(ServerWorld world) {
        accumulateSnowOnGround(world);
        int radius = 8; // around each player
        Random random = world.random;

        for (ServerPlayerEntity player : world.getPlayers()) {
            int playerChunkX = player.getBlockPos().getX() >> 4;
            int playerChunkZ = player.getBlockPos().getZ() >> 4;


            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int chunkX = playerChunkX + dx;
                    int chunkZ = playerChunkZ + dz;
                    var chunk = world.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
                    if (chunk == null) continue;

                    ChunkPos cPos = new ChunkPos(chunkX, chunkZ);
                    short[] mask = CHUNK_SNOW_MASK.computeIfAbsent(cPos, k -> new short[16]); // 16 rows, each short = 16 bits

                    // Pick a few random positions in the chunk
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 5; j++) { // pick a few random positions per chunk
                            int xInChunk = random.nextInt(16);
                            int zInChunk = random.nextInt(16);

                            // Check mask for duplicates
                            if ((mask[zInChunk] & (1 << xInChunk)) != 0) continue; // already used
                            mask[zInChunk] |= (short) (1 << xInChunk); // mark as used

                            int worldX = (chunkX << 4) + xInChunk;
                            int worldZ = (chunkZ << 4) + zInChunk;
                            int y = world.getTopY();
                            BlockPos pos = new BlockPos(worldX, y, worldZ);

                            while (pos.getY() > 0) {
                                BlockState above = world.getBlockState(pos);
                                // if (above.isOf(Blocks.SNOW) || above.isOf(Blocks.SNOW_BLOCK)) break;

                                if (canSnowAt(world, pos) && above.isIn(SnowHelper.SNOW_CAN_COVER)) {
                                    placeSnowUnderTopBlock(world, pos);
                                    break; // move to next random position
                                }

                                pos = pos.down();
                            }
                        }
                    }
                }
            }
        }
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
                    if (world.getBlockState(snowPos.up()).isIn(SNOW_CAN_COVER)) {
                        world.setBlockState(snowPos, Blocks.SNOW.getDefaultState(), 3);
                        BeyondTheBlock.LOGGER.info("Placing Snow: " + snowPos);
                    } else {
                        world.setBlockState(snowPos.down(), Blocks.SNOW_BLOCK.getDefaultState(), 3);
                        BeyondTheBlock.LOGGER.info("Placing Snow Block: " + snowPos.down());
                    }
                }
                break; // stop scanning — only one snow block per column
            }

            pos = pos.down(); // keep scanning down
        }
    }


    public static void clearBitMark(ServerWorld serverWorld, WorldChunk worldChunk) {
        CHUNK_SNOW_MASK.clear();
    }
}
