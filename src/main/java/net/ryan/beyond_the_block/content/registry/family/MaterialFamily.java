package net.ryan.beyond_the_block.content.registry.family;

import net.minecraft.item.Item;

public record MaterialFamily(
        Item material,
        Item sword,
        Item axe,
        Item pickaxe,
        Item hoe,
        Item shovel,
        Item helmet,
        Item chestplate,
        Item leggings,
        Item boots
) {
}