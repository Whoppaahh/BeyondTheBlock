package net.ryan.beyond_the_block.mixin.feature.enchantments.freeze;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.ryan.beyond_the_block.feature.status.FreezeStateHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PillagerEntity.class)
public class PillagerFreezeAttackMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$preventFrozenIllusionerAttack(LivingEntity target, float pullProgress, CallbackInfo ci) {
        PillagerEntity self = (PillagerEntity) (Object) this;
        if (FreezeStateHandler.isFrozen(self)) {
            ci.cancel();
        }
    }
}
