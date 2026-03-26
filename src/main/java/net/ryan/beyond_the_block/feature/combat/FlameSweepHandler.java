package net.ryan.beyond_the_block.feature.combat;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.utils.FlameTrailPoint;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FlameSweepHandler {
    public static ActionResult onEntityAttacked(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hit) {
        if (!(entity instanceof LivingEntity target)) return ActionResult.PASS;

        ItemStack stack = player.getStackInHand(hand);

        if (EnchantmentHelper.getLevel(ModEnchantments.TEMPORAL_SLICE, stack) > 0) {
            target.addStatusEffect(new StatusEffectInstance(ModEffects.TIME_DILATION, 100, 0, false, false, false));
            return ActionResult.SUCCESS;
        }

        int flameSweepLevel = EnchantmentHelper.getLevel(ModEnchantments.FLAME_SWEEP, stack);
        if (flameSweepLevel > 0 && !world.isClient) {
            target.setOnFireFor(3 + 2 * flameSweepLevel);

            double range = 1.0 + 0.5 * flameSweepLevel;
            world.getEntitiesByClass(LivingEntity.class, entity.getBoundingBox().expand(range, 0.25, range),
                            e -> e != player && e != entity && player.canSee(e))
                    .forEach(e -> e.setOnFireFor(3 + 2 * flameSweepLevel));

            if (world instanceof ServerWorld serverWorld)
                spawnFlameSweepParticles(serverWorld, player, flameSweepLevel, stack);

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private static final List<FlameTrailPoint> TRAIL_POINTS = new ArrayList<>();

    public static void spawnFlameSweepParticles(ServerWorld world, PlayerEntity player, int level, ItemStack stack) {
        if (!player.handSwinging) return;

        float progress = player.getAttackCooldownProgress(0.5F);
        if (progress <= 0) return;

        float baseYaw = player.getYaw();
        double radius = 1.5 + 0.25 * level + (stack.getItem() instanceof AxeItem ? 0.3 : 0);
        float arc = stack.getItem() instanceof AxeItem ? 120F + 25F * level : 100F + 20F * level;
        int particleCount = 3 + level * 2 + (stack.getItem() instanceof AxeItem ? 3 : 0);

        // Dynamic sweep arc
        float swingAngle = -arc / 2F + arc * progress + baseYaw;

        for (int i = 0; i < particleCount; i++) {
            float offset = swingAngle + (world.random.nextFloat() - 0.5F) * 10F;
            double rad = Math.toRadians(offset);

            double x = player.getX() - Math.sin(rad) * radius;
            double y = player.getBodyY(0.5) + (world.random.nextDouble() * 0.3);
            double z = player.getZ() + Math.cos(rad) * radius;

            // --- SWEEP_ATTACK PARTICLE (vanilla sweep arc) ---
            world.spawnParticles(ParticleTypes.SWEEP_ATTACK, x, y, z, 1, 0, 0, 0, 0);

            // --- Add trail point for flame particle ---
            TRAIL_POINTS.add(new FlameTrailPoint(new Vec3d(x, y, z), 5)); // flames last 5 ticks
        }

        // --- Tick existing trail points for fading flames ---
        TRAIL_POINTS.removeIf(point -> point.tick(world));
    }

}
