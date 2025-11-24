package net.ryan.beyond_the_block.utils;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.ryan.beyond_the_block.BeyondTheBlock;

public enum MaterialType {
    WOOD(1),
    STONE(2),
    IRON(3),
    GOLD(2), // Weaker but fast, still better than wood
    DIAMOND(4),
    NETHERITE(5);

    private final int strength;

    MaterialType(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }

    public static MaterialType fromItem(Item item) {
        // Raw materials
        if (item.getDefaultStack().isIn(ItemTags.PLANKS)) return WOOD;
        if (item == Items.COBBLESTONE) return STONE;
        if (item == Items.IRON_INGOT) return IRON;
        if (item == Items.GOLD_INGOT) return GOLD;
        if (item == Items.DIAMOND) return DIAMOND;
        if (item == Items.NETHERITE_INGOT) return NETHERITE;

        // Tool materials (e.g., wooden, stone, iron, diamond, etc.)
        if (item == Items.WOODEN_PICKAXE || item == Items.WOODEN_AXE || item == Items.WOODEN_SHOVEL ||
                item == Items.WOODEN_HOE || item == Items.WOODEN_SWORD) {
            return WOOD; // Or whatever material corresponds to the tool
        }
        if (item == Items.STONE_PICKAXE || item == Items.STONE_AXE || item == Items.STONE_SHOVEL ||
                item == Items.STONE_HOE || item == Items.STONE_SWORD) {
            return STONE;
        }
        if (item == Items.IRON_PICKAXE || item == Items.IRON_AXE || item == Items.IRON_SHOVEL ||
                item == Items.IRON_HOE || item == Items.IRON_SWORD) {
            return IRON;
        }
        if (item == Items.GOLDEN_PICKAXE || item == Items.GOLDEN_AXE || item == Items.GOLDEN_SHOVEL ||
                item == Items.GOLDEN_HOE || item == Items.GOLDEN_SWORD) {
            return GOLD;
        }
        if (item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_AXE || item == Items.DIAMOND_SHOVEL ||
                item == Items.DIAMOND_HOE || item == Items.DIAMOND_SWORD) {
            return DIAMOND;
        }
        if (item == Items.NETHERITE_PICKAXE || item == Items.NETHERITE_AXE || item == Items.NETHERITE_SHOVEL ||
                item == Items.NETHERITE_HOE || item == Items.NETHERITE_SWORD) {
            return NETHERITE;
        }

        BeyondTheBlock.LOGGER.info("Unrecognised Item{}", item);
        return null; // If no match is found
    }

}

