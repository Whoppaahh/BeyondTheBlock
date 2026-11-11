package net.ryan.beyond_the_block.mixin.world.netherstacks;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.ryan.beyond_the_block.utils.helpers.NetherStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class NetherSlotMixin {

    @Inject(
            method = "getMaxItemCount(Lnet/minecraft/item/ItemStack;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void beyondTheBlock$increaseNetherStackLimit(
            ItemStack stack,
            CallbackInfoReturnable<Integer> cir
    ) {
        Slot slot = (Slot) (Object) this;

        if (NetherStorage.isNetherStorage(slot)) {
            cir.setReturnValue(NetherStorage.getStackLimit(slot, stack));
        }
    }
}