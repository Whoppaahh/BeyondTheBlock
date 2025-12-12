package net.ryan.beyond_the_block.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.BeyondTheBlock;

public class ModTags {
    public static class Blocks {

        public static final TagKey<Block> NEEDS_RUBY_TOOL = createBlockTag("needs_ruby_tool");
        public static final TagKey<Block> INCORRECT_FOR_RUBY_TOOL = createBlockTag("incorrect_for_ruby_tool");

        private static TagKey<Block> createBlockTag(String name) {
            return TagKey.of(Registry.BLOCK_KEY, Identifier.of(BeyondTheBlock.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createItemTag("transformable_items");

        public static final TagKey<Item> GEM_UPGRADABLE_TOOLS = createItemTag("gem_upgradable_tools");
        public static final TagKey<Item> MOD_CUT_GEMS = createItemTag("mod_cut_gems");
        public static final TagKey<Item> MOD_ARMOURS = createItemTag("mod_armours");
        public static final TagKey<Item> MOD_ORES = createItemTag("mod_ores");
        public static final TagKey<Item> MOD_TOOLS = createItemTag("mod_tools");
        public static final TagKey<Item> MOD_WEAPONS = createItemTag("mod_weapons");


        private static TagKey<Item> createItemTag(String name) {
            return TagKey.of(Registry.ITEM.getKey(), Identifier.of(BeyondTheBlock.MOD_ID, name));
        }
    }

    public static void registerModTags(){
        BeyondTheBlock.LOGGER.info("Registering Mod Tags");
    }
}
