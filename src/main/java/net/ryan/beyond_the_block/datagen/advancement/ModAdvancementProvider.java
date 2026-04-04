package net.ryan.beyond_the_block.datagen.advancement;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;

import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    public ModAdvancementProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        CoreAdvancements.generate(consumer);
        OreAdvancements.generate(consumer);
        // ToolAdvancements.generate(consumer);
        // EnvironmentAdvancements.generate(consumer);
    }
}