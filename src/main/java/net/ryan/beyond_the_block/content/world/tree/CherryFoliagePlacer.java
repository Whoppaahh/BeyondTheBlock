package net.ryan.beyond_the_block.content.world.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.function.BiConsumer;

public class CherryFoliagePlacer extends FoliagePlacer {

    public static final Codec<CherryFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillFoliagePlacerFields(instance).and(instance.group(
                    IntProvider.VALUE_CODEC.fieldOf("height").forGetter(p -> p.height),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("wide_bottom_layer_hole_chance").forGetter(p -> p.wideBottomLayerHoleChance),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("corner_hole_chance").forGetter(p -> p.cornerHoleChance),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_chance").forGetter(p -> p.hangingLeavesChance),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_extension_chance").forGetter(p -> p.hangingLeavesExtensionChance)
            )).apply(instance, CherryFoliagePlacer::new)
    );

    private final IntProvider height;
    private final float wideBottomLayerHoleChance;
    private final float cornerHoleChance;
    private final float hangingLeavesChance;
    private final float hangingLeavesExtensionChance;

    public CherryFoliagePlacer(
            IntProvider radius,
            IntProvider offset,
            IntProvider height,
            float wideBottomLayerHoleChance,
            float cornerHoleChance,
            float hangingLeavesChance,
            float hangingLeavesExtensionChance
    ) {
        super(radius, offset);
        this.height = height;
        this.wideBottomLayerHoleChance = wideBottomLayerHoleChance;
        this.cornerHoleChance = cornerHoleChance;
        this.hangingLeavesChance = hangingLeavesChance;
        this.hangingLeavesExtensionChance = hangingLeavesExtensionChance;
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return ModFoliagePlacerTypes.CHERRY_FOLIAGE_PLACER;
    }


    @Override
    protected void generate(
            TestableWorld world,
            BiConsumer<BlockPos, BlockState> replacer,
            Random random,
            TreeFeatureConfig config,
            int trunkHeight,
            TreeNode treeNode,
            int foliageHeight,
            int radius,
            int offset
    ) {
        BlockPos center = treeNode.getCenter();

        // Small top cap
        generateSquare(world, replacer, random, config, center.up(1), radius - 1, 0, treeNode.isGiantTrunk());

        // Main flat cherry canopy
        generateSquare(world, replacer, random, config, center, radius, 0, treeNode.isGiantTrunk());
        generateSquare(world, replacer, random, config, center.down(1), radius + 1, 0, treeNode.isGiantTrunk());

        // Light hanging leaves around the underside edge
        for (BlockPos pos : BlockPos.iterate(
                center.add(-(radius + 1), -1, -(radius + 1)),
                center.add(radius + 1, -1, radius + 1)
        )) {
            int dx = pos.getX() - center.getX();
            int dz = pos.getZ() - center.getZ();
            boolean edge = Math.abs(dx) == radius + 1 || Math.abs(dz) == radius + 1;

            if (edge && random.nextFloat() < hangingLeavesChance) {
                placeFoliageBlock(world, replacer, random, config, pos.down());
                if (random.nextFloat() < hangingLeavesExtensionChance) {
                    placeFoliageBlock(world, replacer, random, config, pos.down(2));
                }
            }
        }
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return this.height.get(random);
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        int ax = Math.abs(dx);
        int az = Math.abs(dz);

        if (y == -1 && ax == radius && az == radius) {
            return random.nextFloat() < cornerHoleChance;
        }

        if (y == 0 && (ax == radius || az == radius) && ax + az > radius + 1) {
            return random.nextFloat() < wideBottomLayerHoleChance;
        }

        return false;
    }
}