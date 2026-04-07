package net.ryan.beyond_the_block.content.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class ModItemGroup {

    public static ItemGroup ModBlocksTab = FabricItemGroupBuilder.build(
            new Identifier(BeyondTheBlock.MOD_ID, "mod_blocks_tab"), () -> new ItemStack(ModBlocks.GEM_BLOCK));
    public static ItemGroup ModToolTab = FabricItemGroupBuilder.build(
            new Identifier(BeyondTheBlock.MOD_ID, "mod_tools_tab"), () -> new ItemStack(ModItems.RUBY_SWORD));
    public static ItemGroup ModArmourTab = FabricItemGroupBuilder.build(
            new Identifier(BeyondTheBlock.MOD_ID, "mod_armour_tab"), () -> new ItemStack(ModItems.RUBY_HELMET));
}
