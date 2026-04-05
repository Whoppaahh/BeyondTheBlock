package net.ryan.beyond_the_block.feature.combat.handlers;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.utils.FlameTrailPoint;

import java.util.ArrayList;
import java.util.List;

public final class FlameSweepHandler {
    private static final List<FlameTrailPoint> TRAIL_POINTS = new ArrayList<>();

    private FlameSweepHandler() {
    }

    public static boolean apply(PlayerEntity player, World world, LivingEntity target, ItemStack stack, int flameSweepLevel) {
        if (world.isClient) {
            return false;
        }

        target.setOnFireFor(3 + 2 * flameSweepLevel);

        double range = 1.0 + 0.5 * flameSweepLevel;
        world.getEntitiesByClass(
                LivingEntity.class,
                target.getBoundingBox().expand(range, 0.25, range),
                e -> e != player && e != target && player.canSee(e)
        ).forEach(e -> e.setOnFireFor(3 + 2 * flameSweepLevel));

        if (world instanceof ServerWorld serverWorld) {
            spawnFlameSweepParticles(serverWorld, player, flameSweepLevel, stack);
        }

        return true;
    }

    public static void spawnFlameSweepParticles(ServerWorld world, PlayerEntity player, int level, ItemStack stack) {
        if (!player.handSwinging) return;

        float progress = player.getAttackCooldownProgress(0.5F);
        if (progress <= 0) return;

        float baseYaw = player.getYaw();
        double radius = 1.5 + 0.25 * level + (stack.getItem() instanceof AxeItem ? 0.3 : 0);
        float arc = stack.getItem() instanceof AxeItem ? 120F + 25F * level : 100F + 20F * level;
        int particleCount = 3 + level * 2 + (stack.getItem() instanceof AxeItem ? 3 : 0);

        float swingAngle = -arc / 2F + arc * progress + baseYaw;

        for (int i = 0; i < particleCount; i++) {
            float offset = swingAngle + (world.random.nextFloat() - 0.5F) * 10F;
            double rad = Math.toRadians(offset);

            double x = player.getX() - Math.sin(rad) * radius;
            double y = player.getBodyY(0.5) + (world.random.nextDouble() * 0.3);
            double z = player.getZ() + Math.cos(rad) * radius;

            world.spawnParticles(ParticleTypes.SWEEP_ATTACK, x, y, z, 1, 0, 0, 0, 0);
            TRAIL_POINTS.add(new FlameTrailPoint(new Vec3d(x, y, z), 5));
        }

        TRAIL_POINTS.removeIf(point -> point.tick(world));
    }
}