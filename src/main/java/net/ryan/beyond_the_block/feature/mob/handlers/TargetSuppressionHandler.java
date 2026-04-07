package net.ryan.beyond_the_block.feature.mob.handlers;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.content.registry.ModEffects;
import org.jetbrains.annotations.Nullable;

public final class TargetSuppressionHandler {

    private TargetSuppressionHandler() {
    }

    public static boolean shouldBlockTargetAssignment(MobEntity mob, @Nullable LivingEntity target) {
        if (!(target instanceof PlayerEntity player)) {
            return false;
        }

        return player.hasStatusEffect(ModEffects.ETHEREAL_VEIL)
                || mob.hasStatusEffect(StatusEffects.BLINDNESS);
    }

    public static boolean canTargetType(MobEntity mob, EntityType<?> type) {
        if (type != EntityType.PLAYER) {
            return true;
        }

        LivingEntity target = mob.getTarget();
        if (!(target instanceof PlayerEntity player)) {
            return true;
        }

        return !(player.hasStatusEffect(ModEffects.ETHEREAL_VEIL)
                || mob.hasStatusEffect(StatusEffects.BLINDNESS));
    }
}