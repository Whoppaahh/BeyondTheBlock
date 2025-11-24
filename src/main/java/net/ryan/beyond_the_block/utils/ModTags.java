package net.ryan.beyond_the_block.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.BeyondTheBlock;

public class ModTags {
    public static class Blocks {

        public static final TagKey<Block> NEEDS_RUBY_TOOL = createTag("needs_ruby_tool");
        public static final TagKey<Block> INCORRECT_FOR_RUBY_TOOL = createTag("incorrect_for_ruby_tool");


        private static TagKey<Block> createTag(String name) {
            return TagKey.of(Registry.BLOCK.getKey(), Identifier.of(BeyondTheBlock.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");

        public static final TagKey<Item> GEM_UPGRADABLE_TOOLS = createTag("gem_upgradable_tools");
        public static final TagKey<Item> MOD_CUT_GEMS = createTag("mod_cut_gems");
        public static final TagKey<Item> MOD_ARMOURS = createTag("mod_armours");
        public static final TagKey<Item> MOD_ORES = createTag("mod_ores");
        public static final TagKey<Item> MOD_TOOLS = createTag("mod_tools");
        public static final TagKey<Item> MOD_WEAPONS = createTag("mod_weapons");


        private static TagKey<Item> createTag(String name) {
            return TagKey.of(Registry.ITEM.getKey(), Identifier.of(BeyondTheBlock.MOD_ID, name));
        }
    }
}
