package net.ryan.beyond_the_block.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.utils.accessors.MinecartCouplerAccess;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class MinecartTrainUtils {

    private MinecartTrainUtils() {
    }

    public static List<AbstractMinecartEntity> collectTrain(AbstractMinecartEntity start) {
        List<AbstractMinecartEntity> train = new ArrayList<>();

        if (!(start.getWorld() instanceof ServerWorld serverWorld)) {
            return train;
        }

        Set<UUID> visited = new HashSet<>();
        Deque<AbstractMinecartEntity> queue = new ArrayDeque<>();

        queue.add(start);
        visited.add(start.getUuid());

        while (!queue.isEmpty()) {
            AbstractMinecartEntity current = queue.poll();
            train.add(current);

            MinecartCouplerComponent couplers =
                    ((MinecartCouplerAccess) current).beyond_the_block$getCouplers();

            for (CouplerSide side : CouplerSide.values()) {
                UUID otherId = couplers.get(side);
                if (otherId == null || visited.contains(otherId)) {
                    continue;
                }

                Entity entity = serverWorld.getEntity(otherId);
                if (!(entity instanceof AbstractMinecartEntity otherCart)) {
                    continue;
                }

                visited.add(otherId);
                queue.add(otherCart);
            }
        }

        return train;
    }

    public static boolean areInSameTrain(AbstractMinecartEntity a, AbstractMinecartEntity b) {
        for (AbstractMinecartEntity cart : collectTrain(a)) {
            if (cart == b) {
                return true;
            }
        }
        return false;
    }

    public static void propagateMomentum(AbstractMinecartEntity source) {
        List<AbstractMinecartEntity> train = collectTrain(source);
        if (train.size() <= 1) {
            return;
        }

        Vec3d totalVelocity = Vec3d.ZERO;
        for (AbstractMinecartEntity cart : train) {
            totalVelocity = totalVelocity.add(cart.getVelocity());
        }

        Vec3d sharedVelocity = totalVelocity.multiply(1.0D / train.size());

        for (AbstractMinecartEntity cart : train) {
            cart.setVelocity(sharedVelocity);
            cart.velocityDirty = true;
            cart.velocityModified = true;
        }
    }

    public static AbstractMinecartEntity getLeader(AbstractMinecartEntity source) {
        List<AbstractMinecartEntity> train = collectTrain(source);
        if (train.isEmpty()) {
            return source;
        }

        AbstractMinecartEntity leader = train.get(0);
        for (AbstractMinecartEntity cart : train) {
            if (cart.getUuid().compareTo(leader.getUuid()) < 0) {
                leader = cart;
            }
        }

        return leader;
    }
}