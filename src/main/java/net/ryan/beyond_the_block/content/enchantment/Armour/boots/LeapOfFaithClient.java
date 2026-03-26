package net.ryan.beyond_the_block.content.enchantment.Armour.boots;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LeapOfFaithClient {
    private static final Set<UUID> usedJumpKey = new HashSet<>();

    public static boolean canAttemptDoubleJump(PlayerEntity player) {
        return !usedJumpKey.contains(player.getUuid());
    }

    public static void markDoubleJumpUsed(PlayerEntity player) {
        usedJumpKey.add(player.getUuid());
    }

    public static void resetJumpKey(PlayerEntity player) {
        usedJumpKey.remove(player.getUuid());
    }
}

