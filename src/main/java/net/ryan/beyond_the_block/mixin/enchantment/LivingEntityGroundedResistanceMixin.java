package net.ryan.beyond_the_block.mixin.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityGroundedResistanceMixin {

    @ModifyVariable(
            method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private float beyond_the_block$reduceGroundedDamage(float amount, DamageSource source) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.getWorld().isClient || amount <= 0.0f) {
            return amount;
        }

        if (!self.isOnGround()) {
            return amount;
        }

        ItemStack boots = self.getEquippedStack(EquipmentSlot.FEET);
        int level = EnchantmentHelper.getLevel(ModEnchantments.GROUNDED_RESISTANCE, boots);

        if (level <= 0) {
            return amount;
        }

        float multiplier = switch (level) {
            case 1 -> 0.90f; // 10% reduction
            case 2 -> 0.80f; // 20% reduction
            default -> 0.70f; // 30% reduction
        };

        return amount * multiplier;
    }
    @ModifyVariable(
            method = "takeKnockback(DDD)V",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private double beyond_the_block$reduceGroundedKnockback(double strength) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.getWorld().isClient || strength <= 0.0D) {
            return strength;
        }

        if (!self.isOnGround()) {
            return strength;
        }

        ItemStack boots = self.getEquippedStack(EquipmentSlot.FEET);
        int level = EnchantmentHelper.getLevel(ModEnchantments.GROUNDED_RESISTANCE, boots);

        if (level <= 0) {
            return strength;
        }

        double multiplier = switch (level) {
            case 1 -> 0.85D; // 15% less knockback
            case 2 -> 0.70D; // 30% less knockback
            default -> 0.55D; // 45% less knockback
        };

        return strength * multiplier;
    }
}
