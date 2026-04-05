package net.ryan.beyond_the_block.mixin.entity.player;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.enchantment.armour.boots.LeapOfFaithTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerAirJumpTrackerMixin extends LivingEntity implements LeapOfFaithTracker {

    @Unique
    private static final TrackedData<Integer> AIR_JUMP_COUNT =
            DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected PlayerAirJumpTrackerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public TrackedData<Integer> btb$getAirJumpCountKey() {
        return AIR_JUMP_COUNT;
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void btb$initTrackedData(CallbackInfo ci) {
        this.dataTracker.startTracking(AIR_JUMP_COUNT, 0);
    }

    @Inject(method = "jump", at = @At("HEAD"))
    private void btb$onJump(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player.isOnGround()) {
            ((LeapOfFaithTracker) player).setAirJumpCount(1);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void btb$updateAirJumpState(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (player.isOnGround() || player.isTouchingWater() || player.isInLava()) {
            this.dataTracker.set(AIR_JUMP_COUNT, 0);
        }

        if (!player.isOnGround() && ((LeapOfFaithTracker) player).getAirJumpCount() > 0) {
            player.fallDistance = 0.0F;
        }
    }
}