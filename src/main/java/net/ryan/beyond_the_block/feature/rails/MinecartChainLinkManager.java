package net.ryan.beyond_the_block.feature.rails;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.content.entity.MinecartChainLinkEntity;
import net.ryan.beyond_the_block.content.registry.ModEntities;

import java.util.List;
import java.util.UUID;

public final class MinecartChainLinkManager {

    private MinecartChainLinkManager() {
    }

    public static void ensureLinkEntity(ServerWorld world, AbstractMinecartEntity first, AbstractMinecartEntity second) {
        String targetKey = pairKey(first.getUuid(), second.getUuid());

        List<MinecartChainLinkEntity> links = world.getEntitiesByClass(
                MinecartChainLinkEntity.class,
                first.getBoundingBox().expand(128.0D),
                entity -> pairKey(entity.getFirstCart(), entity.getSecondCart()).equals(targetKey)
        );

        if (!links.isEmpty()) {
            return;
        }

        MinecartChainLinkEntity link = new MinecartChainLinkEntity(ModEntities.MINECART_CHAIN_LINK, world);
        link.setFirstCart(first.getUuid());
        link.setSecondCart(second.getUuid());

        double x = (first.getX() + second.getX()) * 0.5D;
        double y = (first.getY() + second.getY()) * 0.5D;
        double z = (first.getZ() + second.getZ()) * 0.5D;
        link.setPosition(x, y, z);

        world.spawnEntity(link);
    }

    public static void removeLinkEntity(ServerWorld world, UUID first, UUID second) {
        String targetKey = pairKey(first, second);

        List<MinecartChainLinkEntity> links = world.getEntitiesByClass(
                MinecartChainLinkEntity.class,
                net.minecraft.util.math.Box.of(
                        new net.minecraft.util.math.Vec3d(0, 0, 0),
                        60000000.0D, 60000000.0D, 60000000.0D
                ),
                entity -> pairKey(entity.getFirstCart(), entity.getSecondCart()).equals(targetKey)
        );

        for (MinecartChainLinkEntity link : links) {
            link.discard();
        }
    }

    public static String pairKey(UUID first, UUID second) {
        if (first == null || second == null) {
            return "";
        }
        return first.compareTo(second) < 0
                ? first + "|" + second
                : second + "|" + first;
    }
}