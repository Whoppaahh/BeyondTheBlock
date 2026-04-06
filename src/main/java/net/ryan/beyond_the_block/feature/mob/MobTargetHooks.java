package net.ryan.beyond_the_block.feature.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.ryan.beyond_the_block.feature.mob.handlers.GuardAssistHandler;
import net.ryan.beyond_the_block.feature.mob.handlers.TargetSuppressionHandler;
import org.jetbrains.annotations.Nullable;

public final class MobTargetHooks {

    private MobTargetHooks() {
    }

    public static boolean shouldBlockTargetAssignment(MobEntity mob, @Nullable LivingEntity target) {
        return TargetSuppressionHandler.shouldBlockTargetAssignment(mob, target);
    }

    public static boolean canTargetType(MobEntity mob, EntityType<?> type) {
        return TargetSuppressionHandler.canTargetType(mob, type);
    }

    public static void afterTargetAssigned(MobEntity mob, @Nullable LivingEntity target) {
        GuardAssistHandler.afterTargetAssigned(mob, target);
    }
}