package net.ryan.beyond_the_block.content.registry.family;

import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public record ModTrimPattern(
        Identifier id,
        Item templateItem,
        String assetName,
        Text displayName
) {
}
