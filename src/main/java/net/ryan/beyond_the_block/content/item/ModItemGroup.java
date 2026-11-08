package net.ryan.beyond_the_block.content.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class ModItemGroup {

    public static final ItemGroup ModBlocksTab = FabricItemGroupBuilder.create(
                    new Identifier(BeyondTheBlock.MOD_ID, "mod_blocks_tab"))
            .icon(() -> new ItemStack(ModBlocks.GEM_BLOCK))
            .build();
    public static final ItemGroup ModToolTab = FabricItemGroupBuilder.create(
                    new Identifier(BeyondTheBlock.MOD_ID, "mod_tools_tab"))
            .icon(() -> new ItemStack(ModItems.RUBY_SWORD))
            .build();
    public static final ItemGroup ModArmourTab = FabricItemGroupBuilder.create(
                    new Identifier(BeyondTheBlock.MOD_ID, "mod_armour_tab"))
            .icon(() -> new ItemStack(ModItems.RUBY_HELMET))
            .build();
}
