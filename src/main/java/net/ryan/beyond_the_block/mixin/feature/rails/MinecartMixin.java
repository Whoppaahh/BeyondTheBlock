package net.ryan.beyond_the_block.mixin.feature.rails;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.content.block.SpeedRailBlock;
import net.ryan.beyond_the_block.utils.accessors.MinecartSpeedAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public abstract class MinecartMixin implements MinecartSpeedAccessor {

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

    @Redirect(
            method = "moveOnRail",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z",
                    ordinal = 0
            )
    )
    private boolean beyond_the_block$poweredCheck(BlockState state, Block block) {
        return state.isOf(block) || state.getBlock() instanceof SpeedRailBlock;
    }



    @Inject(method = "moveOnRail", at = @At("RETURN"))
    private void beyond_the_block$applySpeed(BlockPos pos, BlockState state, CallbackInfo ci) {
        AbstractMinecartEntity self = (AbstractMinecartEntity) (Object) this;

        if (state.getBlock() instanceof SpeedRailBlock && state.get(SpeedRailBlock.POWERED)) {
            int level = state.get(SpeedRailBlock.SPEED_LEVEL);
            beyond_the_block$customSpeed = switch (level) {
                case 1 -> 0.2;
                case 2 -> 0.4;
                case 3 -> 0.6;
                case 4 -> 0.8;
                default -> 0.0;
            };

            Vec3d v = self.getVelocity();
            Vec3d flat = new Vec3d(v.x, 0, v.z);

            double target = beyond_the_block$customSpeed;
            if (target == 0) {
                if (flat.lengthSquared() > 1e-6) {
                    self.setVelocity(0, v.y, 0);
                    self.velocityModified = true;
                }
                return;
            }

            double speed = flat.length();
            if (speed < 1.0E-4) return; // let vanilla push happen


            Vec3d dir = flat.normalize();
            if (speed < target) {
                double newSpeed = Math.min(speed + 0.08, target);
                Vec3d boosted = dir.multiply(newSpeed);
                self.setVelocity(boosted.x, v.y, boosted.z);
                self.velocityModified = true;
            }
        }
    }

    @Inject(method = "getMaxSpeed", at = @At("RETURN"), cancellable = true)
    private void beyond_the_block$getMaxSpeed(CallbackInfoReturnable<Double> cir) {
        if (beyond_the_block$customSpeed > 0) {
            cir.setReturnValue(Math.max(cir.getReturnValue(), beyond_the_block$customSpeed));
        }
    }
}