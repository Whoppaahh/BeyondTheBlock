package net.ryan.beyond_the_block.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.item.Item;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.utils.ModTags;
import net.minecraft.tag.ItemTags;
import net.ryan.beyond_the_block.content.registry.family.BambooWoodSet;
import net.ryan.beyond_the_block.content.registry.family.WoodSet;
import java.util.List;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateTags() {
        generateCustomTags();
        generateToolTags();
        generateWoodItemTags();
    }

    private void generateWoodItemTags() {
        addStandardWoodSetTags(ModBlocks.CHERRY_SET);
        addStandardWoodSetTags(ModBlocks.PALE_OAK_SET);
        addBambooWoodSetTags(ModBlocks.BAMBOO_WOOD_SET);
    }

    private void generateCustomTags() {
        FabricTagProvider<Item>.FabricTagBuilder<Item> transformableItems = getOrCreateTagBuilder(ModTags.Items.TRANSFORMABLE_ITEMS);
        addAll(transformableItems, List.of(
                ModItems.ROSETTE_ITEM,
                ModItems.RAW_ROSETTE_ITEM
        ));

        FabricTagProvider<Item>.FabricTagBuilder<Item> cutGems = getOrCreateTagBuilder(ModTags.Items.MOD_CUT_GEMS);
        addAll(cutGems, List.of(
                ModItems.AMBERINE_ITEM,
                ModItems.AZUROS_ITEM,
                ModItems.ROSETTE_ITEM,
                ModItems.CHROMITE_ITEM,
                ModItems.INDIGRA_ITEM,
                ModItems.MIRANITE_ITEM,
                ModItems.NOCTURNITE_ITEM
        ));

        FabricTagProvider<Item>.FabricTagBuilder<Item> gemUpgradableTools = getOrCreateTagBuilder(ModTags.Items.GEM_UPGRADABLE_TOOLS);
        addAll(gemUpgradableTools, List.of(
                ModItems.AMBERINE_SWORD,
                ModItems.AMBERINE_AXE,

                ModItems.AZUROS_SWORD,
                ModItems.AZUROS_AXE,

                ModItems.CHROMITE_SWORD,
                ModItems.CHROMITE_AXE,

                ModItems.ROSETTE_SWORD,
                ModItems.ROSETTE_AXE,

                ModItems.MIRANITE_SWORD,
                ModItems.MIRANITE_AXE,

                ModItems.RUBY_SWORD,
                ModItems.RUBY_AXE,
                ModItems.RUBY_PICKAXE,
                ModItems.RUBY_HOE,
                ModItems.RUBY_SHOVEL,

                ModItems.XIRION_SWORD,
                ModItems.XIRION_AXE,
                ModItems.XIRION_PICKAXE,
                ModItems.XIRION_HOE,
                ModItems.XIRION_SHOVEL
        ));
    }

    private void generateToolTags() {
        FabricTagProvider<Item>.FabricTagBuilder<Item> swords = getOrCreateTagBuilder(ConventionalItemTags.SWORDS);
        addAll(swords, List.of(
                ModItems.XIRION_SWORD,
                ModItems.RUBY_SWORD,
                ModItems.MIRANITE_SWORD,
                ModItems.AMBERINE_SWORD,
                ModItems.AZUROS_SWORD,
                ModItems.CHROMITE_SWORD,
                ModItems.ROSETTE_SWORD
        ));

        FabricTagProvider<Item>.FabricTagBuilder<Item> axes = getOrCreateTagBuilder(ConventionalItemTags.AXES);
        addAll(axes, List.of(
                ModItems.XIRION_AXE,
                ModItems.RUBY_AXE,
                ModItems.MIRANITE_AXE,
                ModItems.AMBERINE_AXE,
                ModItems.AZUROS_AXE,
                ModItems.CHROMITE_AXE,
                ModItems.ROSETTE_AXE
        ));

        FabricTagProvider<Item>.FabricTagBuilder<Item> hoes = getOrCreateTagBuilder(ConventionalItemTags.HOES);
        addAll(hoes, List.of(
                ModItems.XIRION_HOE,
                ModItems.RUBY_HOE,
                ModItems.MIRANITE_HOE,
                ModItems.AMBERINE_HOE,
                ModItems.AZUROS_HOE,
                ModItems.CHROMITE_HOE,
                ModItems.ROSETTE_HOE
        ));

        FabricTagProvider<Item>.FabricTagBuilder<Item> pickaxes = getOrCreateTagBuilder(ConventionalItemTags.PICKAXES);
        addAll(pickaxes, List.of(
                ModItems.XIRION_PICKAXE,
                ModItems.RUBY_PICKAXE,
                ModItems.MIRANITE_PICKAXE,
                ModItems.AMBERINE_PICKAXE,
                ModItems.AZUROS_PICKAXE,
                ModItems.CHROMITE_PICKAXE,
                ModItems.ROSETTE_PICKAXE
        ));

        FabricTagProvider<Item>.FabricTagBuilder<Item> shovels = getOrCreateTagBuilder(ConventionalItemTags.SHOVELS);
        addAll(shovels, List.of(
                ModItems.XIRION_SHOVEL,
                ModItems.RUBY_SHOVEL,
                ModItems.MIRANITE_SHOVEL,
                ModItems.AMBERINE_SHOVEL,
                ModItems.AZUROS_SHOVEL,
                ModItems.CHROMITE_SHOVEL,
                ModItems.ROSETTE_SHOVEL
        ));
    }

    private <T> void addAll(FabricTagBuilder<T> builder, Iterable<T> entries) {
        for (T entry : entries) {
            builder.add(entry);
        }
    }

    private void addStandardWoodSetTags(WoodSet set) {
        getOrCreateTagBuilder(ItemTags.LOGS)
                .add(set.log().asItem(), set.wood().asItem(), set.strippedLog().asItem(), set.strippedWood().asItem());

        getOrCreateTagBuilder(ItemTags.PLANKS).add(set.planks().asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_SLABS).add(set.slab().asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS).add(set.stairs().asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_FENCES).add(set.fence().asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_DOORS).add(set.door().asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS).add(set.trapdoor().asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS).add(set.button().asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES).add(set.pressurePlate().asItem());
    }

    private void addBambooWoodSetTags(BambooWoodSet set) {
        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(set.planks().asItem(), set.mosaic().asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_SLABS)
                .add(set.slab().asItem(), set.mosaicSlab().asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS)
                .add(set.stairs().asItem(), set.mosaicStairs().asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_FENCES).add(set.fence().asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_DOORS).add(set.door().asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS).add(set.trapdoor().asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS).add(set.button().asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES).add(set.pressurePlate().asItem());
    }
}