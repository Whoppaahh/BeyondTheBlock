package net.ryan.beyond_the_block.mixin.Entities;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.ryan.beyond_the_block.config.ModConfig;
import net.ryan.beyond_the_block.utils.Helpers.VillagerEntityMixinAccessor;
import net.ryan.beyond_the_block.utils.VillagerNameGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin implements VillagerEntityMixinAccessor{

    @Unique
    private double nameVisibilityRange;
    @Unique
    private static final Random RANDOM = new Random();

    @Unique
    private static final TrackedData<String> BEYOND_BASE_NAME =
            DataTracker.registerData(VillagerEntity.class, TrackedDataHandlerRegistry.STRING);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void beyond$initDataTracker(CallbackInfo ci) {
        ((VillagerEntity)(Object)this).getDataTracker().startTracking(BEYOND_BASE_NAME, "");
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void beyond$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        nbt.putString("BeyondBaseName", villager.getDataTracker().get(BEYOND_BASE_NAME));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void beyond$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        if (nbt.contains("BeyondBaseName")) {
            villager.getDataTracker().set(BEYOND_BASE_NAME, nbt.getString("BeyondBaseName"));
        }
    }

    @Override
    public void beyond$setBaseName(String name) {
        ((VillagerEntity)(Object)this).getDataTracker().set(BEYOND_BASE_NAME, name);
    }

    @Override
    public String beyond$getBaseName() {
        return ((VillagerEntity)(Object)this).getDataTracker().get(BEYOND_BASE_NAME);
    }

    @Inject(method = "initialize", at = @At("TAIL"))
    private void beyond$assignBaseName(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        if (villager.isBaby()) return;

        String base = ((VillagerEntityMixinAccessor) villager).beyond$getBaseName();
        if (base == null || base.isBlank()) {
            ModConfig.VillagerNamesConfig cfg = AutoConfig.getConfigHolder(ModConfig.class).getConfig().villagerNames;
            base = VillagerNameGenerator.pickName(cfg.genderMode);
            ((VillagerEntityMixinAccessor) villager).beyond$setBaseName(base);
        }
    }


    /**
     * Called when villager profession changes.
     * We'll only name adults here.
     */
    @Inject(method = "setVillagerData", at = @At("TAIL"))
    private void beyond$onProfessionChange(VillagerData data, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        if (villager.isBaby() || villager.getWorld().isClient()) return; // ❌ skip babies entirely

        assignVillagerName(villager, data);
    }

    /**
     * Called when a baby villager grows up.
     */
    @Inject(method = "onGrowUp", at = @At("TAIL"))
    private void beyond$onGrowUp(CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        if (villager.getWorld().isClient()) return;

        VillagerData data = villager.getVillagerData();
        assignVillagerName(villager, data);
    }

    /**
     * Handles name visibility per tick.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void beyond$updateNameVisibility(CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        if (villager.getWorld().isClient() || villager.isBaby() || !villager.hasCustomName()) return;

        double range = nameVisibilityRange > 0 ? nameVisibilityRange : 8.0;
        Box checkArea = villager.getBoundingBox().expand(range);
        List<PlayerEntity> nearby = villager.getWorld()
                .getEntitiesByClass(PlayerEntity.class, checkArea, e -> true);

        villager.setCustomNameVisible(!nearby.isEmpty());
    }

    /**
     * Shared logic for name assignment.
     */
    @Unique
    private void assignVillagerName(VillagerEntity villager, VillagerData data) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        ModConfig.VillagerNamesConfig cfg = config.villagerNames;
        if (cfg == null || !cfg.enableNames) return;

        nameVisibilityRange = cfg.nameVisibilityRange;
        String baseName = ((VillagerEntityMixinAccessor) villager).beyond$getBaseName();
        if (baseName == null || baseName.isBlank()) {
            baseName = VillagerNameGenerator.pickName(cfg.genderMode);
            ((VillagerEntityMixinAccessor) villager).beyond$setBaseName(baseName);
        }

        VillagerProfession prof = data.getProfession();

        // Skip 'NONE' (unemployed) → just keep base name
        if (prof == VillagerProfession.NONE) {
            villager.setCustomName(Text.literal(baseName).formatted(Formatting.WHITE));
            villager.setCustomNameVisible(false);
            return;
        }

        // --- Handle Alliteration ---
        char c = VillagerNameGenerator.getProfessionTitle(prof).charAt(0);
        List<String> allit = VillagerNameGenerator.pickNamesStartingWith(c, cfg.genderMode);
        if (cfg.useAlliteration && !allit.isEmpty()) {
            baseName = allit.get(RANDOM.nextInt(allit.size()));
            ((VillagerEntityMixinAccessor) villager).beyond$setBaseName(baseName);
        }

        // --- Profession Title and Color ---
        String title = VillagerNameGenerator.getProfessionTitle(prof);
        Formatting color = VillagerNameGenerator.getProfessionColor(prof);

        villager.setCustomName(Text.literal(baseName + " " + title).formatted(color));
        villager.setCustomNameVisible(false);
    }
}



