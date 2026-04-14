package net.ryan.beyond_the_block.content.registry;

import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.registry.family.ModTrimMaterial;
import net.ryan.beyond_the_block.content.registry.family.ModTrimPattern;

public final class ModTrimBootstrap {

    private ModTrimBootstrap() {
    }

    public static void bootstrap() {
        registerMaterials();
        registerPatterns();
    }

    private static void registerMaterials() {
        ModTrimRegistry.registerMaterial(new ModTrimMaterial(
                new Identifier("minecraft", "quartz"),
                Items.QUARTZ,
                "quartz",
                Text.translatable("trim_material.minecraft.quartz")
        ));
        ModTrimRegistry.registerMaterial(new ModTrimMaterial(
                new Identifier("minecraft", "iron"),
                Items.IRON_INGOT,
                "iron",
                Text.translatable("trim_material.minecraft.iron")
        ));
        ModTrimRegistry.registerMaterial(new ModTrimMaterial(
                new Identifier("minecraft", "netherite"),
                Items.NETHERITE_INGOT,
                "netherite",
                Text.translatable("trim_material.minecraft.netherite")
        ));
        ModTrimRegistry.registerMaterial(new ModTrimMaterial(
                new Identifier("minecraft", "redstone"),
                Items.REDSTONE,
                "redstone",
                Text.translatable("trim_material.minecraft.redstone")
        ));
        ModTrimRegistry.registerMaterial(new ModTrimMaterial(
                new Identifier("minecraft", "copper"),
                Items.COPPER_INGOT,
                "copper",
                Text.translatable("trim_material.minecraft.copper")
        ));
        ModTrimRegistry.registerMaterial(new ModTrimMaterial(
                new Identifier("minecraft", "gold"),
                Items.GOLD_INGOT,
                "gold",
                Text.translatable("trim_material.minecraft.gold")
        ));
        ModTrimRegistry.registerMaterial(new ModTrimMaterial(
                new Identifier("minecraft", "emerald"),
                Items.EMERALD,
                "emerald",
                Text.translatable("trim_material.minecraft.emerald")
        ));
        ModTrimRegistry.registerMaterial(new ModTrimMaterial(
                new Identifier("minecraft", "diamond"),
                Items.DIAMOND,
                "diamond",
                Text.translatable("trim_material.minecraft.diamond")
        ));
        ModTrimRegistry.registerMaterial(new ModTrimMaterial(
                new Identifier("minecraft", "lapis"),
                Items.LAPIS_LAZULI,
                "lapis",
                Text.translatable("trim_material.minecraft.lapis")
        ));
        ModTrimRegistry.registerMaterial(new ModTrimMaterial(
                new Identifier("minecraft", "amethyst"),
                Items.AMETHYST_SHARD,
                "amethyst",
                Text.translatable("trim_material.minecraft.amethyst")
        ));
    }

    private static void registerPatterns() {
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "coast"),
                ModItems.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
                "coast",
                Text.translatable("trim_pattern.minecraft.coast")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "dune"),
                ModItems.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,
                "dune",
                Text.translatable("trim_pattern.minecraft.dune")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "eye"),
                ModItems.EYE_ARMOR_TRIM_SMITHING_TEMPLATE,
                "eye",
                Text.translatable("trim_pattern.minecraft.eye")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "host"),
                ModItems.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
                "host",
                Text.translatable("trim_pattern.minecraft.host")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "raiser"),
                ModItems.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,
                "raiser",
                Text.translatable("trim_pattern.minecraft.raiser")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "rib"),
                ModItems.RIB_ARMOR_TRIM_SMITHING_TEMPLATE,
                "rib",
                Text.translatable("trim_pattern.minecraft.rib")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "sentry"),
                ModItems.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE,
                "sentry",
                Text.translatable("trim_pattern.minecraft.sentry")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "shaper"),
                ModItems.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE,
                "shaper",
                Text.translatable("trim_pattern.minecraft.shaper")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "silence"),
                ModItems.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE,
                "silence",
                Text.translatable("trim_pattern.minecraft.silence")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "snout"),
                ModItems.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE,
                "snout",
                Text.translatable("trim_pattern.minecraft.snout")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "spire"),
                ModItems.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,
                "spire",
                Text.translatable("trim_pattern.minecraft.spire")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "tide"),
                ModItems.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
                "tide",
                Text.translatable("trim_pattern.minecraft.tide")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "vex"),
                ModItems.VEX_ARMOR_TRIM_SMITHING_TEMPLATE,
                "vex",
                Text.translatable("trim_pattern.minecraft.vex")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "ward"),
                ModItems.WARD_ARMOR_TRIM_SMITHING_TEMPLATE,
                "ward",
                Text.translatable("trim_pattern.minecraft.ward")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "wayfinder"),
                ModItems.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE,
                "wayfinder",
                Text.translatable("trim_pattern.minecraft.wayfinder")
        ));
        ModTrimRegistry.registerPattern(new ModTrimPattern(
                new Identifier("minecraft", "wild"),
                ModItems.WILD_ARMOR_TRIM_SMITHING_TEMPLATE,
                "wild",
                Text.translatable("trim_pattern.minecraft.wild")
        ));


//        ModTrimRegistry.registerPattern(new ModTrimPattern(
//                new Identifier("minecraft", "flow"),
//                ModItems.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE,
//                "flow",
//                Text.translatable("trim_pattern.minecraft.flow")
//        ));
//
//        ModTrimRegistry.registerPattern(new ModTrimPattern(
//                new Identifier("minecraft", "bolt"),
//                ModItems.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE,
//                "bolt",
//                Text.translatable("trim_pattern.minecraft.bolt")
//        ));
    }
}