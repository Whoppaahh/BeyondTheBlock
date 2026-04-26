package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.feature.pets.PetCommandAccessor;
import net.ryan.beyond_the_block.feature.pets.PetCommandState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TameableEntity.class)
public abstract class TameableEntityCommandMixin implements PetCommandAccessor {

    @Unique
    private static final String BTB_PET_COMMAND_NBT = "btb_pet_command";

    @Unique
    private static final TrackedData<Integer> BTB_PET_COMMAND =
            DataTracker.registerData(TameableEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected TameableEntityCommandMixin(EntityType<? extends TameableEntity> entityType, World world) {
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void btb$initDataTracker(CallbackInfo ci) {
        TameableEntity self = (TameableEntity) (Object) this;
        self.getDataTracker().startTracking(BTB_PET_COMMAND, PetCommandState.FOLLOW.ordinal());
    }

    @Override
    public PetCommandState btb$getPetCommandState() {
        TameableEntity self = (TameableEntity) (Object) this;
        return PetCommandState.fromId(self.getDataTracker().get(BTB_PET_COMMAND));
    }

    @Override
    public void btb$setPetCommandState(PetCommandState state) {
        TameableEntity self = (TameableEntity) (Object) this;
        self.getDataTracker().set(BTB_PET_COMMAND, state.ordinal());
        btb$applyCommandState(self, state);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void btb$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt(BTB_PET_COMMAND_NBT, btb$getPetCommandState().ordinal());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void btb$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        TameableEntity self = (TameableEntity) (Object) this;
        PetCommandState state = PetCommandState.FOLLOW;

        if (nbt.contains(BTB_PET_COMMAND_NBT)) {
            state = PetCommandState.fromId(nbt.getInt(BTB_PET_COMMAND_NBT));
        }

        self.getDataTracker().set(BTB_PET_COMMAND, state.ordinal());
        btb$applyCommandState(self, state);
    }


    @Unique
    private void btb$applyCommandState(TameableEntity self, PetCommandState state) {
        if (state == PetCommandState.STAY) {
            self.setSitting(true);
            self.getNavigation().stop();
        } else {
            self.setSitting(false);
        }
    }
}
