package net.ryan.beyond_the_block.feature.xp_orbs;

import net.minecraft.entity.player.PlayerEntity;

public final class XPUtils {

    public static int getTotalXp(PlayerEntity player) {
        int level = player.experienceLevel;
        float progress = player.experienceProgress;
        int xpForLevel = getXpForLevel(level);
        return xpForLevel + Math.round(progress * player.getNextLevelExperience());
    }

    public static void addXp(PlayerEntity player, int amount) {
        player.addExperience(amount);
    }

    public static int getXpForLevel(int level) {
        if (level <= 16) return level * level + 6 * level;
        if (level <= 31) return (int)(2.5 * level * level - 40.5 * level + 360);
        return (int)(4.5 * level * level - 162.5 * level + 2220);
    }
}

