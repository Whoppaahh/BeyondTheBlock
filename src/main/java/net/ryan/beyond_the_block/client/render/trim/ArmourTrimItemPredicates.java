package net.ryan.beyond_the_block.client.render.trim;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.content.registry.family.ModArmourTrim;

public final class ArmourTrimItemPredicates {

    private ArmourTrimItemPredicates() {
    }

    public static void register() {
        registerVanillaArmor(Items.IRON_HELMET);
        registerVanillaArmor(Items.IRON_CHESTPLATE);
        registerVanillaArmor(Items.IRON_LEGGINGS);
        registerVanillaArmor(Items.IRON_BOOTS);

        registerVanillaArmor(Items.DIAMOND_HELMET);
        registerVanillaArmor(Items.DIAMOND_CHESTPLATE);
        registerVanillaArmor(Items.DIAMOND_LEGGINGS);
        registerVanillaArmor(Items.DIAMOND_BOOTS);

        registerVanillaArmor(Items.GOLDEN_HELMET);
        registerVanillaArmor(Items.GOLDEN_CHESTPLATE);
        registerVanillaArmor(Items.GOLDEN_LEGGINGS);
        registerVanillaArmor(Items.GOLDEN_BOOTS);

        registerVanillaArmor(Items.NETHERITE_HELMET);
        registerVanillaArmor(Items.NETHERITE_CHESTPLATE);
        registerVanillaArmor(Items.NETHERITE_LEGGINGS);
        registerVanillaArmor(Items.NETHERITE_BOOTS);

        registerVanillaArmor(Items.CHAINMAIL_HELMET);
        registerVanillaArmor(Items.CHAINMAIL_CHESTPLATE);
        registerVanillaArmor(Items.CHAINMAIL_LEGGINGS);
        registerVanillaArmor(Items.CHAINMAIL_BOOTS);

        registerVanillaArmor(Items.LEATHER_HELMET);
        registerVanillaArmor(Items.LEATHER_CHESTPLATE);
        registerVanillaArmor(Items.LEATHER_LEGGINGS);
        registerVanillaArmor(Items.LEATHER_BOOTS);

        registerVanillaArmor(ModItems.NOCTURNITE_HELMET);
        registerVanillaArmor(ModItems.NOCTURNITE_CHESTPLATE);
        registerVanillaArmor(ModItems.NOCTURNITE_LEGGINGS);
        registerVanillaArmor(ModItems.NOCTURNITE_BOOTS);

        registerVanillaArmor(ModItems.RUBY_HELMET);
        registerVanillaArmor(ModItems.RUBY_CHESTPLATE);
        registerVanillaArmor(ModItems.RUBY_LEGGINGS);
        registerVanillaArmor(ModItems.RUBY_BOOTS);

        registerVanillaArmor(ModItems.AMBERINE_HELMET);
        registerVanillaArmor(ModItems.AMBERINE_CHESTPLATE);
        registerVanillaArmor(ModItems.AMBERINE_LEGGINGS);
        registerVanillaArmor(ModItems.AMBERINE_BOOTS);

        registerVanillaArmor(ModItems.AZUROS_HELMET);
        registerVanillaArmor(ModItems.AZUROS_CHESTPLATE);
        registerVanillaArmor(ModItems.AZUROS_LEGGINGS);
        registerVanillaArmor(ModItems.AZUROS_BOOTS);

        registerVanillaArmor(ModItems.ROSETTE_HELMET);
        registerVanillaArmor(ModItems.ROSETTE_CHESTPLATE);
        registerVanillaArmor(ModItems.ROSETTE_LEGGINGS);
        registerVanillaArmor(ModItems.ROSETTE_BOOTS);

        registerVanillaArmor(ModItems.XIRION_HELMET);
        registerVanillaArmor(ModItems.XIRION_CHESTPLATE);
        registerVanillaArmor(ModItems.XIRION_LEGGINGS);
        registerVanillaArmor(ModItems.XIRION_BOOTS);

        registerVanillaArmor(ModItems.CHROMITE_HELMET);
        registerVanillaArmor(ModItems.CHROMITE_CHESTPLATE);
        registerVanillaArmor(ModItems.CHROMITE_LEGGINGS);
        registerVanillaArmor(ModItems.CHROMITE_BOOTS);

        registerVanillaArmor(ModItems.MIRANITE_HELMET);
        registerVanillaArmor(ModItems.MIRANITE_CHESTPLATE);
        registerVanillaArmor(ModItems.MIRANITE_LEGGINGS);
        registerVanillaArmor(ModItems.MIRANITE_BOOTS);
    }

    private static void registerVanillaArmor(Item item) {
        registerPatternPredicate(item, "coast");
        registerPatternPredicate(item, "dune");
        registerPatternPredicate(item, "eye");
        registerPatternPredicate(item, "host");
        registerPatternPredicate(item, "raiser");
        registerPatternPredicate(item, "rib");
        registerPatternPredicate(item, "sentry");
        registerPatternPredicate(item, "shaper");
        registerPatternPredicate(item, "silence");
        registerPatternPredicate(item, "snout");
        registerPatternPredicate(item, "spire");
        registerPatternPredicate(item, "tide");
        registerPatternPredicate(item, "vex");
        registerPatternPredicate(item, "ward");
        registerPatternPredicate(item, "wayfinder");
        registerPatternPredicate(item, "wild");
    }

    private static void registerPatternPredicate(Item item, String patternPath) {
        FabricModelPredicateProviderRegistry.register(
                item,
                new Identifier("beyond_the_block", "trim_" + patternPath),
                (stack, world, entity, seed) -> ModArmourTrim.getTrim(stack)
                        .map(trim -> trim.patternId().getPath().equals(patternPath) ? 1.0F : 0.0F)
                        .orElse(0.0F)
        );
    }
}