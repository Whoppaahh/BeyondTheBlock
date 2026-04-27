package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.ryan.beyond_the_block.feature.pets.PetCollarAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TameableEntity.class)
public abstract class TameableEntityCollarMixin implements PetCollarAccessor {

    @Unique
    private static final TrackedData<ItemStack> BTB_COLLAR =
            DataTracker.registerData(TameableEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    @Unique
    private static final String BTB_COLLAR_NBT = "btb_collar";

    @Override
    public ItemStack btb$getCollar() {
        return ((TameableEntity)(Object)this).getDataTracker().get(BTB_COLLAR);
    }

    @Override
    public void btb$setCollar(ItemStack stack) {
        ItemStack copy = stack == null ? ItemStack.EMPTY : stack.copy();
        ((TameableEntity)(Object)this).getDataTracker().set(BTB_COLLAR, copy);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void btb$initCollarTracker(CallbackInfo ci) {
        ((TameableEntity)(Object)this).getDataTracker().startTracking(BTB_COLLAR, ItemStack.EMPTY);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void btb$writeCollar(NbtCompound nbt, CallbackInfo ci) {
        ItemStack collar = btb$getCollar();

        if (!collar.isEmpty()) {
            nbt.put(BTB_COLLAR_NBT, collar.writeNbt(new NbtCompound()));
        } else {
            nbt.remove(BTB_COLLAR_NBT);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void btb$readCollar(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(BTB_COLLAR_NBT)) {
            btb$setCollar(ItemStack.fromNbt(nbt.getCompound(BTB_COLLAR_NBT)));
        } else {
            btb$setCollar(ItemStack.EMPTY);
        }
    }
}