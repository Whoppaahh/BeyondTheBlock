package net.ryan.beyond_the_block.utils;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.entity.EntityEquipmentPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class ModLootTableModifiers {

    //Chests
    private static final Identifier DESERT_PYRAMID = new Identifier("minecraft", "chests/desert_pyramid");
    private static final Identifier IGLO_CHEST = new Identifier("minecraft", "chests/iglo_chest");
    private static final Identifier JUNGLE_TEMPLE = new Identifier("minecraft", "chests/jungle_temple");
    private static final Identifier RUINED_PORTAL = new Identifier("minecraft", "chests/ruined_portal");
    private static final Identifier STRONGHOLD_LIBRARY = new Identifier("minecraft", "chests/stronghold_library");

    //Entity
    private static final Identifier WARDEN = new Identifier("minecraft", "entities/warden");

    //Custom Shrine
    public static final LootFunctionType FilteredEnchantFunction = new LootFunctionType(new FilteredEnchantFunction.Serializer());


    public static void modifyLootTables() {

        Registry.register(Registry.LOOT_FUNCTION_TYPE, new Identifier(BeyondTheBlock.MOD_ID, "filtered_enchant_randomly"), FilteredEnchantFunction);

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (DESERT_PYRAMID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.35f)) // Drops 35% of the time
                        .with(ItemEntry.builder(ModItems.AMBERINE_SHOVEL))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }

            if (IGLO_CHEST.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f)) // Drops 100% of the time
                        .with(ItemEntry.builder(ModItems.AZUROS_PICKAXE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }

            if (JUNGLE_TEMPLE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.5f)) // Drops 50% of the time
                        .with(ItemEntry.builder(ModItems.CHROMITE_SWORD))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (RUINED_PORTAL.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.3f)) // Drops 30% of the time
                        .with(ItemEntry.builder(ModItems.AMBERINE_ITEM))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (STRONGHOLD_LIBRARY.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.3f)) // Drops 30% of the time
                        .with(ItemEntry.builder(ModItems.ECLIPSED_BLOOM))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }

            if (WARDEN.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.KILLER,
                                new EntityPredicate.Builder().equipment(EntityEquipmentPredicate.Builder.create()
                                        .mainhand(ItemPredicate.Builder.create().items(ModItems.MIRANITE_SWORD).build()).build()).build()))
                        .with(ItemEntry.builder(ModItems.XENOLITH_STAFF))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}
