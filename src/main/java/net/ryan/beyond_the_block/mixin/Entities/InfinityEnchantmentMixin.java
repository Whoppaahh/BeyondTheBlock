package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.enchantment.MendingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InfinityEnchantment.class)
public abstract class InfinityEnchantmentMixin {
    @Inject(method = "canAccept", at = @At("HEAD"), cancellable = true)
    private void allowMending(Enchantment other, CallbackInfoReturnable<Boolean> cir){
        if(other instanceof MendingEnchantment){
            cir.setReturnValue(true);
        }
    }
}

