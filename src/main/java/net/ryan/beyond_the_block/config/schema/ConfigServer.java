package net.ryan.beyond_the_block.config.schema;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.ryan.beyond_the_block.config.DropMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config(name = "beyond_the_block-server")
public class ConfigServer implements ConfigData {

    public Worldgen worldgen = new Worldgen();
    public Features features = new Features();

    /* ================= WORLDGEN ================= */

    public static class Worldgen {
        public Ores ores = new Ores();
    }

    public static class Ores {
        public int rubyOre = 6;
        public int miraniteOre = 2;
        public int chromiteOre = 10;
        public int nocturniteOre = 4;
        public int amberineOre = 7;
        public int rosetteOre = 5;
        public int azurosOre = 6;
        public int indigraOre = 3;
        public int xirionOre = 9;
        public int xpOre = 4;
    }

    /* ================= FEATURES ================= */

    public static class Features {
        public Guards guards = new Guards();
        public Shrines shrines = new Shrines();
        public Paths paths = new Paths();
        public PassiveDrops drops = new PassiveDrops();
        public WebDecay webs = new WebDecay();
        public Horses horses = new Horses();
        public DoubleOpenables openables = new DoubleOpenables();
        public Enchantments enchantments = new Enchantments();
        public FireVisuals fire = new FireVisuals();
    }

    public static class FireVisuals {
        public boolean enabled = true;

        // Shared defaults
        public int baseFireColor = 0xFFFFA0;
        public int baseSoulFireColor = 0x78C8FF;

        public FirePriority priority = FirePriority.BLOCK_TAG_BIOME;

        /**
         * Format examples:
         * minecraft:desert=0xFFCC66
         * minecraft:soul_sand=0x66B3FF
         * #minecraft:soul_fire_base_blocks=0x66D0FF
         */
        public List<String> biomeRules = new ArrayList<>(List.of(
                "minecraft:desert=0xFFCC66",
                "minecraft:badlands=0xFF9966",
                "minecraft:soul_sand_valley=0x66CFFF"
        ));

        public List<String> blockRules = new ArrayList<>(List.of(
                "minecraft:soul_sand=0x66BFFF",
                "minecraft:soul_soil=0x7AD6FF",
                "minecraft:netherrack=0xFF9A55"
        ));

        public List<String> blockTagRules = new ArrayList<>(List.of(
                "#minecraft:soul_fire_base_blocks=0x66CFFF"
        ));

        public enum FirePriority {
            BLOCK_TAG_BIOME,
            BLOCK_BIOME_TAG,
            BIOME_BLOCK_TAG
        }
    }

    public static class Shrines {
        public boolean rewardsIncludeVanillaItems = false;
        public boolean rewardsIncludeModdedItems = true;
        @ConfigEntry.BoundedDiscrete(min = 200, max = 240000)
        public long generationInterval = 24000;
    }
    public static class Paths {
        public boolean enabled = true;
        public int maxDistance = 64;
        public boolean useTerrainFollowing = true;
        public boolean preserveDurability = false;
        public String defaultPathBlockId = "minecraft:dirt_path";
        public List<String> allowedStartingBlocks = Arrays.asList(
                "minecraft:grass_block",
                "minecraft:stone",
                "minecraft:dirt",
                "#minecraft:replaceable_plants"
        );
        public List<String> allowedEndingBlocks = Arrays.asList(
                "minecraft:grass_block",
                "minecraft:stone",
                "minecraft:dirt",
                "#minecraft:replaceable_plants"
        );
        @ConfigEntry.BoundedDiscrete(min = 1, max = 9)
        public int minWidth = 1;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 9)
        public int maxWidth = 5;
    }
    public static class PassiveDrops {
        public boolean enableChickenFeathers = true;
        public int chickenFeatherInterval = 600;   // every 30 seconds
        public float chickenFeatherChance = 0.10f; // 10%

        public boolean enableSkeletonBones = true;
        public int skeletonBoneInterval = 800;
        public float skeletonBoneChance = 0.06f;

        public boolean enableSpiderDrops = true;
        public int spiderDropInterval = 700;
        public float spiderDropChance = 0.08f;
        public float cobwebWeight = 0.20f;         // 20% cobweb, 80% string
    }
    public static class WebDecay {
        public int minDecayTicks = 400;     // 20 seconds
        public int maxDecayTicks = 2400;    // 2 minutes

        public float baseDecayChance = 0.75f;
        public float lightDecayBonus = 0.20f;
        public float darknessDecayReduction = 0.15f;

        public int densityRadius = 4;
        public int densityLimit = 6;

        public float normalSpiderRate = 0.10f;
        public float caveSpiderRate = 0.20f;
    }
    public static class DoubleOpenables{
        public boolean enableRecursiveOpening = true;

        @ConfigEntry.BoundedDiscrete(min = 1, max = 32)
        public int recursiveOpeningMaxBlocksDistance = 10;

        /* ================= Block types ================= */

        public boolean enableDoors = true;
        public boolean enableFenceGates = true;
        public boolean enableTrapdoors = true;

        /* ================= Compatibility ================= */

        public boolean enableModIncompatibilityCheck = true;
    }
    /* ---------- Guards ---------- */

    public static class Guards {

        public boolean enabled = true;

        public int reputationRequirementToBeAttacked = -100;
        public int reputationRequirement = 15;
        public boolean guardsRunFromPolarBears = false;
        public boolean guardsOpenDoors = true;
        public boolean guardFormation = true;
        public boolean clericHealing = true;
        public boolean armourerRepairGuardArmour = true;
        public boolean attackAllMobs = false;
        public boolean guardAlwaysShield = false;
        public boolean friendlyFire = true;

        public List<String> mobBlackList = new ArrayList<>();
        public float amountOfHealthRegenerated = 1F;
        public boolean followHero = true;
        public double healthModifier = 20D;
        public double speedModifier = 0.5D;
        public double followRangeModifier = 20D;
        public boolean giveGuardStuffHotv = false;
        public boolean setGuardPatrolHotv = false;
        public float chanceToDropEquipment = 25F;
        public boolean raidAnimals = false;
        public boolean witchesVillager = true;
        public boolean blackSmithHealing = true;
        public boolean convertVillagerIfHaveHotv = false;
        public double guardVillagerHelpRange = 50;
        public boolean illagersRunFromPolarBears = true;
        public boolean villagersRunFromPolarBears = true;
        public boolean guardArrowsHurtVillagers = true;
    }

    /* ---------- Horses (RESTORED) ---------- */

    public static class Horses {
        public boolean enableSwimming = true;
        public boolean undeadCanSwim = false;
        public boolean preventWandering = true;
        public double stayRadius = 10.0;
        public boolean removeMiningPenalty = true;
        public boolean increaseStepHeight = true;
    }

    /* ---------- Enchantments (FIXED) ---------- */

    public static class Enchantments {
        public DropMode dropMode = DropMode.NORMAL;
    }
}