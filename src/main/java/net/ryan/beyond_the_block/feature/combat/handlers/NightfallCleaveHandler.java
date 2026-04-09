package net.ryan.beyond_the_block.feature.combat.handlers;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;

import java.util.List;

public final class NightfallCleaveHandler {
    private NightfallCleaveHandler() {}

    public static void onAttack(LivingEntity attacker, LivingEntity target) {
        ItemStack weapon = attacker.getMainHandStack();
        int level = EnchantmentHelper.getLevel(ModEnchantments.NIGHTFALL_CLEAVE, weapon);
        if (level <= 0) return;

        if (!(weapon.getItem() instanceof AxeItem)) return;
        if (!isDarkEnough(attacker)) return;

        Box area = target.getBoundingBox().expand(5.0);
        List<LivingEntity> nearby = attacker.getWorld().getEntitiesByClass(
                LivingEntity.class,
                area,
                entity -> entity != attacker && entity != target && entity.isAlive()
        );

        if (nearby.isEmpty()) return;

        for (LivingEntity entity : nearby) {
            entity.damage(DamageSource.magic(attacker, attacker), 2.0f);
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 300, 0));
        }

        attacker.getWorld().playSound(
                null,
                attacker.getBlockPos(),
                SoundEvents.ENTITY_GHAST_SCREAM,
                SoundCategory.PLAYERS,
                0.5f,
                1.0f
        );
    }

    private static boolean isDarkEnough(LivingEntity attacker) {
        return attacker.getWorld().getLightLevel(attacker.getBlockPos()) <= 4;
    }
}