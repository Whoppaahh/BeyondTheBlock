package net.ryan.beyond_the_block.enchantment;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.config.DropMode;
import net.ryan.beyond_the_block.config.ModConfig;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.ryan.beyond_the_block.utils.Helpers.ItemStackHelper.mergeItemStacks;

public class MyEnchantmentHelper {
    private static final int BASE_LOG_LIMIT = 50;

    // Handle instant mining effect
    public static void handleInstantMining(PlayerEntity player, ItemStack tool, BlockPos pos, World world, BlockState blockState) {
        if (world.isClient) return;
        int level = net.minecraft.enchantment.EnchantmentHelper.getLevel(ModEnchantments.STONE_BREAKER, tool);
        // Check if the tool has the Instant Mining enchantment
        if (level <= 0) return;

        boolean canBreak = blockState.isIn(BlockTags.BASE_STONE_OVERWORLD) ||
                (level >= 2 && blockState.isIn(ConventionalBlockTags.ORES)) ||
                (level >= 3 && blockState.isOf(Blocks.OBSIDIAN));

        if (canBreak) {
            // Get drops from the block (respects Fortune)
            LootContext.Builder lootContextBuilder = new LootContext.Builder((ServerWorld) world)
                    .parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                    .parameter(LootContextParameters.TOOL, tool)
                    .parameter(LootContextParameters.BLOCK_STATE, blockState)
                    .parameter(LootContextParameters.THIS_ENTITY, player);

            List<ItemStack> drops = blockState.getBlock().getDroppedStacks(blockState, lootContextBuilder);

            // If Fortune is present, multiply drop counts based on Fortune level
            int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, tool);
            for (ItemStack drop : drops) {
                // Increase drop count based on Fortune level
                int adjustedCount = drop.getCount() + fortuneLevel;
                drop.setCount(adjustedCount > 0 ? adjustedCount : drop.getCount()); // Ensure count doesn't go negative
                Block.dropStack(world, pos, drop); // Drop the modified stack
            }

            // Cooldown, break particles, and actual block breaking
            player.getItemCooldownManager().set(tool.getItem(), 1);
            world.syncWorldEvent(2001, pos, Block.getRawIdFromState(blockState)); // Break particles
            world.breakBlock(pos, false, player); // No need for true, since we're already handling drops
            BeyondTheBlock.LOGGER.info("Handling Instant Mining at {} with level {}", pos, level);
        }
    }


    // Handle tree breaking effect (breaking all logs in the tree)
    public static void handleTreeBreaking(PlayerEntity player, ItemStack tool, BlockPos pos, World world) {
        int level = net.minecraft.enchantment.EnchantmentHelper.getLevel(ModEnchantments.TIMBER_CUT, tool);
        if (level <= 0) return;
        BlockState blockState = world.getBlockState(pos);
        if (isLog(blockState)) {
            // Recursively break all logs in the tree
            breakTree(world, pos, player, level);
            BeyondTheBlock.LOGGER.info("Handling Tree Breaking at {}", pos);
        } else {
            BeyondTheBlock.LOGGER.info("Not a log: {}", blockState.toString()); // Debugging log
        }

    }
    // Handle tree breaking effect (breaking all logs in the tree)
    public static void handleTreeStripping(PlayerEntity player, ItemStack tool, BlockPos pos, World world) {
        int level = net.minecraft.enchantment.EnchantmentHelper.getLevel(ModEnchantments.BARKSKIN, tool);
        if (level <= 0) return;
        BlockState blockState = world.getBlockState(pos);
        if (isLog(blockState)) {
            // Recursively break all logs in the tree
            stripTree(world, pos, player, level);
            BeyondTheBlock.LOGGER.info("Handling Tree Stripping at {}", pos);
        } else {
            BeyondTheBlock.LOGGER.info("Not a log: {}", blockState.toString()); // Debugging log
        }

    }

    private static void stripTree(World world, BlockPos origin, PlayerEntity player, int level) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> toCheck = new LinkedList<>();
        int maxLogs = BASE_LOG_LIMIT * level;
        toCheck.add(origin);

        while (!toCheck.isEmpty() && visited.size() < maxLogs) {
            BlockPos current = toCheck.poll();
            if (!visited.add(current)) continue;

            BlockState state = world.getBlockState(current);
            BlockState stripped = getStrippedState(state);
            if (isLog(state)) {
             world.setBlockState(current, stripped, Block.NOTIFY_ALL);

                for (BlockPos offset : BlockPos.iterate(current.add(-level, -level, -level), current.add(level, level, level))) {
                    if (!offset.equals(current) && !visited.contains(offset) && isLog(world.getBlockState(offset))) {
                        toCheck.add(offset.toImmutable());
                    }
                }

            }
        }
    }
    private static @Nullable BlockState getStrippedState(BlockState original) {
        Block block = original.getBlock();

        if (block == Blocks.OAK_LOG)
            return Blocks.STRIPPED_OAK_LOG.getDefaultState().with(net.minecraft.state.property.Properties.AXIS, original.get(net.minecraft.state.property.Properties.AXIS));
        if (block == Blocks.SPRUCE_LOG)
            return Blocks.STRIPPED_SPRUCE_LOG.getDefaultState().with(net.minecraft.state.property.Properties.AXIS, original.get(net.minecraft.state.property.Properties.AXIS));
        if (block == Blocks.BIRCH_LOG)
            return Blocks.STRIPPED_BIRCH_LOG.getDefaultState().with(net.minecraft.state.property.Properties.AXIS, original.get(net.minecraft.state.property.Properties.AXIS));
        if (block == Blocks.JUNGLE_LOG)
            return Blocks.STRIPPED_JUNGLE_LOG.getDefaultState().with(net.minecraft.state.property.Properties.AXIS, original.get(net.minecraft.state.property.Properties.AXIS));
        if (block == Blocks.ACACIA_LOG)
            return Blocks.STRIPPED_ACACIA_LOG.getDefaultState().with(net.minecraft.state.property.Properties.AXIS, original.get(net.minecraft.state.property.Properties.AXIS));
        if (block == Blocks.DARK_OAK_LOG)
            return Blocks.STRIPPED_DARK_OAK_LOG.getDefaultState().with(net.minecraft.state.property.Properties.AXIS, original.get(net.minecraft.state.property.Properties.AXIS));
        if (block == Blocks.MANGROVE_LOG)
            return Blocks.STRIPPED_MANGROVE_LOG.getDefaultState().with(net.minecraft.state.property.Properties.AXIS, original.get(net.minecraft.state.property.Properties.AXIS));
        if (block == Blocks.CRIMSON_STEM)
            return Blocks.STRIPPED_CRIMSON_STEM.getDefaultState().with(net.minecraft.state.property.Properties.AXIS, original.get(net.minecraft.state.property.Properties.AXIS));
        if (block == Blocks.WARPED_STEM)
            return Blocks.STRIPPED_WARPED_STEM.getDefaultState().with(net.minecraft.state.property.Properties.AXIS, original.get(Properties.AXIS));

        return null; // Not a strippable block
    }
    // Break all blocks in a tree structure (log blocks)
    private static void breakTree(World world, BlockPos origin, PlayerEntity player, int level) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> toCheck = new LinkedList<>();
        List<ItemStack> collectedDrops = new ArrayList<>();
        int maxLogs = BASE_LOG_LIMIT * level;
        toCheck.add(origin);

        while (!toCheck.isEmpty() && visited.size() < maxLogs) {
            BlockPos current = toCheck.poll();
            if (!visited.add(current)) continue;

            BlockState state = world.getBlockState(current);
            if (isLog(state)) {
                DropMode dropMode = AutoConfig.getConfigHolder(ModConfig.class).getConfig().enchantments.dropMode;
                switch (dropMode) {
                    case NORMAL -> world.breakBlock(current, true, player); // default drops
                    case MERGED, DIRECT -> {
                        List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld) world, current,
                                world.getBlockEntity(current), player, player.getMainHandStack());
                        collectedDrops.addAll(drops);
                        world.breakBlock(current, false, player); // no drops spawned here
                    }
                }

                BeyondTheBlock.LOGGER.info("Breaking Log: {}", current);

                for (BlockPos offset : BlockPos.iterate(current.add(-level, -level, -level), current.add(level, level, level))) {
                    if (!offset.equals(current) && !visited.contains(offset) && isLog(world.getBlockState(offset))) {
                        toCheck.add(offset.toImmutable());
                    }
                }

                breakNearbyLeaves(world, current, player, level);
            }
        }

        // Now handle all collected drops once
        DropMode dropMode = AutoConfig.getConfigHolder(ModConfig.class).getConfig().enchantments.dropMode;
        if (dropMode == DropMode.MERGED || dropMode == DropMode.DIRECT) {
            handleMergedOrDirectDrops(world, origin, player, collectedDrops, dropMode == DropMode.DIRECT);
        }
    }


    public static void handleMergedOrDirectDrops(World world, BlockPos origin, PlayerEntity player, List<ItemStack> drops, boolean direct) {
        List<ItemStack> merged = mergeItemStacks(drops);

        for (ItemStack stack : merged) {
            if (stack.isEmpty()) continue;

            if (direct) {
                if (!player.getInventory().insertStack(stack)) {
                    spawnStillItem(world, origin, stack);
                }
            } else {
                spawnStillItem(world, origin, stack);
            }
        }
    }

    private static void spawnStillItem(World world, BlockPos pos, ItemStack stack) {
        ItemEntity entity = new ItemEntity(
                world,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                stack.copy()
        );
        entity.setVelocity(0, 0, 0); // prevent motion spread
        entity.setToDefaultPickupDelay();
        world.spawnEntity(entity);
    }



    private static void breakNearbyLeaves(World world, BlockPos origin, PlayerEntity player, int level) {
        if (level <= 1) return;

        int radius = 2 + level;
        DropMode dropMode = AutoConfig.getConfigHolder(ModConfig.class).getConfig().enchantments.dropMode;

        // Only collect if using custom drop logic
        List<ItemStack> collectedDrops = (dropMode != DropMode.NORMAL) ? new ArrayList<>() : null;

        for (BlockPos leafPos : BlockPos.iterate(origin.add(-radius, -radius, -radius), origin.add(radius, radius, radius))) {
            BlockState state = world.getBlockState(leafPos);
            if (!isLeaf(state)) continue;

            switch (dropMode) {
                case NORMAL -> world.breakBlock(leafPos, true, player);
                case MERGED, DIRECT -> {
                    List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld) world, leafPos,
                            world.getBlockEntity(leafPos), player, player.getMainHandStack());
                    collectedDrops.addAll(drops);
                    world.breakBlock(leafPos, false, player);
                }
            }

            BeyondTheBlock.LOGGER.info("Breaking Leaf: {}", leafPos);
        }

        if (collectedDrops != null && !collectedDrops.isEmpty()) {
            handleMergedOrDirectDrops(world, origin, player, collectedDrops, dropMode == DropMode.DIRECT);
        }
    }



    public static boolean isLog(BlockState blockState) {
        return blockState.isIn(BlockTags.LOGS);
    }

    private static boolean isLeaf(BlockState state) {
        return state.isIn(BlockTags.LEAVES);
    }

    private static boolean isTillable(BlockState state) {
        return state.getBlock() == Blocks.DIRT ||
                state.getBlock() == Blocks.GRASS_BLOCK ||
                state.getBlock() == Blocks.COARSE_DIRT ||
                state.getBlock() == Blocks.PODZOL ||
                state.getBlock() == Blocks.MYCELIUM;
    }

    // Till nearby farmland (for Large Tilling enchantment)
    public static void tillArea(World world, BlockPos pos, int level) {
        // Increase the radius based on the enchantment level
        for (BlockPos neighbor : BlockPos.iterate(pos.add(-level, 0, -level), pos.add(level, 0, level))) {
            BlockState neighborState = world.getBlockState(neighbor);

            // Log the block type
            //EmeraldEmpire.LOGGER.info("Checking block at " + neighbor + ": " + neighborState.getBlock());

            if (isTillable(neighborState) && neighborState.getBlock() != Blocks.FARMLAND) {
                BlockPos grassPlantPos = neighbor.up();
                if (world.getBlockState(grassPlantPos).getBlock() == Blocks.GRASS ||
                        world.getBlockState(grassPlantPos).getBlock() instanceof FlowerBlock ||
                        world.getBlockState(grassPlantPos).getBlock() instanceof DeadBushBlock ||
                        world.getBlockState(grassPlantPos).getBlock() instanceof SaplingBlock ||
                        world.getBlockState(grassPlantPos).getBlock() == Blocks.SUNFLOWER) {
                    world.breakBlock(grassPlantPos, true);
                }
                world.setBlockState(neighbor, Blocks.FARMLAND.getDefaultState());
                // EmeraldEmpire.LOGGER.info("Handling Deep Tilling at " + neighbor);
            } else {
                //  EmeraldEmpire.LOGGER.info("Not Tillable");
            }
        }
        if (level == 2) {
            world.setBlockState(pos, Blocks.WATER.getDefaultState(), Block.NOTIFY_ALL);
        }
    }


    public static boolean hasEnchantment(Iterable<ItemStack> stacks, Enchantment enchantment) {
        for (ItemStack stack : stacks) {
            if (EnchantmentHelper.getLevel(enchantment, stack) > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasArmorEnchantment(LivingEntity entity, Enchantment enchantment) {
        return hasEnchantment(entity.getArmorItems(), enchantment);
    }

    public static boolean hasHeldEnchantment(LivingEntity entity, Enchantment enchantment) {
        return hasEnchantment(List.of(entity.getMainHandStack(), entity.getOffHandStack()), enchantment);
    }

    public static int getArmorEnchantmentLevel(LivingEntity entity, Enchantment enchantment) {
        int total = 0;
        for (ItemStack stack : entity.getArmorItems()) {
            total += EnchantmentHelper.getLevel(enchantment, stack);
        }
        return total;
    }

    public static void findLogsToHighlight(BlockPos origin, Set<BlockPos> output, World world, Enchantment enchantment, int level) {
        Queue<BlockPos> toCheck = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        toCheck.add(origin);
        int maxLogs = (BASE_LOG_LIMIT * 2) * level;

        BlockPos.Mutable neighborPos = new BlockPos.Mutable();

        while (!toCheck.isEmpty() && output.size() < maxLogs) {
            BlockPos current = toCheck.poll();
            if (!visited.add(current)) continue;

            BlockState state = world.getBlockState(current);
            if (isLog(state)) {
                output.add(current);

                int radius = level;
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        for (int dz = -radius; dz <= radius; dz++) {
                            if (dx == 0 && dy == 0 && dz == 0) continue;

                            neighborPos.set(current.getX() + dx, current.getY() + dy, current.getZ() + dz);
                            if (!visited.contains(neighborPos) && isLog(world.getBlockState(neighborPos))) {
                                toCheck.add(neighborPos.toImmutable());
                            }
                        }
                    }
                }

                highlightNearbyLeaves(world, current, enchantment, level, output); // You could also optimize this
            }
        }
    }


    private static void highlightNearbyLeaves(World world, BlockPos origin, Enchantment enchantment, int level, Set<BlockPos> output) {
        if(enchantment == ModEnchantments.BARKSKIN) return;
        if (level <= 1) return;

        int radius = 2 + level;

        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    mutable.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);

                    if (output.contains(mutable)) continue;
                    if (!world.isChunkLoaded(mutable)) continue;

                    BlockState state = world.getBlockState(mutable);
                    if (state == null || state.isAir() || !isLeaf(state)) continue;

                    output.add(mutable.toImmutable());
                }
            }
        }
    }


    public static void findTillableBlocksToHighlight(BlockPos pos, Set<BlockPos> output, World world, int level) {
        BlockPos.Mutable basePos = new BlockPos.Mutable();
        BlockPos.Mutable grassPos = new BlockPos.Mutable();

        for (int dx = -level; dx <= level; dx++) {
            for (int dz = -level; dz <= level; dz++) {
                basePos.set(pos.getX() + dx, pos.getY(), pos.getZ() + dz);
                BlockState neighborState = world.getBlockState(basePos);

                if (isTillable(neighborState) && neighborState.getBlock() != Blocks.FARMLAND) {
                    grassPos.set(basePos).move(0, 1, 0); // same as basePos.up()

                    Block blockAbove = world.getBlockState(grassPos).getBlock();
                    if (blockAbove == Blocks.GRASS ||
                            blockAbove instanceof FlowerBlock ||
                            blockAbove instanceof DeadBushBlock ||
                            blockAbove instanceof SaplingBlock ||
                            blockAbove == Blocks.SUNFLOWER) {

                        output.add(grassPos.toImmutable()); // convert before storing
                    }

                    output.add(basePos.toImmutable());
                }
            }
        }
    }


}
