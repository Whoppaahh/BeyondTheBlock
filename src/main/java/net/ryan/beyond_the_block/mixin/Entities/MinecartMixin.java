package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.block.SpeedRailBlock;
import net.ryan.beyond_the_block.utils.Accessors.MinecartSpeedAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public class MinecartMixin implements MinecartSpeedAccessor {
    @Unique
    private double beyond_the_block$customSpeed = 0.0;

    // ===== Accessor implementation =====

    @Override
    public void beyondTheBlock$setCustomSpeed(double speed) {
        this.beyond_the_block$customSpeed = speed;
    }

    @Override
    public double beyondTheBlock$getCustomSpeed() {
        return this.beyond_the_block$customSpeed;
    }

    @Override
    public void beyondTheBlock$clearCustomSpeed() {
        this.beyond_the_block$customSpeed = 0.0;
    }

    @Inject(
            method = "moveOnRail",
            at = @At("TAIL")
    )
    private void beyond_the_block$clampSpeed(
            BlockPos pos,
            BlockState state,
            CallbackInfo ci
    ) {
        if (beyond_the_block$customSpeed <= 0) return;

        AbstractMinecartEntity self = (AbstractMinecartEntity)(Object)this;
        Vec3d v = self.getVelocity();

        Vec3d flat = new Vec3d(v.x, 0, v.z);
        double speed = flat.length();

        if (speed > beyond_the_block$customSpeed) {
            Vec3d clamped = flat.normalize().multiply(beyond_the_block$customSpeed);
            self.setVelocity(clamped.x, v.y, clamped.z);
            self.velocityModified = true;
        }
    }
}