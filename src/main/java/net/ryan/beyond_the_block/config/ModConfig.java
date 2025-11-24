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
import java.util.List;

@Config(name = "beyond_the_block")
public class ModConfig implements ConfigData {


    public OresConfig ores = new OresConfig();
    public GuardsConfig guards = new GuardsConfig();
    public ShrinesConfig shrines = new ShrinesConfig();
    public EnchantmentConfig enchantments = new EnchantmentConfig();
    public MiscConfig misc = new MiscConfig();
    public VillagerNamesConfig villagerNames = new VillagerNamesConfig();

    public AutoCameraConfig autoCamera = new AutoCameraConfig();

    public static class AutoCameraConfig{
        public boolean enableAutoCamera = true;
        public Mode cameraModeOnMount = Mode.PREVIOUS;

        public enum Mode{
            PREVIOUS,
            ALWAYS_FIRST,
            ALWAYS_THIRD
        }
    }

    public static class VillagerNamesConfig{
        public boolean enableNames = true;
        public boolean colouriseNames = true;
        public double nameVisibilityRange = 8.0;
        public boolean useAlliteration = true;

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
    public static class BanksConfig{
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int villageDetectionRange = 32;
        @ConfigEntry.BoundedDiscrete(min = 200, max = 240000)
        public long interestInterval = 24000;
        public float interestRate = 0.05F;
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
