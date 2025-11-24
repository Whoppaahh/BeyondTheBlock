package net.ryan.beyond_the_block.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {

        fabricDataGenerator.addProvider(ModLootTableProvider::new);
        fabricDataGenerator.addProvider(ModRecipeProvider::new);
        fabricDataGenerator.addProvider(ModModelProvider::new);
        fabricDataGenerator.addProvider(ModBlockTagProvider::new);
        fabricDataGenerator.addProvider(ModItemTagProvider::new);
    }
}
