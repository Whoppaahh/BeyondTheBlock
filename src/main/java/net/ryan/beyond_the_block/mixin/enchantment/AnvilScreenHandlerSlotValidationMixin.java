package net.ryan.beyond_the_block.mixin.enchantment;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.ryan.beyond_the_block.mixin.accessors.ForgingScreenHandlerAccessor;
import net.ryan.beyond_the_block.mixin.accessors.ScreenHandlerAccessor;
import net.ryan.beyond_the_block.utils.AnvilInsertionValidator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerSlotValidationMixin {

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;)V", at = @At("TAIL"))
    private void beyond_the_block$replaceSlotsSimple(int syncId, PlayerInventory playerInventory, CallbackInfo ci) {
        beyond_the_block$replaceInputSlots();
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("TAIL"))
    private void beyond_the_block$replaceSlotsContext(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci) {
        beyond_the_block$replaceInputSlots();
    }

    @Unique
    private void beyond_the_block$replaceInputSlots() {
        AnvilScreenHandler self = (AnvilScreenHandler) (Object) this;
        Inventory input = ((ForgingScreenHandlerAccessor) (ForgingScreenHandler) self).beyond_the_block$getInput();
        List<Slot> slots = ((ScreenHandlerAccessor) self).beyond_the_block$getSlots();

        Slot oldLeft = slots.get(0);
        Slot oldRight = slots.get(1);

        Slot newLeft = new Slot(input, 0, oldLeft.x, oldLeft.y) {
            @Override
            public boolean canInsert(ItemStack stack) {
                ItemStack right = input.getStack(1);
                return AnvilInsertionValidator.isValidLeftInput(stack, right);
            }
        };

        Slot newRight = new Slot(input, 1, oldRight.x, oldRight.y) {
            @Override
            public boolean canInsert(ItemStack stack) {
                ItemStack left = input.getStack(0);
                return AnvilInsertionValidator.isValidRightInput(left, stack);
            }
        };

        newLeft.id = oldLeft.id;
        newRight.id = oldRight.id;

        slots.set(0, newLeft);
        slots.set(1, newRight);
    }
}