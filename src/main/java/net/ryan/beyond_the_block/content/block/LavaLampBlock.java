package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class LavaLampBlock extends Block {
    public LavaLampBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(8) == 0) {
            double x = (double)pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
            double y = (double)pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
            double z = (double)pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
            world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.01, 0.0);
        }
    }
}
