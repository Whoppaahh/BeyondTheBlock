package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.utils.Helpers.EntityTagManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityTagManager {

    @Unique
    private static final TrackedData<Boolean> HIDE_NAME =
            DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> FORCED_LOVE =
            DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> WEARING_HALLOWEEN_COSTUME =
            DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> WEARING_SANTA_HAT =
            DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> HAS_CHRISTMAS_NAME =
            DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);


    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
            self.getDataTracker().startTracking(HIDE_NAME, false);
            self.getDataTracker().startTracking(FORCED_LOVE, false);
            self.getDataTracker().startTracking(WEARING_HALLOWEEN_COSTUME, false);
            self.getDataTracker().startTracking(WEARING_SANTA_HAT, false);
            self.getDataTracker().startTracking(HAS_CHRISTMAS_NAME, false);
        }

    @Override
    public boolean beyondTheBlock$shouldHideName() {
        return ((Entity)(Object)this).getDataTracker().get(HIDE_NAME);
    }

    @Override
    public void beyondTheBlock$setHideName(boolean hide) {
        ((Entity)(Object)this).getDataTracker().set(HIDE_NAME, hide);
    }
    @Override
    public boolean beyondTheBlock$wasLoveForced() {
        return ((Entity)(Object)this).getDataTracker().get(FORCED_LOVE);
    }

    @Override
    public void beyondTheBlock$setWasLoveForced(boolean hide) {
        ((Entity)(Object)this).getDataTracker().set(FORCED_LOVE, hide);
    }
    @Override
    public boolean beyondTheBlock$isHalloweenCostume() {
        return ((Entity)(Object)this).getDataTracker().get(WEARING_HALLOWEEN_COSTUME);
    }

    @Override
    public void beyondTheBlock$setHalloweenCostume(boolean hide) {
        ((Entity)(Object)this).getDataTracker().set(WEARING_HALLOWEEN_COSTUME, hide);
    }
    @Override
    public boolean beyondTheBlock$isWearingSantaHat() {
        return ((Entity)(Object)this).getDataTracker().get(WEARING_SANTA_HAT);
    }

    @Override
    public void beyondTheBlock$setWearingSantaHat(boolean hide) {
        ((Entity)(Object)this).getDataTracker().set(WEARING_SANTA_HAT, hide);
    }
    @Override
    public boolean beyondTheBlock$hasChristmasName() {
        return ((Entity)(Object)this).getDataTracker().get(HAS_CHRISTMAS_NAME);
    }

    @Override
    public void beyondTheBlock$setHasChristmasName(boolean hide) {
        ((Entity)(Object)this).getDataTracker().set(HAS_CHRISTMAS_NAME, hide);
    }

    @Inject(method = "writeNbt", at = @At("RETURN"))
    private void writeCustomTag(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        Entity self = (Entity)(Object)this;
        if (self.getDataTracker().get(HIDE_NAME)) {
            nbt.putBoolean("HideSpecialName", true);
        }
        if (self.getDataTracker().get(FORCED_LOVE)) {
            nbt.putBoolean("ForcedLove", true);
        }
        if (self.getDataTracker().get(WEARING_HALLOWEEN_COSTUME)) {
            nbt.putBoolean("WearingHalloweenCostume", true);
        }
        if (self.getDataTracker().get(WEARING_SANTA_HAT)) {
            nbt.putBoolean("WearingSantaHat", true);
        }
        if (self.getDataTracker().get(HAS_CHRISTMAS_NAME)) {
            nbt.putBoolean("HasChristmasName", true);
        }
    }

    @Inject(method = "readNbt", at = @At("RETURN"))
    private void readCustomTag(NbtCompound nbt, CallbackInfo ci) {

        Entity self = (Entity)(Object)this;

        if (nbt.contains("HideSpecialName")) {
            self.getDataTracker().set(HIDE_NAME, nbt.getBoolean("HideSpecialName"));
        }
        if (nbt.contains("ForcedLove")) {
            self.getDataTracker().set(FORCED_LOVE, nbt.getBoolean("ForcedLove"));
        }
        if (nbt.contains("WearingHalloweenCostume")) {
            self.getDataTracker().set(WEARING_HALLOWEEN_COSTUME, nbt.getBoolean("WearingHalloweenCostume"));
           // BeyondTheBlock.LOGGER.info("Halloween costume is set to true");
        }
        if (nbt.contains("WearingSantaHat")) {
            self.getDataTracker().set(WEARING_SANTA_HAT, nbt.getBoolean("WearingSantaHat"));
        }
        if (nbt.contains("HasChristmasName")) {
            self.getDataTracker().set(HAS_CHRISTMAS_NAME, nbt.getBoolean("HasChristmasName"));
        }
    }

    @Inject(method = "playStepSound", at = @At("HEAD"), cancellable = true)
    private void cancelStepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!((Object) this instanceof PlayerEntity player)) return;

        ItemStack boots = player.getEquippedStack(EquipmentSlot.FEET);
        if (EnchantmentHelper.getLevel(ModEnchantments.SILENT_STEPS, boots) > 0 || player.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            ci.cancel(); // Suppress the footstep sound
        }
    }

    @Inject(method = "spawnSprintingParticles", at = @At("HEAD"), cancellable = true)
    private void onSpawnSprintingParticles(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof PlayerEntity player && player.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            ci.cancel(); // prevent sprinting particles
        }
    }
}

