package net.ryan.beyond_the_block.utils;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class FlameTrailPoint {
    private final Vec3d position;
    private int age = 0;
    private final int maxAge;

    public FlameTrailPoint(Vec3d position, int maxAge) {
        this.position = position;
        this.maxAge = maxAge;
    }

    public boolean tick(ServerWorld world) {
        world.spawnParticles(ParticleTypes.FLAME,
                position.x, position.y, position.z,
                1,
                (world.random.nextDouble() - 0.5) * 0.02,
                0.01,
                (world.random.nextDouble() - 0.5) * 0.02,
                0.02);
        age++;
        return age >= maxAge;
    }
}

