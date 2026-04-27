package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.feature.pets.PetHomeAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TameableEntity.class)
public abstract class TameableEntityHomeMixin implements PetHomeAccessor {

    @Unique private static final String BTB_HOME_NBT = "btb_pet_home";
    @Unique private BlockPos btb$homePos = null;

    @Override
    public BlockPos btb$getPetHomePos() {
        return btb$homePos;
    }

    @Override
    public void btb$setPetHomePos(BlockPos pos) {
        btb$homePos = pos == null ? null : pos.toImmutable();
    }

    @Override
    public boolean btb$hasPetHome() {
        return btb$homePos != null;
    }

    @Override
    public void btb$clearPetHome() {
        btb$homePos = null;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void btb$writeHome(NbtCompound nbt, CallbackInfo ci) {
        if (btb$homePos != null) {
            NbtCompound home = new NbtCompound();
            home.putInt("X", btb$homePos.getX());
            home.putInt("Y", btb$homePos.getY());
            home.putInt("Z", btb$homePos.getZ());
            nbt.put(BTB_HOME_NBT, home);
        } else {
            nbt.remove(BTB_HOME_NBT);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void btb$readHome(NbtCompound nbt, CallbackInfo ci) {
        if (!nbt.contains(BTB_HOME_NBT)) {
            btb$homePos = null;
            return;
        }

        NbtCompound home = nbt.getCompound(BTB_HOME_NBT);
        btb$homePos = new BlockPos(
                home.getInt("X"),
                home.getInt("Y"),
                home.getInt("Z")
        );
    }
}