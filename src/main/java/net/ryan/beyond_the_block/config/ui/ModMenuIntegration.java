package net.ryan.beyond_the_block.config.ui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.client.network.ConfigSyncClient;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.config.schema.ConfigClient;
import net.ryan.beyond_the_block.config.sync.ClientSyncedConfigHolder;
import net.ryan.beyond_the_block.config.sync.SyncedServerConfig;

import java.util.ArrayList;
import java.util.List;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            boolean integrated = mc.isIntegratedServerRunning();

            ConfigClient config = AutoConfig.getConfigHolder(ConfigClient.class).getConfig();

            if (integrated && !ClientSyncedConfigHolder.hasConfig()) {
                ConfigSyncClient.requestServerConfig();
            }

            SyncedServerConfig baseServerConfig = ClientSyncedConfigHolder.hasConfig()
                    ? Configs.syncedServerConfig()
                    : SyncedServerConfig.fromServerConfig(Configs.server());

            final SyncedServerConfig[] edited = {baseServerConfig};

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Beyond The Block Config"));

            ConfigEntryBuilder entry = builder.entryBuilder();

            // =========================
            // GAMEPLAY / SERVER
            // =========================
            ConfigCategory gameplay = builder.getOrCreateCategory(Text.literal("Gameplay (Server)"));

            if (integrated) {
                gameplay.addEntry(entry.startTextDescription(Text.literal(
                        "These settings edit the integrated server config and sync the accepted values back to the client."
                )).build());

                // Worldgen -> Ores
                SubCategoryBuilder ores = entry.startSubCategory(Text.literal("Worldgen - Ores")).setExpanded(false);

                ores.add(entry.startIntField(Text.literal("Ruby Ore Vein Size"), edited[0].rubyOre())
                        .setSaveConsumer(v -> edited[0] = new SyncedServerConfig(
                                v, edited[0].miraniteOre(), edited[0].chromiteOre(), edited[0].nocturniteOre(),
                                edited[0].amberineOre(), edited[0].rosetteOre(), edited[0].azurosOre(), edited[0].indigraOre(),
                                edited[0].xirionOre(), edited[0].xpOre(),
                                edited[0].guardsEnabled(), edited[0].guardsReputationRequirementToBeAttacked(), edited[0].guardsReputationRequirement(),
                                edited[0].guardsRunFromPolarBears(), edited[0].guardsOpenDoors(), edited[0].guardFormation(),
                                edited[0].clericHealing(), edited[0].armourerRepairGuardArmour(), edited[0].attackAllMobs(),
                                edited[0].guardAlwaysShield(), edited[0].friendlyFire(), edited[0].guardMobBlackList(),
                                edited[0].amountOfHealthRegenerated(), edited[0].followHero(), edited[0].guardHealthModifier(),
                                edited[0].guardSpeedModifier(), edited[0].guardFollowRangeModifier(), edited[0].giveGuardStuffHotv(),
                                edited[0].setGuardPatrolHotv(), edited[0].chanceToDropEquipment(), edited[0].raidAnimals(),
                                edited[0].witchesVillager(), edited[0].blackSmithHealing(), edited[0].convertVillagerIfHaveHotv(),
                                edited[0].guardVillagerHelpRange(), edited[0].illagersRunFromPolarBears(),
                                edited[0].villagersRunFromPolarBears(), edited[0].guardArrowsHurtVillagers(),
                                edited[0].shrineRewardsIncludeVanillaItems(), edited[0].shrineRewardsIncludeModdedItems(),
                                edited[0].shrineGenerationInterval(),
                                edited[0].pathsEnabled(), edited[0].pathsMaxDistance(), edited[0].pathsUseTerrainFollowing(),
                                edited[0].pathsPreserveDurability(), edited[0].pathsDefaultPathBlockId(),
                                edited[0].pathsAllowedStartingBlocks(), edited[0].pathsAllowedEndingBlocks(),
                                edited[0].pathsMinWidth(), edited[0].pathsMaxWidth(),
                                edited[0].enableChickenFeathers(), edited[0].chickenFeatherInterval(), edited[0].chickenFeatherChance(),
                                edited[0].enableSkeletonBones(), edited[0].skeletonBoneInterval(), edited[0].skeletonBoneChance(),
                                edited[0].enableSpiderDrops(), edited[0].spiderDropInterval(), edited[0].spiderDropChance(),
                                edited[0].cobwebWeight(),
                                edited[0].minDecayTicks(), edited[0].maxDecayTicks(), edited[0].baseDecayChance(),
                                edited[0].lightDecayBonus(), edited[0].darknessDecayReduction(), edited[0].densityRadius(),
                                edited[0].densityLimit(), edited[0].normalSpiderRate(), edited[0].caveSpiderRate(),
                                edited[0].horseSwimmingEnabled(), edited[0].undeadHorseSwimmingEnabled(),
                                edited[0].horsePreventWandering(), edited[0].horseStayRadius(),
                                edited[0].horseRemoveMiningPenalty(), edited[0].horseIncreaseStepHeight(),
                                edited[0].enableRecursiveOpening(), edited[0].recursiveOpeningMaxBlocksDistance(),
                                edited[0].enableDoors(), edited[0].enableFenceGates(), edited[0].enableTrapdoors(),
                                edited[0].enableModIncompatibilityCheck(), edited[0].dropMode()
                        ))
                        .build());

                ores.add(entry.startIntField(Text.literal("Miranite Ore Vein Size"), edited[0].miraniteOre())
                        .setSaveConsumer(v -> edited[0] = copy(edited[0], 1, v)).build());
                ores.add(entry.startIntField(Text.literal("Chromite Ore Vein Size"), edited[0].chromiteOre())
                        .setSaveConsumer(v -> edited[0] = copy(edited[0], 2, v)).build());
                ores.add(entry.startIntField(Text.literal("Nocturnite Ore Vein Size"), edited[0].nocturniteOre())
                        .setSaveConsumer(v -> edited[0] = copy(edited[0], 3, v)).build());
                ores.add(entry.startIntField(Text.literal("Amberine Ore Vein Size"), edited[0].amberineOre())
                        .setSaveConsumer(v -> edited[0] = copy(edited[0], 4, v)).build());
                ores.add(entry.startIntField(Text.literal("Rosette Ore Vein Size"), edited[0].rosetteOre())
                        .setSaveConsumer(v -> edited[0] = copy(edited[0], 5, v)).build());
                ores.add(entry.startIntField(Text.literal("Azuros Ore Vein Size"), edited[0].azurosOre())
                        .setSaveConsumer(v -> edited[0] = copy(edited[0], 6, v)).build());
                ores.add(entry.startIntField(Text.literal("Indigra Ore Vein Size"), edited[0].indigraOre())
                        .setSaveConsumer(v -> edited[0] = copy(edited[0], 7, v)).build());
                ores.add(entry.startIntField(Text.literal("Xirion Ore Vein Size"), edited[0].xirionOre())
                        .setSaveConsumer(v -> edited[0] = copy(edited[0], 8, v)).build());
                ores.add(entry.startIntField(Text.literal("XP Ore Vein Size"), edited[0].xpOre())
                        .setSaveConsumer(v -> edited[0] = copy(edited[0], 9, v)).build());

                gameplay.addEntry(ores.build());

                // Guards
                SubCategoryBuilder guards = entry.startSubCategory(Text.literal("Guards")).setExpanded(false);

                guards.add(entry.startBooleanToggle(Text.literal("Enabled"), edited[0].guardsEnabled())
                        .setSaveConsumer(v -> edited[0] = withGuardsEnabled(edited[0], v)).build());
                guards.add(entry.startIntField(Text.literal("Reputation Requirement To Be Attacked"),
                                edited[0].guardsReputationRequirementToBeAttacked())
                        .setSaveConsumer(v -> edited[0] = withGuardsReputationRequirementToBeAttacked(edited[0], v)).build());
                guards.add(entry.startIntField(Text.literal("Reputation Requirement"),
                                edited[0].guardsReputationRequirement())
                        .setSaveConsumer(v -> edited[0] = withGuardsReputationRequirement(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Guards Run From Polar Bears"),
                                edited[0].guardsRunFromPolarBears())
                        .setSaveConsumer(v -> edited[0] = withGuardsRunFromPolarBears(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Guards Open Doors"),
                                edited[0].guardsOpenDoors())
                        .setSaveConsumer(v -> edited[0] = withGuardsOpenDoors(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Guard Formation"),
                                edited[0].guardFormation())
                        .setSaveConsumer(v -> edited[0] = withGuardFormation(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Cleric Healing"),
                                edited[0].clericHealing())
                        .setSaveConsumer(v -> edited[0] = withClericHealing(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Armourer Repairs Guard Armour"),
                                edited[0].armourerRepairGuardArmour())
                        .setSaveConsumer(v -> edited[0] = withArmourerRepairGuardArmour(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Attack All Mobs"),
                                edited[0].attackAllMobs())
                        .setSaveConsumer(v -> edited[0] = withAttackAllMobs(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Guard Always Shield"),
                                edited[0].guardAlwaysShield())
                        .setSaveConsumer(v -> edited[0] = withGuardAlwaysShield(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Friendly Fire"),
                                edited[0].friendlyFire())
                        .setSaveConsumer(v -> edited[0] = withFriendlyFire(edited[0], v)).build());

                guards.add(entry.startFloatField(Text.literal("Health Regenerated"),
                                edited[0].amountOfHealthRegenerated())
                        .setSaveConsumer(v -> edited[0] = withAmountOfHealthRegenerated(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Follow Hero"),
                                edited[0].followHero())
                        .setSaveConsumer(v -> edited[0] = withFollowHero(edited[0], v)).build());
                guards.add(entry.startDoubleField(Text.literal("Health Modifier"),
                                edited[0].guardHealthModifier())
                        .setSaveConsumer(v -> edited[0] = withGuardHealthModifier(edited[0], v)).build());
                guards.add(entry.startDoubleField(Text.literal("Speed Modifier"),
                                edited[0].guardSpeedModifier())
                        .setSaveConsumer(v -> edited[0] = withGuardSpeedModifier(edited[0], v)).build());
                guards.add(entry.startDoubleField(Text.literal("Follow Range Modifier"),
                                edited[0].guardFollowRangeModifier())
                        .setSaveConsumer(v -> edited[0] = withGuardFollowRangeModifier(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Give Guard Stuff HOTV"),
                                edited[0].giveGuardStuffHotv())
                        .setSaveConsumer(v -> edited[0] = withGiveGuardStuffHotv(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Set Guard Patrol HOTV"),
                                edited[0].setGuardPatrolHotv())
                        .setSaveConsumer(v -> edited[0] = withSetGuardPatrolHotv(edited[0], v)).build());
                guards.add(entry.startFloatField(Text.literal("Chance To Drop Equipment"),
                                edited[0].chanceToDropEquipment())
                        .setSaveConsumer(v -> edited[0] = withChanceToDropEquipment(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Raid Animals"),
                                edited[0].raidAnimals())
                        .setSaveConsumer(v -> edited[0] = withRaidAnimals(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Witches Villager"),
                                edited[0].witchesVillager())
                        .setSaveConsumer(v -> edited[0] = withWitchesVillager(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Blacksmith Healing"),
                                edited[0].blackSmithHealing())
                        .setSaveConsumer(v -> edited[0] = withBlackSmithHealing(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Convert Villager If Have HOTV"),
                                edited[0].convertVillagerIfHaveHotv())
                        .setSaveConsumer(v -> edited[0] = withConvertVillagerIfHaveHotv(edited[0], v)).build());
                guards.add(entry.startDoubleField(Text.literal("Guard Villager Help Range"),
                                edited[0].guardVillagerHelpRange())
                        .setSaveConsumer(v -> edited[0] = withGuardVillagerHelpRange(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Illagers Run From Polar Bears"),
                                edited[0].illagersRunFromPolarBears())
                        .setSaveConsumer(v -> edited[0] = withIllagersRunFromPolarBears(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Villagers Run From Polar Bears"),
                                edited[0].villagersRunFromPolarBears())
                        .setSaveConsumer(v -> edited[0] = withVillagersRunFromPolarBears(edited[0], v)).build());
                guards.add(entry.startBooleanToggle(Text.literal("Guard Arrows Hurt Villagers"),
                                edited[0].guardArrowsHurtVillagers())
                        .setSaveConsumer(v -> edited[0] = withGuardArrowsHurtVillagers(edited[0], v)).build());

                guards.add(entry.startStrList(Text.literal("Mob Blacklist"),
                                new ArrayList<>(edited[0].guardMobBlackList()))
                        .setSaveConsumer(v -> edited[0] = withGuardMobBlackList(edited[0], v))
                        .build());

                gameplay.addEntry(guards.build());

                // Shrines
                SubCategoryBuilder shrines = entry.startSubCategory(Text.literal("Shrines")).setExpanded(false);

                shrines.add(entry.startBooleanToggle(Text.literal("Rewards Include Vanilla Items"),
                                edited[0].shrineRewardsIncludeVanillaItems())
                        .setSaveConsumer(v -> edited[0] = withShrineRewardsIncludeVanillaItems(edited[0], v)).build());
                shrines.add(entry.startBooleanToggle(Text.literal("Rewards Include Modded Items"),
                                edited[0].shrineRewardsIncludeModdedItems())
                        .setSaveConsumer(v -> edited[0] = withShrineRewardsIncludeModdedItems(edited[0], v)).build());
                shrines.add(entry.startLongField(Text.literal("Generation Interval"),
                                edited[0].shrineGenerationInterval())
                        .setSaveConsumer(v -> edited[0] = withShrineGenerationInterval(edited[0], v)).build());

                gameplay.addEntry(shrines.build());

                // Paths
                SubCategoryBuilder paths = entry.startSubCategory(Text.literal("Paths")).setExpanded(false);

                paths.add(entry.startBooleanToggle(Text.literal("Enabled"),
                                edited[0].pathsEnabled())
                        .setSaveConsumer(v -> edited[0] = withPathsEnabled(edited[0], v)).build());
                paths.add(entry.startIntField(Text.literal("Max Distance"),
                                edited[0].pathsMaxDistance())
                        .setSaveConsumer(v -> edited[0] = withPathsMaxDistance(edited[0], v)).build());
                paths.add(entry.startBooleanToggle(Text.literal("Use Terrain Following"),
                                edited[0].pathsUseTerrainFollowing())
                        .setSaveConsumer(v -> edited[0] = withPathsUseTerrainFollowing(edited[0], v)).build());
                paths.add(entry.startBooleanToggle(Text.literal("Preserve Durability"),
                                edited[0].pathsPreserveDurability())
                        .setSaveConsumer(v -> edited[0] = withPathsPreserveDurability(edited[0], v)).build());
                paths.add(entry.startStrField(Text.literal("Default Path Block ID"),
                                edited[0].pathsDefaultPathBlockId())
                        .setSaveConsumer(v -> edited[0] = withPathsDefaultPathBlockId(edited[0], v)).build());
                paths.add(entry.startStrList(Text.literal("Allowed Starting Blocks"),
                                new ArrayList<>(edited[0].pathsAllowedStartingBlocks()))
                        .setSaveConsumer(v -> edited[0] = withPathsAllowedStartingBlocks(edited[0], v)).build());
                paths.add(entry.startStrList(Text.literal("Allowed Ending Blocks"),
                                new ArrayList<>(edited[0].pathsAllowedEndingBlocks()))
                        .setSaveConsumer(v -> edited[0] = withPathsAllowedEndingBlocks(edited[0], v)).build());
                paths.add(entry.startIntField(Text.literal("Minimum Width"),
                                edited[0].pathsMinWidth())
                        .setSaveConsumer(v -> edited[0] = withPathsMinWidth(edited[0], v)).build());
                paths.add(entry.startIntField(Text.literal("Maximum Width"),
                                edited[0].pathsMaxWidth())
                        .setSaveConsumer(v -> edited[0] = withPathsMaxWidth(edited[0], v)).build());

                gameplay.addEntry(paths.build());

                // Passive Drops
                SubCategoryBuilder drops = entry.startSubCategory(Text.literal("Passive Drops")).setExpanded(false);

                drops.add(entry.startBooleanToggle(Text.literal("Enable Chicken Feathers"),
                                edited[0].enableChickenFeathers())
                        .setSaveConsumer(v -> edited[0] = withEnableChickenFeathers(edited[0], v)).build());
                drops.add(entry.startIntField(Text.literal("Chicken Feather Interval"),
                                edited[0].chickenFeatherInterval())
                        .setSaveConsumer(v -> edited[0] = withChickenFeatherInterval(edited[0], v)).build());
                drops.add(entry.startFloatField(Text.literal("Chicken Feather Chance"),
                                edited[0].chickenFeatherChance())
                        .setSaveConsumer(v -> edited[0] = withChickenFeatherChance(edited[0], v)).build());

                drops.add(entry.startBooleanToggle(Text.literal("Enable Skeleton Bones"),
                                edited[0].enableSkeletonBones())
                        .setSaveConsumer(v -> edited[0] = withEnableSkeletonBones(edited[0], v)).build());
                drops.add(entry.startIntField(Text.literal("Skeleton Bone Interval"),
                                edited[0].skeletonBoneInterval())
                        .setSaveConsumer(v -> edited[0] = withSkeletonBoneInterval(edited[0], v)).build());
                drops.add(entry.startFloatField(Text.literal("Skeleton Bone Chance"),
                                edited[0].skeletonBoneChance())
                        .setSaveConsumer(v -> edited[0] = withSkeletonBoneChance(edited[0], v)).build());

                drops.add(entry.startBooleanToggle(Text.literal("Enable Spider Drops"),
                                edited[0].enableSpiderDrops())
                        .setSaveConsumer(v -> edited[0] = withEnableSpiderDrops(edited[0], v)).build());
                drops.add(entry.startIntField(Text.literal("Spider Drop Interval"),
                                edited[0].spiderDropInterval())
                        .setSaveConsumer(v -> edited[0] = withSpiderDropInterval(edited[0], v)).build());
                drops.add(entry.startFloatField(Text.literal("Spider Drop Chance"),
                                edited[0].spiderDropChance())
                        .setSaveConsumer(v -> edited[0] = withSpiderDropChance(edited[0], v)).build());
                drops.add(entry.startFloatField(Text.literal("Cobweb Weight"),
                                edited[0].cobwebWeight())
                        .setSaveConsumer(v -> edited[0] = withCobwebWeight(edited[0], v)).build());

                gameplay.addEntry(drops.build());

                // Web decay
                SubCategoryBuilder webs = entry.startSubCategory(Text.literal("Web Decay")).setExpanded(false);

                webs.add(entry.startIntField(Text.literal("Minimum Decay Ticks"),
                                edited[0].minDecayTicks())
                        .setSaveConsumer(v -> edited[0] = withMinDecayTicks(edited[0], v)).build());
                webs.add(entry.startIntField(Text.literal("Maximum Decay Ticks"),
                                edited[0].maxDecayTicks())
                        .setSaveConsumer(v -> edited[0] = withMaxDecayTicks(edited[0], v)).build());
                webs.add(entry.startFloatField(Text.literal("Base Decay Chance"),
                                edited[0].baseDecayChance())
                        .setSaveConsumer(v -> edited[0] = withBaseDecayChance(edited[0], v)).build());
                webs.add(entry.startFloatField(Text.literal("Light Decay Bonus"),
                                edited[0].lightDecayBonus())
                        .setSaveConsumer(v -> edited[0] = withLightDecayBonus(edited[0], v)).build());
                webs.add(entry.startFloatField(Text.literal("Darkness Decay Reduction"),
                                edited[0].darknessDecayReduction())
                        .setSaveConsumer(v -> edited[0] = withDarknessDecayReduction(edited[0], v)).build());
                webs.add(entry.startIntField(Text.literal("Density Radius"),
                                edited[0].densityRadius())
                        .setSaveConsumer(v -> edited[0] = withDensityRadius(edited[0], v)).build());
                webs.add(entry.startIntField(Text.literal("Density Limit"),
                                edited[0].densityLimit())
                        .setSaveConsumer(v -> edited[0] = withDensityLimit(edited[0], v)).build());
                webs.add(entry.startFloatField(Text.literal("Normal Spider Rate"),
                                edited[0].normalSpiderRate())
                        .setSaveConsumer(v -> edited[0] = withNormalSpiderRate(edited[0], v)).build());
                webs.add(entry.startFloatField(Text.literal("Cave Spider Rate"),
                                edited[0].caveSpiderRate())
                        .setSaveConsumer(v -> edited[0] = withCaveSpiderRate(edited[0], v)).build());

                gameplay.addEntry(webs.build());

                // Horses
                SubCategoryBuilder horses = entry.startSubCategory(Text.literal("Horses")).setExpanded(false);

                horses.add(entry.startBooleanToggle(Text.literal("Enable Swimming"),
                                edited[0].horseSwimmingEnabled())
                        .setSaveConsumer(v -> edited[0] = withHorseSwimmingEnabled(edited[0], v)).build());
                horses.add(entry.startBooleanToggle(Text.literal("Undead Can Swim"),
                                edited[0].undeadHorseSwimmingEnabled())
                        .setSaveConsumer(v -> edited[0] = withUndeadHorseSwimmingEnabled(edited[0], v)).build());
                horses.add(entry.startBooleanToggle(Text.literal("Prevent Wandering"),
                                edited[0].horsePreventWandering())
                        .setSaveConsumer(v -> edited[0] = withHorsePreventWandering(edited[0], v)).build());
                horses.add(entry.startDoubleField(Text.literal("Stay Radius"),
                                edited[0].horseStayRadius())
                        .setSaveConsumer(v -> edited[0] = withHorseStayRadius(edited[0], v)).build());
                horses.add(entry.startBooleanToggle(Text.literal("Remove Mining Penalty"),
                                edited[0].horseRemoveMiningPenalty())
                        .setSaveConsumer(v -> edited[0] = withHorseRemoveMiningPenalty(edited[0], v)).build());
                horses.add(entry.startBooleanToggle(Text.literal("Increase Step Height"),
                                edited[0].horseIncreaseStepHeight())
                        .setSaveConsumer(v -> edited[0] = withHorseIncreaseStepHeight(edited[0], v)).build());

                gameplay.addEntry(horses.build());

                // Double openables
                SubCategoryBuilder openables = entry.startSubCategory(Text.literal("Double Openables")).setExpanded(false);

                openables.add(entry.startBooleanToggle(Text.literal("Enable Recursive Opening"),
                                edited[0].enableRecursiveOpening())
                        .setSaveConsumer(v -> edited[0] = withEnableRecursiveOpening(edited[0], v)).build());
                openables.add(entry.startIntField(Text.literal("Recursive Opening Max Blocks Distance"),
                                edited[0].recursiveOpeningMaxBlocksDistance())
                        .setSaveConsumer(v -> edited[0] = withRecursiveOpeningMaxBlocksDistance(edited[0], v)).build());
                openables.add(entry.startBooleanToggle(Text.literal("Enable Doors"),
                                edited[0].enableDoors())
                        .setSaveConsumer(v -> edited[0] = withEnableDoors(edited[0], v)).build());
                openables.add(entry.startBooleanToggle(Text.literal("Enable Fence Gates"),
                                edited[0].enableFenceGates())
                        .setSaveConsumer(v -> edited[0] = withEnableFenceGates(edited[0], v)).build());
                openables.add(entry.startBooleanToggle(Text.literal("Enable Trapdoors"),
                                edited[0].enableTrapdoors())
                        .setSaveConsumer(v -> edited[0] = withEnableTrapdoors(edited[0], v)).build());
                openables.add(entry.startBooleanToggle(Text.literal("Enable Mod Incompatibility Check"),
                                edited[0].enableModIncompatibilityCheck())
                        .setSaveConsumer(v -> edited[0] = withEnableModIncompatibilityCheck(edited[0], v)).build());

                gameplay.addEntry(openables.build());

                // Enchantments
                SubCategoryBuilder enchantments = entry.startSubCategory(Text.literal("Enchantments")).setExpanded(false);

                enchantments.add(entry.startEnumSelector(Text.literal("Drop Mode"),
                                net.ryan.beyond_the_block.config.DropMode.class, edited[0].dropMode())
                        .setSaveConsumer(v -> edited[0] = withDropMode(edited[0], v))
                        .build());

                gameplay.addEntry(enchantments.build());
            } else {
                gameplay.addEntry(entry.startTextDescription(Text.literal(
                        "Server gameplay settings are only editable while in an integrated singleplayer world."
                )).build());
            }

            // =========================
            // CLIENT VISUALS
            // =========================
            ConfigCategory visuals = builder.getOrCreateCategory(Text.literal("Visuals"));

            SubCategoryBuilder blood = entry.startSubCategory(Text.literal("Blood Effects"))
                    .setExpanded(false);

            blood.add(entry.startBooleanToggle(Text.literal("Enable Blood"),
                            config.visuals.blood.enabled)
                    .setTooltip(Text.literal("Enable blood particle effects when entities are damaged"))
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.visuals.blood.enabled = v)
                    .build());

            blood.add(entry.startIntField(Text.literal("Health Threshold"),
                            config.visuals.blood.healthPercentThreshold)
                    .setTooltip(Text.literal("Only show blood when entity health is below this fraction"))
                    .setDefaultValue(50)
                    .setSaveConsumer(v -> config.visuals.blood.healthPercentThreshold = v)
                    .build());

            blood.add(entry.startEnumSelector(Text.literal("Entities Affected"),
                            ConfigClient.BloodTargetMode.class,
                            config.visuals.blood.targetMode)
                    .setTooltip(Text.literal("Show blood for these entities"))
                    .setDefaultValue(ConfigClient.BloodTargetMode.HUMANOID_ONLY)
                    .setSaveConsumer(v -> config.visuals.blood.targetMode = v)
                    .build());

            visuals.addEntry(blood.build());

            SubCategoryBuilder guardVisuals = entry.startSubCategory(Text.literal("Guard Visuals"))
                    .setExpanded(false);

            guardVisuals.add(entry.startBooleanToggle(Text.literal("Enable Variants"),
                            config.visuals.guards.variants)
                    .setSaveConsumer(v -> config.visuals.guards.variants = v)
                    .build());

            guardVisuals.add(entry.startBooleanToggle(Text.literal("Show Berets"),
                            config.visuals.guards.berets)
                    .setSaveConsumer(v -> config.visuals.guards.berets = v)
                    .build());

            guardVisuals.add(entry.startBooleanToggle(Text.literal("Show Shoulder Pads"),
                            config.visuals.guards.shoulderPads)
                    .setSaveConsumer(v -> config.visuals.guards.shoulderPads = v)
                    .build());

            visuals.addEntry(guardVisuals.build());

            SubCategoryBuilder horseCam = entry.startSubCategory(Text.literal("Horse Camera"))
                    .setExpanded(false);

            horseCam.add(entry.startFloatField(Text.literal("Minimum Alpha"),
                            config.visuals.horses.minAlpha)
                    .setSaveConsumer(v -> config.visuals.horses.minAlpha = v)
                    .build());

            horseCam.add(entry.startFloatField(Text.literal("Fade Pitch"),
                            config.visuals.horses.fadePitch)
                    .setSaveConsumer(v -> config.visuals.horses.fadePitch = v)
                    .build());

            horseCam.add(entry.startBooleanToggle(Text.literal("Enable Head Offset"),
                            config.visuals.horses.headPitchOffset)
                    .setSaveConsumer(v -> config.visuals.horses.headPitchOffset = v)
                    .build());

            horseCam.add(entry.startFloatField(Text.literal("Head Offset Degrees"),
                            config.visuals.horses.headOffsetDegrees)
                    .setSaveConsumer(v -> config.visuals.horses.headOffsetDegrees = v)
                    .build());

            visuals.addEntry(horseCam.build());

            SubCategoryBuilder names = entry.startSubCategory(Text.literal("Entity Names"))
                    .setExpanded(false);

            names.add(entry.startBooleanToggle(Text.literal("Enable Names"),
                            config.visuals.names.enabled)
                    .setSaveConsumer(v -> config.visuals.names.enabled = v)
                    .build());

            names.add(entry.startBooleanToggle(Text.literal("Only When Employed"),
                            config.visuals.names.onlyWhenEmployed)
                    .setSaveConsumer(v -> config.visuals.names.onlyWhenEmployed = v)
                    .build());

            names.add(entry.startBooleanToggle(Text.literal("Use Colours"),
                            config.visuals.names.colourise)
                    .setSaveConsumer(v -> config.visuals.names.colourise = v)
                    .build());

            names.add(entry.startDoubleField(Text.literal("Visibility Range"),
                            config.visuals.names.visibilityRange)
                    .setSaveConsumer(v -> config.visuals.names.visibilityRange = v)
                    .build());

            names.add(entry.startBooleanToggle(Text.literal("Alliteration"),
                            config.visuals.names.alliteration)
                    .setSaveConsumer(v -> config.visuals.names.alliteration = v)
                    .build());

            visuals.addEntry(names.build());

            visuals.addEntry(entry.startBooleanToggle(Text.literal("Custom Title Logo"),
                            config.visuals.title.customLogo)
                    .setSaveConsumer(v -> config.visuals.title.customLogo = v)
                    .build());

            visuals.addEntry(entry.startBooleanToggle(Text.literal("Show Enchantment Highlights"),
                            config.visuals.enchantments.showHighlights)
                    .setSaveConsumer(v -> config.visuals.enchantments.showHighlights = v)
                    .build());

            // =========================
            // HUD
            // =========================
            ConfigCategory hud = builder.getOrCreateCategory(Text.literal("HUD"));

            SubCategoryBuilder trajectory = entry.startSubCategory(Text.literal("Trajectory"))
                    .setExpanded(false);

            trajectory.add(entry.startBooleanToggle(Text.literal("Enable Trajectory Preview"),
                            config.hud.trajectory.enabled)
                    .setSaveConsumer(v -> config.hud.trajectory.enabled = v)
                    .build());

            trajectory.add(entry.startBooleanToggle(Text.literal("Only While Aiming"),
                            config.hud.trajectory.onlyWhileAiming)
                    .setSaveConsumer(v -> config.hud.trajectory.onlyWhileAiming = v)
                    .build());

            trajectory.add(entry.startBooleanToggle(Text.literal("Require Sneak"),
                            config.hud.trajectory.requireSneak)
                    .setSaveConsumer(v -> config.hud.trajectory.requireSneak = v)
                    .build());

            trajectory.add(entry.startIntField(Text.literal("Max Steps"),
                            config.hud.trajectory.maxSteps)
                    .setSaveConsumer(v -> config.hud.trajectory.maxSteps = v)
                    .build());

            hud.addEntry(trajectory.build());

            SubCategoryBuilder camera = entry.startSubCategory(Text.literal("Auto Camera"))
                    .setExpanded(false);

            camera.add(entry.startBooleanToggle(Text.literal("Enable Auto Camera"),
                            config.hud.camera.enabled)
                    .setSaveConsumer(v -> config.hud.camera.enabled = v)
                    .build());

            hud.addEntry(camera.build());

            SubCategoryBuilder pathsHud = entry.startSubCategory(Text.literal("Path HUD"))
                    .setExpanded(false);

            pathsHud.add(entry.startBooleanToggle(Text.literal("Preview Mode"),
                            config.hud.paths.previewMode)
                    .setSaveConsumer(v -> config.hud.paths.previewMode = v)
                    .build());

            pathsHud.add(entry.startBooleanToggle(Text.literal("Show Width HUD"),
                            config.hud.paths.showWidthHud)
                    .setSaveConsumer(v -> config.hud.paths.showWidthHud = v)
                    .build());

            hud.addEntry(pathsHud.build());

            builder.setSavingRunnable(() -> {
                AutoConfig.getConfigHolder(ConfigClient.class).save();

                if (integrated) {
                    ConfigSyncClient.sendServerConfigUpdate(edited[0]);
                }
            });

            return builder.build();
        };
    }

    // =========================
    // Small copy helpers
    // =========================

    private static SyncedServerConfig copy(SyncedServerConfig c, int oreIndex, int value) {
        return new SyncedServerConfig(
                oreIndex == 0 ? value : c.rubyOre(),
                oreIndex == 1 ? value : c.miraniteOre(),
                oreIndex == 2 ? value : c.chromiteOre(),
                oreIndex == 3 ? value : c.nocturniteOre(),
                oreIndex == 4 ? value : c.amberineOre(),
                oreIndex == 5 ? value : c.rosetteOre(),
                oreIndex == 6 ? value : c.azurosOre(),
                oreIndex == 7 ? value : c.indigraOre(),
                oreIndex == 8 ? value : c.xirionOre(),
                oreIndex == 9 ? value : c.xpOre(),

                c.guardsEnabled(),
                c.guardsReputationRequirementToBeAttacked(),
                c.guardsReputationRequirement(),
                c.guardsRunFromPolarBears(),
                c.guardsOpenDoors(),
                c.guardFormation(),
                c.clericHealing(),
                c.armourerRepairGuardArmour(),
                c.attackAllMobs(),
                c.guardAlwaysShield(),
                c.friendlyFire(),
                c.guardMobBlackList(),
                c.amountOfHealthRegenerated(),
                c.followHero(),
                c.guardHealthModifier(),
                c.guardSpeedModifier(),
                c.guardFollowRangeModifier(),
                c.giveGuardStuffHotv(),
                c.setGuardPatrolHotv(),
                c.chanceToDropEquipment(),
                c.raidAnimals(),
                c.witchesVillager(),
                c.blackSmithHealing(),
                c.convertVillagerIfHaveHotv(),
                c.guardVillagerHelpRange(),
                c.illagersRunFromPolarBears(),
                c.villagersRunFromPolarBears(),
                c.guardArrowsHurtVillagers(),

                c.shrineRewardsIncludeVanillaItems(),
                c.shrineRewardsIncludeModdedItems(),
                c.shrineGenerationInterval(),

                c.pathsEnabled(),
                c.pathsMaxDistance(),
                c.pathsUseTerrainFollowing(),
                c.pathsPreserveDurability(),
                c.pathsDefaultPathBlockId(),
                c.pathsAllowedStartingBlocks(),
                c.pathsAllowedEndingBlocks(),
                c.pathsMinWidth(),
                c.pathsMaxWidth(),

                c.enableChickenFeathers(),
                c.chickenFeatherInterval(),
                c.chickenFeatherChance(),
                c.enableSkeletonBones(),
                c.skeletonBoneInterval(),
                c.skeletonBoneChance(),
                c.enableSpiderDrops(),
                c.spiderDropInterval(),
                c.spiderDropChance(),
                c.cobwebWeight(),

                c.minDecayTicks(),
                c.maxDecayTicks(),
                c.baseDecayChance(),
                c.lightDecayBonus(),
                c.darknessDecayReduction(),
                c.densityRadius(),
                c.densityLimit(),
                c.normalSpiderRate(),
                c.caveSpiderRate(),

                c.horseSwimmingEnabled(),
                c.undeadHorseSwimmingEnabled(),
                c.horsePreventWandering(),
                c.horseStayRadius(),
                c.horseRemoveMiningPenalty(),
                c.horseIncreaseStepHeight(),

                c.enableRecursiveOpening(),
                c.recursiveOpeningMaxBlocksDistance(),
                c.enableDoors(),
                c.enableFenceGates(),
                c.enableTrapdoors(),
                c.enableModIncompatibilityCheck(),

                c.dropMode()
        );
    }

    private static SyncedServerConfig withGuardsEnabled(SyncedServerConfig c, boolean v) {
        return new SyncedServerConfig(
                c.rubyOre(), c.miraniteOre(), c.chromiteOre(), c.nocturniteOre(), c.amberineOre(),
                c.rosetteOre(), c.azurosOre(), c.indigraOre(), c.xirionOre(), c.xpOre(),
                v, c.guardsReputationRequirementToBeAttacked(), c.guardsReputationRequirement(),
                c.guardsRunFromPolarBears(), c.guardsOpenDoors(), c.guardFormation(), c.clericHealing(),
                c.armourerRepairGuardArmour(), c.attackAllMobs(), c.guardAlwaysShield(), c.friendlyFire(),
                c.guardMobBlackList(), c.amountOfHealthRegenerated(), c.followHero(), c.guardHealthModifier(),
                c.guardSpeedModifier(), c.guardFollowRangeModifier(), c.giveGuardStuffHotv(), c.setGuardPatrolHotv(),
                c.chanceToDropEquipment(), c.raidAnimals(), c.witchesVillager(), c.blackSmithHealing(),
                c.convertVillagerIfHaveHotv(), c.guardVillagerHelpRange(), c.illagersRunFromPolarBears(),
                c.villagersRunFromPolarBears(), c.guardArrowsHurtVillagers(),
                c.shrineRewardsIncludeVanillaItems(), c.shrineRewardsIncludeModdedItems(), c.shrineGenerationInterval(),
                c.pathsEnabled(), c.pathsMaxDistance(), c.pathsUseTerrainFollowing(), c.pathsPreserveDurability(),
                c.pathsDefaultPathBlockId(), c.pathsAllowedStartingBlocks(), c.pathsAllowedEndingBlocks(),
                c.pathsMinWidth(), c.pathsMaxWidth(),
                c.enableChickenFeathers(), c.chickenFeatherInterval(), c.chickenFeatherChance(),
                c.enableSkeletonBones(), c.skeletonBoneInterval(), c.skeletonBoneChance(),
                c.enableSpiderDrops(), c.spiderDropInterval(), c.spiderDropChance(), c.cobwebWeight(),
                c.minDecayTicks(), c.maxDecayTicks(), c.baseDecayChance(), c.lightDecayBonus(),
                c.darknessDecayReduction(), c.densityRadius(), c.densityLimit(), c.normalSpiderRate(), c.caveSpiderRate(),
                c.horseSwimmingEnabled(), c.undeadHorseSwimmingEnabled(), c.horsePreventWandering(), c.horseStayRadius(),
                c.horseRemoveMiningPenalty(), c.horseIncreaseStepHeight(),
                c.enableRecursiveOpening(), c.recursiveOpeningMaxBlocksDistance(), c.enableDoors(),
                c.enableFenceGates(), c.enableTrapdoors(), c.enableModIncompatibilityCheck(), c.dropMode()
        );
    }

    private static SyncedServerConfig withGuardsReputationRequirementToBeAttacked(SyncedServerConfig c, int v) { return new SyncedServerConfig(
            c.rubyOre(), c.miraniteOre(), c.chromiteOre(), c.nocturniteOre(), c.amberineOre(), c.rosetteOre(), c.azurosOre(), c.indigraOre(), c.xirionOre(), c.xpOre(),
            c.guardsEnabled(), v, c.guardsReputationRequirement(), c.guardsRunFromPolarBears(), c.guardsOpenDoors(), c.guardFormation(), c.clericHealing(), c.armourerRepairGuardArmour(),
            c.attackAllMobs(), c.guardAlwaysShield(), c.friendlyFire(), c.guardMobBlackList(), c.amountOfHealthRegenerated(), c.followHero(), c.guardHealthModifier(),
            c.guardSpeedModifier(), c.guardFollowRangeModifier(), c.giveGuardStuffHotv(), c.setGuardPatrolHotv(), c.chanceToDropEquipment(), c.raidAnimals(), c.witchesVillager(),
            c.blackSmithHealing(), c.convertVillagerIfHaveHotv(), c.guardVillagerHelpRange(), c.illagersRunFromPolarBears(), c.villagersRunFromPolarBears(), c.guardArrowsHurtVillagers(),
            c.shrineRewardsIncludeVanillaItems(), c.shrineRewardsIncludeModdedItems(), c.shrineGenerationInterval(), c.pathsEnabled(), c.pathsMaxDistance(), c.pathsUseTerrainFollowing(),
            c.pathsPreserveDurability(), c.pathsDefaultPathBlockId(), c.pathsAllowedStartingBlocks(), c.pathsAllowedEndingBlocks(), c.pathsMinWidth(), c.pathsMaxWidth(),
            c.enableChickenFeathers(), c.chickenFeatherInterval(), c.chickenFeatherChance(), c.enableSkeletonBones(), c.skeletonBoneInterval(), c.skeletonBoneChance(), c.enableSpiderDrops(),
            c.spiderDropInterval(), c.spiderDropChance(), c.cobwebWeight(), c.minDecayTicks(), c.maxDecayTicks(), c.baseDecayChance(), c.lightDecayBonus(), c.darknessDecayReduction(),
            c.densityRadius(), c.densityLimit(), c.normalSpiderRate(), c.caveSpiderRate(), c.horseSwimmingEnabled(), c.undeadHorseSwimmingEnabled(), c.horsePreventWandering(),
            c.horseStayRadius(), c.horseRemoveMiningPenalty(), c.horseIncreaseStepHeight(), c.enableRecursiveOpening(), c.recursiveOpeningMaxBlocksDistance(), c.enableDoors(),
            c.enableFenceGates(), c.enableTrapdoors(), c.enableModIncompatibilityCheck(), c.dropMode()
    );}
    private static SyncedServerConfig withGuardsReputationRequirement(SyncedServerConfig c, int v) { return with(c, x -> x.guardsReputationRequirement = v); }
    private static SyncedServerConfig withGuardsRunFromPolarBears(SyncedServerConfig c, boolean v) { return with(c, x -> x.guardsRunFromPolarBears = v); }
    private static SyncedServerConfig withGuardsOpenDoors(SyncedServerConfig c, boolean v) { return with(c, x -> x.guardsOpenDoors = v); }
    private static SyncedServerConfig withGuardFormation(SyncedServerConfig c, boolean v) { return with(c, x -> x.guardFormation = v); }
    private static SyncedServerConfig withClericHealing(SyncedServerConfig c, boolean v) { return with(c, x -> x.clericHealing = v); }
    private static SyncedServerConfig withArmourerRepairGuardArmour(SyncedServerConfig c, boolean v) { return with(c, x -> x.armourerRepairGuardArmour = v); }
    private static SyncedServerConfig withAttackAllMobs(SyncedServerConfig c, boolean v) { return with(c, x -> x.attackAllMobs = v); }
    private static SyncedServerConfig withGuardAlwaysShield(SyncedServerConfig c, boolean v) { return with(c, x -> x.guardAlwaysShield = v); }
    private static SyncedServerConfig withFriendlyFire(SyncedServerConfig c, boolean v) { return with(c, x -> x.friendlyFire = v); }
    private static SyncedServerConfig withGuardMobBlackList(SyncedServerConfig c, List<String> v) { return with(c, x -> x.guardMobBlackList = new ArrayList<>(v)); }
    private static SyncedServerConfig withAmountOfHealthRegenerated(SyncedServerConfig c, float v) { return with(c, x -> x.amountOfHealthRegenerated = v); }
    private static SyncedServerConfig withFollowHero(SyncedServerConfig c, boolean v) { return with(c, x -> x.followHero = v); }
    private static SyncedServerConfig withGuardHealthModifier(SyncedServerConfig c, double v) { return with(c, x -> x.guardHealthModifier = v); }
    private static SyncedServerConfig withGuardSpeedModifier(SyncedServerConfig c, double v) { return with(c, x -> x.guardSpeedModifier = v); }
    private static SyncedServerConfig withGuardFollowRangeModifier(SyncedServerConfig c, double v) { return with(c, x -> x.guardFollowRangeModifier = v); }
    private static SyncedServerConfig withGiveGuardStuffHotv(SyncedServerConfig c, boolean v) { return with(c, x -> x.giveGuardStuffHotv = v); }
    private static SyncedServerConfig withSetGuardPatrolHotv(SyncedServerConfig c, boolean v) { return with(c, x -> x.setGuardPatrolHotv = v); }
    private static SyncedServerConfig withChanceToDropEquipment(SyncedServerConfig c, float v) { return with(c, x -> x.chanceToDropEquipment = v); }
    private static SyncedServerConfig withRaidAnimals(SyncedServerConfig c, boolean v) { return with(c, x -> x.raidAnimals = v); }
    private static SyncedServerConfig withWitchesVillager(SyncedServerConfig c, boolean v) { return with(c, x -> x.witchesVillager = v); }
    private static SyncedServerConfig withBlackSmithHealing(SyncedServerConfig c, boolean v) { return with(c, x -> x.blackSmithHealing = v); }
    private static SyncedServerConfig withConvertVillagerIfHaveHotv(SyncedServerConfig c, boolean v) { return with(c, x -> x.convertVillagerIfHaveHotv = v); }
    private static SyncedServerConfig withGuardVillagerHelpRange(SyncedServerConfig c, double v) { return with(c, x -> x.guardVillagerHelpRange = v); }
    private static SyncedServerConfig withIllagersRunFromPolarBears(SyncedServerConfig c, boolean v) { return with(c, x -> x.illagersRunFromPolarBears = v); }
    private static SyncedServerConfig withVillagersRunFromPolarBears(SyncedServerConfig c, boolean v) { return with(c, x -> x.villagersRunFromPolarBears = v); }
    private static SyncedServerConfig withGuardArrowsHurtVillagers(SyncedServerConfig c, boolean v) { return with(c, x -> x.guardArrowsHurtVillagers = v); }

    private static SyncedServerConfig withShrineRewardsIncludeVanillaItems(SyncedServerConfig c, boolean v) { return with(c, x -> x.shrineRewardsIncludeVanillaItems = v); }
    private static SyncedServerConfig withShrineRewardsIncludeModdedItems(SyncedServerConfig c, boolean v) { return with(c, x -> x.shrineRewardsIncludeModdedItems = v); }
    private static SyncedServerConfig withShrineGenerationInterval(SyncedServerConfig c, long v) { return with(c, x -> x.shrineGenerationInterval = v); }

    private static SyncedServerConfig withPathsEnabled(SyncedServerConfig c, boolean v) { return with(c, x -> x.pathsEnabled = v); }
    private static SyncedServerConfig withPathsMaxDistance(SyncedServerConfig c, int v) { return with(c, x -> x.pathsMaxDistance = v); }
    private static SyncedServerConfig withPathsUseTerrainFollowing(SyncedServerConfig c, boolean v) { return with(c, x -> x.pathsUseTerrainFollowing = v); }
    private static SyncedServerConfig withPathsPreserveDurability(SyncedServerConfig c, boolean v) { return with(c, x -> x.pathsPreserveDurability = v); }
    private static SyncedServerConfig withPathsDefaultPathBlockId(SyncedServerConfig c, String v) { return with(c, x -> x.pathsDefaultPathBlockId = v); }
    private static SyncedServerConfig withPathsAllowedStartingBlocks(SyncedServerConfig c, List<String> v) { return with(c, x -> x.pathsAllowedStartingBlocks = new ArrayList<>(v)); }
    private static SyncedServerConfig withPathsAllowedEndingBlocks(SyncedServerConfig c, List<String> v) { return with(c, x -> x.pathsAllowedEndingBlocks = new ArrayList<>(v)); }
    private static SyncedServerConfig withPathsMinWidth(SyncedServerConfig c, int v) { return with(c, x -> x.pathsMinWidth = v); }
    private static SyncedServerConfig withPathsMaxWidth(SyncedServerConfig c, int v) { return with(c, x -> x.pathsMaxWidth = v); }

    private static SyncedServerConfig withEnableChickenFeathers(SyncedServerConfig c, boolean v) { return with(c, x -> x.enableChickenFeathers = v); }
    private static SyncedServerConfig withChickenFeatherInterval(SyncedServerConfig c, int v) { return with(c, x -> x.chickenFeatherInterval = v); }
    private static SyncedServerConfig withChickenFeatherChance(SyncedServerConfig c, float v) { return with(c, x -> x.chickenFeatherChance = v); }
    private static SyncedServerConfig withEnableSkeletonBones(SyncedServerConfig c, boolean v) { return with(c, x -> x.enableSkeletonBones = v); }
    private static SyncedServerConfig withSkeletonBoneInterval(SyncedServerConfig c, int v) { return with(c, x -> x.skeletonBoneInterval = v); }
    private static SyncedServerConfig withSkeletonBoneChance(SyncedServerConfig c, float v) { return with(c, x -> x.skeletonBoneChance = v); }
    private static SyncedServerConfig withEnableSpiderDrops(SyncedServerConfig c, boolean v) { return with(c, x -> x.enableSpiderDrops = v); }
    private static SyncedServerConfig withSpiderDropInterval(SyncedServerConfig c, int v) { return with(c, x -> x.spiderDropInterval = v); }
    private static SyncedServerConfig withSpiderDropChance(SyncedServerConfig c, float v) { return with(c, x -> x.spiderDropChance = v); }
    private static SyncedServerConfig withCobwebWeight(SyncedServerConfig c, float v) { return with(c, x -> x.cobwebWeight = v); }

    private static SyncedServerConfig withMinDecayTicks(SyncedServerConfig c, int v) { return with(c, x -> x.minDecayTicks = v); }
    private static SyncedServerConfig withMaxDecayTicks(SyncedServerConfig c, int v) { return with(c, x -> x.maxDecayTicks = v); }
    private static SyncedServerConfig withBaseDecayChance(SyncedServerConfig c, float v) { return with(c, x -> x.baseDecayChance = v); }
    private static SyncedServerConfig withLightDecayBonus(SyncedServerConfig c, float v) { return with(c, x -> x.lightDecayBonus = v); }
    private static SyncedServerConfig withDarknessDecayReduction(SyncedServerConfig c, float v) { return with(c, x -> x.darknessDecayReduction = v); }
    private static SyncedServerConfig withDensityRadius(SyncedServerConfig c, int v) { return with(c, x -> x.densityRadius = v); }
    private static SyncedServerConfig withDensityLimit(SyncedServerConfig c, int v) { return with(c, x -> x.densityLimit = v); }
    private static SyncedServerConfig withNormalSpiderRate(SyncedServerConfig c, float v) { return with(c, x -> x.normalSpiderRate = v); }
    private static SyncedServerConfig withCaveSpiderRate(SyncedServerConfig c, float v) { return with(c, x -> x.caveSpiderRate = v); }

    private static SyncedServerConfig withHorseSwimmingEnabled(SyncedServerConfig c, boolean v) { return with(c, x -> x.horseSwimmingEnabled = v); }
    private static SyncedServerConfig withUndeadHorseSwimmingEnabled(SyncedServerConfig c, boolean v) { return with(c, x -> x.undeadHorseSwimmingEnabled = v); }
    private static SyncedServerConfig withHorsePreventWandering(SyncedServerConfig c, boolean v) { return with(c, x -> x.horsePreventWandering = v); }
    private static SyncedServerConfig withHorseStayRadius(SyncedServerConfig c, double v) { return with(c, x -> x.horseStayRadius = v); }
    private static SyncedServerConfig withHorseRemoveMiningPenalty(SyncedServerConfig c, boolean v) { return with(c, x -> x.horseRemoveMiningPenalty = v); }
    private static SyncedServerConfig withHorseIncreaseStepHeight(SyncedServerConfig c, boolean v) { return with(c, x -> x.horseIncreaseStepHeight = v); }

    private static SyncedServerConfig withEnableRecursiveOpening(SyncedServerConfig c, boolean v) { return with(c, x -> x.enableRecursiveOpening = v); }
    private static SyncedServerConfig withRecursiveOpeningMaxBlocksDistance(SyncedServerConfig c, int v) { return with(c, x -> x.recursiveOpeningMaxBlocksDistance = v); }
    private static SyncedServerConfig withEnableDoors(SyncedServerConfig c, boolean v) { return with(c, x -> x.enableDoors = v); }
    private static SyncedServerConfig withEnableFenceGates(SyncedServerConfig c, boolean v) { return with(c, x -> x.enableFenceGates = v); }
    private static SyncedServerConfig withEnableTrapdoors(SyncedServerConfig c, boolean v) { return with(c, x -> x.enableTrapdoors = v); }
    private static SyncedServerConfig withEnableModIncompatibilityCheck(SyncedServerConfig c, boolean v) { return with(c, x -> x.enableModIncompatibilityCheck = v); }

    private static SyncedServerConfig withDropMode(SyncedServerConfig c, net.ryan.beyond_the_block.config.DropMode v) { return with(c, x -> x.dropMode = v); }

    private interface Mutator {
        void apply(Mutable m);
    }

    private static SyncedServerConfig with(SyncedServerConfig c, Mutator mutator) {
        Mutable m = new Mutable(c);
        mutator.apply(m);
        return m.build();
    }

    private static final class Mutable {
        int rubyOre;
        int miraniteOre;
        int chromiteOre;
        int nocturniteOre;
        int amberineOre;
        int rosetteOre;
        int azurosOre;
        int indigraOre;
        int xirionOre;
        int xpOre;

        boolean guardsEnabled;
        int guardsReputationRequirementToBeAttacked;
        int guardsReputationRequirement;
        boolean guardsRunFromPolarBears;
        boolean guardsOpenDoors;
        boolean guardFormation;
        boolean clericHealing;
        boolean armourerRepairGuardArmour;
        boolean attackAllMobs;
        boolean guardAlwaysShield;
        boolean friendlyFire;
        List<String> guardMobBlackList;
        float amountOfHealthRegenerated;
        boolean followHero;
        double guardHealthModifier;
        double guardSpeedModifier;
        double guardFollowRangeModifier;
        boolean giveGuardStuffHotv;
        boolean setGuardPatrolHotv;
        float chanceToDropEquipment;
        boolean raidAnimals;
        boolean witchesVillager;
        boolean blackSmithHealing;
        boolean convertVillagerIfHaveHotv;
        double guardVillagerHelpRange;
        boolean illagersRunFromPolarBears;
        boolean villagersRunFromPolarBears;
        boolean guardArrowsHurtVillagers;

        boolean shrineRewardsIncludeVanillaItems;
        boolean shrineRewardsIncludeModdedItems;
        long shrineGenerationInterval;

        boolean pathsEnabled;
        int pathsMaxDistance;
        boolean pathsUseTerrainFollowing;
        boolean pathsPreserveDurability;
        String pathsDefaultPathBlockId;
        List<String> pathsAllowedStartingBlocks;
        List<String> pathsAllowedEndingBlocks;
        int pathsMinWidth;
        int pathsMaxWidth;

        boolean enableChickenFeathers;
        int chickenFeatherInterval;
        float chickenFeatherChance;
        boolean enableSkeletonBones;
        int skeletonBoneInterval;
        float skeletonBoneChance;
        boolean enableSpiderDrops;
        int spiderDropInterval;
        float spiderDropChance;
        float cobwebWeight;

        int minDecayTicks;
        int maxDecayTicks;
        float baseDecayChance;
        float lightDecayBonus;
        float darknessDecayReduction;
        int densityRadius;
        int densityLimit;
        float normalSpiderRate;
        float caveSpiderRate;

        boolean horseSwimmingEnabled;
        boolean undeadHorseSwimmingEnabled;
        boolean horsePreventWandering;
        double horseStayRadius;
        boolean horseRemoveMiningPenalty;
        boolean horseIncreaseStepHeight;

        boolean enableRecursiveOpening;
        int recursiveOpeningMaxBlocksDistance;
        boolean enableDoors;
        boolean enableFenceGates;
        boolean enableTrapdoors;
        boolean enableModIncompatibilityCheck;

        net.ryan.beyond_the_block.config.DropMode dropMode;

        Mutable(SyncedServerConfig c) {
            rubyOre = c.rubyOre();
            miraniteOre = c.miraniteOre();
            chromiteOre = c.chromiteOre();
            nocturniteOre = c.nocturniteOre();
            amberineOre = c.amberineOre();
            rosetteOre = c.rosetteOre();
            azurosOre = c.azurosOre();
            indigraOre = c.indigraOre();
            xirionOre = c.xirionOre();
            xpOre = c.xpOre();

            guardsEnabled = c.guardsEnabled();
            guardsReputationRequirementToBeAttacked = c.guardsReputationRequirementToBeAttacked();
            guardsReputationRequirement = c.guardsReputationRequirement();
            guardsRunFromPolarBears = c.guardsRunFromPolarBears();
            guardsOpenDoors = c.guardsOpenDoors();
            guardFormation = c.guardFormation();
            clericHealing = c.clericHealing();
            armourerRepairGuardArmour = c.armourerRepairGuardArmour();
            attackAllMobs = c.attackAllMobs();
            guardAlwaysShield = c.guardAlwaysShield();
            friendlyFire = c.friendlyFire();
            guardMobBlackList = new ArrayList<>(c.guardMobBlackList());
            amountOfHealthRegenerated = c.amountOfHealthRegenerated();
            followHero = c.followHero();
            guardHealthModifier = c.guardHealthModifier();
            guardSpeedModifier = c.guardSpeedModifier();
            guardFollowRangeModifier = c.guardFollowRangeModifier();
            giveGuardStuffHotv = c.giveGuardStuffHotv();
            setGuardPatrolHotv = c.setGuardPatrolHotv();
            chanceToDropEquipment = c.chanceToDropEquipment();
            raidAnimals = c.raidAnimals();
            witchesVillager = c.witchesVillager();
            blackSmithHealing = c.blackSmithHealing();
            convertVillagerIfHaveHotv = c.convertVillagerIfHaveHotv();
            guardVillagerHelpRange = c.guardVillagerHelpRange();
            illagersRunFromPolarBears = c.illagersRunFromPolarBears();
            villagersRunFromPolarBears = c.villagersRunFromPolarBears();
            guardArrowsHurtVillagers = c.guardArrowsHurtVillagers();

            shrineRewardsIncludeVanillaItems = c.shrineRewardsIncludeVanillaItems();
            shrineRewardsIncludeModdedItems = c.shrineRewardsIncludeModdedItems();
            shrineGenerationInterval = c.shrineGenerationInterval();

            pathsEnabled = c.pathsEnabled();
            pathsMaxDistance = c.pathsMaxDistance();
            pathsUseTerrainFollowing = c.pathsUseTerrainFollowing();
            pathsPreserveDurability = c.pathsPreserveDurability();
            pathsDefaultPathBlockId = c.pathsDefaultPathBlockId();
            pathsAllowedStartingBlocks = new ArrayList<>(c.pathsAllowedStartingBlocks());
            pathsAllowedEndingBlocks = new ArrayList<>(c.pathsAllowedEndingBlocks());
            pathsMinWidth = c.pathsMinWidth();
            pathsMaxWidth = c.pathsMaxWidth();

            enableChickenFeathers = c.enableChickenFeathers();
            chickenFeatherInterval = c.chickenFeatherInterval();
            chickenFeatherChance = c.chickenFeatherChance();
            enableSkeletonBones = c.enableSkeletonBones();
            skeletonBoneInterval = c.skeletonBoneInterval();
            skeletonBoneChance = c.skeletonBoneChance();
            enableSpiderDrops = c.enableSpiderDrops();
            spiderDropInterval = c.spiderDropInterval();
            spiderDropChance = c.spiderDropChance();
            cobwebWeight = c.cobwebWeight();

            minDecayTicks = c.minDecayTicks();
            maxDecayTicks = c.maxDecayTicks();
            baseDecayChance = c.baseDecayChance();
            lightDecayBonus = c.lightDecayBonus();
            darknessDecayReduction = c.darknessDecayReduction();
            densityRadius = c.densityRadius();
            densityLimit = c.densityLimit();
            normalSpiderRate = c.normalSpiderRate();
            caveSpiderRate = c.caveSpiderRate();

            horseSwimmingEnabled = c.horseSwimmingEnabled();
            undeadHorseSwimmingEnabled = c.undeadHorseSwimmingEnabled();
            horsePreventWandering = c.horsePreventWandering();
            horseStayRadius = c.horseStayRadius();
            horseRemoveMiningPenalty = c.horseRemoveMiningPenalty();
            horseIncreaseStepHeight = c.horseIncreaseStepHeight();

            enableRecursiveOpening = c.enableRecursiveOpening();
            recursiveOpeningMaxBlocksDistance = c.recursiveOpeningMaxBlocksDistance();
            enableDoors = c.enableDoors();
            enableFenceGates = c.enableFenceGates();
            enableTrapdoors = c.enableTrapdoors();
            enableModIncompatibilityCheck = c.enableModIncompatibilityCheck();

            dropMode = c.dropMode();
        }

        SyncedServerConfig build() {
            return new SyncedServerConfig(
                    rubyOre, miraniteOre, chromiteOre, nocturniteOre, amberineOre, rosetteOre, azurosOre, indigraOre, xirionOre, xpOre,
                    guardsEnabled, guardsReputationRequirementToBeAttacked, guardsReputationRequirement, guardsRunFromPolarBears,
                    guardsOpenDoors, guardFormation, clericHealing, armourerRepairGuardArmour, attackAllMobs, guardAlwaysShield,
                    friendlyFire, guardMobBlackList, amountOfHealthRegenerated, followHero, guardHealthModifier, guardSpeedModifier,
                    guardFollowRangeModifier, giveGuardStuffHotv, setGuardPatrolHotv, chanceToDropEquipment, raidAnimals,
                    witchesVillager, blackSmithHealing, convertVillagerIfHaveHotv, guardVillagerHelpRange, illagersRunFromPolarBears,
                    villagersRunFromPolarBears, guardArrowsHurtVillagers,
                    shrineRewardsIncludeVanillaItems, shrineRewardsIncludeModdedItems, shrineGenerationInterval,
                    pathsEnabled, pathsMaxDistance, pathsUseTerrainFollowing, pathsPreserveDurability, pathsDefaultPathBlockId,
                    pathsAllowedStartingBlocks, pathsAllowedEndingBlocks, pathsMinWidth, pathsMaxWidth,
                    enableChickenFeathers, chickenFeatherInterval, chickenFeatherChance, enableSkeletonBones, skeletonBoneInterval,
                    skeletonBoneChance, enableSpiderDrops, spiderDropInterval, spiderDropChance, cobwebWeight,
                    minDecayTicks, maxDecayTicks, baseDecayChance, lightDecayBonus, darknessDecayReduction, densityRadius, densityLimit,
                    normalSpiderRate, caveSpiderRate,
                    horseSwimmingEnabled, undeadHorseSwimmingEnabled, horsePreventWandering, horseStayRadius,
                    horseRemoveMiningPenalty, horseIncreaseStepHeight,
                    enableRecursiveOpening, recursiveOpeningMaxBlocksDistance, enableDoors, enableFenceGates,
                    enableTrapdoors, enableModIncompatibilityCheck, dropMode
            );
        }
    }
}