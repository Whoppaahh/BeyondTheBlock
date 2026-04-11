package net.ryan.beyond_the_block.content.registry.family;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public record WoodSet(
        Block log,
        Block wood,
        Block strippedLog,
        Block strippedWood,
        Block planks,
        Block slab,
        Block stairs,
        Block fence,
        Block fenceGate,
        Block door,
        Block trapdoor,
        Block button,
        Block pressurePlate,
        Block sign,
        Block wallSign,
        Item signItem,
        Block hangingSign,
        Block wallHangingSign,
        Item hangingSignItem
) {}
