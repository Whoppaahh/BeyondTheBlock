package net.ryan.beyond_the_block.content.effect.Harmful;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class ZombificationEffect extends StatusEffect {

    public ZombificationEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    /**
     * Called each tick while the effect is active (server-side).
     * We'll watch for the final tick (duration == 1) and do the conversion then.
     */
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getWorld().isClient) return;

        StatusEffectInstance inst = entity.getStatusEffect(this);
        if (inst == null) return;

        int remaining = inst.getDuration(); // ticks left
        ServerWorld world = (ServerWorld) entity.getWorld();

        // When effect is about to expire (last tick), trigger conversion
        if (remaining == 1) {
            if (entity instanceof VillagerEntity villager) {
                ZombieVillagerEntity zombieVillager = villager.convertTo(
                        EntityType.ZOMBIE_VILLAGER, false
                );

                if (zombieVillager != null) {
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        ItemStack stack = villager.getEquippedStack(slot);
                        if (!stack.isEmpty()) {
                            zombieVillager.equipStack(slot, stack.copy());
                        }
                    }
                    zombieVillager.initialize(world,
                            world.getLocalDifficulty(zombieVillager.getBlockPos()),
                            SpawnReason.CONVERSION,
                            null, null);
                }
            } else if (entity instanceof PlayerEntity player) {
                applyPlayerZombification(player, amplifier);
            } else if (entity instanceof HorseEntity horse) {
                ZombieHorseEntity zombieHorse = horse.convertTo(
                        EntityType.ZOMBIE_HORSE, false
                );
                if (zombieHorse != null) {
                    zombieHorse.initialize(world,
                            world.getLocalDifficulty(zombieHorse.getBlockPos()),
                            SpawnReason.CONVERSION,
                            null, null);
                }
            } else if (entity instanceof PiglinEntity piglin) {
                ZombifiedPiglinEntity zombPig = piglin.convertTo(
                        EntityType.ZOMBIFIED_PIGLIN, true
                );
                if (zombPig != null) {
                    zombPig.initialize(world,
                            world.getLocalDifficulty(zombPig.getBlockPos()),
                            SpawnReason.CONVERSION,
                            null, null);
                }
            }

        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // run every tick so we can inspect remaining duration
        return true;
    }


    /**
     * For players we can't replace the PlayerEntity with a mob safely server-side.
     * Instead we apply a set of strong "zombie-like" effects:
     * - Slowness
     * - Weakness
     * - Hunger
     * - Disable natural regen (we do that by repeatedly applying regeneration-blocking via attributes is complex,
     *   so as a simple approach we continuously apply damage or set health behavior — here we'll prevent natural regen by
     *   applying a long-lasting custom flag or re-applying effects; a full prevention requires more hooks).
     *
     * A proper "player -> mob" transformation requires client-side cosmetic changes (skin/renderer) AND careful handling.
     */
    private void applyPlayerZombification(PlayerEntity player, int amplifier) {
        // Ensure server-only
        if (player.world.isClient) return;

        // Give a bunch of negative effects for a while (server-side)
        int seconds = 20 * 60; // 60 seconds of effects as an example
        player.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.SLOWNESS, seconds, 2)); // slowness III
        player.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.WEAKNESS, seconds, 1));
        player.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.HUNGER, seconds, 1));

        // Make them take sunlight damage like zombies? We cannot change the player class easily here.
        // Instead we can periodically damage them if sunlight is detected (recommend hooking a server tick event).
        // For now: send a short message and leave the mechanical "zombie-ness" to other systems.
        if (player.getWorld() instanceof ServerWorld) {
            player.sendMessage(Text.literal("You feel dead inside... you have been zombified!"), false);
        }
    }
}

