package net.ryan.beyond_the_block.feature.horses;

import net.minecraft.entity.passive.AbstractHorseEntity;

public interface HorseEnchantHooks {

    default void onTick(AbstractHorseEntity horse, int level) {}

    default boolean onJump(AbstractHorseEntity horse, int level) {
        return false;
    }
}