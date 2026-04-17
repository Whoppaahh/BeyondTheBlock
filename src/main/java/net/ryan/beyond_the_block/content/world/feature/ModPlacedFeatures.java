package net.ryan.beyond_the_block.content.world.feature;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;
import net.ryan.beyond_the_block.content.registry.ModBlocks;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryEntry<PlacedFeature> CHERRY_TREE_PLACED = PlacedFeatures.register(
            "cherry_tree_placed",
            ModConfiguredFeatures.CHERRY_TREE,
            PlacedFeatures.createCountExtraModifier(2, 0.1f, 1),
            SquarePlacementModifier.of(),
            PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
            PlacedFeatures.wouldSurvive(ModBlocks.CHERRY_SAPLING),
            BiomePlacementModifier.of()
    );

    public static final RegistryEntry<PlacedFeature> PALE_OAK_TREE_PLACED = PlacedFeatures.register(
            "pale_oak_tree_placed",
            ModConfiguredFeatures.PALE_OAK_TREE,
            PlacedFeatures.createCountExtraModifier(3, 0.1f, 1),
            SquarePlacementModifier.of(),
            PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
            PlacedFeatures.wouldSurvive(ModBlocks.PALE_OAK_SAPLING),
            BiomePlacementModifier.of()
    );

    //region XP
    public static final RegistryEntry<PlacedFeature> XP_ORE_PLACED = PlacedFeatures.register("xp_ore_placed",
            ModConfiguredFeatures.XP_ORE, modifiersWithRarity(6,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(-48), YOffset.fixed(0))));
    public static final RegistryEntry<PlacedFeature> NETHER_XP_ORE_PLACED = PlacedFeatures.register("nether_xp_ore_placed",
            ModConfiguredFeatures.NETHER_XP_ORE, modifiersWithRarity(6,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(48))));
    public static final RegistryEntry<PlacedFeature> END_XP_ORE_PLACED = PlacedFeatures.register("end_xp_ore_placed",
            ModConfiguredFeatures.END_XP_ORE, modifiersWithCount(6,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(48))));
    //endregion
    //region Ruby
    public static final RegistryEntry<PlacedFeature> RUBY_ORE_PLACED = PlacedFeatures.register("ruby_ore_placed",
            ModConfiguredFeatures.RUBY_ORE, modifiersWithRarity(6,
                    HeightRangePlacementModifier.trapezoid(YOffset.fixed(-80), YOffset.fixed(80))));
    //endregion
    //region Miranite
    public static final RegistryEntry<PlacedFeature> MIRANITE_ORE_PLACED = PlacedFeatures.register("miranite_ore_placed",
            ModConfiguredFeatures.MIRANITE_ORE, modifiersWithCount(2,
                    HeightRangePlacementModifier.trapezoid(YOffset.fixed(-80), YOffset.fixed(80))));
    public static final RegistryEntry<PlacedFeature> NETHER_MIRANITE_ORE_PLACED = PlacedFeatures.register("nether_miranite_ore_placed",
            ModConfiguredFeatures.NETHER_MIRANITE_ORE, modifiersWithCount(2,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(80))));
    public static final RegistryEntry<PlacedFeature> END_MIRANITE_ORE_PLACED = PlacedFeatures.register("end_miranite_ore_placed",
            ModConfiguredFeatures.END_MIRANITE_ORE, modifiersWithCount(2,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(80))));
    //endregion
    //region Chromite
    public static final RegistryEntry<PlacedFeature> CHROMITE_ORE_PLACED = PlacedFeatures.register("chromite_ore_placed",
            ModConfiguredFeatures.CHROMITE_ORE, modifiersWithCount(10,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(-32), YOffset.fixed(16))));
    public static final RegistryEntry<PlacedFeature> NETHER_CHROMITE_ORE_PLACED = PlacedFeatures.register("nether_chromite_ore_placed",
            ModConfiguredFeatures.NETHER_CHROMITE_ORE, modifiersWithCount(10,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(46))));
    public static final RegistryEntry<PlacedFeature> END_CHROMITE_ORE_PLACED = PlacedFeatures.register("end_chromite_ore_placed",
            ModConfiguredFeatures.END_CHROMITE_ORE, modifiersWithCount(10,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(32))));
    //endregion
    //region Nocturnite
    public static final RegistryEntry<PlacedFeature> NOCTURNITE_ORE_PLACED = PlacedFeatures.register("nocturnite_ore_placed",
            ModConfiguredFeatures.NOCTURNITE_ORE, modifiersWithRarity(6,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(-48), YOffset.fixed(0))));
    public static final RegistryEntry<PlacedFeature> NETHER_NOCTURNITE_ORE_PLACED = PlacedFeatures.register("nether_nocturnite_ore_placed",
            ModConfiguredFeatures.NETHER_NOCTURNITE_ORE, modifiersWithRarity(6,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(48))));
    public static final RegistryEntry<PlacedFeature> END_NOCTURNITE_ORE_PLACED = PlacedFeatures.register("end_nocturnite_ore_placed",
            ModConfiguredFeatures.END_NOCTURNITE_ORE, modifiersWithCount(6,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(48))));
    //endregion
    //region Amberine
    public static final RegistryEntry<PlacedFeature> AMBERINE_ORE_PLACED = PlacedFeatures.register("amberine_ore_placed",
            ModConfiguredFeatures.AMBERINE_ORE, modifiersWithCount(9,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(32), YOffset.fixed(64))));
    //endregion
    //region Rosette
    public static final RegistryEntry<PlacedFeature> ROSETTE_ORE_PLACED = PlacedFeatures.register("rosette_ore_placed",
            ModConfiguredFeatures.ROSETTE_ORE, modifiersWithCount(8,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(16), YOffset.fixed(48))));
    public static final RegistryEntry<PlacedFeature> NETHER_ROSETTE_ORE_PLACED = PlacedFeatures.register("nether_rosette_ore_placed",
            ModConfiguredFeatures.NETHER_ROSETTE_ORE, modifiersWithCount(8,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(16), YOffset.fixed(48))));
    public static final RegistryEntry<PlacedFeature> END_ROSETTE_ORE_PLACED = PlacedFeatures.register("end_rosette_ore_placed",
            ModConfiguredFeatures.END_ROSETTE_ORE, modifiersWithCount(8,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(16), YOffset.fixed(48))));
    //endregion
    //region Azuros
    public static final RegistryEntry<PlacedFeature> AZUROS_ORE_PLACED = PlacedFeatures.register("azuros_ore_placed",
            ModConfiguredFeatures.AZUROS_ORE, modifiersWithCount(7,
                    HeightRangePlacementModifier.trapezoid(YOffset.fixed(-16), YOffset.fixed(32))));
    public static final RegistryEntry<PlacedFeature> NETHER_AZUROS_ORE_PLACED = PlacedFeatures.register("nether_azuros_ore_placed",
            ModConfiguredFeatures.NETHER_AZUROS_ORE, modifiersWithCount(7,
                    HeightRangePlacementModifier.trapezoid(YOffset.fixed(0), YOffset.fixed(32))));
    public static final RegistryEntry<PlacedFeature> END_AZUROS_ORE_PLACED = PlacedFeatures.register("end_azuros_ore_placed",
            ModConfiguredFeatures.END_AZUROS_ORE, modifiersWithCount(7,
                    HeightRangePlacementModifier.trapezoid(YOffset.fixed(0), YOffset.fixed(32))));
    //endregion
    //region Indigra
    public static final RegistryEntry<PlacedFeature> INDIGRA_ORE_PLACED = PlacedFeatures.register("indigra_ore_placed",
            ModConfiguredFeatures.INDIGRA_ORE, modifiersWithCount(4,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(-40), YOffset.fixed(8))));
    public static final RegistryEntry<PlacedFeature> NETHER_INDIGRA_ORE_PLACED = PlacedFeatures.register("nether_indigra_ore_placed",
            ModConfiguredFeatures.NETHER_INDIGRA_ORE, modifiersWithCount(4,
                    HeightRangePlacementModifier.trapezoid(YOffset.fixed(0), YOffset.fixed(58))));
    public static final RegistryEntry<PlacedFeature> END_INDIGRA_ORE_PLACED = PlacedFeatures.register("end_indigra_ore_placed",
            ModConfiguredFeatures.END_INDIGRA_ORE, modifiersWithCount(4,
                    HeightRangePlacementModifier.trapezoid(YOffset.fixed(0), YOffset.fixed(58))));
    //endregion
    //region Xirion
    public static final RegistryEntry<PlacedFeature> XIRION_ORE_PLACED = PlacedFeatures.register("xirion_ore_placed",
            ModConfiguredFeatures.XIRION_ORE, modifiersWithCount(9,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(-50), YOffset.fixed(80))));
    public static final RegistryEntry<PlacedFeature> NETHER_XIRION_ORE_PLACED = PlacedFeatures.register("nether_xirion_ore_placed",
            ModConfiguredFeatures.NETHER_XIRION_ORE, modifiersWithCount(9,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(50))));
    public static final RegistryEntry<PlacedFeature> END_XIRION_ORE_PLACED = PlacedFeatures.register("end_xirion_ore_placed",
            ModConfiguredFeatures.END_XIRION_ORE, modifiersWithCount(9,
                    HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(50))));
    //endregion

    private static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacementModifier.of(count), heightModifier);
    }

    private static List<PlacementModifier> modifiersWithRarity(int chance, PlacementModifier heightModifier) {
        return modifiers(RarityFilterPlacementModifier.of(chance), heightModifier);
    }


}
