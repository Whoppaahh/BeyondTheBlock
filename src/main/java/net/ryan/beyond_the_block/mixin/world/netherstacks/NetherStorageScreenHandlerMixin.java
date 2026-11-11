package net.ryan.beyond_the_block.mixin.world.netherstacks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import net.ryan.beyond_the_block.utils.helpers.NetherStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class NetherStorageScreenHandlerMixin {

    @Shadow
    @Final
    public DefaultedList<Slot> slots;

    /**
     * @author
     * @reason Allow existing stacks to merge up to their destination slot limit.
     */
    @Overwrite
    public boolean insertItem(
            ItemStack stack,
            int startIndex,
            int endIndex,
            boolean fromLast
    ) {
        boolean bl = false;
        int i = startIndex;

        if (fromLast) {
            i = endIndex - 1;
        }

        // First pass: merge into existing stacks.
        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (fromLast) {
                    if (i < startIndex) break;
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.slots.get(i);
                ItemStack itemStack = slot.getStack();

                if (!itemStack.isEmpty()
                        && ItemStack.canCombine(stack, itemStack)) {

                    // Use the destination slot's actual item limit.
                    int maxCount = slot.getMaxItemCount(itemStack);

                    int j = itemStack.getCount() + stack.getCount();

                    if (j <= maxCount) {
                        stack.setCount(0);
                        itemStack.setCount(j);
                        slot.markDirty();
                        bl = true;

                    } else if (itemStack.getCount() < maxCount) {
                        stack.decrement(maxCount - itemStack.getCount());
                        itemStack.setCount(maxCount);
                        slot.markDirty();
                        bl = true;
                    }
                }

                if (fromLast) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        // Second pass: insert into empty slots.
        if (!stack.isEmpty()) {
            if (fromLast) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (true) {
                if (fromLast) {
                    if (i < startIndex) break;
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.slots.get(i);
                ItemStack itemStack = slot.getStack();

                if (itemStack.isEmpty() && slot.canInsert(stack)) {
                    if (stack.getCount() > slot.getMaxItemCount()) {
                        slot.setStack(stack.split(slot.getMaxItemCount()));
                    } else {
                        slot.setStack(stack.split(stack.getCount()));
                    }

                    slot.markDirty();
                    bl = true;
                    break;
                }

                if (fromLast) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return bl;
    }

    @Inject(
            method = "onSlotClick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void beyondTheBlock$cloneNetherStack(
            int slotIndex,
            int button,
            SlotActionType actionType,
            PlayerEntity player,
            CallbackInfo ci
    ) {
        ScreenHandler handler = (ScreenHandler) (Object) this;

        // Only handle creative middle-click cloning.
        if (actionType != SlotActionType.CLONE
                || !player.getAbilities().creativeMode
                || !handler.getCursorStack().isEmpty()) {
            return;
        }

        // Ensure the clicked slot exists.
        if (slotIndex < 0 || slotIndex >= handler.slots.size()) {
            return;
        }

        Slot slot = handler.getSlot(slotIndex);

        // Only override cloning for Nether storage.
        if (!NetherStorage.isNetherStorage(slot) || !slot.hasStack()) {
            return;
        }

        ItemStack clonedStack = slot.getStack().copy();

        // Use the expanded slot limit rather than the item's vanilla limit.
        clonedStack.setCount(slot.getMaxItemCount(clonedStack));

        handler.setCursorStack(clonedStack);
        handler.sendContentUpdates();

        ci.cancel();
    }
}