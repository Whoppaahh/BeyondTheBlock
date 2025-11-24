package net.ryan.beyond_the_block.utils;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public enum ToolType {
    PICKAXE,
    AXE,
    SHOVEL,
    HOE,
    SWORD;

    public boolean matches(Item item) {
        return switch (this) {
            case PICKAXE -> item == Items.WOODEN_PICKAXE || item == Items.STONE_PICKAXE || item == Items.IRON_PICKAXE || item == Items.GOLDEN_PICKAXE || item == Items.DIAMOND_PICKAXE || item == Items.NETHERITE_PICKAXE;
            case AXE -> item == Items.WOODEN_AXE || item == Items.STONE_AXE || item == Items.IRON_AXE || item == Items.GOLDEN_AXE || item == Items.DIAMOND_AXE || item == Items.NETHERITE_AXE;
            case SHOVEL -> item == Items.WOODEN_SHOVEL || item == Items.STONE_SHOVEL || item == Items.IRON_SHOVEL || item == Items.GOLDEN_SHOVEL || item == Items.DIAMOND_SHOVEL || item == Items.NETHERITE_SHOVEL;
            case HOE -> item == Items.WOODEN_HOE || item == Items.STONE_HOE || item == Items.IRON_HOE || item == Items.GOLDEN_HOE || item == Items.DIAMOND_HOE || item == Items.NETHERITE_HOE;
            case SWORD -> item == Items.WOODEN_SWORD || item == Items.STONE_SWORD || item == Items.IRON_SWORD || item == Items.GOLDEN_SWORD || item == Items.DIAMOND_SWORD || item == Items.NETHERITE_SWORD;
        };
    }
    public Item getItemForMaterial(MaterialType material) {
        return switch (this) {
            case PICKAXE -> switch (material) {
                case WOOD -> Items.WOODEN_PICKAXE;
                case STONE -> Items.STONE_PICKAXE;
                case IRON -> Items.IRON_PICKAXE;
                case GOLD -> Items.GOLDEN_PICKAXE;
                case DIAMOND -> Items.DIAMOND_PICKAXE;
                case NETHERITE -> Items.NETHERITE_PICKAXE;
            };
            case AXE -> switch (material) {
                case WOOD -> Items.WOODEN_AXE;
                case STONE -> Items.STONE_AXE;
                case IRON -> Items.IRON_AXE;
                case GOLD -> Items.GOLDEN_AXE;
                case DIAMOND -> Items.DIAMOND_AXE;
                case NETHERITE -> Items.NETHERITE_AXE;
            };
            case SHOVEL -> switch (material) {
                case WOOD -> Items.WOODEN_SHOVEL;
                case STONE -> Items.STONE_SHOVEL;
                case IRON -> Items.IRON_SHOVEL;
                case GOLD -> Items.GOLDEN_SHOVEL;
                case DIAMOND -> Items.DIAMOND_SHOVEL;
                case NETHERITE -> Items.NETHERITE_SHOVEL;
            };
            case HOE -> switch (material) {
                case WOOD -> Items.WOODEN_HOE;
                case STONE -> Items.STONE_HOE;
                case IRON -> Items.IRON_HOE;
                case GOLD -> Items.GOLDEN_HOE;
                case DIAMOND -> Items.DIAMOND_HOE;
                case NETHERITE -> Items.NETHERITE_HOE;
            };
            case SWORD -> switch (material) {
                case WOOD -> Items.WOODEN_SWORD;
                case STONE -> Items.STONE_SWORD;
                case IRON -> Items.IRON_SWORD;
                case GOLD -> Items.GOLDEN_SWORD;
                case DIAMOND -> Items.DIAMOND_SWORD;
                case NETHERITE -> Items.NETHERITE_SWORD;
            };
        };
    }

}

