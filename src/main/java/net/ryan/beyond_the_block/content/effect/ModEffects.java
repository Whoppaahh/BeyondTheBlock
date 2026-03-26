package net.ryan.beyond_the_block.content.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.content.effect.Beneficial.*;
import net.ryan.beyond_the_block.content.effect.Harmful.*;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.awt.*;
import java.util.function.Supplier;

public class ModEffects {

    public static StatusEffect PHASE;
    public static StatusEffect ZOMBIFICATION;
    public static StatusEffect CUPID;

    public static StatusEffect FREEZE;
    public static StatusEffect ECHO;
    public static StatusEffect ELEMENTAL_CHARGE;
    public static StatusEffect MAGNETIC_PULL;
    public static StatusEffect SPLIT_SOUL;
    public static StatusEffect TIME_DILATION;

    public static StatusEffect AURA_OF_THORNS;
    public static StatusEffect CLARITY;
    public static StatusEffect ETHEREAL_VEIL;
    public static StatusEffect SOUL_LINK;
    public static StatusEffect BLEED;
    public static StatusEffect CRYSTALIZE;
    public static StatusEffect HEAVY_STEP;
    public static StatusEffect MIND_FOG;

    public static StatusEffect registerStatusEffect(String name, Supplier<StatusEffect> effectSupplier) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(BeyondTheBlock.MOD_ID, name), effectSupplier.get());
    }

    public static void registerEffects() {

        PHASE = registerStatusEffect("phase", () -> new PhasedStatusEffect(StatusEffectCategory.NEUTRAL, 0x2B0033));
        ZOMBIFICATION = registerStatusEffect("zombification", () -> new ZombificationEffect(StatusEffectCategory.HARMFUL, Color.GREEN.getRGB()));
        Potion zombifcation_potion = new Potion(new StatusEffectInstance(ZOMBIFICATION, 20 * 10));
        Registry.register(Registry.POTION, new Identifier(BeyondTheBlock.MOD_ID, "zombification_potion"), zombifcation_potion);
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Items.ROTTEN_FLESH, zombifcation_potion);
        CUPID = registerStatusEffect("cupid", () -> new CupidStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFF66AA));

        FREEZE = registerStatusEffect("freeze", () -> new FreezeEffect(StatusEffectCategory.HARMFUL, 3124687));
        ECHO = registerStatusEffect("echo", () -> new EchoEffect(StatusEffectCategory.BENEFICIAL, 4625247));
        ELEMENTAL_CHARGE = registerStatusEffect("elemental_charge", () -> new ElementalChargeEffect(StatusEffectCategory.HARMFUL, 16753920));
        MAGNETIC_PULL = registerStatusEffect("magnetic_pull", () -> new MagneticPullEffect(StatusEffectCategory.NEUTRAL, 12255232));
        SPLIT_SOUL = registerStatusEffect("split_soul", () -> new SplitSoulEffect(StatusEffectCategory.BENEFICIAL, 52428));
        TIME_DILATION = registerStatusEffect("time_dilation", () -> new TimeDilationEffect(StatusEffectCategory.HARMFUL, 16711680));
        AURA_OF_THORNS = registerStatusEffect("aura_of_thorns", () -> new AuraOfThornsEffect(StatusEffectCategory.BENEFICIAL, 0x8B8B00));
        CLARITY = registerStatusEffect("clarity", () -> new ClarityEffect(StatusEffectCategory.BENEFICIAL, 0xADD8E6));
        ETHEREAL_VEIL = registerStatusEffect("ethereal_veil", () -> new EtherealVeilEffect(StatusEffectCategory.BENEFICIAL, 0xE6E6FA));
        SOUL_LINK = registerStatusEffect("soul_link", () -> new SoulLinkEffect(StatusEffectCategory.BENEFICIAL, 0xFFD700));
        BLEED = registerStatusEffect("bleed", () -> new BleedEffect(StatusEffectCategory.HARMFUL, 0x8B0000));
        CRYSTALIZE = registerStatusEffect("crystalize", () -> new CrystalizedEffect(StatusEffectCategory.HARMFUL, 0xA9A9A9));
        HEAVY_STEP = registerStatusEffect("heavy_step", () -> new HeavyStepEffect(StatusEffectCategory.HARMFUL, 0x8B4513));
        MIND_FOG = registerStatusEffect("mind_fog", () -> new MindFogEffect(StatusEffectCategory.HARMFUL, 0x808080));
    }
}
