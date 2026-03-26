package net.ryan.beyond_the_block.mixin.entity;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.ryan.beyond_the_block.utils.Glowable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin implements Glowable {

    @Unique
    private static final TrackedData<Boolean> GLOWING =
            DataTracker.registerData(SheepEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void bt$initDataTracker(org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        ((SheepEntity)(Object)this).getDataTracker().startTracking(GLOWING, false);
    }

    @Override
    public boolean bt$isGlowing() {
        return ((SheepEntity)(Object)this).getDataTracker().get(GLOWING);
    }

    @Override
    public void bt$setGlowing(boolean value) {
        ((SheepEntity)(Object)this).getDataTracker().set(GLOWING, value);
    }

    // Save/load NBT
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void bt$writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("Glowing", bt$isGlowing());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void bt$readNbt(NbtCompound nbt, CallbackInfo ci) {
        bt$setGlowing(nbt.getBoolean("Glowing"));
    }


    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void bt$interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);
        SheepEntity self = (SheepEntity)(Object)this;

        // Apply glow
        if (stack.isOf(Items.GLOW_INK_SAC)) {
            if (!self.getWorld().isClient) {
                bt$setGlowing(true);
                if (!player.getAbilities().creativeMode) stack.decrement(1);
            }

            self.playSound(SoundEvents.ITEM_GLOW_INK_SAC_USE, 1.0F, 1.0F);
            cir.setReturnValue(ActionResult.SUCCESS);
            return;
        }

        // Remove glow with water bucket
        if (stack.isOf(Items.WATER_BUCKET)) {
            if (!self.getWorld().isClient) {
                bt$setGlowing(false);
                if (!player.getAbilities().creativeMode) {
                    player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                }
            }
            self.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}