package net.ryan.beyond_the_block.feature.paths;

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
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.config.schema.ConfigServer;
import net.ryan.beyond_the_block.config.sync.SyncedServerConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

    public static int getWidth(ItemStack stack, ConfigServer config) {
        return getWidth(stack, config.features.paths.minWidth, config.features.paths.maxWidth);
    }

    public static int getWidth(ItemStack stack, int minWidth, int maxWidth) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!nbt.contains(TAG_WIDTH)) {
            nbt.putInt(TAG_WIDTH, 1);
        }

        int width = nbt.getInt(TAG_WIDTH);
        return Math.max(minWidth, Math.min(maxWidth, width));
    }

    public static void setWidth(ItemStack stack, int width) {
        width = Math.max(Configs.server().features.paths.minWidth, Math.min(Configs.server().features.paths.maxWidth, width));

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

    public static BlockState resolveDefaultPathBlock(ConfigServer config) {
        Identifier id = Identifier.tryParse(config.features.paths.defaultPathBlockId);
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

    public static List<BlockPos> widenLine(List<BlockPos> centerLine, int width, Direction primaryDirection) {
        if (width <= 1) return centerLine;

        List<BlockPos> out = new ArrayList<>();

        int leftCount = (width - 1) / 2;
        int rightCount = width / 2;

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

            for (int i = 1; i <= leftCount; i++) {
                out.add(center.offset(left, i));
            }

            for (int i = 1; i <= rightCount; i++) {
                out.add(center.offset(right, i));
            }
        }

        return out;
    }

    public static Direction getPrimaryDirection(BlockPos start, BlockPos end) {
        int dx = end.getX() - start.getX();
        int dz = end.getZ() - start.getZ();
        if (Math.abs(dx) >= Math.abs(dz)) {
            return dx >= 0 ? Direction.EAST : Direction.WEST;
        } else {
            return dz >= 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    // --- Shared affected-position generation ---

    public static List<BlockPos> computeAffectedPositions(
            World world,
            BlockPos start,
            BlockPos end,
            int width,
            boolean useTerrainFollowing,
            Set<Block> allowedStart,
            Set<Block> allowedEnd
    ) {
        BlockPos adjustedStart = adjustToTerrain(world, start, useTerrainFollowing);
        BlockPos adjustedEnd = adjustToTerrain(world, end, useTerrainFollowing);

        if (allowedStart != null && !allowedStart.isEmpty()) {
            if (!allowedStart.contains(world.getBlockState(adjustedStart).getBlock())) {
                return List.of();
            }
        }

        if (allowedEnd != null && !allowedEnd.isEmpty()) {
            if (!allowedEnd.contains(world.getBlockState(adjustedEnd).getBlock())) {
                return List.of();
            }
        }

        List<BlockPos> centerLine = computeLine2D(start, end);
        Direction direction = getPrimaryDirection(start, end);
        List<BlockPos> widened = widenLine(centerLine, width, direction);

        LinkedHashSet<BlockPos> adjusted = new LinkedHashSet<>();
        for (BlockPos pos : widened) {
            BlockPos adjustedPos = adjustToTerrain(world, pos, useTerrainFollowing).toImmutable();

            if (allowedEnd != null && !allowedEnd.isEmpty()) {
                Block currentBlock = world.getBlockState(adjustedPos).getBlock();
                if (!allowedEnd.contains(currentBlock)) {
                    continue;
                }
            }

            adjusted.add(adjustedPos);
        }

        return new ArrayList<>(adjusted);
    }

    public static List<BlockPos> computeAffectedPositions(
            World world,
            BlockPos start,
            BlockPos end,
            int width,
            boolean useTerrainFollowing
    ) {
        return computeAffectedPositions(world, start, end, width, useTerrainFollowing, null, null);
    }

    public static List<BlockPos> computeAffectedPositionsForPreview(
            World world,
            ItemStack stack,
            BlockPos start,
            BlockPos end,
            SyncedServerConfig syncedConfig
    ) {
        int width = getWidth(stack, syncedConfig.pathsMinWidth(), syncedConfig.pathsMaxWidth());

        Set<Block> allowedStart = resolveBlockList(syncedConfig.pathsAllowedStartingBlocks());
        Set<Block> allowedEnd = resolveBlockList(syncedConfig.pathsAllowedEndingBlocks());

        return computeAffectedPositions(
                world,
                start,
                end,
                width,
                syncedConfig.pathsUseTerrainFollowing(),
                allowedStart,
                allowedEnd
        );
    }

    // --- Terrain following ---

    public static BlockPos adjustToTerrain(World world, BlockPos pos, boolean useTerrainFollowing) {
        if (!useTerrainFollowing) {
            return pos;
        }

        int minY = world.getBottomY();
        int maxY = world.getTopY() - 1;

        BlockPos.Mutable cursor = pos.mutableCopy();

        // Clamp Y into world bounds
        if (cursor.getY() < minY) cursor.setY(minY);
        if (cursor.getY() > maxY) cursor.setY(maxY);

        // If already valid surface, use it
        if (isWalkableSurface(world, cursor)) {
            return cursor.toImmutable();
        }

        // Search nearby (down first, then up)
        for (int offset = 1; offset <= 8; offset++) {

            int downY = cursor.getY() - offset;
            if (downY >= minY) {
                BlockPos.Mutable down = new BlockPos.Mutable(cursor.getX(), downY, cursor.getZ());
                if (isWalkableSurface(world, down)) {
                    return down.toImmutable();
                }
            }

            int upY = cursor.getY() + offset;
            if (upY <= maxY) {
                BlockPos.Mutable up = new BlockPos.Mutable(cursor.getX(), upY, cursor.getZ());
                if (isWalkableSurface(world, up)) {
                    return up.toImmutable();
                }
            }
        }

        // Fallback (only if nothing found nearby)
        int fallbackTopY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) - 1;
        return new BlockPos(pos.getX(), fallbackTopY, pos.getZ());
    }

    private static boolean isWalkableSurface(World world, BlockPos pos) {
        BlockState floor = world.getBlockState(pos);

        // Must be solid ground
        if (floor.isAir() || !floor.getMaterial().isSolid()) {
            return false;
        }

        // Space above must be clear
        BlockPos above = pos.up();
        if (!world.getBlockState(above).getCollisionShape(world, above).isEmpty()) {
            return false;
        }

        // Headroom (prevents clipping into ceilings)
        BlockPos above2 = above.up();
        return world.getBlockState(above2).getCollisionShape(world, above2).isEmpty();
    }

    // --- Path material resolution ---

    public static BlockState resolvePathBlockFor(World world, PlayerEntity player, BlockPos pos, ConfigServer config) {
        ItemStack offhand = player.getOffHandStack();
        if (offhand.getItem() instanceof BlockItem blockItem) {
            return blockItem.getBlock().getDefaultState();
        }

        Biome biome = world.getBiome(pos).value();
        Biome.Precipitation precipitation = biome.getPrecipitation();
        if (precipitation == Biome.Precipitation.SNOW) {
            return Blocks.SNOW.getDefaultState();
        }

        float temperature = biome.getTemperature();
        if (temperature >= 1.5f) {
            return Blocks.SANDSTONE.getDefaultState();
        }

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