package net.ryan.beyond_the_block.utils.accessors;

import org.spongepowered.asm.mixin.Unique;

public interface FurnaceAccessor {
    @Unique
    int btb$getCookTime();
    @Unique
    int btb$getCookTimeTotal();
    @Unique
    boolean btb$getIsBurning();
    @Unique
    void btb$setCookTime(int cookTime);
}
