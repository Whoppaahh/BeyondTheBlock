package net.ryan.beyond_the_block.mixin.feature.horses;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.config.access.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityHorseInteractionMixin {
    @Inject(method = "stopRiding", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$preventUnderwaterDismount(CallbackInfo ci) {
        if (!Configs.server().features.horses.enableSwimming) return;

        LivingEntity self = (LivingEntity) (Object) this;

        if (!(self instanceof PlayerEntity player)) return;
        if (!player.isTouchingWater()) return;

        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof AbstractHorseEntity)) return;

        // Prevent forced underwater eject
        ci.cancel();
    }
}
