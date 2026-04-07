package net.ryan.beyond_the_block.datagen.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.ryan.beyond_the_block.content.registry.ModBlocks;

import java.util.List;

public final class ModDatagenFamilies {

    public static final List<Block> SHELVES = List.of(
            ModBlocks.OAK_SHELF_BLOCK,
            ModBlocks.SPRUCE_SHELF_BLOCK,
            ModBlocks.BIRCH_SHELF_BLOCK,
            ModBlocks.JUNGLE_SHELF_BLOCK,
            ModBlocks.ACACIA_SHELF_BLOCK,
            ModBlocks.DARK_OAK_SHELF_BLOCK,
            ModBlocks.MANGROVE_SHELF_BLOCK,
            ModBlocks.BAMBOO_SHELF_BLOCK,
            ModBlocks.CRIMSON_SHELF_BLOCK,
            ModBlocks.WARPED_SHELF_BLOCK
    );

    public static final List<Block> SPONGES = List.of(
            ModBlocks.COMPRESSED_SPONGE,
            ModBlocks.DOUBLE_COMPRESSED_SPONGE,
            ModBlocks.TRIPLE_COMPRESSED_SPONGE,
            ModBlocks.WET_COMPRESSED_SPONGE,
            ModBlocks.WET_DOUBLE_COMPRESSED_SPONGE,
            ModBlocks.WET_TRIPLE_COMPRESSED_SPONGE
    );

    public static final List<Block> SHRINE_BLOCKS = List.of(
            ModBlocks.SHRINE_CORE_BLOCK,
            ModBlocks.SHRINE_HEADS_BLOCK,
            ModBlocks.SHRINE_DECOR_BLOCK,
            ModBlocks.SINGLE_INPUT_BLOCK,
            ModBlocks.DOUBLE_INPUT_BLOCK
    );

    public static final List<Block> SIMPLE_UTILITY_BLOCKS = List.of(
            ModBlocks.GEM_BLOCK,
            ModBlocks.LAVA_LAMP_BLOCK,
            ModBlocks.PEDESTAL_BLOCK
    );

    public static final List<Block> MACHINE_STYLE_BLOCKS = List.of(
            ModBlocks.PLAYER_VAULT_BLOCK,
            ModBlocks.INFI_FURNACE_BLOCK,
            ModBlocks.DECRAFTER_BLOCK
    );

    public static final List<Block> RAIL_STYLE_BLOCKS = List.of(
            ModBlocks.SPEED_RAIL_BLOCK
    );

    private ModDatagenFamilies() {
    }

    public static List<Item> asItems(List<Block> blocks) {
        return blocks.stream().map(Block::asItem).toList();
    }
}