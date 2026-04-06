package net.ryan.beyond_the_block.feature.interaction.handlers;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.utils.ReachHelper;

public final class ReachDistanceHandler {

    private ReachDistanceHandler() {
    }

    public static double getAdjustedBreakDistanceSquared(ServerPlayerEntity player, double vanillaSquaredDistance) {
        double bonus = ReachHelper.getReachBonusDouble(player.getMainHandStack());
        if (bonus <= 0.0) {
            return vanillaSquaredDistance;
        }

        double vanillaReach = Math.sqrt(vanillaSquaredDistance);
        double boostedReach = vanillaReach + bonus;
        return boostedReach * boostedReach;
    }

    public static double getVanillaBreakDistanceSquared() {
        return ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE;
    }
}