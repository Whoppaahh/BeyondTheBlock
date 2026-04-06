package net.ryan.beyond_the_block.mixin.feature.enchantments;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.ryan.beyond_the_block.feature.status.LivingEntityStatusHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityStatusMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$beforeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (LivingEntityStatusHooks.shouldCancelDamage(self, source, amount)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "damage", at = @At("TAIL"))
    private void beyond_the_block$afterDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        LivingEntity self = (LivingEntity) (Object) this;
        LivingEntityStatusHooks.afterSuccessfulDamage(self, source, amount);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void beyond_the_block$onDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        LivingEntityStatusHooks.onDeath(self, source);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void beyond_the_block$statusTick(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.getWorld().isClient) {
            return;
        }

        LivingEntityStatusHooks.onTick(self);
    }
}