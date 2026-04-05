package net.ryan.beyond_the_block.mixin.feature.enchantments.freeze;

import net.minecraft.entity.mob.MobEntity;
import net.ryan.beyond_the_block.feature.status.FreezeStateHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityFreezeMixin {

    @Inject(method = "tickNewAi", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$preventFrozenAi(CallbackInfo ci) {
        MobEntity mob = (MobEntity) (Object) this;
        if (FreezeStateHandler.isFrozen(mob)) {
            mob.getNavigation().stop();
            mob.setTarget(null);
            ci.cancel();
        }
    }
}