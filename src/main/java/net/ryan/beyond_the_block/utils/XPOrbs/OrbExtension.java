package net.ryan.beyond_the_block.utils.XPOrbs;

import net.minecraft.entity.player.PlayerEntity;

public interface OrbExtension {
    PlayerEntity beyond_the_block$getTargetPlayer();
    void beyond_the_block$setTargetPlayer(PlayerEntity player);

    int beyond_the_block$getAmount();
    void beyond_the_block$setAmount(int value);
}