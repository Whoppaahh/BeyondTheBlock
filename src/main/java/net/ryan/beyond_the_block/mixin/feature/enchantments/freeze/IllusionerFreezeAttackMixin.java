package net.ryan.beyond_the_block.mixin.feature.enchantments.freeze;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.IllusionerEntity;
import net.ryan.beyond_the_block.feature.status.handlers.FreezeStateHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IllusionerEntity.class)
public class IllusionerFreezeAttackMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$preventFrozenIllusionerAttack(LivingEntity target, float pullProgress, CallbackInfo ci) {
        IllusionerEntity self = (IllusionerEntity) (Object) this;
        if (FreezeStateHandler.isFrozen(self)) {
            ci.cancel();
        }
    }
}
