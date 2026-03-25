package net.ryan.beyond_the_block.enchantment.Tools.axes;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.Configs;
import net.ryan.beyond_the_block.config.DropMode;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.enchantment.MyEnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.ryan.beyond_the_block.enchantment.MyEnchantmentHelper.isLog;

public class BarkskinEnchantment extends Enchantment {
    public BarkskinEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
        PlayerBlockBreakEvents.BEFORE.register(this::onBlockBreak);
    }


    private boolean onBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        ItemStack tool = player.getMainHandStack();
        int level = EnchantmentHelper.getLevel(ModEnchantments.BARKSKIN, tool);

        if (level > 0 && tool.getItem() instanceof AxeItem) {
            Block originalBlock = state.getBlock();

            if(isStrippedLog(originalBlock.getDefaultState())){
                return true;
            }
            if (isLog(originalBlock.getDefaultState())) {
                if (level == 1) {
                    // Level 1: Strip entire tree and drop stripped logs
                    stripTreeAndDrop(world, pos, originalBlock, player, tool, false, false);
                } else if (level == 2 || level == 3) {
                    // Level 2 and 3: Strip entire tree and drop both stripped and normal logs
                    stripTreeAndDrop(world, pos, originalBlock, player, tool, true, level == 3);
                }
                return false;
            }
        }
        return true;
    }

    private static final Set<Block> STRIPPED_LOGS = Set.of(
            Blocks.STRIPPED_OAK_LOG,
            Blocks.STRIPPED_SPRUCE_LOG,
            Blocks.STRIPPED_BIRCH_LOG,
            Blocks.STRIPPED_JUNGLE_LOG,
            Blocks.STRIPPED_ACACIA_LOG,
            Blocks.STRIPPED_DARK_OAK_LOG,
            Blocks.STRIPPED_MANGROVE_LOG,
            Blocks.STRIPPED_CRIMSON_STEM,
            Blocks.STRIPPED_WARPED_STEM
    );

    private boolean isStrippedLog(BlockState state) {
        return STRIPPED_LOGS.contains(state.getBlock());
    }

    private void stripTreeAndDrop(World world, BlockPos start, Block logBlock, PlayerEntity player, ItemStack tool, boolean dropNormalLogs, boolean noDamage) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> toCheck = new ArrayDeque<>();
        toCheck.add(start);

        List<ItemStack> collectedDrops = new ArrayList<>();
        DropMode dropMode = Configs.server().features.enchantments.dropMode;

        while (!toCheck.isEmpty()) {
            BlockPos current = toCheck.poll();

            if (visited.contains(current)) continue;
            visited.add(current);

            BlockState state = world.getBlockState(current);
            if (state.getBlock() != logBlock) continue;

            BlockState stripped = getStrippedState(state);
            if (stripped != null) {
                // Remove block without drops
                world.removeBlock(current, false);

                // Collect drops
                collectedDrops.add(new ItemStack(stripped.getBlock()));
                if (dropNormalLogs) {
                    collectedDrops.add(new ItemStack(logBlock));
                }

                if (!noDamage) {
                    tool.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
                }
            }

            for (BlockPos offset : BlockPos.iterateOutwards(current, 1, 1, 1)) {
                if (!offset.equals(current) && !visited.contains(offset) && isLog(world.getBlockState(offset))) {
                    toCheck.add(offset.toImmutable());
                }
            }
        }

        // Drop or insert collected drops based on drop mode
        if (!collectedDrops.isEmpty()) {
            switch (dropMode) {
                case NORMAL -> {
                    // Default behavior: drop all collected items normally
                    for (ItemStack stack : collectedDrops) {
                        Block.dropStack(world, start, stack);
                    }
                }
                case MERGED -> MyEnchantmentHelper.handleMergedOrDirectDrops(world, start, player, collectedDrops, false);
                case DIRECT -> MyEnchantmentHelper.handleMergedOrDirectDrops(world, start, player, collectedDrops, true);
            }
        }
    }



    private @Nullable BlockState getStrippedState(BlockState original) {
        Block block = original.getBlock();

        if (block == Blocks.OAK_LOG)
            return Blocks.STRIPPED_OAK_LOG.getDefaultState().with(Properties.AXIS, original.get(Properties.AXIS));
        if (block == Blocks.SPRUCE_LOG)
            return Blocks.STRIPPED_SPRUCE_LOG.getDefaultState().with(Properties.AXIS, original.get(Properties.AXIS));
        if (block == Blocks.BIRCH_LOG)
            return Blocks.STRIPPED_BIRCH_LOG.getDefaultState().with(Properties.AXIS, original.get(Properties.AXIS));
        if (block == Blocks.JUNGLE_LOG)
            return Blocks.STRIPPED_JUNGLE_LOG.getDefaultState().with(Properties.AXIS, original.get(Properties.AXIS));
        if (block == Blocks.ACACIA_LOG)
            return Blocks.STRIPPED_ACACIA_LOG.getDefaultState().with(Properties.AXIS, original.get(Properties.AXIS));
        if (block == Blocks.DARK_OAK_LOG)
            return Blocks.STRIPPED_DARK_OAK_LOG.getDefaultState().with(Properties.AXIS, original.get(Properties.AXIS));
        if (block == Blocks.MANGROVE_LOG)
            return Blocks.STRIPPED_MANGROVE_LOG.getDefaultState().with(Properties.AXIS, original.get(Properties.AXIS));
        if (block == Blocks.CRIMSON_STEM)
            return Blocks.STRIPPED_CRIMSON_STEM.getDefaultState().with(Properties.AXIS, original.get(Properties.AXIS));
        if (block == Blocks.WARPED_STEM)
            return Blocks.STRIPPED_WARPED_STEM.getDefaultState().with(Properties.AXIS, original.get(Properties.AXIS));

        return null; // Not a strippable block
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || stack.isOf(Items.BOOK);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != ModEnchantments.TIMBER_CUT;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
