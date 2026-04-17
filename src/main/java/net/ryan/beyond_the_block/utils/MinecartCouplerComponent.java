package net.ryan.beyond_the_block.utils;


import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class MinecartCouplerComponent {

    private UUID front;
    private UUID back;

    public UUID get(CouplerSide side) {
        return side == CouplerSide.FRONT ? front : back;
    }

    public void set(CouplerSide side, UUID uuid) {
        if (side == CouplerSide.FRONT) {
            front = uuid;
        } else {
            back = uuid;
        }
    }

    public boolean isOccupied(CouplerSide side) {
        return get(side) != null;
    }

    public void clear(CouplerSide side) {
        set(side, null);
    }

    public boolean hasAny() {
        return front != null || back != null;
    }

    /* ---------- NBT ---------- */

    public void writeNbt(NbtCompound nbt) {
        if (front != null) nbt.putUuid("FrontCoupler", front);
        if (back != null) nbt.putUuid("BackCoupler", back);
    }

    public void readNbt(NbtCompound nbt) {
        front = nbt.containsUuid("FrontCoupler") ? nbt.getUuid("FrontCoupler") : null;
        back = nbt.containsUuid("BackCoupler") ? nbt.getUuid("BackCoupler") : null;
    }
}