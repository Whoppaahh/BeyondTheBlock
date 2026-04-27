package net.ryan.beyond_the_block.mixin.feature.horses;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.ryan.beyond_the_block.content.enchantment.horses.HorseEnchantDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityEnchantTickMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void btb$horseEnchantTick(CallbackInfo ci) {
        HorseEnchantDispatcher.tick((AbstractHorseEntity)(Object)this);
    }
}