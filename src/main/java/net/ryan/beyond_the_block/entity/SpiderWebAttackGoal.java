package net.ryan.beyond_the_block.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.Random;

public class SpiderWebAttackGoal extends Goal {
    private final SpiderEntity spider;
    private LivingEntity target;
    private final Random random = new Random();

    private int cooldown = 60; // ticks between attacks
    private int timer = 0;

    private boolean isCharging = false;
    private int chargeTimer = 0;

    public SpiderWebAttackGoal(SpiderEntity spider) {
        this.spider = spider;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        target = spider.getTarget();
        if (target == null) return false;

        double distSq = spider.squaredDistanceTo(target);
        // Only start if target is further than melee range but not too far
        return distSq > 16 && distSq < 625; // 4–25 blocks
    }

    @Override
    public void start() {
        spider.getNavigation().stop();
    }

    @Override
    public void stop() {
        isCharging = false;
        chargeTimer = 0;
    }

    @Override
    public boolean shouldContinue() {
        if (target == null) return false;
        double distSq = spider.squaredDistanceTo(target);
        return isCharging || (distSq > 16 && distSq < 625);
    }

    @Override
    public void tick() {
        if (timer > 0) {
            timer--;
            return;
        }
        if (target == null) return;

        // Always face target gradually
        spider.getLookControl().lookAt(target, 30.0F, 30.0F);

        if (isCharging) {
            performChargeTick();
            return;
        }

        boolean useArc = random.nextDouble() < 0.2; // 20% chance for arc
        if (useArc) {
            startCharge();
        } else {
            performSingleAttack();
            timer = cooldown;
        }
    }

    private void startCharge() {
        isCharging = true;
        chargeTimer = 40; // charge for 2 seconds
        // Spawn string item particles
        if (spider.getWorld() instanceof ServerWorld serverWorld) {
            int count = 4;
            for (int i = 0; i < count; i++) {
                serverWorld.spawnParticles(
                        new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.STRING)),
                        spider.getX() + (random.nextDouble() - 0.5) * 0.2,
                        spider.getY() + 1 + (random.nextDouble() - 0.5) * 0.2,
                        spider.getZ() + (random.nextDouble() - 0.5) * 0.2,
                        1,
                        0, 0, 0,
                        0.01
                );
            }
        }
    }

    private void performChargeTick() {
        chargeTimer--;

        // Root spider
        spider.getNavigation().stop();
        spider.setVelocity(0, spider.getVelocity().y, 0);

        // Spawn cobweb block particles
        if (spider.getWorld() instanceof ServerWorld serverWorld) {
            int count = 3;
            for (int i = 0; i < count; i++) {
                serverWorld.spawnParticles(
                        new BlockStateParticleEffect(
                                ParticleTypes.BLOCK,
                                Blocks.COBWEB.getDefaultState()
                        ),
                        spider.getX() + (random.nextDouble() - 0.5) * 0.25,
                        spider.getY() + 1 + (random.nextDouble() - 0.5) * 0.25,
                        spider.getZ() + (random.nextDouble() - 0.5) * 0.25,
                        1,
                        0, 0, 0,
                        0
                );
            }
        }
        // Fire arc when charge ends
        if (chargeTimer <= 0) {
            performArcAttack();
            isCharging = false;
            timer = cooldown;
        }
    }

    private void performSingleAttack() {
        if (target == null) return;

        // Compute direction and rotation
        Vec3d toTarget = target.getPos().subtract(spider.getPos()).normalize();
        setSpiderFacing(toTarget);

        CobwebProjectileEntity web = new CobwebProjectileEntity(spider.getWorld(), spider, false);
        web.setPos(spider.getX(), spider.getEyeY(), spider.getZ());
        web.setVelocity(toTarget.x, toTarget.y, toTarget.z, 1.2f, 0);
        spider.getWorld().spawnEntity(web);
    }

    private void performArcAttack() {
        if (target == null) return;

        Vec3d toTarget = target.getPos().subtract(spider.getPos()).normalize();
        setSpiderFacing(toTarget);

        for (int i = 0; i < 5; i++) {
            double angle = (i - 2) * 6.0; // spread ~30° total
            Vec3d rotated = rotateVectorY(toTarget, angle);

            double pitchOffset = (random.nextDouble() - 0.5) * 0.2;
            Vec3d finalDir = new Vec3d(rotated.x, rotated.y + pitchOffset, rotated.z).normalize();

            CobwebProjectileEntity web = new CobwebProjectileEntity(spider.getWorld(), spider, true);
            web.setPos(spider.getX(), spider.getEyeY(), spider.getZ());
            web.setVelocity(finalDir.x, finalDir.y, finalDir.z, 1.2f, 0);
            spider.getWorld().spawnEntity(web);
        }

        spawnParticles(ParticleTypes.SMOKE, 10, 0.4, 0.4, 0.4, 0.02);
    }

    private void setSpiderFacing(Vec3d direction) {
        double dx = direction.x;
        double dy = direction.y;
        double dz = direction.z;

        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(dy, Math.sqrt(dx*dx + dz*dz))));

        spider.setYaw(yaw);
        spider.setPitch(pitch);
        spider.headYaw = yaw;
        spider.bodyYaw = yaw;
    }

    private Vec3d rotateVectorY(Vec3d vec, double degrees) {
        double rad = Math.toRadians(degrees);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new Vec3d(vec.x * cos - vec.z * sin, vec.y, vec.x * sin + vec.z * cos);
    }

    private void spawnParticles(ParticleEffect type, int count,
                                double offsetX, double offsetY, double offsetZ, double speed) {
        if (spider.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    type,
                    spider.getX(),
                    spider.getBodyY(0.5),
                    spider.getZ(),
                    count,
                    offsetX, offsetY, offsetZ,
                    speed
            );
        }
    }
}
