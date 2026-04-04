package net.ryan.beyond_the_block.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.ryan.beyond_the_block.datagen.advancement.ModAdvancementProvider;
import net.ryan.beyond_the_block.datagen.lang.ModEnglishLangProvider;
import net.ryan.beyond_the_block.datagen.loot.ModLootTableProvider;
import net.ryan.beyond_the_block.datagen.models.ModModelProvider;
import net.ryan.beyond_the_block.datagen.recipes.ModRecipeProvider;
import net.ryan.beyond_the_block.datagen.tags.ModBlockTagProvider;
import net.ryan.beyond_the_block.datagen.tags.ModItemTagProvider;

public class ModDataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {

        fabricDataGenerator.addProvider(ModLootTableProvider::new);
        fabricDataGenerator.addProvider(ModRecipeProvider::new);
        fabricDataGenerator.addProvider(ModModelProvider::new);
        fabricDataGenerator.addProvider(ModBlockTagProvider::new);
        fabricDataGenerator.addProvider(ModItemTagProvider::new);
        fabricDataGenerator.addProvider(ModAdvancementProvider::new);
        fabricDataGenerator.addProvider(ModEnglishLangProvider::new);
    }
}
