package net.ryan.beyond_the_block.feature.horses;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public final class MountUtils {

    public static void safelyKickOffMount(Entity entity) {
        if (entity.getVehicle() != null) {
            entity.stopRiding();
            entity.setVelocity(Vec3d.ZERO);
            return;
        }

        for (Entity passenger : List.copyOf(entity.getPassengerList())) {
            if (passenger instanceof PlayerEntity) continue;
            passenger.stopRiding();
            passenger.setVelocity(Vec3d.ZERO);
        }
    }
}

