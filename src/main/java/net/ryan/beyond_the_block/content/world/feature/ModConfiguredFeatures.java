package net.ryan.beyond_the_block.content.world.feature;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.feature.util.CaveSurface;
import net.minecraft.world.gen.foliage.DarkOakFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.block.PinkPetalsBlock;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.content.world.tree.CherryFoliagePlacer;
import net.ryan.beyond_the_block.content.world.tree.CherryTrunkPlacer;
import net.ryan.beyond_the_block.content.world.tree.decorator.PaleMossTreeDecorator;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.List;
import java.util.OptionalInt;

public class ModConfiguredFeatures {

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> CHERRY_TREE =
            ConfiguredFeatures.register("cherry_tree", Feature.TREE,
                    new TreeFeatureConfig.Builder(
                            BlockStateProvider.of(ModBlocks.CHERRY_SET.log()),
                            new CherryTrunkPlacer(
                                    7, 1, 0,
                                    ConstantIntProvider.create(2),
                                    UniformIntProvider.create(3, 5),
                                    UniformIntProvider.create(-4, -2),
                                    ConstantIntProvider.create(1)
                            ),
                            BlockStateProvider.of(ModBlocks.CHERRY_LEAVES),
                            new CherryFoliagePlacer(
                                    ConstantIntProvider.create(2),
                                    ConstantIntProvider.create(0),
                                    ConstantIntProvider.create(3),
                                    0.35F,
                                    0.2F,
                                    0.35F,
                                    0.15F
                            ),
                            new TwoLayersFeatureSize(1, 0, 2)
                    )
                            .dirtProvider(BlockStateProvider.of(Blocks.DIRT))
                            .ignoreVines()
                            .build()
            );
    private static final BlockStateProvider PINK_PETALS_PROVIDER = createPinkPetalsProvider();
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PINK_PETALS_PATCH =
            ConfiguredFeatures.register("pink_petals_patch", Feature.RANDOM_PATCH,
                    ConfiguredFeatures.createRandomPatchFeatureConfig(
                            96,
                            PlacedFeatures.createEntry(
                                    Feature.SIMPLE_BLOCK,
                                    new SimpleBlockFeatureConfig(
                                            (PINK_PETALS_PROVIDER)
                                    ),
                                    BlockFilterPlacementModifier.of(
                                            BlockPredicate.allOf(
                                                    BlockPredicate.replaceable(),
                                                    BlockPredicate.wouldSurvive(ModBlocks.PINK_PETALS.getDefaultState(), BlockPos.ORIGIN),
                                                    BlockPredicate.matchingBlocks(
                                                            BlockPos.ORIGIN.down(),
                                                            List.of(
                                                                    Blocks.GRASS_BLOCK,
                                                                    Blocks.DIRT,
                                                                    Blocks.COARSE_DIRT,
                                                                    Blocks.PODZOL,
                                                                    Blocks.ROOTED_DIRT,
                                                                    Blocks.MOSS_BLOCK
                                                            )
                                                    )
                                            )
                            )
                    )
            ));

    private static WeightedBlockStateProvider createPinkPetalsProvider() {
        DataPool.Builder<BlockState> builder = DataPool.builder();

        for (Direction direction : Direction.Type.HORIZONTAL) {
            builder.add(ModBlocks.PINK_PETALS.getDefaultState()
                    .with(PinkPetalsBlock.FACING, direction)
                    .with(PinkPetalsBlock.FLOWER_AMOUNT, 1), 8);

            builder.add(ModBlocks.PINK_PETALS.getDefaultState()
                    .with(PinkPetalsBlock.FACING, direction)
                    .with(PinkPetalsBlock.FLOWER_AMOUNT, 2), 5);

            builder.add(ModBlocks.PINK_PETALS.getDefaultState()
                    .with(PinkPetalsBlock.FACING, direction)
                    .with(PinkPetalsBlock.FLOWER_AMOUNT, 3), 3);

            builder.add(ModBlocks.PINK_PETALS.getDefaultState()
                    .with(PinkPetalsBlock.FACING, direction)
                    .with(PinkPetalsBlock.FLOWER_AMOUNT, 4), 1);
        }

        return new WeightedBlockStateProvider(builder);
    }
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> PALE_OAK_TREE =
            ConfiguredFeatures.register("pale_oak_tree", Feature.TREE,
                    new TreeFeatureConfig.Builder(
                            BlockStateProvider.of(ModBlocks.PALE_OAK_SET.log()),
                            new DarkOakTrunkPlacer(6, 2, 1),
                            BlockStateProvider.of(ModBlocks.PALE_OAK_LEAVES),
                            new DarkOakFoliagePlacer(ConstantIntProvider.create(0), ConstantIntProvider.create(0)),
                            new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                    )
                            .dirtProvider(BlockStateProvider.of(Blocks.DIRT))
                            .ignoreVines()
                            .build()
            );

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> PALE_OAK_TREE_NATURAL =
            ConfiguredFeatures.register("pale_oak_tree_natural", Feature.TREE,
                    new TreeFeatureConfig.Builder(
                            BlockStateProvider.of(ModBlocks.PALE_OAK_SET.log()),
                            new DarkOakTrunkPlacer(6, 2, 1),
                            BlockStateProvider.of(ModBlocks.PALE_OAK_LEAVES),
                            new DarkOakFoliagePlacer(
                                    ConstantIntProvider.create(0),
                                    ConstantIntProvider.create(0)
                            ),
                            new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                    )
                            .dirtProvider(BlockStateProvider.of(Blocks.DIRT))
                            .decorators(List.of(
                                    new PaleMossTreeDecorator(0.8F, 0.15F, 0.4F)
                            ))
                            .ignoreVines()
                            .build()
            );

    public static final RegistryEntry<ConfiguredFeature<VegetationPatchFeatureConfig, ?>> PALE_MOSS_PATCH =
            ConfiguredFeatures.register("pale_moss_patch", Feature.VEGETATION_PATCH,
                    new VegetationPatchFeatureConfig(
                            BlockTags.MOSS_REPLACEABLE,
                            BlockStateProvider.of(ModBlocks.PALE_MOSS_BLOCK),
                            PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                                    new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.PALE_MOSS_CARPET))),
                            VerticalSurfaceType.FLOOR,
                            UniformIntProvider.create(2, 4),
                            0.3F,
                            5,
                            0.35F,
                            UniformIntProvider.create(2, 4),
                            1
                    )
            );

    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> EYEBLOSSOM_PATCH =
            ConfiguredFeatures.register("eyeblossom_patch", Feature.RANDOM_PATCH,
                    ConfiguredFeatures.createRandomPatchFeatureConfig(
                            24,
                            PlacedFeatures.createEntry(
                                    Feature.SIMPLE_BLOCK,
                                    new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.CLOSED_EYEBLOSSOM))
                            )
                    )
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
