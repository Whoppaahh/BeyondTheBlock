package net.ryan.beyond_the_block.mixin;

import net.minecraft.enchantment.InfinityEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InfinityEnchantment.class)
public abstract class InfinityEnchantmentMixin {
    @Inject(method = "getMaxLevel", at = @At("HEAD"), cancellable = true)
    private void injectMaxLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(3); // Infinity I–III
    }
}
