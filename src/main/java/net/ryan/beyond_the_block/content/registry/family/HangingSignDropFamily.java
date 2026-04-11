package net.ryan.beyond_the_block.content.registry.family;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public record HangingSignDropFamily(
        Block hangingSign,
        Block wallHangingSign,
        Item dropItem
) {}
