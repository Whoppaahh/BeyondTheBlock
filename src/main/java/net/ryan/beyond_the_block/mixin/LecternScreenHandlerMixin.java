package net.ryan.beyond_the_block.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.mixin.accessors.LecternScreenHandlerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternScreenHandler.class)
public class LecternScreenHandlerMixin {

    @Inject(method = "onButtonClick", at = @At("HEAD"), cancellable = true)
    private void onTakeBook(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir) {
        if (id == 3) { // TAKE_BOOK_BUTTON_ID
            System.out.println("[DEBUG] Take Book button clicked!");
            LecternScreenHandler self = (LecternScreenHandler) (Object) this;
            ItemStack stack = ((LecternScreenHandlerAccessor) self).getInventory().getStack(0);

            if (isRiddleBook(stack)) {
                // Give a *copy* of the book instead of removing it
                System.out.println("[DEBUG] It's a riddle book. Giving a copy...");
                ItemStack copy = stack.copy();
                if(!player.getInventory().contains(copy)) {
                    if (!player.getInventory().insertStack(copy)) {
                        player.dropItem(copy, false);
                    }
                }
                // Prevent the default behavior (removing the book)
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    serverPlayer.closeHandledScreen();
                }
                cir.setReturnValue(true);
            }
        }
    }


    @Unique
    private boolean isRiddleBook(ItemStack stack) {
        if (stack.getItem() instanceof WrittenBookItem) {
            NbtCompound tag = stack.getNbt();
            return tag != null && "Riddle".equals(tag.getString("title")) && "The Ancients".equals(tag.getString("author"));
        }
        return false;
    }
}

