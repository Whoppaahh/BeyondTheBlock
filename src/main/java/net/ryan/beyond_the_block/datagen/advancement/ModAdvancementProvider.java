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
        Advancement root = CoreAdvancements.generateRoot(consumer);

        OreAdvancements.generate(root, consumer);
        ToolAdvancements.generate(root, consumer);
        ArmourAdvancements.generate(root, consumer);
        EnvironmentAdvancements.generate(root, consumer);
        UtilityAdvancements.generate(root, consumer);
        ShrineAdvancements.generate(root, consumer);
    }
}