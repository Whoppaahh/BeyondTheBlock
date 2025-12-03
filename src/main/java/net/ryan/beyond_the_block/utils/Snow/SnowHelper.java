package net.ryan.beyond_the_block.utils.Snow;

import net.minecraft.block.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.ryan.beyond_the_block.BeyondTheBlock;

public class SnowHelper {

    // Main tag used for snow accumulation AND the debugger
    public static final TagKey<Block> SNOW_CAN_COVER =
            TagKey.of(Registry.BLOCK_KEY, new Identifier(BeyondTheBlock.MOD_ID, "snow_can_cover"));

    // Frequency + performance tuning
    private static final int TICK_INTERVAL = 20;
    private static final int MAX_RADIUS_CHUNKS = 6;
    private static final int NEAR_RADIUS = 2;
    private static final int MID_RADIUS = 4;

    private static final int ATTEMPTS_NEAR = 4;
    private static final int ATTEMPTS_MID  = 2;
    private static final int ATTEMPTS_FAR  = 1;

    private static final int MAX_PLACEMENTS_PER_TICK = 128;

    private static final int MAX_DOWNWARD_TAG_SCAN = 16;
    private static final int MAX_GROUND_SCAN_DEPTH = 20;

    private static int tickCounter = 0;

    // ------------------------------------------------------------------------
    // MAIN TICK LOOP
    // ------------------------------------------------------------------------
    public static void tick(ServerWorld world) {
        tickCounter++;
        if (tickCounter % TICK_INTERVAL != 0) return;
        if (!world.isRaining()) return;

        int placements = 0;

        outer:
        for (ServerPlayerEntity player : world.getPlayers()) {

            int pcx = player.getBlockPos().getX() >> 4;
            int pcz = player.getBlockPos().getZ() >> 4;

            for (int dx = -MAX_RADIUS_CHUNKS; dx <= MAX_RADIUS_CHUNKS; dx++) {
                for (int dz = -MAX_RADIUS_CHUNKS; dz <= MAX_RADIUS_CHUNKS; dz++) {

                    if (placements >= MAX_PLACEMENTS_PER_TICK) break outer;

                    Chunk rawChunk = world.getChunk(pcx + dx, pcz + dz, ChunkStatus.FULL, false);
                    if (!(rawChunk instanceof WorldChunk chunk)) continue;

                    int dist = Math.max(Math.abs(dx), Math.abs(dz));
                    int attempts = dist <= NEAR_RADIUS ? ATTEMPTS_NEAR :
                            dist <= MID_RADIUS  ? ATTEMPTS_MID  :
                                    ATTEMPTS_FAR;

                    for (int i = 0; i < attempts; i++) {
                        if (placements >= MAX_PLACEMENTS_PER_TICK) break outer;

                        int x = (chunk.getPos().x << 4) + world.random.nextInt(16);
                        int z = (chunk.getPos().z << 4) + world.random.nextInt(16);

                        BlockPos top = getTrueColumnTop(world, x, z);
                        if (top == null) continue;
                        if (!canSnowAt(world, top)) continue;

                        // NEW: leaf-skipping downward tag scan
                        BlockPos tagged = findTaggedBelow(world, top);
                        if (tagged != null) {
                            BlockPos ground = findRealGround(world, tagged.up());
                            if (ground != null) {
                                placeFullSnowBlock(world, ground);
                                placements++;
                            }
                            continue;
                        }

                        // fallback: snow layer in open areas
                        BlockState topState = world.getBlockState(top);
                        if (topState.isAir()) {
                            placeSnowLayer(world, top);
                            placements++;
                        }
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    // CAN SNOW AT POSITION
    // ------------------------------------------------------------------------
    public static boolean canSnowAt(ServerWorld world, BlockPos pos) {
        Biome biome = world.getBiome(pos).value();

        if (!world.isRaining()) return false;
        if (biome.getPrecipitation() == Biome.Precipitation.SNOW) return true;

        return biome.getTemperature() <= 0.15F;
    }

    // ------------------------------------------------------------------------
    // GET TRUE COLUMN TOP (no heightmap)
    // ------------------------------------------------------------------------
    public static BlockPos getTrueColumnTop(WorldAccess world, int x, int z) {
        BlockPos.Mutable m = new BlockPos.Mutable(x, world.getTopY(), z);

        while (m.getY() > world.getBottomY()) {
            if (!world.getBlockState(m).isAir()) {
                return m.toImmutable();
            }
            m.move(0, -1, 0);
        }
        return null;
    }

    // ------------------------------------------------------------------------
    // LEAF-SKIPPING DOWNWARD SCAN (this is the key fix)
    // ------------------------------------------------------------------------
    public static BlockPos findTaggedBelow(WorldAccess world, BlockPos from) {
        BlockPos.Mutable m = from.mutableCopy();

        for (int i = 0; i < MAX_DOWNWARD_TAG_SCAN; i++) {
            BlockState s = world.getBlockState(m);
            Material mat = s.getMaterial();

            // If this block is tagged → we found it
            if (s.isIn(SNOW_CAN_COVER)) {
                return m.toImmutable();
            }

            // Skip all vegetation forms
            if (mat == Material.PLANT ||
                    mat == Material.REPLACEABLE_PLANT ||
                    mat == Material.UNDERWATER_PLANT ||
                    mat == Material.REPLACEABLE_UNDERWATER_PLANT) {

                m.move(0, -1, 0);
                continue;
            }

            // Skip leaves entirely (leaf canopies)
            if (mat == Material.LEAVES) {
                m.move(0, -1, 0);
                continue;
            }

            // Skip snow layers & snow blocks
            if (s.isOf(Blocks.SNOW) || s.isOf(Blocks.SNOW_BLOCK)) {
                m.move(0, -1, 0);
                continue;
            }

            // Skip air
            if (s.isAir()) {
                m.move(0, -1, 0);
                continue;
            }

            // Hit solid, non-tag, non-skippable block → stop
            return null;
        }

        return null;
    }

    // ------------------------------------------------------------------------
    // FIND SOLID GROUND
    // ------------------------------------------------------------------------
    public static BlockPos findRealGround(WorldAccess world, BlockPos from) {
        BlockPos.Mutable m = from.mutableCopy();

        for (int i = 0; i < MAX_GROUND_SCAN_DEPTH; i++) {
            m.move(0, -1, 0);

            BlockState s = world.getBlockState(m);
            Material mat = s.getMaterial();

            if (mat == Material.PLANT ||
                    mat == Material.REPLACEABLE_PLANT ||
                    mat == Material.UNDERWATER_PLANT ||
                    mat == Material.REPLACEABLE_UNDERWATER_PLANT) continue;

            if (s.isAir()) continue;

            return m.toImmutable();
        }
        return null;
    }

    // ------------------------------------------------------------------------
    // BLOCK PLACEMENT (no neighbor updates)
    // ------------------------------------------------------------------------
    public static void placeFullSnowBlock(WorldAccess world, BlockPos pos) {
        world.setBlockState(pos, Blocks.SNOW_BLOCK.getDefaultState(), 2);
    }

    public static void placeSnowLayer(WorldAccess world, BlockPos pos) {
        BlockState s = world.getBlockState(pos);

        if (s.isAir() || s.getMaterial().isReplaceable()) {
            world.setBlockState(pos, Blocks.SNOW.getDefaultState(), 2);
        } else if (s.isOf(Blocks.SNOW)) {
            int layers = s.get(SnowBlock.LAYERS);
            if (layers < 8) {
                world.setBlockState(pos, s.with(SnowBlock.LAYERS, layers + 1), 2);
            }
        }
    }
}
