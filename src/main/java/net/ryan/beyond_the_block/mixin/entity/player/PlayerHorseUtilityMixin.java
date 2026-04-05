package net.ryan.beyond_the_block.mixin.entity.player;

import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.config.access.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerHorseUtilityMixin {

    @Inject(
            method = "getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F",
            at = @At("RETURN"),
            cancellable = true
    )
    private void btb$removeMountedMiningPenalty(BlockState state, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (!Configs.server().features.horses.removeMiningPenalty) return;
        if (!player.hasVehicle()) return;
        if (!(player.getVehicle() instanceof AbstractHorseEntity)) return;

        cir.setReturnValue(cir.getReturnValue() / 0.2F);
    }
}