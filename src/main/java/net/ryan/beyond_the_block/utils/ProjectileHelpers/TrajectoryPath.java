package net.ryan.beyond_the_block.utils.ProjectileHelpers;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public final class TrajectoryPath {

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

    public final List<Vec3d> points;
    public final HitKind hitKind;
    public final Vec3d hitPos;        // null if none
    public final BlockPos hitBlock;  // only set when BLOCK hit
    public final int hitEntityId;    // only set when ENTITY hit
    public final WeaponKind weaponKind;

    public TrajectoryPath(List<Vec3d> points, HitKind hitKind, Vec3d hitPos, BlockPos hitBlock, int hitEntityId, WeaponKind weaponKind) {
        this.points = points;
        this.hitKind = hitKind;
        this.hitPos = hitPos;
        this.hitBlock = hitBlock;
        this.hitEntityId = hitEntityId;
        this.weaponKind = weaponKind;
    }

    public static final TrajectoryPath EMPTY = new TrajectoryPath(List.of(), HitKind.NONE, null, null, -1, WeaponKind.OTHER);

    public boolean isEmpty() {
        return points == null || points.isEmpty();
    }
}

