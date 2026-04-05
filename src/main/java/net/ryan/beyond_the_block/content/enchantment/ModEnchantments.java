package net.ryan.beyond_the_block.content.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.content.enchantment.armour.WardingGlyphEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.boots.GroundedResistanceEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.boots.LeapOfFaithEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.boots.SilentStepsEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.chestplates.DurabilityBoostEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.chestplates.EchoGuardEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.helmets.IronCladVisionEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.helmets.MindWardEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.helmets.RadiantAuraEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.helmets.ShadowsVeilEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.leggings.FrozenMomentumEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.leggings.GracefulMovementEnchantment;
import net.ryan.beyond_the_block.content.enchantment.armour.leggings.NightstrideEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.FishingCookingEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.axes.BarkskinEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.axes.NightfallCleaveEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.axes.TimbercutEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.hoes.DeepTillEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.hoes.GardensBountyEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.hoes.NightCultivationEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.pickaxes.ShadowMiningEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.pickaxes.SmeltingStrikeEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.pickaxes.StonebreakerEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.shovels.DarkDigEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.shovels.EarthShatterEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.shovels.FertilityEnchantment;
import net.ryan.beyond_the_block.content.enchantment.tools.swords.*;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.feature.combat.TemporalSliceHandler;

public class ModEnchantments {

    public static Enchantment FISHING_COOKING = register("fishing_cooking",
            new FishingCookingEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND));
    public static Enchantment FLAME_SWEEP = register("flame_sweep",
            new FlameSweepEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment WRATH_OF_THOR = register("wrath_of_thor",
            new WrathOfThorEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.BOW, EnchantmentTarget.CROSSBOW, EquipmentSlot.MAINHAND));
    public static Enchantment HOMING = register("homing",
            new ArrowHomingEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.BOW, EnchantmentTarget.CROSSBOW, EquipmentSlot.MAINHAND));
    //region Armour
    public static Enchantment WARDING_GLYPH = register("warding_glyph",
            new WardingGlyphEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND));
    //region Boots
    public static Enchantment LEAP_OF_FAITH = register("leap_of_faith",
            new LeapOfFaithEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET));
    public static Enchantment GROUNDED_RESISTANCE = register("grounded_resistance",
            new GroundedResistanceEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET));
    public static Enchantment SILENT_STEPS = register("silent_steps",
            new SilentStepsEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET));
    //endregion
    //region Chestplates
    public static Enchantment ECHO_GUARD = register("echo_guard",
            new EchoGuardEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static Enchantment DURABILITY_BOOST = register("durability_boost",
            new DurabilityBoostEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_CHEST, EquipmentSlot.CHEST));

    //endregion
    //region Helmets
    public static Enchantment IRON_CLAD_VISION = register("iron_clad_vision",
            new IronCladVisionEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD));
    public static Enchantment RADIANT_AURA = register("radiant_aura",
            new RadiantAuraEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD));
    public static Enchantment SHADOWS_VEIL = register("shadows_veil",
            new ShadowsVeilEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD));
    public static Enchantment MIND_WARD = register("mind_ward",
            new MindWardEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD));
    //endgregion
    //endregion
    //region Leggings
    public static Enchantment FROZEN_MOMENTUM = register("frozen_momentum",
            new FrozenMomentumEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_LEGS, EquipmentSlot.LEGS));
    public static Enchantment GRACEFUL_MOVEMENT = register("graceful_movement",
            new GracefulMovementEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_LEGS, EquipmentSlot.LEGS));
    public static Enchantment NIGHT_STRIDE = register("night_stride",
            new NightstrideEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.ARMOR_LEGS, EquipmentSlot.LEGS));
    //endregion
    //endregion
    //region Tools
    //region Axes
    public static Enchantment NIGHTFALL_CLEAVE = register("nightfall_cleave",
            new NightfallCleaveEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment TIMBER_CUT = register("timber_cut",
            new TimbercutEnchantment(Enchantment.Rarity.RARE,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment BARKSKIN = register("barkskin",
            new BarkskinEnchantment(Enchantment.Rarity.RARE,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    //endregion
    //region Hoes
    public static Enchantment DEEP_TILL = register("deep_till",
            new DeepTillEnchantment(Enchantment.Rarity.RARE,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment GARDENS_BOUNTY = register("gardens_bounty",
            new GardensBountyEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment NIGHT_CULTIVATION = register("night_cultivation",
            new NightCultivationEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    //endregion
    //region Pickaxes
    public static Enchantment SHADOW_MINING = register("shadow_mining",
            new ShadowMiningEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment STONE_BREAKER = register("stone_breaker",
            new StonebreakerEnchantment(Enchantment.Rarity.RARE,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment SMELTING_STRIKE = register("smelting_strike",
            new SmeltingStrikeEnchantment(Enchantment.Rarity.RARE,
                    EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND));
    //endregion
    //region Shovels
    public static Enchantment DARK_DIG = register("dark_dig",
            new DarkDigEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment EARTH_SHATTER = register("earth_shatter",
            new EarthShatterEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment FERTILITY = register("fertility",
            new FertilityEnchantment(Enchantment.Rarity.RARE,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    //endregion
    //region Swords
    public static Enchantment LIFE_STEAL = register("life_steal",
            new LifeStealEnchantment(Enchantment.Rarity.RARE,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment PHANTOM_SLASH = register("phantom_slash",
            new PhantomSlashEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment RESILIENT_STRIKE = register("resilient_strike",
            new ResilientStrikeEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    public static Enchantment TEMPORAL_SLICE = register("temporal_slice",
            new TemporalSliceEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));
    //endregion
    //endregion

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(Registry.ENCHANTMENT, new Identifier(BeyondTheBlock.MOD_ID, name), enchantment);
    }

    public static void registerModEnchantments() {
        TemporalSliceHandler.register();
        BeyondTheBlock.LOGGER.info("Registering Enchantments for " + BeyondTheBlock.MOD_ID);
    }
}
