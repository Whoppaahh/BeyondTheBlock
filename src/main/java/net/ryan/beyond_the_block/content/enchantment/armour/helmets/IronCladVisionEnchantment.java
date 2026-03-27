package net.ryan.beyond_the_block.content.enchantment.armour.helmets;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class IronCladVisionEnchantment extends Enchantment {

    private static final UUID TOUGHNESS_UUID = UUID.fromString("cbe7d6f4-1c69-4d2a-aec7-8c12634212ab");
    private static final UUID KNOCKBACK_UUID = UUID.fromString("b209f3f6-6757-460a-8a4b-7314b8141825");
    private static boolean effectApplied = false;

    public IronCladVisionEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slots) {
        super(weight, target, slots);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armour && armour.getSlotType() == EquipmentSlot.HEAD || stack.isOf(Items.BOOK);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.AQUA_AFFINITY; // Example conflict
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public static void tick(PlayerEntity player) {
        ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);
        int level = EnchantmentHelper.getLevel(ModEnchantments.IRON_CLAD_VISION, helmet);

        if (level > 0) {
            // Night Vision (always refreshed)
            StatusEffectInstance current = player.getStatusEffect(StatusEffects.NIGHT_VISION);
            if (current == null) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false, false));
                effectApplied = true;
            }

            // Toughness and knockback attributes
            if (!player.getAttributes().hasModifierForAttribute(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, TOUGHNESS_UUID)) {
                Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS))
                        .addPersistentModifier(new EntityAttributeModifier(TOUGHNESS_UUID, "IronClad Toughness", 1.0, EntityAttributeModifier.Operation.ADDITION));
            }

            if (level >= 2 && !player.getAttributes().hasModifierForAttribute(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, KNOCKBACK_UUID)) {
                Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))
                        .addPersistentModifier(new EntityAttributeModifier(KNOCKBACK_UUID, "IronClad Knockback", 0.05, EntityAttributeModifier.Operation.ADDITION));
            }

            // Glow nearby mobs (level 2+)
            if (level >= 2) {
                List<MobEntity> mobs = player.getWorld().getEntitiesByClass(MobEntity.class,
                        player.getBoundingBox().expand(10), m -> m.isAlive() && !m.hasStatusEffect(StatusEffects.GLOWING));
                for (MobEntity mob : mobs) {
                    mob.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 0));
                }
            }

            // Resistance when low health (level 3)
            if (level >= 3 && player.getHealth() <= 12 && !player.hasStatusEffect(StatusEffects.RESISTANCE)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 0, true, false, false)); // 5s Resistance I
            }

        } else {
            // Remove modifiers if not wearing helmet with enchantment
            if (player.hasStatusEffect(StatusEffects.NIGHT_VISION) && effectApplied) {
                player.removeStatusEffect(StatusEffects.NIGHT_VISION);
                effectApplied = false;
            }

            Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).removeModifier(TOUGHNESS_UUID);
            Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)).removeModifier(KNOCKBACK_UUID);
        }
    }

    // Register this with a server tick event
    public static void registerTickHandler(ServerWorld serverWorld) {
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                tick(player);
            }
    }
}
