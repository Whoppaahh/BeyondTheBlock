package net.ryan.beyond_the_block.utils.battle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public class BattleUtil {
    public static LivingEntity getLivingEntity(MinecraftServer server, UUID uuid) {
        for (ServerWorld world : server.getWorlds()) {
            Entity entity = world.getEntity(uuid);

            if (entity instanceof LivingEntity livingEntity) {
                return livingEntity;
            }
        }

        return null;
    }

    public static boolean isOwnedBy(LivingEntity entity, UUID playerUuid) {
        if (entity instanceof TameableEntity tameable) {
            return playerUuid.equals(tameable.getOwnerUuid());
        }

        return false;
    }

    public static boolean isValidBattle(LivingEntity entity) {
        return entity instanceof TameableEntity;
    }
}