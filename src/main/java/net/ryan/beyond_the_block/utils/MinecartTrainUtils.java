package net.ryan.beyond_the_block.utils;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public final class MinecartTrainUtils {

    private MinecartTrainUtils() {}

    /**
     * Finds the full connected train starting from a cart.
     */
    public static List<AbstractMinecartEntity> collectTrain(
            AbstractMinecartEntity start
    ) {
        World world = start.getWorld();
        Set<UUID> visited = new HashSet<>();
        Deque<AbstractMinecartEntity> queue = new ArrayDeque<>();
        List<AbstractMinecartEntity> train = new ArrayList<>();

        queue.add(start);
        visited.add(start.getUuid());

        while (!queue.isEmpty()) {
            AbstractMinecartEntity current = queue.poll();
            train.add(current);

            LinkedMinecartComponent link =
                    ((MinecartLinkAccess) current).beyond_the_block$getLink();

            if (!link.hasLink()) continue;

            UUID otherId = link.getLinkedCart();
            if (visited.contains(otherId)) continue;

            AbstractMinecartEntity other =
                    (AbstractMinecartEntity) world.getEntity(otherId);

            if (other == null) continue;

            visited.add(otherId);
            queue.add(other);
        }

        return train;
    }

    /**
     * Applies shared momentum across the entire train.
     */
    public static void propagateMomentum(
            AbstractMinecartEntity source
    ) {
        List<AbstractMinecartEntity> train = collectTrain(source);
        if (train.size() <= 1) return;

        Vec3d totalVelocity = Vec3d.ZERO;

        for (AbstractMinecartEntity cart : train) {
            totalVelocity = totalVelocity.add(cart.getVelocity());
        }

        Vec3d sharedVelocity = totalVelocity.multiply(1.0 / train.size());

        for (AbstractMinecartEntity cart : train) {
            cart.setVelocity(sharedVelocity);
            cart.velocityDirty = true;
        }
    }
}