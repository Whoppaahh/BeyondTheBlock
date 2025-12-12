package net.ryan.beyond_the_block.utils.Helpers;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.ryan.beyond_the_block.config.ModConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PathToolHelper {

    public static final String TAG_PATH_START = "PathStart";
    public static final String TAG_WIDTH = "PathWidth";

    private PathToolHelper() {}

    // --- NBT handling ---

    public static boolean hasStart(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.contains(TAG_PATH_START, NbtElement.COMPOUND_TYPE);
    }

    public static int getWidth(ItemStack stack, ModConfig config) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!nbt.contains(TAG_WIDTH)) {
            nbt.putInt(TAG_WIDTH, 1); // default
        }

        int width = nbt.getInt(TAG_WIDTH);

        // Clamp to config min/max
        width = Math.max(config.pathConfig.minWidth, Math.min(config.pathConfig.maxWidth, width));

        return width;
    }

    public static void setWidth(ItemStack stack, int width) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).get();
        width = Math.max(config.pathConfig.minWidth, Math.min(config.pathConfig.maxWidth, width));

        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(TAG_WIDTH, width);
    }

    public static void setStart(ItemStack stack, BlockPos pos) {
        NbtCompound nbt = stack.getOrCreateNbt();
        NbtCompound tag = new NbtCompound();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        nbt.put(TAG_PATH_START, tag);
    }

    public static BlockPos getStart(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        NbtCompound tag = nbt.getCompound(TAG_PATH_START);
        return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }

    public static void clearStart(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.remove(TAG_PATH_START);
    }

    // --- Config resolution helpers ---

    public static Set<Block> resolveBlockList(List<String> ids) {
        Set<Block> set = new HashSet<>();
        for (String s : ids) {
            Identifier id = Identifier.tryParse(s);
            if (id == null) continue;
            Block block = Registry.BLOCK.get(id);
            if (block != Blocks.AIR) {
                set.add(block);
            }
        }
        return set;
    }

    public static BlockState resolveDefaultPathBlock(ModConfig config) {
        Identifier id = Identifier.tryParse(config.pathConfig.defaultPathBlockId);
        Block block = (id != null) ? Registry.BLOCK.get(id) : Blocks.DIRT_PATH;
        if (block == Blocks.AIR) block = Blocks.DIRT_PATH;
        return block.getDefaultState();
    }

    // --- Path line computation ---

    public static List<BlockPos> computeLine2D(BlockPos start, BlockPos end) {
        List<BlockPos> result = new ArrayList<>();

        int x1 = start.getX();
        int z1 = start.getZ();
        int x2 = end.getX();
        int z2 = end.getZ();

        int dx = Math.abs(x2 - x1);
        int dz = Math.abs(z2 - z1);

        int sx = x1 < x2 ? 1 : -1;
        int sz = z1 < z2 ? 1 : -1;

        int err = dx - dz;

        while (true) {
            result.add(new BlockPos(x1, start.getY(), z1));
            if (x1 == x2 && z1 == z2) break;

            int e2 = 2 * err;
            if (e2 > -dz) {
                err -= dz;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                z1 += sz;
            }
        }

        return result;
    }

    // Expand a central line into a width-wide stripe
    public static List<BlockPos> widenLine(List<BlockPos> centerLine, int width, Direction primaryDirection) {
        if (width <= 1) return centerLine;

        List<BlockPos> out = new ArrayList<>();
        int half = width / 2;

        // Perpendicular directions in XZ plane
        Direction left;
        Direction right;

        if (primaryDirection == Direction.NORTH || primaryDirection == Direction.SOUTH) {
            left = Direction.EAST;
            right = Direction.WEST;
        } else {
            left = Direction.NORTH;
            right = Direction.SOUTH;
        }

        for (BlockPos center : centerLine) {
            out.add(center);
            for (int i = 1; i <= half; i++) {
                out.add(center.offset(left, i));
                out.add(center.offset(right, i));
            }
        }

        return out;
    }

    // Determine primary direction from start to end (used for width spreading)
    public static Direction getPrimaryDirection(BlockPos start, BlockPos end) {
        int dx = end.getX() - start.getX();
        int dz = end.getZ() - start.getZ();
        if (Math.abs(dx) >= Math.abs(dz)) {
            return dx >= 0 ? Direction.EAST : Direction.WEST;
        } else {
            return dz >= 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    // --- Terrain following ---

    public static BlockPos adjustToTerrain(World world, BlockPos pos, boolean useTerrainFollowing) {
        if (!useTerrainFollowing) {
            return pos;
        }

        int topY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) - 1;
        if (topY < world.getBottomY()) {
            return pos;
        }
        return new BlockPos(pos.getX(), topY, pos.getZ());
    }

    // --- Path material resolution ---

    public static BlockState resolvePathBlockFor(World world, PlayerEntity player, BlockPos pos, ModConfig config) {
        // 1. Offhand block has priority
        ItemStack offhand = player.getOffHandStack();
        if (offhand.getItem() instanceof BlockItem blockItem) {
            return blockItem.getBlock().getDefaultState();
        }

        // 2. Biome-based
        Biome biome = world.getBiome(pos).value();
        // You can refine these heuristics as you like
        Biome.Precipitation precipitation = biome.getPrecipitation();
        if (precipitation == Biome.Precipitation.SNOW) {
            return Blocks.SNOW.getDefaultState();
        }

        // A simple category-ish heuristic: "dry" biomes -> sandstone-ish
        // If you want, check RegistryKey or tags for more precise logic.
        // Here we just approximate based on temperature.
        float temperature = biome.getTemperature();
        if (temperature >= 1.5f) {
            return Blocks.SANDSTONE.getDefaultState();
        }

        // 3. Fallback default from config
        return resolveDefaultPathBlock(config);
    }

    // --- Distance constraint ---

    public static boolean withinMaxDistance(BlockPos start, BlockPos end, int maxDist) {
        int dx = start.getX() - end.getX();
        int dz = start.getZ() - end.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        return dist <= maxDist;
    }
}

