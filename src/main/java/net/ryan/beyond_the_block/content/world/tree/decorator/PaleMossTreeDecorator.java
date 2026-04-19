package net.ryan.beyond_the_block.content.world.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.ryan.beyond_the_block.content.registry.ModBlocks;

public class PaleMossTreeDecorator extends TreeDecorator {

    public static final Codec<PaleMossTreeDecorator> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.floatRange(0.0F, 1.0F).fieldOf("ground_probability").forGetter(d -> d.groundProbability),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("leaves_probability").forGetter(d -> d.leavesProbability),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("trunk_probability").forGetter(d -> d.trunkProbability)
            ).apply(instance, PaleMossTreeDecorator::new)
    );

    private final float groundProbability;
    private final float leavesProbability;
    private final float trunkProbability;

    public PaleMossTreeDecorator(float groundProbability, float leavesProbability, float trunkProbability) {
        this.groundProbability = groundProbability;
        this.leavesProbability = leavesProbability;
        this.trunkProbability = trunkProbability;
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return ModTreeDecoratorTypes.PALE_MOSS;
    }

    @Override
    public void generate(TreeDecorator.Generator generator) {
        Random random = generator.getRandom();

        // trunk-side moss
        for (BlockPos logPos : generator.getLogPositions()) {
            if (random.nextFloat() >= trunkProbability) continue;

            Direction dir = Direction.Type.HORIZONTAL.random(random);
            BlockPos side = logPos.offset(dir);

            if (generator.isAir(side)) {
                generator.replace(side, ModBlocks.PALE_MOSS_BLOCK.getDefaultState());
            }
        }

        // simple hanging moss from leaves
        for (BlockPos leafPos : generator.getLeavesPositions()) {
            if (random.nextFloat() >= leavesProbability) continue;

            BlockPos below = leafPos.down();
            if (generator.isAir(below)) {
                generator.replace(below, ModBlocks.PALE_MOSS_BLOCK.getDefaultState());

                BlockPos below2 = below.down();
                if (generator.isAir(below2) && random.nextFloat() < 0.5F) {
                    generator.replace(below2, ModBlocks.PALE_MOSS_BLOCK.getDefaultState());
                }
            }
        }

        // small moss patch near roots
        for (BlockPos rootPos : generator.getRootPositions()) {
            if (random.nextFloat() >= groundProbability) continue;

            for (BlockPos pos : BlockPos.iterate(rootPos.add(-2, -1, -2), rootPos.add(2, -1, 2))) {
                BlockPos target = pos.toImmutable();
                BlockPos above = target.up();

                if (generator.isAir(above)) {
                    generator.replace(target, ModBlocks.PALE_MOSS_BLOCK.getDefaultState());
                }
            }
        }
    }
}