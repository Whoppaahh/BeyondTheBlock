package net.ryan.beyond_the_block.client.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static net.ryan.beyond_the_block.network.packets.PacketIDs.LEAP_OF_FAITH_PACKET_ID;

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


    public static void handleJumpPress(MinecraftClient client) {
        if (client.player == null || client.isPaused()) return;

        PlayerEntity player = client.player;

        boolean pressingJump = client.options.jumpKey.isPressed();
        boolean canDoubleJump = !player.isOnGround() && !player.getAbilities().flying;

        if (pressingJump && canDoubleJump && canAttemptDoubleJump(player)) {
            // Prevent holding space from constantly retriggering
            markDoubleJumpUsed(player);

            // Send a packet to server
            ClientPlayNetworking.send(LEAP_OF_FAITH_PACKET_ID, PacketByteBufs.create());
        } else if (!pressingJump) {
            resetJumpKey(player);
        }
    }
}

