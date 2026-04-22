package net.ryan.beyond_the_block.feature.rails;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MinecartLinkingState {

    private static final Map<UUID, AbstractMinecartEntity> STATE = new HashMap<>();

    private MinecartLinkingState() {
    }

    public static void set(PlayerEntity player, AbstractMinecartEntity cart) {
        STATE.put(player.getUuid(), cart);
    }

    public static AbstractMinecartEntity get(PlayerEntity player) {
        return STATE.get(player.getUuid());
    }

    public static void clear(PlayerEntity player) {
        STATE.remove(player.getUuid());
    }
}