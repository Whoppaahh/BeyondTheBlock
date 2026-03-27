package net.ryan.beyond_the_block.content.enchantment.Armour.boots;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;

public interface LeapOfFaithTracker {
    TrackedData<Integer> btb$getAirJumpCountKey();
    default int getAirJumpCount() {
        return ((PlayerEntity) this).getDataTracker().get(btb$getAirJumpCountKey());
    }

    default void setAirJumpCount(int value) {
        ((PlayerEntity) this).getDataTracker().set(btb$getAirJumpCountKey(), value);
    }
}

