package net.ryan.beyond_the_block.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.utils.ModTags;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateTags() {
        // Define tags for items here
        getOrCreateTagBuilder(ModTags.Items.TRANSFORMABLE_ITEMS)
                .add(ModItems.ROSETTE_ITEM)
                .add(ModItems.RAW_ROSETTE_ITEM);

        //region Cut Gems
        getOrCreateTagBuilder(ModTags.Items.MOD_CUT_GEMS)
                .add(ModItems.AMBERINE_ITEM)
                .add(ModItems.AZUROS_ITEM)
                .add(ModItems.ROSETTE_ITEM)
                .add(ModItems.CHROMITE_ITEM)
                .add(ModItems.INDIGRA_ITEM)
                .add(ModItems.MIRANITE_ITEM)
                .add(ModItems.NOCTURNITE_ITEM);
        //endregion
        //region Gem Upgradable Tools
        getOrCreateTagBuilder(ModTags.Items.GEM_UPGRADABLE_TOOLS)
                .add(ModItems.AMBERINE_SWORD)
                .add(ModItems.AMBERINE_AXE)

                .add(ModItems.AZUROS_SWORD)
                .add(ModItems.AZUROS_AXE)

                .add(ModItems.CHROMITE_SWORD)
                .add(ModItems.CHROMITE_AXE)

                .add(ModItems.ROSETTE_SWORD)
                .add(ModItems.ROSETTE_AXE)

                .add(ModItems.MIRANITE_SWORD)
                .add(ModItems.MIRANITE_AXE)

                .add(ModItems.RUBY_SWORD)
                .add(ModItems.RUBY_AXE)
                .add(ModItems.RUBY_PICKAXE)
                .add(ModItems.RUBY_HOE)
                .add(ModItems.RUBY_SHOVEL)

                .add(ModItems.XIRION_SWORD)
                .add(ModItems.XIRION_AXE)
                .add(ModItems.XIRION_PICKAXE)
                .add(ModItems.XIRION_HOE)
                .add(ModItems.XIRION_SHOVEL);
        //endregion
        //region Tool Tags
        getOrCreateTagBuilder(ConventionalItemTags.SWORDS)
                .add(ModItems.XIRION_SWORD)
                .add(ModItems.RUBY_SWORD)
                .add(ModItems.MIRANITE_SWORD)
                .add(ModItems.AMBERINE_SWORD)
                .add(ModItems.AZUROS_SWORD)
                .add(ModItems.CHROMITE_SWORD)
                .add(ModItems.ROSETTE_SWORD);

        getOrCreateTagBuilder(ConventionalItemTags.AXES)
                .add(ModItems.XIRION_AXE)
                .add(ModItems.RUBY_AXE)
                .add(ModItems.MIRANITE_AXE)
                .add(ModItems.AMBERINE_AXE)
                .add(ModItems.AZUROS_AXE)
                .add(ModItems.CHROMITE_AXE)
                .add(ModItems.ROSETTE_AXE);

        getOrCreateTagBuilder(ConventionalItemTags.HOES)
                .add(ModItems.XIRION_HOE)
                .add(ModItems.RUBY_HOE)
                .add(ModItems.MIRANITE_HOE)
                .add(ModItems.AMBERINE_HOE)
                .add(ModItems.AZUROS_HOE)
                .add(ModItems.CHROMITE_HOE)
                .add(ModItems.ROSETTE_HOE);

        getOrCreateTagBuilder(ConventionalItemTags.PICKAXES)
                .add(ModItems.XIRION_PICKAXE)
                .add(ModItems.RUBY_PICKAXE)
                .add(ModItems.MIRANITE_PICKAXE)
                .add(ModItems.AMBERINE_PICKAXE)
                .add(ModItems.AZUROS_PICKAXE)
                .add(ModItems.CHROMITE_PICKAXE)
                .add(ModItems.ROSETTE_PICKAXE);

        getOrCreateTagBuilder(ConventionalItemTags.SHOVELS)
                .add(ModItems.XIRION_SHOVEL)
                .add(ModItems.RUBY_SHOVEL)
                .add(ModItems.MIRANITE_SHOVEL)
                .add(ModItems.AMBERINE_SHOVEL)
                .add(ModItems.AZUROS_SHOVEL)
                .add(ModItems.CHROMITE_SHOVEL)
                .add(ModItems.ROSETTE_SHOVEL);

        //endregion

    }
}
