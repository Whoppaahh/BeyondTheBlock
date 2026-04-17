package net.ryan.beyond_the_block.utils;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public final class MinecartLinkingState {

    private static final Map<UUID, Object> STATE = new WeakHashMap<>();

    public static void set(PlayerEntity player, Object cart) {
        STATE.put(player.getUuid(), cart);
    }

    public static <T> T get(PlayerEntity player) {
        return (T) STATE.get(player.getUuid());
    }

    public static void clear(PlayerEntity player) {
        STATE.remove(player.getUuid());
    }
}