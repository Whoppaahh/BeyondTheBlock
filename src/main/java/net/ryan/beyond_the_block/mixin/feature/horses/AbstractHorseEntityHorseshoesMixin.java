package net.ryan.beyond_the_block.mixin.feature.horses;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.ryan.beyond_the_block.feature.horses.HorseEquipmentAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityHorseshoesMixin implements HorseEquipmentAccessor {

    @Unique
    private static final TrackedData<ItemStack> BTB_HORSESHOES =
            DataTracker.registerData(AbstractHorseEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    @Unique
    private static final String BTB_HORSESHOES_NBT = "btb_horseshoes";

    @Override
    public ItemStack btb$getHorseshoes() {
        return ((AbstractHorseEntity) (Object) this).getDataTracker().get(BTB_HORSESHOES);
    }

    @Override
    public void btb$setHorseshoes(ItemStack stack) {
        ItemStack copy = stack == null ? ItemStack.EMPTY : stack.copy();
        ((AbstractHorseEntity) (Object) this).getDataTracker().set(BTB_HORSESHOES, copy);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void btb$initHorseshoesTracker(CallbackInfo ci) {
        ((AbstractHorseEntity) (Object) this).getDataTracker().startTracking(BTB_HORSESHOES, ItemStack.EMPTY);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void btb$writeHorseshoes(NbtCompound nbt, CallbackInfo ci) {
        ItemStack shoes = btb$getHorseshoes();

        if (!shoes.isEmpty()) {
            nbt.put(BTB_HORSESHOES_NBT, shoes.writeNbt(new NbtCompound()));
        } else {
            nbt.remove(BTB_HORSESHOES_NBT);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void btb$readHorseshoes(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(BTB_HORSESHOES_NBT)) {
            btb$setHorseshoes(ItemStack.fromNbt(nbt.getCompound(BTB_HORSESHOES_NBT)));
        } else {
            btb$setHorseshoes(ItemStack.EMPTY);
        }
    }
}