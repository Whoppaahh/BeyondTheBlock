package net.ryan.beyond_the_block.mixin.feature.enchantments;

import net.minecraft.entity.LivingEntity;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityFreezeMixin {
    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    private void freezeStopAI(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.world.isClient) return; // Skip client-side

        if (entity.hasStatusEffect(ModEffects.FREEZE)) {
            // Optional: prevent AI movement
            ci.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void freezeInPlace(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.world.isClient) return; // Skip client-side
        if (entity.hasStatusEffect(ModEffects.FREEZE)) {
            ci.cancel();
        }
    }

    @Inject(method = "updateLimbs", at = @At("HEAD"), cancellable = true)
    private void freezeStopMovement(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.world.isClient) return; // Skip client-side
        if (entity.hasStatusEffect(ModEffects.FREEZE)) {
            ci.cancel();
        }
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void preventJump(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.hasStatusEffect(ModEffects.FREEZE)) {
            ci.cancel(); // cancels jump completely
        }
    }
}
