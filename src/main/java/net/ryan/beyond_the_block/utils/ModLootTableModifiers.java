package net.ryan.beyond_the_block.utils;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
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
    private static final Identifier ELDER_GUARDIAN = new Identifier("minecraft", "entities/elder_guardian");

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

            // SENTRY - Pillager Outpost
            if (id.equals(LootTables.PILLAGER_OUTPOST_CHEST)) {
                addTemplate(tableBuilder, ModItems.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

            // DUNE - Desert Pyramid
            if (id.equals(LootTables.DESERT_PYRAMID_CHEST)) {
                addTemplate(tableBuilder, ModItems.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

            // COAST - Shipwreck Treasure
            if (id.equals(LootTables.SHIPWRECK_TREASURE_CHEST)) {
                addTemplate(tableBuilder, ModItems.COAST_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

            // WILD - Jungle Temple
            if (id.equals(LootTables.JUNGLE_TEMPLE_CHEST)) {
                addTemplate(tableBuilder, ModItems.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

            // WARD - Ancient City
            if (id.equals(LootTables.ANCIENT_CITY_CHEST)) {
                addTemplate(tableBuilder, ModItems.WARD_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

            // SILENCE - Ancient City
            if (id.equals(LootTables.ANCIENT_CITY_CHEST)) {
                addTemplate(tableBuilder, ModItems.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

            // VEX - Woodland Mansion
            if (id.equals(LootTables.WOODLAND_MANSION_CHEST)) {
                addTemplate(tableBuilder, ModItems.VEX_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

            // SNOUT - Bastion Treasure
            if (id.equals(LootTables.BASTION_TREASURE_CHEST)) {
                addTemplate(tableBuilder, ModItems.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

            // RIB - Nether Fortress
            if (id.equals(LootTables.NETHER_BRIDGE_CHEST)) {
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1)));
            }

            // EYE - Stronghold Corridor
            if (id.equals(LootTables.STRONGHOLD_CORRIDOR_CHEST)) {
                addTemplate(tableBuilder, ModItems.EYE_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

            // SPIRE - End City
            if (id.equals(LootTables.END_CITY_TREASURE_CHEST)) {
                addTemplate(tableBuilder, ModItems.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

            // Netherite Upgrade - Bastion Treasure
            if (id.equals(LootTables.BASTION_TREASURE_CHEST)) {
                addTemplate(tableBuilder, ModItems.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
            }

            // TIDE - no native Ocean Monument chest in 1.19.2
            // Temporary survival source workaround:
            if (id.equals(ELDER_GUARDIAN)) {
                addTemplate(tableBuilder, ModItems.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

        });
    }
    private static void addTemplate(net.minecraft.loot.LootTable.Builder tableBuilder, net.minecraft.item.Item item) {
        tableBuilder.pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(item).weight(1)));
    }
}
