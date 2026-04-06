package net.ryan.beyond_the_block.feature.mob.handlers;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.entity.villager.guard.GuardEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class GuardAssistHandler {

    private GuardAssistHandler() {
    }

    public static void afterTargetAssigned(MobEntity attacker, @Nullable LivingEntity target) {
        if (target == null || attacker instanceof GuardEntity) {
            return;
        }

        boolean isVillagerOrGuard = target.getType() == EntityType.VILLAGER || target instanceof GuardEntity;
        if (isVillagerOrGuard) {
            callNearbyGuardsAndGolems(attacker);
        }

        if (attacker instanceof IronGolemEntity golem && target instanceof GuardEntity) {
            golem.setTarget(null);
        }
    }

    private static void callNearbyGuardsAndGolems(MobEntity attacker) {
        double range = Configs.server().features.guards.guardVillagerHelpRange;

        List<MobEntity> nearby = attacker.getWorld().getNonSpectatingEntities(
                MobEntity.class,
                attacker.getBoundingBox().expand(range, 5.0D, range)
        );

        for (MobEntity helper : nearby) {
            if (helper == attacker) {
                continue;
            }

            if (!(helper instanceof GuardEntity || helper instanceof IronGolemEntity)) {
                continue;
            }

            if (helper.getTarget() != null) {
                continue;
            }

            helper.setTarget(attacker);
        }
    }
}