package net.ryan.beyond_the_block.client.render.trim;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.content.registry.ModTrimRegistry;
import net.ryan.beyond_the_block.content.registry.family.ModArmourTrim;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public final class ArmourTrimItemPredicates {

    private static final Identifier TRIM_MATERIAL = new Identifier(BeyondTheBlock.MOD_ID, "trim_material");

    private ArmourTrimItemPredicates() {
    }

    public static void register() {
        // Vanilla armor
        registerTrimPredicates(Items.LEATHER_HELMET);
        registerTrimPredicates(Items.LEATHER_CHESTPLATE);
        registerTrimPredicates(Items.LEATHER_LEGGINGS);
        registerTrimPredicates(Items.LEATHER_BOOTS);

        registerTrimPredicates(Items.CHAINMAIL_HELMET);
        registerTrimPredicates(Items.CHAINMAIL_CHESTPLATE);
        registerTrimPredicates(Items.CHAINMAIL_LEGGINGS);
        registerTrimPredicates(Items.CHAINMAIL_BOOTS);

        registerTrimPredicates(Items.IRON_HELMET);
        registerTrimPredicates(Items.IRON_CHESTPLATE);
        registerTrimPredicates(Items.IRON_LEGGINGS);
        registerTrimPredicates(Items.IRON_BOOTS);

        registerTrimPredicates(Items.GOLDEN_HELMET);
        registerTrimPredicates(Items.GOLDEN_CHESTPLATE);
        registerTrimPredicates(Items.GOLDEN_LEGGINGS);
        registerTrimPredicates(Items.GOLDEN_BOOTS);

        registerTrimPredicates(Items.DIAMOND_HELMET);
        registerTrimPredicates(Items.DIAMOND_CHESTPLATE);
        registerTrimPredicates(Items.DIAMOND_LEGGINGS);
        registerTrimPredicates(Items.DIAMOND_BOOTS);

        registerTrimPredicates(Items.NETHERITE_HELMET);
        registerTrimPredicates(Items.NETHERITE_CHESTPLATE);
        registerTrimPredicates(Items.NETHERITE_LEGGINGS);
        registerTrimPredicates(Items.NETHERITE_BOOTS);

        // Modded armor
        registerTrimPredicates(ModItems.RUBY_HELMET);
        registerTrimPredicates(ModItems.RUBY_CHESTPLATE);
        registerTrimPredicates(ModItems.RUBY_LEGGINGS);
        registerTrimPredicates(ModItems.RUBY_BOOTS);

        registerTrimPredicates(ModItems.NOCTURNITE_HELMET);
        registerTrimPredicates(ModItems.NOCTURNITE_CHESTPLATE);
        registerTrimPredicates(ModItems.NOCTURNITE_LEGGINGS);
        registerTrimPredicates(ModItems.NOCTURNITE_BOOTS);

        registerTrimPredicates(ModItems.AZUROS_HELMET);
        registerTrimPredicates(ModItems.AZUROS_CHESTPLATE);
        registerTrimPredicates(ModItems.AZUROS_LEGGINGS);
        registerTrimPredicates(ModItems.AZUROS_BOOTS);

        registerTrimPredicates(ModItems.AMBERINE_HELMET);
        registerTrimPredicates(ModItems.AMBERINE_CHESTPLATE);
        registerTrimPredicates(ModItems.AMBERINE_LEGGINGS);
        registerTrimPredicates(ModItems.AMBERINE_BOOTS);

        registerTrimPredicates(ModItems.MIRANITE_HELMET);
        registerTrimPredicates(ModItems.MIRANITE_CHESTPLATE);
        registerTrimPredicates(ModItems.MIRANITE_LEGGINGS);
        registerTrimPredicates(ModItems.MIRANITE_BOOTS);

        registerTrimPredicates(ModItems.CHROMITE_HELMET);
        registerTrimPredicates(ModItems.CHROMITE_CHESTPLATE);
        registerTrimPredicates(ModItems.CHROMITE_LEGGINGS);
        registerTrimPredicates(ModItems.CHROMITE_BOOTS);

        registerTrimPredicates(ModItems.ROSETTE_HELMET);
        registerTrimPredicates(ModItems.ROSETTE_CHESTPLATE);
        registerTrimPredicates(ModItems.ROSETTE_LEGGINGS);
        registerTrimPredicates(ModItems.ROSETTE_BOOTS);

        registerTrimPredicates(ModItems.XIRION_HELMET);
        registerTrimPredicates(ModItems.XIRION_CHESTPLATE);
        registerTrimPredicates(ModItems.XIRION_LEGGINGS);
        registerTrimPredicates(ModItems.XIRION_BOOTS);
    }

    private static void registerTrimPredicates(Item item) {

        FabricModelPredicateProviderRegistry.register(item, TRIM_MATERIAL, (stack, world, entity, seed) ->
                ModArmourTrim.getTrim(stack)
                        .map(trim -> (float) ModTrimRegistry.getMaterialIndex(trim.materialId()))
                        .orElse(0.0F)
        );
    }
}