package net.ryan.beyond_the_block.mixin.feature.enchantments;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.ryan.beyond_the_block.feature.combat.EnchantmentCombatHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityEnchantmentMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void btb$combatTick(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.getWorld().isClient) return;

        EnchantmentCombatHooks.onTick(self);
    }

    @Inject(method = "onAttacking", at = @At("HEAD"))
    private void btb$onAttacking(Entity target, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(target instanceof LivingEntity livingTarget)) return;

        EnchantmentCombatHooks.onAttack(self, livingTarget);
    }

    @Inject(method = "damage", at = @At("TAIL"))
    private void btb$afterDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        LivingEntity self = (LivingEntity) (Object) this;
        EnchantmentCombatHooks.afterDamage(self, source, amount);
    }
}