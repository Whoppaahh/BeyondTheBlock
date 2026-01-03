package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.ryan.beyond_the_block.utils.Naming.NameableMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class BaseNameLivingEntityMixin implements NameableMob {

    /**
     * IMPORTANT:
     * This must be a DIFFERENT TrackedData key from VillagerEntityMixin.
     */
    @Unique
    private static final TrackedData<String> BEYOND_BASE_NAME =
            DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.STRING);

    // ------------------------------------------------------------
    // DataTracker
    // ------------------------------------------------------------

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void beyond$initBaseName(CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;

        // Villagers manage their own base name — skip
        if (self instanceof VillagerEntity) return;

        self.getDataTracker().startTracking(BEYOND_BASE_NAME, "");
    }

    // ------------------------------------------------------------
    // NBT Persistence
    // ------------------------------------------------------------

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void beyond$writeBaseName(NbtCompound nbt, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof VillagerEntity) return;

        nbt.putString("BeyondBaseName", self.getDataTracker().get(BEYOND_BASE_NAME));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void beyond$readBaseName(NbtCompound nbt, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof VillagerEntity) return;

        if (nbt.contains("BeyondBaseName")) {
            self.getDataTracker().set(BEYOND_BASE_NAME, nbt.getString("BeyondBaseName"));
        }
    }

    // ------------------------------------------------------------
    // NameableMob
    // ------------------------------------------------------------

    @Override
    public void beyondTheBlock$setBaseName(String name) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof VillagerEntity) return;

        self.getDataTracker().set(BEYOND_BASE_NAME, name);
    }

    @Override
    public String beyondTheBlock$getBaseName() {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof VillagerEntity) return "";

        return self.getDataTracker().get(BEYOND_BASE_NAME);
    }

}
