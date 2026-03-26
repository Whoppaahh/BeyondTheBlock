package net.ryan.beyond_the_block.seasonal;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.feature.naming.EntityTagManager;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class HalloweenFeatures {

    private static final Random RANDOM = new Random();
    private static final Set<UUID> BAT_MARKED_PLAYERS = new HashSet<>();

    public static void register() {

        // Start hook — apply costumes
        HolidayFeatureRegistry.registerStart(HolidayManager.Holiday.HALLOWEEN, () -> {
            ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
                if (entity instanceof ZombieEntity
                        || entity instanceof HuskEntity
                        || entity instanceof DrownedEntity
                        || entity instanceof SkeletonEntity) {

                    LivingEntity mob = (LivingEntity) entity;
                    tryEquipPumpkin(mob);
                    tryEquipCostume(mob);
                }
            });
            ServerTickEvents.END_SERVER_TICK.register(server -> {

                for (ServerWorld world : server.getWorlds()) {
                    boolean isNight = !world.isDay();

                    for (BatEntity bat : world.getEntitiesByClass(BatEntity.class, new Box(-3.0E7, 0, -3.0E7, 3.0E7, 256, 3.0E7), BatEntity::isAlive)) {

                        if (!isNight) continue; // only attack at night

                        PlayerEntity target = world.getClosestPlayer(bat, 10);
                        if (target != null) {
                            // Swoop motion
                            Vec3d dir = target.getPos().subtract(bat.getPos()).normalize();
                            bat.setVelocity(dir.multiply(0.4));
                            bat.lookAtEntity(target, 30.0F, 30.0F);

                            // Particles
                            world.spawnParticles(ParticleTypes.SMOKE, bat.getX(), bat.getY(), bat.getZ(), 2, 0.1, 0.1, 0.1, 0.0);

                            // Damage & mark player if close
                            if (bat.distanceTo(target) < 1.5F) {
                                target.damage(DamageSource.mob(bat), 2.0F);
                                target.sendMessage(Text.literal("You have now become a vampire"));
                                target.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 10));
                                target.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION));
                                target.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH));
                                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED));
                                BAT_MARKED_PLAYERS.add(target.getUuid());
                            }
                        }
                    }

                    // ☀️ Burn marked players in sunlight
                    if (world.isDay()) {
                        for (ServerPlayerEntity player : world.getPlayers()) {
                            if (BAT_MARKED_PLAYERS.contains(player.getUuid()) && isInSunlight(player)) {
                                player.removeStatusEffect(StatusEffects.NIGHT_VISION);
                                player.removeStatusEffect(StatusEffects.STRENGTH);
                                player.removeStatusEffect(StatusEffects.SPEED);
                                player.setOnFireFor(8);
                                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 10));
                                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER));
                                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS));
                                player.sendMessage(Text.literal("The vampires blood has passed, you remain human"));
                                BAT_MARKED_PLAYERS.remove(player.getUuid());
                            }
                        }
                    }
                }
            });

        });

        // End hook — cleanup
        HolidayFeatureRegistry.registerEnd(HolidayManager.Holiday.HALLOWEEN, () -> {

            for (ServerWorld world : HolidayManager.currentServer.getWorlds()) {

                for (ServerPlayerEntity player : world.getPlayers()) {
                    var playerPos = player.getBlockPos();
                    Vec3d min = new Vec3d(playerPos.getX() - 128.5, playerPos.getY() - 64.5, playerPos.getZ() - 128.5);
                    Vec3d max = new Vec3d(playerPos.getX() + 128.5, playerPos.getY() + 64.5, playerPos.getZ() + 128.5);
                    Box box = new Box(min, max);

                    for (LivingEntity mob : world.getEntitiesByClass(LivingEntity.class, box,
                            e -> e instanceof EntityTagManager flag && flag.beyondTheBlock$isHalloweenCostume())) {

                        EntityTagManager flag = (EntityTagManager) mob;
                        mob.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        mob.equipStack(EquipmentSlot.CHEST, ItemStack.EMPTY);
                        mob.equipStack(EquipmentSlot.LEGS, ItemStack.EMPTY);
                        mob.equipStack(EquipmentSlot.FEET, ItemStack.EMPTY);
                        flag.beyondTheBlock$setHalloweenCostume(false);
                    }
                }
            }

        });
    }

    private static boolean isInSunlight(PlayerEntity player) {
        return player.getWorld().isDay() && player.getWorld().isSkyVisible(player.getBlockPos());
    }

    // Costume helpers
    private static void tryEquipPumpkin(LivingEntity entity) {
        if (RANDOM.nextFloat() < 0.25f && entity.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
            ItemStack pumpkin = new ItemStack(RANDOM.nextFloat() < 0.2f ? Items.JACK_O_LANTERN : Items.CARVED_PUMPKIN);
            entity.equipStack(EquipmentSlot.HEAD, pumpkin);
            if (entity instanceof EntityTagManager flag) flag.beyondTheBlock$setHalloweenCostume(true);
        }
    }

    private static void tryEquipCostume(LivingEntity entity) {
        if (RANDOM.nextFloat() < 0.2f) {
            entity.equipStack(EquipmentSlot.CHEST, dyedArmor(Items.LEATHER_CHESTPLATE, getRandomSpookyColor()));
            entity.equipStack(EquipmentSlot.LEGS, dyedArmor(Items.LEATHER_LEGGINGS, getRandomSpookyColor()));
            entity.equipStack(EquipmentSlot.FEET, dyedArmor(Items.LEATHER_BOOTS, getRandomSpookyColor()));
            if (entity instanceof EntityTagManager flag) {
                flag.beyondTheBlock$setHalloweenCostume(true);
                BeyondTheBlock.LOGGER.info("Halloween costume set for {}", entity.getName().getString());
            }
        }
    }

    private static ItemStack dyedArmor(Item armorItem, int color) {
        ItemStack stack = new ItemStack(armorItem);
        if (stack.getItem() instanceof DyeableArmorItem armor) {
            armor.setColor(stack, color);
        }
        return stack;
    }

    private static int getRandomSpookyColor() {
        return switch (RANDOM.nextInt(4)) {
            case 0 -> 0xFF7518; // orange
            case 1 -> 0x5500AA; // purple
            case 2 -> 0x222222; // black
            case 3 -> 0x008000; // dark green
            default -> 0xFFFFFF;
        };
    }
}
