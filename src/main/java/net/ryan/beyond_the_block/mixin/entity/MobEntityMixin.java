package net.ryan.beyond_the_block.mixin.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.ryan.beyond_the_block.feature.mob.MobTargetHooks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$beforeSetTarget(@Nullable LivingEntity target, CallbackInfo ci) {
        MobEntity self = (MobEntity) (Object) this;

        if (MobTargetHooks.shouldBlockTargetAssignment(self, target)) {
            ci.cancel();
        }
    }

    @Inject(method = "canTarget", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$canTarget(EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        MobEntity self = (MobEntity) (Object) this;

        if (!MobTargetHooks.canTargetType(self, type)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "setTarget", at = @At("TAIL"))
    private void beyond_the_block$afterSetTarget(@Nullable LivingEntity target, CallbackInfo ci) {
        MobEntity self = (MobEntity) (Object) this;
        MobTargetHooks.afterTargetAssigned(self, target);
    }
}