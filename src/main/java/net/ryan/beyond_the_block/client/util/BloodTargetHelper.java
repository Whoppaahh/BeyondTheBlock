package net.ryan.beyond_the_block.client.util;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.LivingEntity;
import net.ryan.beyond_the_block.config.schema.ConfigClient;
import net.ryan.beyond_the_block.utils.ModTags;

public final class BloodTargetHelper {

    private BloodTargetHelper() {
    }

    public static boolean shouldShowBlood(LivingEntity entity) {
        ConfigClient config = AutoConfig.getConfigHolder(ConfigClient.class).getConfig();

        if (!config.visuals.blood.enabled) {
            return false;
        }

        if (!passesTargetMode(entity, config.visuals.blood.targetMode)) {
            return false;
        }

        return passesHealthThreshold(entity, config.visuals.blood.healthPercentThreshold);
    }

    public static boolean passesTargetMode(LivingEntity entity, ConfigClient.BloodTargetMode mode) {
        return switch (mode) {
            case NONE -> false;
            case HUMANOID_ONLY -> isHumanoid(entity);
            case HUMANOID_AND_PASSIVE -> isHumanoid(entity) || isPassive(entity);
            case ALL -> true;
        };
    }

    public static boolean passesHealthThreshold(LivingEntity entity, int healthPercentThreshold) {
        if (entity.isDead() || entity.getMaxHealth() <= 0.0F) {
            return false;
        }

        float currentFraction = entity.getHealth() / entity.getMaxHealth();
        float thresholdFraction = healthPercentThreshold / 100.0F;
        return currentFraction <= thresholdFraction;
    }

    public static boolean isHumanoid(LivingEntity entity) {
        return entity.getType().isIn(ModTags.Entities.BLEEDS_HUMANOID);
    }

    public static boolean isPassive(LivingEntity entity) {
        return entity.getType().isIn(ModTags.Entities.BLEEDS_PASSIVE);
    }
}