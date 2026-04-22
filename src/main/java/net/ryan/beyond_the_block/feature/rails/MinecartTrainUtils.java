package net.ryan.beyond_the_block.feature.rails;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.utils.accessors.MinecartCouplerAccess;
import net.ryan.beyond_the_block.utils.accessors.MinecartSpeedAccessor;

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

    public static double getSharedCustomSpeed(List<AbstractMinecartEntity> train) {
        double maxSpeed = 0.0D;

        for (AbstractMinecartEntity cart : train) {
            if (cart instanceof MinecartSpeedAccessor accessor) {
                maxSpeed = Math.max(maxSpeed, accessor.beyondTheBlock$getCustomSpeed());
            }
        }

        return maxSpeed;
    }

    public static void applySharedCustomSpeed(List<AbstractMinecartEntity> train, double speed) {
        for (AbstractMinecartEntity cart : train) {
            if (cart instanceof MinecartSpeedAccessor accessor) {
                if (speed > 0.0D) {
                    accessor.beyondTheBlock$setCustomSpeed(speed);
                } else {
                    accessor.beyondTheBlock$clearCustomSpeed();
                }
            }
        }
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