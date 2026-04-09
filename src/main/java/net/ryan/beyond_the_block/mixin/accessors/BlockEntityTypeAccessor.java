package net.ryan.beyond_the_block.mixin.accessors;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(BlockEntityType.class)
public interface BlockEntityTypeAccessor {

    @Accessor("blocks")
    Set<Block> btb$getBlocks();

    @Mutable
    @Accessor("blocks")
    void btb$setBlocks(Set<Block> blocks);
}