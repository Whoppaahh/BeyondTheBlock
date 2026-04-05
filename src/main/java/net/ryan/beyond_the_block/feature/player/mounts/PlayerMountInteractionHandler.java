package net.ryan.beyond_the_block.feature.player.mounts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.ryan.beyond_the_block.feature.horses.MountUtils;

public final class PlayerMountInteractionHandler {
    private PlayerMountInteractionHandler() {}

    public static ActionResult trySneakKick(PlayerEntity player, Entity entity) {
        if (player.getWorld().isClient) return null;
        if (!player.isSneaking()) return null;
        if (entity instanceof PlayerEntity) return null;

        if (!entity.hasVehicle() && entity.getPassengerList().isEmpty()) {
            return null;
        }

        MountUtils.safelyKickOffMount(entity);
        return ActionResult.SUCCESS;
    }
}