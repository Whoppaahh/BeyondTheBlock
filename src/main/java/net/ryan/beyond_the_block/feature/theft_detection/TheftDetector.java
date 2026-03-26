package net.ryan.beyond_the_block.feature.theft_detection;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageGossipType;
import net.minecraft.world.RaycastContext;
import net.ryan.beyond_the_block.content.village.GuardVillager.GuardEntity;


public class TheftDetector {

    public static void onItemTaken(ServerWorld world, BlockPos chestPos, PlayerEntity player) {
        if (!isTaggedVillageChest(world, chestPos)) return;

        for (VillagerEntity villager : world.getEntitiesByClass(VillagerEntity.class, new Box(chestPos).expand(10), v -> true)) {
            if (canSeeTheft(world, villager, player)) {
                villager.getGossip().startGossip(player.getUuid(), VillageGossipType.MINOR_NEGATIVE, 10);
                return; // Only punish once
            }
        }
        for (GuardEntity guard : world.getEntitiesByClass(GuardEntity.class, new Box(chestPos).expand(10), v -> true)) {
            if (canSeeTheft(world, guard, player)) {
                guard.getGossips().startGossip(player.getUuid(), VillageGossipType.MINOR_NEGATIVE, 10);
                return;
            }
        }
    }

    private static boolean isTaggedVillageChest(ServerWorld world, BlockPos pos) {
        return VillageContainerTagger.get(world).isVillageContainer(pos);
    }

    private static boolean canSeeTheft(ServerWorld world, VillagerEntity villager, PlayerEntity player) {
        Vec3d villagerEyes = villager.getEyePos();
        Vec3d playerEyes = player.getEyePos();
        Vec3d lookVec = villager.getRotationVec(1.0F);

        // Check facing
        Vec3d toPlayer = playerEyes.subtract(villagerEyes).normalize();
        if (lookVec.dotProduct(toPlayer) < 0.85) return false; // Not looking toward player

        // Check line of sight
        HitResult result = world.raycast(new RaycastContext(
                villagerEyes, playerEyes,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                villager
        ));

        return result.getType() == HitResult.Type.MISS;
    }

    private static boolean canSeeTheft(ServerWorld world, GuardEntity guard, PlayerEntity player) {
        Vec3d villagerEyes = guard.getEyePos();
        Vec3d playerEyes = player.getEyePos();
        Vec3d lookVec = guard.getRotationVec(1.0F);

        // Check facing
        Vec3d toPlayer = playerEyes.subtract(villagerEyes).normalize();
        if (lookVec.dotProduct(toPlayer) < 0.85) return false; // Not looking toward player

        // Check line of sight
        HitResult result = world.raycast(new RaycastContext(
                villagerEyes, playerEyes,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                guard
        ));

        return result.getType() == HitResult.Type.MISS;
    }
}
