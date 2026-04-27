package net.ryan.beyond_the_block.feature.pets;

import net.minecraft.util.math.BlockPos;

public interface PetHomeAccessor {
    BlockPos btb$getPetHomePos();
    void btb$setPetHomePos(BlockPos pos);
    boolean btb$hasPetHome();
    void btb$clearPetHome();
}