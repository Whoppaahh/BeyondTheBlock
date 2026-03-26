package net.ryan.beyond_the_block.feature.projectile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.access.Configs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class TrajectoryHelper {

    private TrajectoryHelper() {}

    public static TrajectoryPath computeTrajectory(MinecraftClient client, float tickDelta) {
        PlayerEntity player = client.player;
        World world = client.world;
        if (player == null || world == null) return TrajectoryPath.EMPTY;


        if (!Configs.client().hud.trajectory.enabled) return TrajectoryPath.EMPTY;

        ItemStack stack = player.getMainHandStack();
        boolean isBow = stack.getItem() instanceof BowItem;
        boolean isCrossbow = stack.getItem() instanceof CrossbowItem;
        boolean isTrident = stack.getItem() instanceof TridentItem;
        boolean isThrowable =
                stack.getItem() instanceof SnowballItem ||
                        stack.getItem() instanceof EnderPearlItem ||
                        stack.getItem() instanceof ThrowablePotionItem ||
                        stack.getItem() instanceof EggItem;

        TrajectoryPath.WeaponKind weaponKind = TrajectoryPath.WeaponKind.OTHER;

        if (isBow && !Configs.client().hud.trajectory.showBow) return TrajectoryPath.EMPTY;
        if (isCrossbow && !Configs.client().hud.trajectory.showCrossbow) return TrajectoryPath.EMPTY;
        if (isTrident && !Configs.client().hud.trajectory.showTrident) return TrajectoryPath.EMPTY;
        if (isThrowable && !Configs.client().hud.trajectory.showThrowables) return TrajectoryPath.EMPTY;

        if (isBow) weaponKind = TrajectoryPath.WeaponKind.BOW;
        else if (isCrossbow) weaponKind = TrajectoryPath.WeaponKind.CROSSBOW;

        // Gating: sneak + aiming
        if (Configs.client().hud.trajectory.requireSneak && !player.isSneaking()) return TrajectoryPath.EMPTY;

        if (Configs.client().hud.trajectory.onlyWhileAiming) {
            if (isBow) {
                if (!player.isUsingItem() || player.getActiveItem() != stack) return TrajectoryPath.EMPTY;
            } else if (isCrossbow) {
                if (!CrossbowItem.isCharged(stack)) return TrajectoryPath.EMPTY;
            } else if (isTrident || isThrowable) {
                if (!player.isUsingItem()) return TrajectoryPath.EMPTY;
            }
        }

        // Initial pos & direction
        Vec3d eyePos = player.getCameraPosVec(tickDelta);
        Vec3d dir = getShootDirection(player, tickDelta);

        // Initial speed close to vanilla
        double speed = getInitialSpeed(player, stack, isBow, isCrossbow, isTrident, isThrowable);
        if (speed <= 0) return TrajectoryPath.EMPTY;

        Vec3d velocity = dir.multiply(speed);
        Vec3d pos = eyePos.add(dir.multiply(0.16)); // small forward offset like vanilla

        List<Vec3d> points = new ArrayList<>();
        TrajectoryPath.HitKind hitKind = TrajectoryPath.HitKind.NONE;
        Vec3d hitPos = null;
        BlockPos hitBlockPos = null;
        int hitEntityId = -1;

        // Per-tick simulation: this is the key fix
        int maxTicks = Math.max(1, Configs.client().hud.trajectory.maxSteps);
        for (int tick = 0; tick < maxTicks; tick++) {
            Vec3d nextPos = pos.add(velocity);

            // Block raycast
            HitResult blockHit = world.raycast(new RaycastContext(
                    pos,
                    nextPos,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.ANY,
                    player
            ));

            // Entity raycast
            EntityHitResult entityHit = raycastEntity(world, player, pos, nextPos, e -> e != player && e.isAlive());

            boolean blockCollision = blockHit != null && blockHit.getType() == HitResult.Type.BLOCK;
            boolean entityCollision = entityHit != null;

            points.add(pos);

            if (blockCollision || entityCollision) {
                if (entityCollision && (!blockCollision ||
                        pos.distanceTo(entityHit.getPos()) < pos.distanceTo(blockHit.getPos()))) {
                    hitPos = entityHit.getPos();
                    hitKind = TrajectoryPath.HitKind.ENTITY;
                    hitEntityId = entityHit.getEntity().getId();
                } else if (blockCollision) {
                    hitPos = blockHit.getPos();
                    hitKind = TrajectoryPath.HitKind.BLOCK;
                    hitBlockPos = ((BlockHitResult) blockHit).getBlockPos();
                }

                points.add(hitPos);
                return new TrajectoryPath(points, hitKind, hitPos, hitBlockPos, hitEntityId, weaponKind);
            }

            // Advance to next tick position
            pos = nextPos;

            // Apply vanilla-like per-tick physics
            velocity = applyPhysicsPerTick(world, pos, velocity);
        }

        if (points.isEmpty()) return TrajectoryPath.EMPTY;
        return new TrajectoryPath(points, hitKind, hitPos, hitBlockPos, hitEntityId, weaponKind);
    }

    private static Vec3d getShootDirection(PlayerEntity player, float tickDelta) {
        float pitch = player.getPitch(tickDelta);
        float yaw = player.getYaw(tickDelta);

        float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        float g = -MathHelper.sin(pitch * ((float)Math.PI / 180F));
        float h =  MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        return new Vec3d(f, g, h);
    }

    private static double getInitialSpeed(PlayerEntity player, ItemStack stack,
                                          boolean bow, boolean crossbow,
                                          boolean trident, boolean throwable) {
        if (bow) {
            int useTicks = player.getItemUseTime();
            float pull = BowItem.getPullProgress(useTicks);
            if (pull < 0.1F) return 0;
            // Vanilla: power = pull * 3.0F; arrow.setVelocity(..., power * 3.0F, 1.0F)
            float power = pull * 3.0F;
            if (power > 1.0F) power = 1.0F;
            return power * 3.0F;
        }
        if (crossbow) {
            // Close to CrossbowItem's arrow speed
            return 3.15F;
        }
        if (trident) {
            return 2.5F;
        }
        if (throwable) {
            return 1.5F;
        }
        return 0;
    }

    private static Vec3d applyPhysicsPerTick(World world, Vec3d pos, Vec3d vel) {
        // This mirrors ArrowEntity tick physics reasonably closely:
        boolean inWater = !world.getFluidState(new BlockPos(pos)).isEmpty();

        double drag = inWater ? 0.6D : 0.99D;
        double gravity = 0.05D;

        double vx = vel.x * drag;
        double vz = vel.z * drag;
        double vy = vel.y * drag - gravity;

        return new Vec3d(vx, vy, vz);
    }

    private static EntityHitResult raycastEntity(World world,
                                                 PlayerEntity player,
                                                 Vec3d start,
                                                 Vec3d end,
                                                 Predicate<Entity> predicate) {
        Box box = new Box(start, end).expand(0.3D);
        return ProjectileUtil.getEntityCollision(world, player, start, end, box, predicate);
    }
}
