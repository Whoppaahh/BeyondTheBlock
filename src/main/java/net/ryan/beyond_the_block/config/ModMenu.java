package net.ryan.beyond_the_block.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.text.Text;

public class ModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("BTB Config"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            ConfigCategory shrineConfig = builder.getOrCreateCategory(Text.literal("Shrines"));
            shrineConfig.addEntry(entryBuilder
                            .startBooleanToggle(
                            Text.literal("Vanilla Loot"),
                            config.shrines.rewardsIncludeVanillaItems)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> config.shrines.rewardsIncludeVanillaItems = newValue).build());
            shrineConfig.addEntry(entryBuilder
                    .startBooleanToggle(
                            Text.literal("Modded Loot"),
                            config.shrines.rewardsIncludeModdedItems)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.shrines.rewardsIncludeModdedItems = newValue)
                    .build());
            shrineConfig.addEntry(entryBuilder
                            .startLongField(
                            Text.literal("Generation Interval"),
                            config.shrines.generationInterval)
                    .setDefaultValue(24000)
                    .setSaveConsumer(newValue -> config.shrines.generationInterval = newValue)
                    .build());

            ConfigCategory oresConfig = builder.getOrCreateCategory(Text.literal("Ores"));
            oresConfig.addEntry(entryBuilder.startTextDescription(Text.literal("=== Ores: Modded ===")).build());
            oresConfig.addEntry(entryBuilder.startIntField(Text.literal("Ruby Ore Vein Size"), config.ores.rubyOre).setDefaultValue(6).setSaveConsumer(v -> config.ores.rubyOre = v).build());
            oresConfig.addEntry(entryBuilder.startIntField(Text.literal("Miranite Ore Vein Size"), config.ores.miraniteOre).setDefaultValue(2).setSaveConsumer(v -> config.ores.miraniteOre = v).build());
            oresConfig.addEntry(entryBuilder.startIntField(Text.literal("Chromite Ore Vein Size"), config.ores.chromiteOre).setDefaultValue(10).setSaveConsumer(v -> config.ores.chromiteOre = v).build());
            oresConfig.addEntry(entryBuilder.startIntField(Text.literal("Nocturnite Ore Vein Size"), config.ores.nocturniteOre).setDefaultValue(4).setSaveConsumer(v -> config.ores.nocturniteOre = v).build());
            oresConfig.addEntry(entryBuilder.startIntField(Text.literal("Amberine Ore Vein Size"), config.ores.amberineOre).setDefaultValue(7).setSaveConsumer(v -> config.ores.amberineOre = v).build());
            oresConfig.addEntry(entryBuilder.startIntField(Text.literal("Rosette Ore Vein Size"), config.ores.rosetteOre).setDefaultValue(5).setSaveConsumer(v -> config.ores.rosetteOre = v).build());
            oresConfig.addEntry(entryBuilder.startIntField(Text.literal("Azuros Ore Vein Size"), config.ores.azurosOre).setDefaultValue(6).setSaveConsumer(v -> config.ores.azurosOre = v).build());
            oresConfig.addEntry(entryBuilder.startIntField(Text.literal("Indigra Ore Vein Size"), config.ores.indigraOre).setDefaultValue(3).setSaveConsumer(v -> config.ores.indigraOre = v).build());
            oresConfig.addEntry(entryBuilder.startIntField(Text.literal("Xirion Ore Vein Size"), config.ores.xirionOre).setDefaultValue(9).setSaveConsumer(v -> config.ores.xirionOre = v).build());
            oresConfig.addEntry(entryBuilder.startIntField(Text.literal("XP Ore Vein Size"), config.ores.xpOre).setDefaultValue(4).setSaveConsumer(v -> config.ores.xpOre = v).build());


            // Guards
            ConfigCategory guardsConfig = builder.getOrCreateCategory(Text.literal("Guards"));
            SubCategoryBuilder visualSub = entryBuilder.startSubCategory(Text.literal("=== Guard: Visuals ==="))
                            .setExpanded(false);
            visualSub.add(entryBuilder.startBooleanToggle(Text.literal("Guard Variants"), config.guards.visuals.guardVariants).setDefaultValue(false).setSaveConsumer(v -> config.guards.visuals.guardVariants = v).build());
            visualSub.add(entryBuilder.startBooleanToggle(Text.literal("Guard Wear Berets"), config.guards.visuals.guardBerets).setDefaultValue(false).setSaveConsumer(v -> config.guards.visuals.guardBerets = v).build());
            visualSub.add(entryBuilder.startBooleanToggle(Text.literal("Display Shoulder Pads"), config.guards.visuals.displayShoulderPads).setDefaultValue(false).setSaveConsumer(v -> config.guards.visuals.displayShoulderPads = v).build());
            guardsConfig.addEntry(visualSub.build());

            SubCategoryBuilder statsSub = entryBuilder.startSubCategory(Text.literal("=== Guard: Stats ==="))
                    .setExpanded(false);
            statsSub.add(entryBuilder.startDoubleField(Text.literal("Health Modifier"), config.guards.behavior.healthModifier).setDefaultValue(20D).setSaveConsumer(v -> config.guards.behavior.healthModifier = v).build());
            statsSub.add(entryBuilder.startFloatField(Text.literal("Amount of Health Regenerated"), config.guards.behavior.amountOfHealthRegenerated).setDefaultValue(1F).setSaveConsumer(v -> config.guards.behavior.amountOfHealthRegenerated = v).build());
            statsSub.add(entryBuilder.startDoubleField(Text.literal("Speed Modifier"), config.guards.behavior.speedModifier).setDefaultValue(0.5D).setSaveConsumer(v -> config.guards.behavior.speedModifier = v).build());
            statsSub.add(entryBuilder.startDoubleField(Text.literal("Follow Range Modifier"), config.guards.behavior.followRangeModifier).setDefaultValue(20D).setSaveConsumer(v -> config.guards.behavior.followRangeModifier = v).build());
            statsSub.add(entryBuilder.startFloatField(Text.literal("Chance to Drop Equipment"), config.guards.behavior.chanceToDropEquipment).setDefaultValue(25F).setSaveConsumer(v -> config.guards.behavior.chanceToDropEquipment = v).build());
            statsSub.add(entryBuilder.startBooleanToggle(Text.literal("Guards Open Doors"), config.guards.behavior.guardsOpenDoors).setDefaultValue(true).setSaveConsumer(v -> config.guards.behavior.guardsOpenDoors = v).build());
            statsSub.add(entryBuilder.startDoubleField(Text.literal("Villager Assist Range"), config.guards.behavior.guardVillagerHelpRange).setDefaultValue(50).setSaveConsumer(v -> config.guards.behavior.guardVillagerHelpRange = v).build());
            guardsConfig.addEntry(statsSub.build());

            SubCategoryBuilder repSub = entryBuilder.startSubCategory(Text.literal("=== Guard: Reputation ==="))
                    .setExpanded(false);
            repSub.add(entryBuilder.startBooleanToggle(Text.literal("Hero Status Required to give Guards Items"), config.guards.behavior.giveGuardStuffHotv).setDefaultValue(false).setSaveConsumer(v -> config.guards.behavior.giveGuardStuffHotv = v).build());
            repSub.add(entryBuilder.startBooleanToggle(Text.literal("Hero Status Required to set Guard Patrol"), config.guards.behavior.setGuardPatrolHotv).setDefaultValue(false).setSaveConsumer(v -> config.guards.behavior.setGuardPatrolHotv = v).build());
            repSub.add(entryBuilder.startBooleanToggle(Text.literal("Hero Status to convert Villagers"), config.guards.behavior.convertVillagerIfHaveHotv).setDefaultValue(false).setSaveConsumer(v -> config.guards.behavior.convertVillagerIfHaveHotv = v).build());
            repSub.add(entryBuilder.startBooleanToggle(Text.literal("Follow Hero"), config.guards.behavior.followHero).setDefaultValue(true).setSaveConsumer(v -> config.guards.behavior.followHero = v).build());
            repSub.add(entryBuilder.startIntField(Text.literal("Reputation Requirement"), config.guards.behavior.reputationRequirement).setDefaultValue(15).setSaveConsumer(v -> config.guards.behavior.reputationRequirement = v).build());
            repSub.add(entryBuilder.startIntField(Text.literal("Reputation Required to be Attacked"), config.guards.behavior.reputationRequirementToBeAttacked).setDefaultValue(-100).setSaveConsumer(v -> config.guards.behavior.reputationRequirementToBeAttacked = v).build());
            guardsConfig.addEntry(repSub.build());

            SubCategoryBuilder combatSub = entryBuilder.startSubCategory(Text.literal("=== Guard: Combat ==="))
                    .setExpanded(false);
            combatSub.add(entryBuilder.startBooleanToggle(Text.literal("Guard Formation"), config.guards.behavior.guardFormation).setDefaultValue(false).setSaveConsumer(v -> config.guards.behavior.guardFormation = v).build());
            combatSub.add(entryBuilder.startBooleanToggle(Text.literal("Cleric Healing"), config.guards.behavior.clericHealing).setDefaultValue(true).setSaveConsumer(v -> config.guards.behavior.clericHealing = v).build());
            combatSub.add(entryBuilder.startBooleanToggle(Text.literal("Blacksmith Healing"), config.guards.behavior.blackSmithHealing).setDefaultValue(true).setSaveConsumer(v -> config.guards.behavior.blackSmithHealing = v).build());
            combatSub.add(entryBuilder.startBooleanToggle(Text.literal("Armourer Repairs Guards Armour"), config.guards.behavior.armourerRepairGuardArmour).setDefaultValue(true).setSaveConsumer(v -> config.guards.behavior.armourerRepairGuardArmour = v).build());
            guardsConfig.addEntry(combatSub.build());

            ConfigCategory enchantmentsConfig = builder.getOrCreateCategory(Text.literal("Enchantments"));
            enchantmentsConfig.addEntry(ConfigEntryBuilder.create().startTextDescription(Text.literal("=== Timbercut/Barkskin ===")).build());
            enchantmentsConfig.addEntry(ConfigEntryBuilder.create().startEnumSelector(
                            Text.literal("Enchantment Drop Mode"),
                            DropMode.class,
                            config.enchantments.dropMode)
                    .setDefaultValue(DropMode.NORMAL)
                    .setSaveConsumer(newValue -> config.enchantments.dropMode = newValue)
                    .build());
            enchantmentsConfig.addEntry(ConfigEntryBuilder.create().startBooleanToggle(
                            Text.literal("Enchantment Show Highlights"),
                            config.enchantments.showHighlights)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.enchantments.showHighlights = newValue)
                    .build());
            ConfigCategory miscConfig = builder.getOrCreateCategory(Text.literal("Misc"));
            miscConfig.addEntry(ConfigEntryBuilder.create().startTextDescription(Text.literal("=== Title Logo ===")).build());
            miscConfig.addEntry(ConfigEntryBuilder.create().startBooleanToggle(
                            Text.literal("Enabled Custom Title Logo"),
                            config.misc.titleLogo)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.misc.titleLogo = newValue)
                    .build());
            miscConfig.addEntry(ConfigEntryBuilder.create().startTextDescription(Text.literal("=== Blood Effects ===")).build());
            miscConfig.addEntry(ConfigEntryBuilder.create().startBooleanToggle(
                            Text.literal("Blood Particles"),
                            config.misc.showBlood)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.misc.showBlood = newValue)
                    .build());
            if (config.misc.showBlood) {
                miscConfig.addEntry(ConfigEntryBuilder.create().startFloatField(
                                Text.literal("Health Fraction"),
                                config.misc.healthFraction)
                        .setDefaultValue(0.25F)
                        .setSaveConsumer(newValue -> config.misc.healthFraction = newValue)
                        .build());
            }

            ConfigCategory villagerNames = builder.getOrCreateCategory(Text.literal("Villager Names"));
            villagerNames.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Enable Villager Names"),
                            config.villagerNames.enableNames)
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.villagerNames.enableNames = v)
                    .build());

            villagerNames.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Use Profession Colours"),
                            config.villagerNames.colouriseNames)
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.villagerNames.colouriseNames = v)
                    .build());

            villagerNames.addEntry(entryBuilder.startDoubleField(
                            Text.literal("Name Visibility Range"),
                            config.villagerNames.nameVisibilityRange)
                    .setDefaultValue(8.0)
                    .setSaveConsumer(v -> config.villagerNames.nameVisibilityRange = v)
                    .build());

            villagerNames.addEntry(entryBuilder.startBooleanToggle(
                            Text.literal("Alliterative Names"),
                            config.villagerNames.useAlliteration)
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.villagerNames.useAlliteration = v)
                    .build());

            villagerNames.addEntry(entryBuilder.startEnumSelector(
                            Text.literal("Name Gender Mode"),
                            ModConfig.VillagerNamesConfig.GenderMode.class,
                            config.villagerNames.genderMode)
                    .setDefaultValue(ModConfig.VillagerNamesConfig.GenderMode.BOTH)
                    .setSaveConsumer(v -> config.villagerNames.genderMode = v)
                    .build());

            builder.setSavingRunnable(AutoConfig.getConfigHolder(ModConfig.class)::save);
            return builder.build();
        };
    }
}
