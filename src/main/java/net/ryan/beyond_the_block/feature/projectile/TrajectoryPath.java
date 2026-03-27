package net.ryan.beyond_the_block.feature.projectile;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/**
 * @param hitPos      null if none
 * @param hitBlock    only set when BLOCK hit
 * @param hitEntityId only set when ENTITY hit
 */
public record TrajectoryPath(List<Vec3d> points, HitKind hitKind, Vec3d hitPos, BlockPos hitBlock, int hitEntityId,
                             WeaponKind weaponKind) {

    public enum HitKind {
        NONE,
        BLOCK,
        ENTITY
    }

    public enum WeaponKind {
        BOW,
        CROSSBOW,
        OTHER
    }

    public static final TrajectoryPath EMPTY = new TrajectoryPath(List.of(), HitKind.NONE, null, null, -1, WeaponKind.OTHER);

    public boolean isEmpty() {
        return points == null || points.isEmpty();
    }
}

