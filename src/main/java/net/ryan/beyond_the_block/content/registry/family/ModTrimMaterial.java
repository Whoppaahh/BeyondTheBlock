package net.ryan.beyond_the_block.content.registry.family;

import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public record ModTrimMaterial(
        Identifier id,
        Item ingredient,
        String assetName,
        Text displayName
) {

}

