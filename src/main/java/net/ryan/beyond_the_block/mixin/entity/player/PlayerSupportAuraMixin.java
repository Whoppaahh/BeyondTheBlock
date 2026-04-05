package net.ryan.beyond_the_block.mixin.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.feature.player.support.RadiantAuraHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerSupportAuraMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void btb$tickSupportAuras(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        RadiantAuraHandler.onTick(player);
    }
}