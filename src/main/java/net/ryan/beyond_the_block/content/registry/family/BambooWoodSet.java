package net.ryan.beyond_the_block.content.registry.family;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public record BambooWoodSet(
        Block bambooBlock,
        Block strippedBambooBlock,
        Block planks,
        Block slab,
        Block stairs,
        Block fence,
        Block fenceGate,
        Block door,
        Block trapdoor,
        Block button,
        Block pressurePlate,
        Block mosaic,
        Block mosaicSlab,
        Block mosaicStairs,
        Block sign,
        Block wallSign,
        Item signItem
) {}
