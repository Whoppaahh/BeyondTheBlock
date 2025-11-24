package net.ryan.beyond_the_block.enchantment.Armour.boots;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;

public interface LeapOfFaithTracker {
    TrackedData<Integer> emeraldEmpire$getAirJumpCountKey();
    default int getAirJumpCount() {
        return ((PlayerEntity) this).getDataTracker().get(emeraldEmpire$getAirJumpCountKey());
    }

    default void setAirJumpCount(int value) {
        ((PlayerEntity) this).getDataTracker().set(emeraldEmpire$getAirJumpCountKey(), value);
    }
}

