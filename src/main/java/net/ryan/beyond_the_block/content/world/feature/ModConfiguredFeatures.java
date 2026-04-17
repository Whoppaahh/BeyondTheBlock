package net.ryan.beyond_the_block.content.world.feature;

import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.List;

public class ModConfiguredFeatures {

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> CHERRY_TREE =
            ConfiguredFeatures.register("cherry_tree", Feature.TREE,
                    new TreeFeatureConfig.Builder(
                            BlockStateProvider.of(ModBlocks.CHERRY_LOG),
                            new StraightTrunkPlacer(7, 2, 0),
                            BlockStateProvider.of(ModBlocks.CHERRY_LEAVES),
                            new BlobFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), 4),
                            new TwoLayersFeatureSize(1, 0, 2)
                    )
                            .dirtProvider(BlockStateProvider.of(Blocks.DIRT))
                            .ignoreVines()
                            .build()
            );

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> PALE_OAK_TREE =
            ConfiguredFeatures.register("pale_oak_tree", Feature.TREE,
                    new TreeFeatureConfig.Builder(
                            BlockStateProvider.of(ModBlocks.PALE_OAK_LOG),
                            new StraightTrunkPlacer(6, 2, 0),
                            BlockStateProvider.of(ModBlocks.PALE_OAK_LEAVES),
                            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                            new TwoLayersFeatureSize(1, 0, 1)
                    )
                            .dirtProvider(BlockStateProvider.of(Blocks.DIRT))
                            .ignoreVines()
                            .build()
            );

    //region XP
    public static final List<OreFeatureConfig.Target> OVERWORLD_XP_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.EXPERIENCE_BLOCK.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.EXPERIENCE_BLOCK.getDefaultState()));
    public static final List<OreFeatureConfig.Target> NETHER_XP_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.BASE_STONE_NETHER, ModBlocks.EXPERIENCE_BLOCK.getDefaultState()));
    public static final List<OreFeatureConfig.Target> END_XP_ORES = List.of(
            OreFeatureConfig.createTarget(new BlockMatchRuleTest(Blocks.END_STONE), ModBlocks.EXPERIENCE_BLOCK.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> XP_ORE =
            ConfiguredFeatures.register("xp_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_XP_ORES, Configs.server().worldgen.ores.xpOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> NETHER_XP_ORE =
            ConfiguredFeatures.register("nether_xp_ore",Feature.ORE, new OreFeatureConfig(NETHER_XP_ORES, Configs.server().worldgen.ores.xpOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> END_XP_ORE =
            ConfiguredFeatures.register("end_xp_ore",Feature.ORE, new OreFeatureConfig(END_XP_ORES, Configs.server().worldgen.ores.xpOre));
    //endregion
    //region Ruby
    private static final List<OreFeatureConfig.Target> OVERWORLD_RUBY_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.RUBY_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_RUBY_ORE.getDefaultState()));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> RUBY_ORE =
            ConfiguredFeatures.register("ruby_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_RUBY_ORES, Configs.server().worldgen.ores.rubyOre));
//endregion
    //region Miranite
    public static final List<OreFeatureConfig.Target> OVERWORLD_MIRANITE_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.MIRANITE_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_MIRANITE_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> NETHER_MIRANITE_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.BASE_STONE_NETHER, ModBlocks.NETHER_MIRANITE_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> END_MIRANITE_ORES = List.of(
            OreFeatureConfig.createTarget(new BlockMatchRuleTest(Blocks.END_STONE), ModBlocks.END_MIRANITE_ORE.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> MIRANITE_ORE =
            ConfiguredFeatures.register("miranite_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_MIRANITE_ORES, Configs.server().worldgen.ores.miraniteOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> NETHER_MIRANITE_ORE =
            ConfiguredFeatures.register("nether_miranite_ore",Feature.ORE, new OreFeatureConfig(NETHER_MIRANITE_ORES, Configs.server().worldgen.ores.miraniteOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> END_MIRANITE_ORE =
            ConfiguredFeatures.register("end_miranite_ore",Feature.ORE, new OreFeatureConfig(END_MIRANITE_ORES, Configs.server().worldgen.ores.miraniteOre));
    //endregion
    //region Chromite
    public static final List<OreFeatureConfig.Target> OVERWORLD_CHROMITE_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.CHROMITE_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_CHROMITE_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> NETHER_CHROMITE_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.BASE_STONE_NETHER, ModBlocks.NETHER_CHROMITE_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> END_CHROMITE_ORES = List.of(
            OreFeatureConfig.createTarget(new BlockMatchRuleTest(Blocks.END_STONE), ModBlocks.END_CHROMITE_ORE.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> CHROMITE_ORE =
            ConfiguredFeatures.register("chromite_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_CHROMITE_ORES, Configs.server().worldgen.ores.chromiteOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> NETHER_CHROMITE_ORE =
            ConfiguredFeatures.register("nether_chromite_ore",Feature.ORE, new OreFeatureConfig(NETHER_CHROMITE_ORES, Configs.server().worldgen.ores.chromiteOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> END_CHROMITE_ORE =
            ConfiguredFeatures.register("end_chromite_ore",Feature.ORE, new OreFeatureConfig(END_CHROMITE_ORES, Configs.server().worldgen.ores.chromiteOre));
    //endregion
    //region Nocturnite
    public static final List<OreFeatureConfig.Target> OVERWORLD_NOCTURNITE_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.NOCTURNITE_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_NOCTURNITE_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> NETHER_NOCTURNITE_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.BASE_STONE_NETHER, ModBlocks.NETHER_NOCTURNITE_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> END_NOCTURNITE_ORES = List.of(
            OreFeatureConfig.createTarget(new BlockMatchRuleTest(Blocks.END_STONE), ModBlocks.END_NOCTURNITE_ORE.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> NOCTURNITE_ORE =
            ConfiguredFeatures.register("nocturnite_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_NOCTURNITE_ORES, Configs.server().worldgen.ores.nocturniteOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> NETHER_NOCTURNITE_ORE =
            ConfiguredFeatures.register("nether_nocturnite_ore",Feature.ORE, new OreFeatureConfig(NETHER_NOCTURNITE_ORES, Configs.server().worldgen.ores.nocturniteOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> END_NOCTURNITE_ORE =
            ConfiguredFeatures.register("end_nocturnite_ore",Feature.ORE, new OreFeatureConfig(END_NOCTURNITE_ORES, Configs.server().worldgen.ores.nocturniteOre));
    //endregion
    //region Amberine
    public static final List<OreFeatureConfig.Target> OVERWORLD_AMBERINE_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.AMBERINE_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_AMBERINE_ORE.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> AMBERINE_ORE =
            ConfiguredFeatures.register("amberine_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_AMBERINE_ORES, Configs.server().worldgen.ores.amberineOre));
    //endregion
    //region Rosette
    public static final List<OreFeatureConfig.Target> OVERWORLD_ROSETTE_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.ROSETTE_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_ROSETTE_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> NETHER_ROSETTE_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.BASE_STONE_NETHER, ModBlocks.NETHER_ROSETTE_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> END_ROSETTE_ORES = List.of(
            OreFeatureConfig.createTarget(new BlockMatchRuleTest(Blocks.END_STONE), ModBlocks.END_ROSETTE_ORE.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ROSETTE_ORE =
            ConfiguredFeatures.register("rosette_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_ROSETTE_ORES, Configs.server().worldgen.ores.rosetteOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> NETHER_ROSETTE_ORE =
            ConfiguredFeatures.register("nether_rosette_ore",Feature.ORE, new OreFeatureConfig(NETHER_ROSETTE_ORES, Configs.server().worldgen.ores.rosetteOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> END_ROSETTE_ORE =
            ConfiguredFeatures.register("end_rosette_ore",Feature.ORE, new OreFeatureConfig(END_ROSETTE_ORES, Configs.server().worldgen.ores.rosetteOre));
    //endregion
    //region Azuros
    public static final List<OreFeatureConfig.Target> OVERWORLD_AZUROS_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.AZUROS_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_AZUROS_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> NETHER_AZUROS_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.BASE_STONE_NETHER, ModBlocks.NETHER_AZUROS_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> END_AZUROS_ORES = List.of(
            OreFeatureConfig.createTarget(new BlockMatchRuleTest(Blocks.END_STONE), ModBlocks.END_AZUROS_ORE.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> AZUROS_ORE =
            ConfiguredFeatures.register("azuros_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_AZUROS_ORES, Configs.server().worldgen.ores.azurosOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> NETHER_AZUROS_ORE =
            ConfiguredFeatures.register("nether_azuros_ore",Feature.ORE, new OreFeatureConfig(NETHER_AZUROS_ORES, Configs.server().worldgen.ores.azurosOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> END_AZUROS_ORE =
            ConfiguredFeatures.register("end_azuros_ore",Feature.ORE, new OreFeatureConfig(END_AZUROS_ORES, Configs.server().worldgen.ores.azurosOre));
    //endregion
    //region Indigra
    public static final List<OreFeatureConfig.Target> OVERWORLD_INDIGRA_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.INDIGRA_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_INDIGRA_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> NETHER_INDIGRA_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.BASE_STONE_NETHER, ModBlocks.NETHER_INDIGRA_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> END_INDIGRA_ORES = List.of(
            OreFeatureConfig.createTarget(new BlockMatchRuleTest(Blocks.END_STONE), ModBlocks.END_INDIGRA_ORE.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> INDIGRA_ORE =
            ConfiguredFeatures.register("indigra_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_INDIGRA_ORES, Configs.server().worldgen.ores.indigraOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> NETHER_INDIGRA_ORE =
            ConfiguredFeatures.register("nether_indigra_ore",Feature.ORE, new OreFeatureConfig(NETHER_INDIGRA_ORES, Configs.server().worldgen.ores.indigraOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> END_INDIGRA_ORE =
            ConfiguredFeatures.register("end_indigra_ore",Feature.ORE, new OreFeatureConfig(END_INDIGRA_ORES, Configs.server().worldgen.ores.indigraOre));
    //endregion
    //region Xirion
    public static final List<OreFeatureConfig.Target> OVERWORLD_XIRION_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.XIRION_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_XIRION_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> NETHER_XIRION_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.BASE_STONE_NETHER, ModBlocks.NETHER_XIRION_ORE.getDefaultState()));
    public static final List<OreFeatureConfig.Target> END_XIRION_ORES = List.of(
            OreFeatureConfig.createTarget(new BlockMatchRuleTest(Blocks.END_STONE), ModBlocks.END_XIRION_ORE.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> XIRION_ORE =
            ConfiguredFeatures.register("xirion_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_XIRION_ORES, Configs.server().worldgen.ores.xirionOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> NETHER_XIRION_ORE =
            ConfiguredFeatures.register("nether_xirion_ore",Feature.ORE, new OreFeatureConfig(NETHER_XIRION_ORES, Configs.server().worldgen.ores.xirionOre));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> END_XIRION_ORE =
            ConfiguredFeatures.register("end_xirion_ore",Feature.ORE, new OreFeatureConfig(END_XIRION_ORES, Configs.server().worldgen.ores.xirionOre));
    //endregion
    public static void registerConfiguredFeatures(){
        BeyondTheBlock.LOGGER.info("Registering Configured Features for " + BeyondTheBlock.MOD_ID);
    }

}
