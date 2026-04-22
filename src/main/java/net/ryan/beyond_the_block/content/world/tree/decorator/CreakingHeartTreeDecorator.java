package net.ryan.beyond_the_block.content.world.tree.decorator;

import com.mojang.serialization.Codec;
import net.minecraft.block.PillarBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.ryan.beyond_the_block.content.block.CreakingHeartBlock;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.utils.CreakingHeartState;
import net.ryan.beyond_the_block.utils.ModTags;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CreakingHeartTreeDecorator extends TreeDecorator {
    public static final Codec<CreakingHeartTreeDecorator> CODEC =
            Codec.floatRange(0.0F, 1.0F)
                    .fieldOf("probability")
                    .xmap(CreakingHeartTreeDecorator::new, d -> d.probability)
                    .codec();

    private final float probability;

    public CreakingHeartTreeDecorator(float probability) {
        this.probability = probability;
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return ModTreeDecoratorTypes.CREAKING_HEART;
    }

    @Override
    public void generate(Generator generator) {
        Random random = generator.getRandom();

        if (random.nextFloat() >= this.probability) {
            return;
        }

        List<BlockPos> logs = new ArrayList<>(generator.getLogPositions());
        if (logs.size() < 6) {
            return;
        }

        logs.sort(Comparator.comparingInt(BlockPos::getY));

        int minY = logs.get(0).getY();
        int maxY = logs.get(logs.size() - 1).getY();

        List<BlockPos> candidates = new ArrayList<>();

        for (BlockPos pos : logs) {
            int y = pos.getY();

            // Keep it away from base and top
            if (y <= minY + 1) continue;
            if (y >= maxY - 2) continue;

            // Only positions embedded vertically in the trunk
            if (!hasVerticalSupport(generator, pos)) continue;

            // Prefer positions that feel internal to the trunk mass
            if (!hasEnoughHorizontalSupport(generator, pos, 2)) continue;

            candidates.add(pos.toImmutable());
        }

        if (candidates.isEmpty()) {
            return;
        }

        // Bias downward: choose from lower half of valid candidates
        int upperBound = Math.max(1, (candidates.size() + 1) / 2);
        BlockPos chosen = candidates.get(random.nextInt(upperBound));

        generator.replace(chosen, ModBlocks.CREAKING_HEART.getDefaultState()
                .with(PillarBlock.AXIS, Direction.Axis.Y)
                .with(CreakingHeartBlock.CREAKING_HEART_STATE, CreakingHeartState.DORMANT)
                .with(CreakingHeartBlock.NATURAL, true));
    }

    private static boolean hasVerticalSupport(Generator generator, BlockPos pos) {
        BlockPos below = pos.down();
        BlockPos above = pos.up();

        boolean hasLogBelow = generator.getWorld().testBlockState(
                below,
                state -> state.isIn(ModTags.Blocks.PALE_OAK_LOGS)
                        && state.contains(PillarBlock.AXIS)
                        && state.get(PillarBlock.AXIS) == Direction.Axis.Y
        );

        boolean hasLogAbove = generator.getWorld().testBlockState(
                above,
                state -> state.isIn(ModTags.Blocks.PALE_OAK_LOGS)
                        && state.contains(PillarBlock.AXIS)
                        && state.get(PillarBlock.AXIS) == Direction.Axis.Y
        );

        return hasLogBelow && hasLogAbove;
    }

    private static boolean hasEnoughHorizontalSupport(Generator generator, BlockPos pos, int minimumNeighbors) {
        int neighbors = 0;

        for (Direction direction : Direction.Type.HORIZONTAL) {
            if (generator.getWorld().testBlockState(
                    pos.offset(direction),
                    state -> state.isIn(ModTags.Blocks.PALE_OAK_LOGS)
            )) {
                neighbors++;
            }
        }

        return neighbors >= minimumNeighbors;
    }
}