package net.ryan.beyond_the_block.utils.helpers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class HelmetEnchantmentHandler {

    private static final UUID IRON_CLAD_TOUGHNESS_UUID = UUID.fromString("cbe7d6f4-1c69-4d2a-aec7-8c12634212ab");
    private static final UUID IRON_CLAD_KNOCKBACK_UUID = UUID.fromString("b209f3f6-6757-460a-8a4b-7314b8141825");

    private HelmetEnchantmentHandler() {
    }

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(HelmetEnchantmentHandler::onEndWorldTick);
    }

    private static void onEndWorldTick(ServerWorld world) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            tickIronCladVision(player);
            tickShadowsVeil(player);
        }
    }

    private static void tickIronCladVision(ServerPlayerEntity player) {
        ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);
        int level = EnchantmentHelper.getLevel(ModEnchantments.IRON_CLAD_VISION, helmet);

        if (level <= 0) {
            removeIronCladModifiers(player);
            return;
        }

        // Refresh short-duration night vision instead of using Integer.MAX_VALUE
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.NIGHT_VISION,
                220,
                0,
                false,
                false,
                false
        ));

        applyIronCladModifiers(player, level);

        if (level >= 2) {
            List<MobEntity> mobs = player.getWorld().getEntitiesByClass(
                    MobEntity.class,
                    player.getBoundingBox().expand(10.0),
                    mob -> mob.isAlive()
            );

            for (MobEntity mob : mobs) {
                mob.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.GLOWING,
                        40,
                        0,
                        false,
                        false,
                        true
                ));
            }
        }

        if (level >= 3 && player.getHealth() <= 12.0f) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.RESISTANCE,
                    40,
                    0,
                    true,
                    false,
                    false
            ));
        }
    }

    private static void applyIronCladModifiers(ServerPlayerEntity player, int level) {
        var toughness = Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
        var knockback = Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));

        if (toughness.getModifier(IRON_CLAD_TOUGHNESS_UUID) == null) {
            toughness.addPersistentModifier(new EntityAttributeModifier(
                    IRON_CLAD_TOUGHNESS_UUID,
                    "IronClad Toughness",
                    1.0,
                    EntityAttributeModifier.Operation.ADDITION
            ));
        }

        if (level >= 2) {
            if (knockback.getModifier(IRON_CLAD_KNOCKBACK_UUID) == null) {
                knockback.addPersistentModifier(new EntityAttributeModifier(
                        IRON_CLAD_KNOCKBACK_UUID,
                        "IronClad Knockback",
                        0.05,
                        EntityAttributeModifier.Operation.ADDITION
                ));
            }
        } else {
            knockback.removeModifier(IRON_CLAD_KNOCKBACK_UUID);
        }
    }

    private static void removeIronCladModifiers(ServerPlayerEntity player) {
        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS))
                .removeModifier(IRON_CLAD_TOUGHNESS_UUID);
        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))
                .removeModifier(IRON_CLAD_KNOCKBACK_UUID);
    }

    private static void tickShadowsVeil(ServerPlayerEntity player) {
        ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);
        int level = EnchantmentHelper.getLevel(ModEnchantments.SHADOWS_VEIL, helmet);

        if (level <= 0) {
            return;
        }

        boolean shouldApplyInvisibility = switch (level) {
            case 1 -> player.isSneaking();
            default -> true;
        };

        if (shouldApplyInvisibility) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.INVISIBILITY,
                    40,
                    0,
                    false,
                    false,
                    false
            ));
        }
    }
}
