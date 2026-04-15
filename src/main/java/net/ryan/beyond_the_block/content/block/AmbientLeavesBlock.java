package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AmbientLeavesBlock extends LeavesBlock {
    private final DefaultParticleType fallingParticle;
    private final int particleChance;

    public AmbientLeavesBlock(Settings settings, DefaultParticleType fallingParticle, int particleChance) {
        super(settings);
        this.fallingParticle = fallingParticle;
        this.particleChance = particleChance;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        if (random.nextInt(this.particleChance) != 0) {
            return;
        }

        BlockPos below = pos.down();
        if (world.getBlockState(below).isOpaqueFullCube(world, below)) {
            return;
        }

        double x = pos.getX() + random.nextDouble();
        double y = pos.getY() - 0.05D;
        double z = pos.getZ() + random.nextDouble();

        double vx = (random.nextDouble() - 0.5D) * 0.01D;
        double vy = -0.03D;
        double vz = (random.nextDouble() - 0.5D) * 0.01D;

        world.addParticle(this.fallingParticle, x, y, z, vx, vy, vz);
    }
}