package net.ryan.beyond_the_block.feature.theft;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageGossipType;
import net.minecraft.world.LightType;
import net.minecraft.world.RaycastContext;
import net.ryan.beyond_the_block.advancements.ModCriteria;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.entity.villager.guard.GuardEntity;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

import java.util.List;

public class TheftDetector {

    public static void onItemTaken(ServerWorld world, BlockPos chestPos, PlayerEntity player) {
        if (!Configs.server().features.theft.enabled) return;
        if (!isTaggedVillageChest(world, chestPos)) return;

        int range = Configs.server().features.theft.witnessRange;
        int gossipAmount = Configs.server().features.theft.gossipAmount;

        Box searchBox = new Box(chestPos).expand(range);
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, searchBox,
                e -> e instanceof VillagerEntity || e instanceof GuardEntity);

        boolean caught = false;
        BeyondTheBlock.LOGGER.info("Theft occurring at {}! Checking for {} witnesses in range {}.", chestPos, entities.size(), range);
        for (LivingEntity entity : entities) {
            double effectiveRange = range;
            if (Configs.server().features.theft.useLightDetection) {
                int light = world.getLightLevel(LightType.BLOCK, player.getBlockPos());
                // In darkness, range is reduced (minimum 2 blocks)
                effectiveRange = Math.max(2.0, range * (light / 15.0));
            }

            if (Configs.server().features.theft.useStealthDetection && player.isSneaking()) {
                effectiveRange *= 0.5;
            }

            if (entity.squaredDistanceTo(player) > effectiveRange * effectiveRange) {
                continue;
            }

            if (canSeeTheft(world, entity, player)) {
                BeyondTheBlock.LOGGER.info("Witness {} CAUGHT player stealing!", entity);
                
                // Visual feedback for being caught
                world.spawnParticles(net.minecraft.particle.ParticleTypes.ANGRY_VILLAGER,
                        entity.getX(), entity.getEyeY(), entity.getZ(),
                        5, 0.3, 0.3, 0.3, 0.1);

                player.sendMessage(net.minecraft.text.Text.literal("§cYou were caught stealing by a " + (entity instanceof VillagerEntity ? "Villager" : "Guard") + "!"), true);

                if (entity instanceof VillagerEntity villager) {
                    villager.getGossip().startGossip(player.getUuid(), VillageGossipType.MINOR_NEGATIVE, gossipAmount);
                } else if (entity instanceof GuardEntity guard) {
                    guard.getGossips().startGossip(player.getUuid(), VillageGossipType.MINOR_NEGATIVE, gossipAmount);
                    guard.setTarget(player); // Guards immediately target the thief
                }
                caught = true;
                
                // Alert nearby guards even if they didn't see it directly (if configured/logic permits)
                alertNearbyGuards(world, player, entities);
                break; // Only punish once
            }
        }

        if (player instanceof ServerPlayerEntity serverPlayer) {
            ModCriteria.THEFT.trigger(serverPlayer, caught);
        }
    }

    private static void alertNearbyGuards(ServerWorld world, PlayerEntity player, List<LivingEntity> witnesses) {
        for (LivingEntity witness : witnesses) {
            if (witness instanceof GuardEntity guard) {
                guard.setTarget(player);
            }
        }
    }

    private static boolean isTaggedVillageChest(ServerWorld world, BlockPos pos) {
        return VillageContainerTagger.get(world).isVillageContainer(pos);
    }

    private static boolean canSeeTheft(ServerWorld world, LivingEntity observer, PlayerEntity player) {
        if (observer.isSleeping()) {
            BeyondTheBlock.LOGGER.info("Witness {} is sleeping and cannot see theft.", observer);
            return false;
        }

        Vec3d observerEyes = observer.getEyePos();
        Vec3d playerEyes = player.getEyePos();
        Vec3d lookVec = observer.getRotationVec(1.0F);

        // Check facing
        Vec3d toPlayer = playerEyes.subtract(observerEyes).normalize();
        double dot = lookVec.dotProduct(toPlayer);

        double threshold = 0.85;
        if (Configs.server().features.theft.useStealthDetection && player.isSneaking()) {
            threshold = 0.95; // Narrower FOV required to see sneaking player
        }

        if (dot < threshold) {
            BeyondTheBlock.LOGGER.info("Witness {} not looking at player (dot: {})", observer, dot);
            return false;
        }

        // Check line of sight
        HitResult result = world.raycast(new RaycastContext(
                observerEyes, playerEyes,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                observer
        ));

        if (result.getType() != HitResult.Type.MISS) {
            BeyondTheBlock.LOGGER.info("Witness {} LOS blocked by {} at {}", observer, result.getType(), result.getPos());
        }
        return result.getType() == HitResult.Type.MISS;
    }
}
