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
import net.ryan.beyond_the_block.utils.VillagerNames.VillagerEntityMixinAccessor;
import net.ryan.beyond_the_block.utils.VillagerNames.VillagerNameGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin implements VillagerEntityMixinAccessor {

    @Unique
    private static final TrackedData<String> BEYOND_BASE_NAME =
            DataTracker.registerData(VillagerEntity.class, TrackedDataHandlerRegistry.STRING);

    @Unique
    private double nameVisibilityRange = 8.0;

    @Unique
    private static final Random RANDOM = new Random();

    // ------------------------------------------------------------
    // Data Tracker: Base Name
    // ------------------------------------------------------------

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void beyond$initDataTracker(CallbackInfo ci) {
        ((VillagerEntity)(Object)this).getDataTracker().startTracking(BEYOND_BASE_NAME, "");
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void beyond$writeNameToNbt(NbtCompound nbt, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        nbt.putString("BeyondBaseName", villager.getDataTracker().get(BEYOND_BASE_NAME));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void beyond$readNameFromNbt(NbtCompound nbt, CallbackInfo ci) {
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

    // ------------------------------------------------------------
    // Profession Change = Name Assignment Trigger
    // ------------------------------------------------------------

    @Inject(method = "setVillagerData", at = @At("TAIL"))
    private void beyond$onProfessionChange(VillagerData data, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        if (villager.getWorld().isClient()) return;

        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        ModConfig.VillagerNamesConfig cfg = config.villagerNames;
        if (!cfg.enableNames) return;

        nameVisibilityRange = cfg.nameVisibilityRange;

        // Babies never receive names
        if (villager.isBaby()) {
            villager.setCustomNameVisible(false);
            return;
        }

        VillagerProfession profession = data.getProfession();

        // Config option: Only name employed villagers
        if (cfg.nameOnlyWhenEmployed && profession == VillagerProfession.NONE) {
            villager.setCustomName(null);
            villager.setCustomNameVisible(false);
            return;
        }

        // Perform full naming
        beyond$assignName(villager, profession, cfg);
    }

    // ------------------------------------------------------------
    // Name Assignment
    // ------------------------------------------------------------

    @Unique
    private void beyond$assignName(VillagerEntity villager, VillagerProfession profession, ModConfig.VillagerNamesConfig cfg) {

        // Base name (persisted)
        String baseName = villager.getDataTracker().get(BEYOND_BASE_NAME);
        if (baseName == null || baseName.isBlank()) {
            baseName = VillagerNameGenerator.pickName(cfg.genderMode);
            villager.getDataTracker().set(BEYOND_BASE_NAME, baseName);
        }

        // Profession title
        String profTitle = VillagerNameGenerator.getProfessionTitle(profession);

        // Alliteration only for real professions
        if (cfg.useAlliteration && profession != VillagerProfession.NONE) {
            char letter = Character.toUpperCase(profTitle.charAt("the ".length()));
            List<String> options = VillagerNameGenerator.pickNamesStartingWith(letter, cfg.genderMode);

            if (!options.isEmpty()) {
                baseName = options.get(RANDOM.nextInt(options.size()));
                villager.getDataTracker().set(BEYOND_BASE_NAME, baseName);
            }
        }

        Formatting color = cfg.colouriseNames
                ? VillagerNameGenerator.getProfessionColor(profession)
                : Formatting.WHITE;

        villager.setCustomName(Text.literal(baseName + " " + profTitle).formatted(color));
        villager.setCustomNameVisible(false);
    }

    // ------------------------------------------------------------
    // Visibility Update
    // ------------------------------------------------------------

    @Inject(method = "tick", at = @At("TAIL"))
    private void beyond$updateVisibility(CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity)(Object)this;
        if (villager.getWorld().isClient()) return;
        if (villager.isBaby() || !villager.hasCustomName()) return;

        Box box = villager.getBoundingBox().expand(nameVisibilityRange);
        List<PlayerEntity> players = villager.getWorld()
                .getEntitiesByClass(PlayerEntity.class, box, e -> true);

        villager.setCustomNameVisible(!players.isEmpty());
    }
}
