package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.block.FurnaceBlock;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.block.ModBlocks;
import net.ryan.beyond_the_block.utils.Glowable;
import net.ryan.beyond_the_block.utils.InvisibleItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin implements InvisibleItemFrame, Glowable {

    @Unique
    private int glowInkUseCount = 0; // track how many times glow ink was applied

    @Unique
    private static final TrackedData<Boolean> INVISIBLE_FLAG =
            DataTracker.registerData(ItemFrameEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> GLOWING =
            DataTracker.registerData(ItemFrameEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Override
    public boolean bt$isGlowing() {
        return ((ItemFrameEntity)(Object)this).getDataTracker().get(GLOWING);
    }

    @Override
    public void bt$setGlowing(boolean value) {
        ((ItemFrameEntity)(Object)this).getDataTracker().set(GLOWING, value);
    }



    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initData(CallbackInfo ci) {
        ((ItemFrameEntity)(Object)this).getDataTracker().startTracking(INVISIBLE_FLAG, false);
        ((ItemFrameEntity)(Object)this).getDataTracker().startTracking(GLOWING, false);
    }

    @Override
    public boolean bt$isInvisibleItemFrame() {
        return ((ItemFrameEntity)(Object)this).getDataTracker().get(INVISIBLE_FLAG);
    }

    @Override
    public void bt$setInvisibleItemFrame(boolean value) {
        ((ItemFrameEntity)(Object)this).getDataTracker().set(INVISIBLE_FLAG, value);
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void bt$toggleInvisible(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);
        ItemFrameEntity self = (ItemFrameEntity) (Object) this;

        if (stack.isOf(Items.GLOW_INK_SAC)) {
            if (!self.getWorld().isClient) {
                ItemStack frameItem = self.getHeldItemStack();
                if(!frameItem.isEmpty()) {
                    frameItem.getOrCreateNbt().putBoolean("Glowing", true);
                    self.setHeldItemStack(frameItem);
                    glowInkUseCount++;
                    if(glowInkUseCount >= 2){
                        bt$setGlowing(true);
                        self.setGlowing(true);
                    }
                    if (!player.getAbilities().creativeMode) stack.decrement(1);
                }
                if(!frameItem.hasNbt()){
                    self.playSound(SoundEvents.ITEM_GLOW_INK_SAC_USE, 1.0F, 1.0F);
                }
            }
            cir.setReturnValue(ActionResult.SUCCESS);
        }
        if (stack.isOf(Items.INK_SAC)) {
            if (!self.getWorld().isClient) {
                ItemStack frameItem = self.getHeldItemStack();
                if(!frameItem.isEmpty() && frameItem.hasNbt()) {
                    frameItem.getNbt().remove("Glowing");
                    self.setHeldItemStack(frameItem);
                    glowInkUseCount = 0;
                    bt$setGlowing(false);
                    self.setGlowing(false);
                    if (!player.getAbilities().creativeMode) stack.decrement(1);
                }
                if(!frameItem.hasNbt()){
                    self.playSound(SoundEvents.ITEM_INK_SAC_USE, 1.0F, 1.0F);
                }
            }

            cir.setReturnValue(ActionResult.SUCCESS);
        }

        if (stack.isOf(Items.PHANTOM_MEMBRANE)) {
            if (!self.getWorld().isClient) {
                bt$setInvisibleItemFrame(!bt$isInvisibleItemFrame());
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
            self.playSound(SoundEvents.ENTITY_PHANTOM_FLAP, 0.4F, 1.2F);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void bt$writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("InvisibleFrame", bt$isInvisibleItemFrame());
        nbt.putBoolean("Glowing", bt$isGlowing());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void bt$readNbt(NbtCompound nbt, CallbackInfo ci) {
        bt$setInvisibleItemFrame(nbt.getBoolean("InvisibleFrame"));
        bt$setGlowing(nbt.getBoolean("Glowing"));
    }
}