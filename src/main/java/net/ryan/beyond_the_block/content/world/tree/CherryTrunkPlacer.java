package net.ryan.beyond_the_block.content.world.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CherryTrunkPlacer extends TrunkPlacer {

    public static final Codec<CherryTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.intRange(0, 32).fieldOf("base_height").forGetter(p -> p.baseHeightValue),
                    Codec.intRange(0, 24).fieldOf("first_random_height").forGetter(p -> p.firstRandomHeightValue),
                    Codec.intRange(0, 24).fieldOf("second_random_height").forGetter(p -> p.secondRandomHeightValue),
                    IntProvider.VALUE_CODEC.fieldOf("branch_count").forGetter(p -> p.branchCount),
                    IntProvider.VALUE_CODEC.fieldOf("branch_horizontal_length").forGetter(p -> p.branchHorizontalLength),
                    UniformIntProvider.CODEC.fieldOf("branch_start_offset_from_top").forGetter(p -> p.branchStartOffsetFromTop),
                    IntProvider.VALUE_CODEC.fieldOf("branch_end_offset_from_top").forGetter(p -> p.branchEndOffsetFromTop)
            ).apply(instance, CherryTrunkPlacer::new)
    );

    private final int baseHeightValue;
    private final int firstRandomHeightValue;
    private final int secondRandomHeightValue;
    private final IntProvider branchCount;
    private final IntProvider branchHorizontalLength;
    private final UniformIntProvider branchStartOffsetFromTop;
    private final IntProvider branchEndOffsetFromTop;

    public CherryTrunkPlacer(
            int baseHeight,
            int firstRandomHeight,
            int secondRandomHeight,
            IntProvider branchCount,
            IntProvider branchHorizontalLength,
            UniformIntProvider branchStartOffsetFromTop,
            IntProvider branchEndOffsetFromTop
    ) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.baseHeightValue = baseHeight;
        this.firstRandomHeightValue = firstRandomHeight;
        this.secondRandomHeightValue = secondRandomHeight;
        this.branchCount = branchCount;
        this.branchHorizontalLength = branchHorizontalLength;
        this.branchStartOffsetFromTop = branchStartOffsetFromTop;
        this.branchEndOffsetFromTop = branchEndOffsetFromTop;
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return ModTrunkPlacerTypes.CHERRY_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(
            TestableWorld world,
            BiConsumer<BlockPos, BlockState> replacer,
            Random random,
            int height,
            BlockPos startPos,
            TreeFeatureConfig config
    ) {
        setToDirt(world, replacer, random, startPos.down(), config);

        List<FoliagePlacer.TreeNode> nodes = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            getAndSetState(world, replacer, random, startPos.up(y), config);
        }

        nodes.add(new FoliagePlacer.TreeNode(startPos.up(height), 3, false));

        int branches = branchCount.get(random);
        Direction[] directions = Direction.Type.HORIZONTAL.stream().toArray(Direction[]::new);

        for (int i = 0; i < branches; i++) {
            Direction dir = directions[random.nextInt(directions.length)];
            int startOffset = height + branchStartOffsetFromTop.get(random);
            int horizontal = branchHorizontalLength.get(random);
            int endOffset = branchEndOffsetFromTop.get(random);

            BlockPos.Mutable mutable = startPos.up(startOffset).mutableCopy();

            for (int step = 0; step < horizontal; step++) {
                mutable.move(dir);
                if (step > 0 && random.nextBoolean()) {
                    mutable.move(Direction.UP);
                }
                getAndSetState(world, replacer, random, mutable, config);
            }

            BlockPos foliagePos = mutable.up(endOffset);
            nodes.add(new FoliagePlacer.TreeNode(foliagePos, 2, false));
        }

        return nodes;
    }
}