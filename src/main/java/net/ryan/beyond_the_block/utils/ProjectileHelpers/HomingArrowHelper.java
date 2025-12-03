package net.ryan.beyond_the_block.utils.ProjectileHelpers;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomingArrowHelper {
    private static final Map<UUID, UUID> pendingHomingTargets = new HashMap<>();

    public static void setTarget(PlayerEntity player, UUID target) {
        pendingHomingTargets.put(player.getUuid(), target);
    }

    public static UUID consumeTarget(PlayerEntity player) {
        return pendingHomingTargets.remove(player.getUuid());
    }
}

