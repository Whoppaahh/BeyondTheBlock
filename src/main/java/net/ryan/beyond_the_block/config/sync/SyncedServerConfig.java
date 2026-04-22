package net.ryan.beyond_the_block.config.sync;

import net.minecraft.network.PacketByteBuf;
import net.ryan.beyond_the_block.config.DropMode;
import net.ryan.beyond_the_block.config.schema.ConfigServer;

import java.util.ArrayList;
import java.util.List;

public record SyncedServerConfig(
        // Worldgen -> Ores
        int rubyOre,
        int miraniteOre,
        int chromiteOre,
        int nocturniteOre,
        int amberineOre,
        int rosetteOre,
        int azurosOre,
        int indigraOre,
        int xirionOre,
        int xpOre,

        // Features -> Guards
        boolean guardsEnabled,
        int guardsReputationRequirementToBeAttacked,
        int guardsReputationRequirement,
        boolean guardsRunFromPolarBears,
        boolean guardsOpenDoors,
        boolean guardFormation,
        boolean clericHealing,
        boolean armourerRepairGuardArmour,
        boolean attackAllMobs,
        boolean guardAlwaysShield,
        boolean friendlyFire,
        List<String> guardMobBlackList,
        float amountOfHealthRegenerated,
        boolean followHero,
        double guardHealthModifier,
        double guardSpeedModifier,
        double guardFollowRangeModifier,
        boolean giveGuardStuffHotv,
        boolean setGuardPatrolHotv,
        float chanceToDropEquipment,
        boolean raidAnimals,
        boolean witchesVillager,
        boolean blackSmithHealing,
        boolean convertVillagerIfHaveHotv,
        double guardVillagerHelpRange,
        boolean illagersRunFromPolarBears,
        boolean villagersRunFromPolarBears,
        boolean guardArrowsHurtVillagers,

        // Features -> Shrines
        boolean shrineRewardsIncludeVanillaItems,
        boolean shrineRewardsIncludeModdedItems,
        long shrineGenerationInterval,

        // Features -> Paths
        boolean pathsEnabled,
        int pathsMaxDistance,
        boolean pathsUseTerrainFollowing,
        boolean pathsPreserveDurability,
        String pathsDefaultPathBlockId,
        List<String> pathsAllowedStartingBlocks,
        List<String> pathsAllowedEndingBlocks,
        int pathsMinWidth,
        int pathsMaxWidth,

        // Features -> PassiveDrops
        boolean enableChickenFeathers,
        int chickenFeatherInterval,
        float chickenFeatherChance,
        boolean enableSkeletonBones,
        int skeletonBoneInterval,
        float skeletonBoneChance,
        boolean enableSpiderDrops,
        int spiderDropInterval,
        float spiderDropChance,
        float cobwebWeight,

        // Features -> WebDecay
        int minDecayTicks,
        int maxDecayTicks,
        float baseDecayChance,
        float lightDecayBonus,
        float darknessDecayReduction,
        int densityRadius,
        int densityLimit,
        float normalSpiderRate,
        float caveSpiderRate,

        // Features -> Horses
        boolean horseSwimmingEnabled,
        boolean undeadHorseSwimmingEnabled,
        boolean horsePreventWandering,
        double horseStayRadius,
        boolean horseRemoveMiningPenalty,
        boolean horseIncreaseStepHeight,

        // Features -> DoubleOpenables
        boolean enableRecursiveOpening,
        int recursiveOpeningMaxBlocksDistance,
        boolean enableDoors,
        boolean enableFenceGates,
        boolean enableTrapdoors,
        boolean enableModIncompatibilityCheck,

        // Features -> Fire
        boolean fireEnabled,
        int fireBaseColor,
        int fireSoulBaseColor,
        String firePriority,
        List<String> fireBiomeRules,
        List<String> fireBlockRules,
        List<String> fireBlockTagRules,

        // Features -> Enchantments
        DropMode dropMode

) {
    public static final int PROTOCOL_VERSION = 3;

    public SyncedServerConfig {
        guardMobBlackList = List.copyOf(guardMobBlackList);
        pathsAllowedStartingBlocks = List.copyOf(pathsAllowedStartingBlocks);
        pathsAllowedEndingBlocks = List.copyOf(pathsAllowedEndingBlocks);
        fireBiomeRules = List.copyOf(fireBiomeRules);
        fireBlockRules = List.copyOf(fireBlockRules);
        fireBlockTagRules = List.copyOf(fireBlockTagRules);
    }

    public static SyncedServerConfig fromServerConfig(ConfigServer cfg) {
        return new SyncedServerConfig(
                // Ores
                cfg.worldgen.ores.rubyOre,
                cfg.worldgen.ores.miraniteOre,
                cfg.worldgen.ores.chromiteOre,
                cfg.worldgen.ores.nocturniteOre,
                cfg.worldgen.ores.amberineOre,
                cfg.worldgen.ores.rosetteOre,
                cfg.worldgen.ores.azurosOre,
                cfg.worldgen.ores.indigraOre,
                cfg.worldgen.ores.xirionOre,
                cfg.worldgen.ores.xpOre,

                // Guards
                cfg.features.guards.enabled,
                cfg.features.guards.reputationRequirementToBeAttacked,
                cfg.features.guards.reputationRequirement,
                cfg.features.guards.guardsRunFromPolarBears,
                cfg.features.guards.guardsOpenDoors,
                cfg.features.guards.guardFormation,
                cfg.features.guards.clericHealing,
                cfg.features.guards.armourerRepairGuardArmour,
                cfg.features.guards.attackAllMobs,
                cfg.features.guards.guardAlwaysShield,
                cfg.features.guards.friendlyFire,
                cfg.features.guards.mobBlackList,
                cfg.features.guards.amountOfHealthRegenerated,
                cfg.features.guards.followHero,
                cfg.features.guards.healthModifier,
                cfg.features.guards.speedModifier,
                cfg.features.guards.followRangeModifier,
                cfg.features.guards.giveGuardStuffHotv,
                cfg.features.guards.setGuardPatrolHotv,
                cfg.features.guards.chanceToDropEquipment,
                cfg.features.guards.raidAnimals,
                cfg.features.guards.witchesVillager,
                cfg.features.guards.blackSmithHealing,
                cfg.features.guards.convertVillagerIfHaveHotv,
                cfg.features.guards.guardVillagerHelpRange,
                cfg.features.guards.illagersRunFromPolarBears,
                cfg.features.guards.villagersRunFromPolarBears,
                cfg.features.guards.guardArrowsHurtVillagers,

                // Shrines
                cfg.features.shrines.rewardsIncludeVanillaItems,
                cfg.features.shrines.rewardsIncludeModdedItems,
                cfg.features.shrines.generationInterval,

                // Paths
                cfg.features.paths.enabled,
                cfg.features.paths.maxDistance,
                cfg.features.paths.useTerrainFollowing,
                cfg.features.paths.preserveDurability,
                cfg.features.paths.defaultPathBlockId,
                cfg.features.paths.allowedStartingBlocks,
                cfg.features.paths.allowedEndingBlocks,
                cfg.features.paths.minWidth,
                cfg.features.paths.maxWidth,

                // Passive drops
                cfg.features.drops.enableChickenFeathers,
                cfg.features.drops.chickenFeatherInterval,
                cfg.features.drops.chickenFeatherChance,
                cfg.features.drops.enableSkeletonBones,
                cfg.features.drops.skeletonBoneInterval,
                cfg.features.drops.skeletonBoneChance,
                cfg.features.drops.enableSpiderDrops,
                cfg.features.drops.spiderDropInterval,
                cfg.features.drops.spiderDropChance,
                cfg.features.drops.cobwebWeight,

                // Web decay
                cfg.features.webs.minDecayTicks,
                cfg.features.webs.maxDecayTicks,
                cfg.features.webs.baseDecayChance,
                cfg.features.webs.lightDecayBonus,
                cfg.features.webs.darknessDecayReduction,
                cfg.features.webs.densityRadius,
                cfg.features.webs.densityLimit,
                cfg.features.webs.normalSpiderRate,
                cfg.features.webs.caveSpiderRate,

                // Horses
                cfg.features.horses.enableSwimming,
                cfg.features.horses.undeadCanSwim,
                cfg.features.horses.preventWandering,
                cfg.features.horses.stayRadius,
                cfg.features.horses.removeMiningPenalty,
                cfg.features.horses.increaseStepHeight,

                // Openables
                cfg.features.openables.enableRecursiveOpening,
                cfg.features.openables.recursiveOpeningMaxBlocksDistance,
                cfg.features.openables.enableDoors,
                cfg.features.openables.enableFenceGates,
                cfg.features.openables.enableTrapdoors,
                cfg.features.openables.enableModIncompatibilityCheck,

                // Fire
                cfg.features.fire.enabled,
                cfg.features.fire.baseFireColor,
                cfg.features.fire.baseSoulFireColor,
                cfg.features.fire.priority.name(),
                cfg.features.fire.biomeRules,
                cfg.features.fire.blockRules,
                cfg.features.fire.blockTagRules,

                // Enchantments
                cfg.features.enchantments.dropMode
        );
    }

    public void applyToServerConfig(ConfigServer cfg) {
        // Ores
        cfg.worldgen.ores.rubyOre = clamp(rubyOre, 0, 128);
        cfg.worldgen.ores.miraniteOre = clamp(miraniteOre, 0, 128);
        cfg.worldgen.ores.chromiteOre = clamp(chromiteOre, 0, 128);
        cfg.worldgen.ores.nocturniteOre = clamp(nocturniteOre, 0, 128);
        cfg.worldgen.ores.amberineOre = clamp(amberineOre, 0, 128);
        cfg.worldgen.ores.rosetteOre = clamp(rosetteOre, 0, 128);
        cfg.worldgen.ores.azurosOre = clamp(azurosOre, 0, 128);
        cfg.worldgen.ores.indigraOre = clamp(indigraOre, 0, 128);
        cfg.worldgen.ores.xirionOre = clamp(xirionOre, 0, 128);
        cfg.worldgen.ores.xpOre = clamp(xpOre, 0, 128);

        // Guards
        cfg.features.guards.enabled = guardsEnabled;
        cfg.features.guards.reputationRequirementToBeAttacked = guardsReputationRequirementToBeAttacked;
        cfg.features.guards.reputationRequirement = guardsReputationRequirement;
        cfg.features.guards.guardsRunFromPolarBears = guardsRunFromPolarBears;
        cfg.features.guards.guardsOpenDoors = guardsOpenDoors;
        cfg.features.guards.guardFormation = guardFormation;
        cfg.features.guards.clericHealing = clericHealing;
        cfg.features.guards.armourerRepairGuardArmour = armourerRepairGuardArmour;
        cfg.features.guards.attackAllMobs = attackAllMobs;
        cfg.features.guards.guardAlwaysShield = guardAlwaysShield;
        cfg.features.guards.friendlyFire = friendlyFire;
        cfg.features.guards.mobBlackList = new ArrayList<>(guardMobBlackList);
        cfg.features.guards.amountOfHealthRegenerated = amountOfHealthRegenerated;
        cfg.features.guards.followHero = followHero;
        cfg.features.guards.healthModifier = guardHealthModifier;
        cfg.features.guards.speedModifier = guardSpeedModifier;
        cfg.features.guards.followRangeModifier = guardFollowRangeModifier;
        cfg.features.guards.giveGuardStuffHotv = giveGuardStuffHotv;
        cfg.features.guards.setGuardPatrolHotv = setGuardPatrolHotv;
        cfg.features.guards.chanceToDropEquipment = chanceToDropEquipment;
        cfg.features.guards.raidAnimals = raidAnimals;
        cfg.features.guards.witchesVillager = witchesVillager;
        cfg.features.guards.blackSmithHealing = blackSmithHealing;
        cfg.features.guards.convertVillagerIfHaveHotv = convertVillagerIfHaveHotv;
        cfg.features.guards.guardVillagerHelpRange = guardVillagerHelpRange;
        cfg.features.guards.illagersRunFromPolarBears = illagersRunFromPolarBears;
        cfg.features.guards.villagersRunFromPolarBears = villagersRunFromPolarBears;
        cfg.features.guards.guardArrowsHurtVillagers = guardArrowsHurtVillagers;

        // Shrines
        cfg.features.shrines.rewardsIncludeVanillaItems = shrineRewardsIncludeVanillaItems;
        cfg.features.shrines.rewardsIncludeModdedItems = shrineRewardsIncludeModdedItems;
        cfg.features.shrines.generationInterval = clamp(shrineGenerationInterval, 200L, 240000L);

        // Paths
        cfg.features.paths.enabled = pathsEnabled;
        cfg.features.paths.maxDistance = clamp(pathsMaxDistance, 1, 256);
        cfg.features.paths.useTerrainFollowing = pathsUseTerrainFollowing;
        cfg.features.paths.preserveDurability = pathsPreserveDurability;
        cfg.features.paths.defaultPathBlockId = blankToDefault(pathsDefaultPathBlockId, "minecraft:dirt_path");
        cfg.features.paths.allowedStartingBlocks = new ArrayList<>(pathsAllowedStartingBlocks);
        cfg.features.paths.allowedEndingBlocks = new ArrayList<>(pathsAllowedEndingBlocks);
        cfg.features.paths.minWidth = clamp(pathsMinWidth, 1, 9);
        cfg.features.paths.maxWidth = Math.max(cfg.features.paths.minWidth, clamp(pathsMaxWidth, 1, 9));

        // Passive drops
        cfg.features.drops.enableChickenFeathers = enableChickenFeathers;
        cfg.features.drops.chickenFeatherInterval = clamp(chickenFeatherInterval, 1, Integer.MAX_VALUE);
        cfg.features.drops.chickenFeatherChance = chickenFeatherChance;
        cfg.features.drops.enableSkeletonBones = enableSkeletonBones;
        cfg.features.drops.skeletonBoneInterval = clamp(skeletonBoneInterval, 1, Integer.MAX_VALUE);
        cfg.features.drops.skeletonBoneChance = skeletonBoneChance;
        cfg.features.drops.enableSpiderDrops = enableSpiderDrops;
        cfg.features.drops.spiderDropInterval = clamp(spiderDropInterval, 1, Integer.MAX_VALUE);
        cfg.features.drops.spiderDropChance = spiderDropChance;
        cfg.features.drops.cobwebWeight = cobwebWeight;

        // Web decay
        cfg.features.webs.minDecayTicks = clamp(minDecayTicks, 1, Integer.MAX_VALUE);
        cfg.features.webs.maxDecayTicks = Math.max(cfg.features.webs.minDecayTicks, clamp(maxDecayTicks, 1, Integer.MAX_VALUE));
        cfg.features.webs.baseDecayChance = baseDecayChance;
        cfg.features.webs.lightDecayBonus = lightDecayBonus;
        cfg.features.webs.darknessDecayReduction = darknessDecayReduction;
        cfg.features.webs.densityRadius = clamp(densityRadius, 0, 64);
        cfg.features.webs.densityLimit = clamp(densityLimit, 0, 256);
        cfg.features.webs.normalSpiderRate = normalSpiderRate;
        cfg.features.webs.caveSpiderRate = caveSpiderRate;

        // Horses
        cfg.features.horses.enableSwimming = horseSwimmingEnabled;
        cfg.features.horses.undeadCanSwim = undeadHorseSwimmingEnabled;
        cfg.features.horses.preventWandering = horsePreventWandering;
        cfg.features.horses.stayRadius = clamp(horseStayRadius, 2.0D, 64.0D);
        cfg.features.horses.removeMiningPenalty = horseRemoveMiningPenalty;
        cfg.features.horses.increaseStepHeight = horseIncreaseStepHeight;

        // Openables
        cfg.features.openables.enableRecursiveOpening = enableRecursiveOpening;
        cfg.features.openables.recursiveOpeningMaxBlocksDistance = clamp(recursiveOpeningMaxBlocksDistance, 1, 32);
        cfg.features.openables.enableDoors = enableDoors;
        cfg.features.openables.enableFenceGates = enableFenceGates;
        cfg.features.openables.enableTrapdoors = enableTrapdoors;
        cfg.features.openables.enableModIncompatibilityCheck = enableModIncompatibilityCheck;

        // Fire
        cfg.features.fire.enabled = fireEnabled;
        cfg.features.fire.baseFireColor = fireBaseColor;
        cfg.features.fire.baseSoulFireColor = fireSoulBaseColor;
        cfg.features.fire.priority = parseFirePriority(firePriority);
        cfg.features.fire.biomeRules = new ArrayList<>(fireBiomeRules);
        cfg.features.fire.blockRules = new ArrayList<>(fireBlockRules);
        cfg.features.fire.blockTagRules = new ArrayList<>(fireBlockTagRules);

        // Enchantments
        cfg.features.enchantments.dropMode = dropMode == null ? DropMode.NORMAL : dropMode;
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(PROTOCOL_VERSION);

        // Ores
        buf.writeInt(rubyOre);
        buf.writeInt(miraniteOre);
        buf.writeInt(chromiteOre);
        buf.writeInt(nocturniteOre);
        buf.writeInt(amberineOre);
        buf.writeInt(rosetteOre);
        buf.writeInt(azurosOre);
        buf.writeInt(indigraOre);
        buf.writeInt(xirionOre);
        buf.writeInt(xpOre);

        // Guards
        buf.writeBoolean(guardsEnabled);
        buf.writeInt(guardsReputationRequirementToBeAttacked);
        buf.writeInt(guardsReputationRequirement);
        buf.writeBoolean(guardsRunFromPolarBears);
        buf.writeBoolean(guardsOpenDoors);
        buf.writeBoolean(guardFormation);
        buf.writeBoolean(clericHealing);
        buf.writeBoolean(armourerRepairGuardArmour);
        buf.writeBoolean(attackAllMobs);
        buf.writeBoolean(guardAlwaysShield);
        buf.writeBoolean(friendlyFire);

        buf.writeInt(guardMobBlackList.size());
        for (String value : guardMobBlackList) {
            buf.writeString(value);
        }

        buf.writeFloat(amountOfHealthRegenerated);
        buf.writeBoolean(followHero);
        buf.writeDouble(guardHealthModifier);
        buf.writeDouble(guardSpeedModifier);
        buf.writeDouble(guardFollowRangeModifier);
        buf.writeBoolean(giveGuardStuffHotv);
        buf.writeBoolean(setGuardPatrolHotv);
        buf.writeFloat(chanceToDropEquipment);
        buf.writeBoolean(raidAnimals);
        buf.writeBoolean(witchesVillager);
        buf.writeBoolean(blackSmithHealing);
        buf.writeBoolean(convertVillagerIfHaveHotv);
        buf.writeDouble(guardVillagerHelpRange);
        buf.writeBoolean(illagersRunFromPolarBears);
        buf.writeBoolean(villagersRunFromPolarBears);
        buf.writeBoolean(guardArrowsHurtVillagers);

        // Shrines
        buf.writeBoolean(shrineRewardsIncludeVanillaItems);
        buf.writeBoolean(shrineRewardsIncludeModdedItems);
        buf.writeLong(shrineGenerationInterval);

        // Paths
        buf.writeBoolean(pathsEnabled);
        buf.writeInt(pathsMaxDistance);
        buf.writeBoolean(pathsUseTerrainFollowing);
        buf.writeBoolean(pathsPreserveDurability);
        buf.writeString(pathsDefaultPathBlockId);

        buf.writeInt(pathsAllowedStartingBlocks.size());
        for (String value : pathsAllowedStartingBlocks) {
            buf.writeString(value);
        }

        buf.writeInt(pathsAllowedEndingBlocks.size());
        for (String value : pathsAllowedEndingBlocks) {
            buf.writeString(value);
        }

        buf.writeInt(pathsMinWidth);
        buf.writeInt(pathsMaxWidth);

        // Passive drops
        buf.writeBoolean(enableChickenFeathers);
        buf.writeInt(chickenFeatherInterval);
        buf.writeFloat(chickenFeatherChance);
        buf.writeBoolean(enableSkeletonBones);
        buf.writeInt(skeletonBoneInterval);
        buf.writeFloat(skeletonBoneChance);
        buf.writeBoolean(enableSpiderDrops);
        buf.writeInt(spiderDropInterval);
        buf.writeFloat(spiderDropChance);
        buf.writeFloat(cobwebWeight);

        // Web decay
        buf.writeInt(minDecayTicks);
        buf.writeInt(maxDecayTicks);
        buf.writeFloat(baseDecayChance);
        buf.writeFloat(lightDecayBonus);
        buf.writeFloat(darknessDecayReduction);
        buf.writeInt(densityRadius);
        buf.writeInt(densityLimit);
        buf.writeFloat(normalSpiderRate);
        buf.writeFloat(caveSpiderRate);

        // Horses
        buf.writeBoolean(horseSwimmingEnabled);
        buf.writeBoolean(undeadHorseSwimmingEnabled);
        buf.writeBoolean(horsePreventWandering);
        buf.writeDouble(horseStayRadius);
        buf.writeBoolean(horseRemoveMiningPenalty);
        buf.writeBoolean(horseIncreaseStepHeight);

        // Openables
        buf.writeBoolean(enableRecursiveOpening);
        buf.writeInt(recursiveOpeningMaxBlocksDistance);
        buf.writeBoolean(enableDoors);
        buf.writeBoolean(enableFenceGates);
        buf.writeBoolean(enableTrapdoors);
        buf.writeBoolean(enableModIncompatibilityCheck);

        // Fire
        buf.writeBoolean(fireEnabled);
        buf.writeInt(fireBaseColor);
        buf.writeInt(fireSoulBaseColor);
        buf.writeString(firePriority);

        buf.writeInt(fireBiomeRules.size());
        for (String value : fireBiomeRules) {
            buf.writeString(value);
        }

        buf.writeInt(fireBlockRules.size());
        for (String value : fireBlockRules) {
            buf.writeString(value);
        }

        buf.writeInt(fireBlockTagRules.size());
        for (String value : fireBlockTagRules) {
            buf.writeString(value);
        }

        // Enchantments
        buf.writeEnumConstant(dropMode);
    }

    public static SyncedServerConfig read(PacketByteBuf buf) {
        int version = buf.readInt();
        if (version != PROTOCOL_VERSION) {
            throw new IllegalStateException(
                    "Unsupported synced server config version: " + version + ", expected " + PROTOCOL_VERSION
            );
        }

        // Ores
        int rubyOre = buf.readInt();
        int miraniteOre = buf.readInt();
        int chromiteOre = buf.readInt();
        int nocturniteOre = buf.readInt();
        int amberineOre = buf.readInt();
        int rosetteOre = buf.readInt();
        int azurosOre = buf.readInt();
        int indigraOre = buf.readInt();
        int xirionOre = buf.readInt();
        int xpOre = buf.readInt();

        // Guards
        boolean guardsEnabled = buf.readBoolean();
        int guardsReputationRequirementToBeAttacked = buf.readInt();
        int guardsReputationRequirement = buf.readInt();
        boolean guardsRunFromPolarBears = buf.readBoolean();
        boolean guardsOpenDoors = buf.readBoolean();
        boolean guardFormation = buf.readBoolean();
        boolean clericHealing = buf.readBoolean();
        boolean armourerRepairGuardArmour = buf.readBoolean();
        boolean attackAllMobs = buf.readBoolean();
        boolean guardAlwaysShield = buf.readBoolean();
        boolean friendlyFire = buf.readBoolean();

        int blackListSize = buf.readInt();
        List<String> guardMobBlackList = new ArrayList<>(blackListSize);
        for (int i = 0; i < blackListSize; i++) {
            guardMobBlackList.add(buf.readString());
        }

        float amountOfHealthRegenerated = buf.readFloat();
        boolean followHero = buf.readBoolean();
        double guardHealthModifier = buf.readDouble();
        double guardSpeedModifier = buf.readDouble();
        double guardFollowRangeModifier = buf.readDouble();
        boolean giveGuardStuffHotv = buf.readBoolean();
        boolean setGuardPatrolHotv = buf.readBoolean();
        float chanceToDropEquipment = buf.readFloat();
        boolean raidAnimals = buf.readBoolean();
        boolean witchesVillager = buf.readBoolean();
        boolean blackSmithHealing = buf.readBoolean();
        boolean convertVillagerIfHaveHotv = buf.readBoolean();
        double guardVillagerHelpRange = buf.readDouble();
        boolean illagersRunFromPolarBears = buf.readBoolean();
        boolean villagersRunFromPolarBears = buf.readBoolean();
        boolean guardArrowsHurtVillagers = buf.readBoolean();

        // Shrines
        boolean shrineRewardsIncludeVanillaItems = buf.readBoolean();
        boolean shrineRewardsIncludeModdedItems = buf.readBoolean();
        long shrineGenerationInterval = buf.readLong();

        // Paths
        boolean pathsEnabled = buf.readBoolean();
        int pathsMaxDistance = buf.readInt();
        boolean pathsUseTerrainFollowing = buf.readBoolean();
        boolean pathsPreserveDurability = buf.readBoolean();
        String pathsDefaultPathBlockId = buf.readString();

        int startSize = buf.readInt();
        List<String> pathsAllowedStartingBlocks = new ArrayList<>(startSize);
        for (int i = 0; i < startSize; i++) {
            pathsAllowedStartingBlocks.add(buf.readString());
        }

        int endSize = buf.readInt();
        List<String> pathsAllowedEndingBlocks = new ArrayList<>(endSize);
        for (int i = 0; i < endSize; i++) {
            pathsAllowedEndingBlocks.add(buf.readString());
        }

        int pathsMinWidth = buf.readInt();
        int pathsMaxWidth = buf.readInt();

        // Passive drops
        boolean enableChickenFeathers = buf.readBoolean();
        int chickenFeatherInterval = buf.readInt();
        float chickenFeatherChance = buf.readFloat();
        boolean enableSkeletonBones = buf.readBoolean();
        int skeletonBoneInterval = buf.readInt();
        float skeletonBoneChance = buf.readFloat();
        boolean enableSpiderDrops = buf.readBoolean();
        int spiderDropInterval = buf.readInt();
        float spiderDropChance = buf.readFloat();
        float cobwebWeight = buf.readFloat();

        // Web decay
        int minDecayTicks = buf.readInt();
        int maxDecayTicks = buf.readInt();
        float baseDecayChance = buf.readFloat();
        float lightDecayBonus = buf.readFloat();
        float darknessDecayReduction = buf.readFloat();
        int densityRadius = buf.readInt();
        int densityLimit = buf.readInt();
        float normalSpiderRate = buf.readFloat();
        float caveSpiderRate = buf.readFloat();

        // Horses
        boolean horseSwimmingEnabled = buf.readBoolean();
        boolean undeadHorseSwimmingEnabled = buf.readBoolean();
        boolean horsePreventWandering = buf.readBoolean();
        double horseStayRadius = buf.readDouble();
        boolean horseRemoveMiningPenalty = buf.readBoolean();
        boolean horseIncreaseStepHeight = buf.readBoolean();

        // Openables
        boolean enableRecursiveOpening = buf.readBoolean();
        int recursiveOpeningMaxBlocksDistance = buf.readInt();
        boolean enableDoors = buf.readBoolean();
        boolean enableFenceGates = buf.readBoolean();
        boolean enableTrapdoors = buf.readBoolean();
        boolean enableModIncompatibilityCheck = buf.readBoolean();

        // Fire
        boolean fireEnabled = buf.readBoolean();
        int fireBaseColor = buf.readInt();
        int fireSoulBaseColor = buf.readInt();
        String firePriority = buf.readString();

        int fireBiomeRuleCount = buf.readInt();
        List<String> fireBiomeRules = new ArrayList<>(fireBiomeRuleCount);
        for (int i = 0; i < fireBiomeRuleCount; i++) {
            fireBiomeRules.add(buf.readString());
        }

        int fireBlockRuleCount = buf.readInt();
        List<String> fireBlockRules = new ArrayList<>(fireBlockRuleCount);
        for (int i = 0; i < fireBlockRuleCount; i++) {
            fireBlockRules.add(buf.readString());
        }

        int fireBlockTagRuleCount = buf.readInt();
        List<String> fireBlockTagRules = new ArrayList<>(fireBlockTagRuleCount);
        for (int i = 0; i < fireBlockTagRuleCount; i++) {
            fireBlockTagRules.add(buf.readString());
        }

        // Enchantments
        DropMode dropMode = buf.readEnumConstant(DropMode.class);

        return new SyncedServerConfig(
                rubyOre,
                miraniteOre,
                chromiteOre,
                nocturniteOre,
                amberineOre,
                rosetteOre,
                azurosOre,
                indigraOre,
                xirionOre,
                xpOre,

                guardsEnabled,
                guardsReputationRequirementToBeAttacked,
                guardsReputationRequirement,
                guardsRunFromPolarBears,
                guardsOpenDoors,
                guardFormation,
                clericHealing,
                armourerRepairGuardArmour,
                attackAllMobs,
                guardAlwaysShield,
                friendlyFire,
                guardMobBlackList,
                amountOfHealthRegenerated,
                followHero,
                guardHealthModifier,
                guardSpeedModifier,
                guardFollowRangeModifier,
                giveGuardStuffHotv,
                setGuardPatrolHotv,
                chanceToDropEquipment,
                raidAnimals,
                witchesVillager,
                blackSmithHealing,
                convertVillagerIfHaveHotv,
                guardVillagerHelpRange,
                illagersRunFromPolarBears,
                villagersRunFromPolarBears,
                guardArrowsHurtVillagers,

                shrineRewardsIncludeVanillaItems,
                shrineRewardsIncludeModdedItems,
                shrineGenerationInterval,

                pathsEnabled,
                pathsMaxDistance,
                pathsUseTerrainFollowing,
                pathsPreserveDurability,
                pathsDefaultPathBlockId,
                pathsAllowedStartingBlocks,
                pathsAllowedEndingBlocks,
                pathsMinWidth,
                pathsMaxWidth,

                enableChickenFeathers,
                chickenFeatherInterval,
                chickenFeatherChance,
                enableSkeletonBones,
                skeletonBoneInterval,
                skeletonBoneChance,
                enableSpiderDrops,
                spiderDropInterval,
                spiderDropChance,
                cobwebWeight,

                minDecayTicks,
                maxDecayTicks,
                baseDecayChance,
                lightDecayBonus,
                darknessDecayReduction,
                densityRadius,
                densityLimit,
                normalSpiderRate,
                caveSpiderRate,

                horseSwimmingEnabled,
                undeadHorseSwimmingEnabled,
                horsePreventWandering,
                horseStayRadius,
                horseRemoveMiningPenalty,
                horseIncreaseStepHeight,

                enableRecursiveOpening,
                recursiveOpeningMaxBlocksDistance,
                enableDoors,
                enableFenceGates,
                enableTrapdoors,
                enableModIncompatibilityCheck,

                fireEnabled,
                fireBaseColor,
                fireSoulBaseColor,
                firePriority,
                fireBiomeRules,
                fireBlockRules,
                fireBlockTagRules,

                dropMode
        );
    }

    public static SyncedServerConfig defaults() {
        return fromServerConfig(new ConfigServer());
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static long clamp(long value, long min, long max) {
        return Math.max(min, Math.min(max, value));
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private static ConfigServer.FireVisuals.FirePriority parseFirePriority(String value) {
        try {
            return ConfigServer.FireVisuals.FirePriority.valueOf(value);
        } catch (Exception ignored) {
            return ConfigServer.FireVisuals.FirePriority.BLOCK_TAG_BIOME;
        }
    }
}