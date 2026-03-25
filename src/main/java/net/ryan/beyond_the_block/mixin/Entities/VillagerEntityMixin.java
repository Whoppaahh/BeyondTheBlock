package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.ryan.beyond_the_block.config.Configs;
import net.ryan.beyond_the_block.utils.Naming.NameEngine;
import net.ryan.beyond_the_block.utils.Naming.NameableMob;
import net.ryan.beyond_the_block.utils.Naming.VillagerNameGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin implements NameableMob {

    @Unique
    private static final TrackedData<String> BEYOND_BASE_NAME =
            DataTracker.registerData(VillagerEntity.class, TrackedDataHandlerRegistry.STRING);

    @Unique
    private double nameVisibilityRange = 8.0;

    // ------------------------------------------------------------
    // Data Tracker: Base Name
    // ------------------------------------------------------------

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTracker(CallbackInfo ci) {
        ((VillagerEntity)(Object)this).getDataTracker().startTracking(BEYOND_BASE_NAME, "");
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeNameToNbt(NbtCompound nbt, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        nbt.putString("BeyondBaseName", villager.getDataTracker().get(BEYOND_BASE_NAME));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readNameFromNbt(NbtCompound nbt, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        if (nbt.contains("BeyondBaseName")) {
            villager.getDataTracker().set(BEYOND_BASE_NAME, nbt.getString("BeyondBaseName"));
        }
    }

    @Override
    public void beyondTheBlock$setBaseName(String name) {
        ((VillagerEntity)(Object)this).getDataTracker().set(BEYOND_BASE_NAME, name);
    }

    @Override
    public String beyondTheBlock$getBaseName() {
        return ((VillagerEntity)(Object)this).getDataTracker().get(BEYOND_BASE_NAME);
    }

    // ------------------------------------------------------------
    // Profession Change = Name Assignment Trigger
    // ------------------------------------------------------------

    @Inject(method = "setVillagerData", at = @At("TAIL"))
    private void onProfessionChange(VillagerData data, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        if (villager.getWorld().isClient()) return;

        if (!Configs.client().visuals.names.enabled) return;

        nameVisibilityRange = Configs.client().visuals.names.visibilityRange;

        // Babies never receive names
        if (villager.isBaby()) {
            villager.setCustomNameVisible(false);
            return;
        }

        VillagerProfession profession = data.getProfession();

        // Config option: Only name employed villagers
        if (Configs.client().visuals.names.onlyWhenEmployed && profession == VillagerProfession.NONE) {
            villager.setCustomName(null);
            villager.setCustomNameVisible(false);
            return;
        }

        // Perform full naming
        NameEngine.assignName(villager, this, VillagerNameGenerator.getProfessionTitle(profession),
                VillagerNameGenerator.getProfessionColor(profession),
                Configs.client(),
                profession != VillagerProfession.NONE);
    }

    // ------------------------------------------------------------
    // Visibility Update
    // ------------------------------------------------------------

    @Inject(method = "tick", at = @At("TAIL"))
    private void updateVisibility(CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        if (villager.getWorld().isClient()) return;
        if (villager.isBaby() || !villager.hasCustomName()) return;

        Box box = villager.getBoundingBox().expand(nameVisibilityRange);
        List<PlayerEntity> players = villager.getWorld()
                .getEntitiesByClass(PlayerEntity.class, box, e -> true);

        villager.setCustomNameVisible(!players.isEmpty());
    }
}
