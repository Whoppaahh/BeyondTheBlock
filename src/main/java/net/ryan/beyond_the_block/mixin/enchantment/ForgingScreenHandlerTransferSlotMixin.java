package net.ryan.beyond_the_block.mixin.enchantment;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.ryan.beyond_the_block.mixin.accessors.ForgingScreenHandlerAccessor;
import net.ryan.beyond_the_block.utils.AnvilInsertionValidator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgingScreenHandler.class)
public abstract class ForgingScreenHandlerTransferSlotMixin {

    @Inject(method = "transferSlot", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$blockInvalidAnvilQuickMove(
            PlayerEntity player,
            int index,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        if (!((Object) this instanceof AnvilScreenHandler self)) {
            return;
        }

        // 0 = left input, 1 = right input, 2 = output
        if (index <= 2) {
            return;
        }

        ItemStack source = self.getSlot(index).getStack();
        if (source.isEmpty()) {
            return;
        }

        Inventory input = ((ForgingScreenHandlerAccessor) self).beyond_the_block$getInput();
        ItemStack left = input.getStack(0);
        ItemStack right = input.getStack(1);

        if (!AnvilInsertionValidator.canGoIntoEitherInput(left, right, source)) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}