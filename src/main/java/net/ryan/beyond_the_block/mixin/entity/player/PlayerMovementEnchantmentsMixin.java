package net.ryan.beyond_the_block.mixin.entity.player;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.feature.player.movement.FrozenMomentumHandler;
import net.ryan.beyond_the_block.feature.player.movement.GracefulMovementHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerMovementEnchantmentsMixin {

    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    private void btb$movementSpeed(CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        Float override = FrozenMomentumHandler.getMovementSpeedOverride(player);
        if (override != null) {
            cir.setReturnValue(override);
        }
    }

    @Inject(method = "travel", at = @At("HEAD"))
    private void btb$travel(Vec3d movementInput, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        FrozenMomentumHandler.onTravel(player, movementInput);
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void btb$fallDamage(float fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        Float value = GracefulMovementHandler.getFallDamageOverride(player, fallDistance);
        if (value != null) {
            cir.setReturnValue(value);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void btb$tickMovementEnchantments(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        GracefulMovementHandler.onTick(player);
        // Radiant Aura can also live here if you want one movement/support mixin,
        // but I prefer a separate support mixin.
    }
}