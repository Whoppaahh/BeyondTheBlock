package net.ryan.beyond_the_block.feature.horses;

import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class StayNearData {
    public Vec3d center;
    public UUID owner;

    public StayNearData(Vec3d center, UUID owner) {
        this.center = center;
        this.owner = owner;
    }
}

