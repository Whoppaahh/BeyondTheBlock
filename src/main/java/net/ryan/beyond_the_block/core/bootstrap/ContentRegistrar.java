package net.ryan.beyond_the_block.core.bootstrap;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.ryan.beyond_the_block.advancements.ModCriteria;
import net.ryan.beyond_the_block.content.entity.villager.ModVillagers;
import net.ryan.beyond_the_block.content.registry.*;
import net.ryan.beyond_the_block.content.registry.family.ModSignTypes;
import net.ryan.beyond_the_block.content.riddles.RiddleComponents;
import net.ryan.beyond_the_block.content.sound.ModSounds;
import net.ryan.beyond_the_block.content.world.biome.ModBiomeGeneration;
import net.ryan.beyond_the_block.content.world.biome.ModBiomes;
import net.ryan.beyond_the_block.content.world.dimension.ModDimensions;
import net.ryan.beyond_the_block.content.world.feature.ModConfiguredFeatures;
import net.ryan.beyond_the_block.content.world.feature.ModPlacedFeatures;
import net.ryan.beyond_the_block.content.world.gen.ModOreGeneration;
import net.ryan.beyond_the_block.content.world.gen.ModTreeGeneration;
import net.ryan.beyond_the_block.content.world.tree.ModFoliagePlacerTypes;
import net.ryan.beyond_the_block.content.world.tree.ModTrunkPlacerTypes;
import net.ryan.beyond_the_block.content.world.tree.decorator.ModTreeDecoratorTypes;
import net.ryan.beyond_the_block.event.ModEvents;
import net.ryan.beyond_the_block.network.ServerNetworking;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;
import net.ryan.beyond_the_block.utils.ModLootTableModifiers;
import net.ryan.beyond_the_block.utils.ModTags;
import net.ryan.beyond_the_block.utils.helpers.SandToGlassManager;
import terrablender.api.Regions;


public class ContentRegistrar {

    private static final int ASTRACINDER_FUEL = 22500;
    private static final int ECLIPSED_BLOOM_FUEL = 32767;

    public static final RiddleComponents RIDDLE_COMPONENTS = new RiddleComponents();

    public static void register() {
        ModTreeDecoratorTypes.register();
        ModTrunkPlacerTypes.register();
        ModFoliagePlacerTypes.register();

        ModConfiguredFeatures.registerConfiguredFeatures();
        ModBiomes.register();

        ModSignTypes.init();
        ModBlocks.registerModBlocks();
        ModBlockEntities.registerModBlockEntities();
        ModItems.registerModItems();
        ModTrimBootstrap.bootstrap();
        ModVillagers.registerModVillagers();
        ModRecipes.registerModRecipes();
        ModParticles.registerModParticles();
        ModEnchantments.registerModEnchantments();
        ModEffects.registerEffects();
        ModTags.registerModTags();
        ModOreGeneration.generateModOres();
        ModTreeGeneration.generateModTrees();
        ModLootTableModifiers.modifyLootTables();
        ModDimensions.register();
        ModEntities.registerEntities();
        ModScreenHandlers.registerScreenHandlers();
        ModSounds.registerSounds();
        SandToGlassManager.register();
        ModCriteria.init();


        registerFuel();
        RIDDLE_COMPONENTS.loadFromJson();
        ServerNetworking.registerC2SPackets();
        ModEvents.register();
    }

    private static void registerFuel() {
        FuelRegistry.INSTANCE.add(ModItems.ASTRACINDER, ASTRACINDER_FUEL);
        FuelRegistry.INSTANCE.add(ModItems.ECLIPSED_BLOOM, ECLIPSED_BLOOM_FUEL);
    }
}
