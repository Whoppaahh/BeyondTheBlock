package net.ryan.beyond_the_block.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.utils.GUI.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config(name = "beyond_the_block")
public class ModConfig implements ConfigData {


    public OresConfig ores = new OresConfig();
    public GuardsConfig guards = new GuardsConfig();
    public ShrinesConfig shrines = new ShrinesConfig();
    public EnchantmentConfig enchantments = new EnchantmentConfig();
    public MiscConfig misc = new MiscConfig();


    public NamesConfig mobNames = new NamesConfig();
    public AutoCameraConfig autoCamera = new AutoCameraConfig();
    public TrajectoryConfig trajectoryConfig = new TrajectoryConfig();
    public PathConfig pathConfig = new PathConfig();
    public PassiveMobDropConfig passiveDropsConfig = new PassiveMobDropConfig();
    public WebConfig webConfig = new WebConfig();
    public HorseConfig horseConfig = new HorseConfig();
    public DoubeOpenablesConfig doubleOpenables = new DoubeOpenablesConfig();

    public static class DoubeOpenablesConfig {
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

    public static class HorseConfig{
        public boolean enableSwimming = true;
        public boolean undeadCanSwim = false;

        public boolean preventWandering = true;
        public double stayRadius = 10.0;

        public boolean removeMiningPenalty = true;
        public boolean increaseStepHeight = true;

        public float minAlpha = 0.25F;
        public float fadePitch = 30.0F;

        public boolean headPitchOffset = true;
        public float headOffsetDegrees = 30.0F;

    }

    public static class WebConfig{
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

    public static class PassiveMobDropConfig {
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

    public static class PathConfig{
        public boolean enabled = true;
        public int maxDistance = 64;
        public boolean useTerrainFollowing = true;
        public boolean preserveDurability = false;
        public boolean previewMode = true;
        public String defaultPathBlockId = "minecraft:dirt_path";
        public List<String> allowedStartingBlocks = Arrays.asList(
                "minecraft:grass_block",
                "minecraft:stone",
                "minecraft:dirt"
        );
        public List<String> allowedEndingBlocks = Arrays.asList(
                "minecraft:grass_block",
                "minecraft:stone"
        );
        @ConfigEntry.BoundedDiscrete(min = 1, max = 9)
        public int minWidth = 1;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 9)
        public int maxWidth = 7;
        public boolean showWidthHud = true;
    }

    public static class TrajectoryConfig{
        public boolean enabled = true;

        public boolean showBow = true;
        public boolean showCrossbow = true;
        public boolean showTrident = false;      // future use
        public boolean showThrowables = false;   // snowballs, pearls, potions

        // When to show
        public boolean onlyWhileAiming = true;   // right-click / drawing
        public boolean requireSneak = false;     // only show while sneaking

        // Quality
        public int maxSteps = 64;

        // Visuals
        public boolean gradient = true;
        public boolean thickLine = true;
        public int thicknessLines = 3;           // number of parallel lines
        public float thicknessOffset = 0.03f;    // world offset between lines
        public boolean showImpactMarker = true;
        public int colorNone = 0x19FFE0;   // cyan-ish for no collision in range
        public int colorBlock = 0xFF5555;  // red for block hits
        public int colorEntity = 0xFFF16A; // yellow for entity hits
    }

    public static class AutoCameraConfig{
        public boolean enableAutoCamera = true;
        public Mode cameraModeOnMount = Mode.PREVIOUS;

        public enum Mode{
            PREVIOUS,
            ALWAYS_FIRST,
            ALWAYS_THIRD
        }
    }

    public static class NamesConfig {
        public boolean enableNames = true;
        public boolean nameOnlyWhenEmployed = true;
        public boolean colouriseNames = true;
        public double nameVisibilityRange = 8.0;
        public boolean useAlliteration = true;
        public boolean nameIronGolems = true;
        public boolean nameTamedMobs = true;

        public GenderMode genderMode = GenderMode.BOTH;

        public enum GenderMode{
            MALE, FEMALE, BOTH
        }
    }

    public static class OresConfig{
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
    public static class MiscConfig{
        public boolean titleLogo = true;
        public boolean showBlood = true;
        public float healthFraction = 0.25F;
    }
    public static class EnchantmentConfig{
        public DropMode dropMode = DropMode.NORMAL;
        public boolean showHighlights = true;
    }
    public static class ShrinesConfig {
        //Shrines
        public boolean rewardsIncludeVanillaItems = false;
        public boolean rewardsIncludeModdedItems = true;
        @ConfigEntry.BoundedDiscrete(min = 200, max = 240000)
        public long generationInterval = 24000;
    }

    public static class GuardsConfig{
        public GuardVisuals visuals = new GuardVisuals();
        public static class GuardVisuals {
            //Guards Visuals
            public boolean guardBerets = false;
            public boolean guardVariants = false;
            public boolean displayShoulderPads = false;
        }

        public GuardBehaviour behavior = new GuardBehaviour();
        public static class GuardBehaviour {
            //Guards Behaviour and Logic
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
    }


    public void validateConfig(){
        if (!shrines.rewardsIncludeVanillaItems && !shrines.rewardsIncludeModdedItems) {
            // Log a warning and force vanilla to true as fallback
            BeyondTheBlock.LOGGER.warn("Invalid config: Both vanilla and modded loot disabled. Defaulting to vanilla loot.");
            shrines.rewardsIncludeVanillaItems = true;

            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client != null) {
                    client.execute(() -> ToastUtils.showToast("Invalid Loot Config", "One must be enabled, defaulting to Vanilla Loot"));
                }
            }
        }
    }
}
