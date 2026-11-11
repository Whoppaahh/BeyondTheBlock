package net.ryan.beyond_the_block.mixin.world.netherstacks;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.ryan.beyond_the_block.utils.helpers.NetherLargeInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleInventory.class)
public abstract class SimpleInventoryMixin implements Inventory, NetherLargeInventory {

    @Shadow @Final
    private DefaultedList<ItemStack> stacks;

    @Unique
    private boolean beyondTheBlock$netherStorage;

    @Override
    public void beyondTheBlock$setNetherStorage(boolean value) {
        this.beyondTheBlock$netherStorage = value;
    }

    @Override
    public boolean beyondTheBlock$isNetherStorage() {
        return this.beyondTheBlock$netherStorage;
    }

    @Override
    public int getMaxCountPerStack() {
        return beyondTheBlock$netherStorage
                ? 512
                : Inventory.MAX_COUNT_PER_STACK;
    }

    @Inject(method = "setStack", at = @At("HEAD"), cancellable = true)
    private void beyondTheBlock$allowLargeStacks(
            int slot,
            ItemStack stack,
            CallbackInfo ci
    ) {
        if (!this.beyondTheBlock$netherStorage) {
            return;
        }

        if (slot >= 0 && slot < this.stacks.size()) {
            this.stacks.set(slot, stack);
            this.markDirty();
        }

        ci.cancel();
    }
}