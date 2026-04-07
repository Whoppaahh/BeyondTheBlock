package net.ryan.beyond_the_block.content.registry.family;

import net.minecraft.block.Block;

public record ShrineBlockSet(
        Block pedestal,
        Block core,
        Block heads,
        Block decor,
        Block doubleInput,
        Block singleInput
) {}
