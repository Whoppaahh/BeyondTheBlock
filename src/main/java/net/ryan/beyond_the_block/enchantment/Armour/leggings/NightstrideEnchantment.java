package net.ryan.beyond_the_block.enchantment.Armour.leggings;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NightstrideEnchantment extends Enchantment {

    private static final Map<UUID, Long> invisibilityCooldown = new HashMap<>();
    private static boolean effectApplied = false;

    public NightstrideEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slots) {
        super(weight, type, slots);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armour && armour.getSlotType() == EquipmentSlot.LEGS || stack.isOf(Items.BOOK);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.SWIFT_SNEAK; // Example conflict
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public static void tick(ServerPlayerEntity player) {
        // Get the leggings and check the level of the enchantment
        ItemStack leggings = player.getEquippedStack(EquipmentSlot.LEGS);
        int level = EnchantmentHelper.getLevel(ModEnchantments.NIGHT_STRIDE, leggings);

        BlockPos pos = player.getBlockPos();
        ServerWorld world = player.getWorld();

        // Check if it is dark or night
        boolean isDark = world.getLightLevel(LightType.BLOCK, pos) <= 7;
        boolean isNight = world.isNight();

        // If the leggings are equipped and the conditions (dark or night) are met, apply effects
        if (level > 0 && (isDark || isNight)) {
            // Apply SPEED effect if it's not already applied
            if (!player.hasStatusEffect(StatusEffects.SPEED)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, Integer.MAX_VALUE, level, false, false, false));
                effectApplied = true;
            }

            // Apply JUMP_BOOST effect if level is 2 or greater
            if (level >= 2 && !player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, Integer.MAX_VALUE, level, false, false, false));
                effectApplied = true;
            }

            // Apply Sprint over water ability at level 3
            if (level == 3 && player.isSprinting() && player.isOnGround()) {
                BlockPos below = pos.down();
                if (world.getBlockState(below).isOf(Blocks.WATER)) {
                    world.setBlockState(below, Blocks.BARRIER.getDefaultState(), 3);
                    Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                        world.setBlockState(below, Blocks.AIR.getDefaultState(), 3);
                    }, 10 * 50, TimeUnit.MILLISECONDS);
                }
            }
        } else {
            // Remove effects if the leggings are no longer equipped or the conditions are not met
            if(effectApplied) {
                if (player.hasStatusEffect(StatusEffects.SPEED)) {
                    player.removeStatusEffect(StatusEffects.SPEED);
                }
                if (player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                    player.removeStatusEffect(StatusEffects.JUMP_BOOST);
                }
                effectApplied = false;
            }
        }
    }


    public static void registerTickHandler(ServerWorld serverWorld) {
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                tick(player);
            }

        // Invisibility on damage
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (entity instanceof PlayerEntity player) {
                ItemStack leggings = player.getEquippedStack(EquipmentSlot.LEGS);
                int level = net.minecraft.enchantment.EnchantmentHelper.getLevel(ModEnchantments.NIGHT_STRIDE, leggings);

                if (level >= 3 && !invisibilityCooldown.containsKey(player.getUuid())) {
                    if (!player.hasStatusEffect(StatusEffects.INVISIBILITY)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 200, 0));
                        invisibilityCooldown.put(player.getUuid(), world.getTime());
                    }
                }
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            long now = server.getOverworld().getTime();
            invisibilityCooldown.entrySet().removeIf(entry -> now - entry.getValue() > 100); // cooldown ~5s
        });
    }
}
