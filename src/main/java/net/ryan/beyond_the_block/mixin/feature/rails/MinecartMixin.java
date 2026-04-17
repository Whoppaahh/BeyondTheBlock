package net.ryan.beyond_the_block.mixin.feature.rails;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.RailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.content.block.SpeedRailBlock;
import net.ryan.beyond_the_block.utils.CouplerSide;
import net.ryan.beyond_the_block.utils.MinecartCouplerComponent;
import net.ryan.beyond_the_block.utils.MinecartLinkingState;
import net.ryan.beyond_the_block.utils.MinecartTrainUtils;
import net.ryan.beyond_the_block.utils.accessors.MinecartCouplerAccess;
import net.ryan.beyond_the_block.utils.accessors.MinecartSpeedAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(AbstractMinecartEntity.class)
public abstract class MinecartMixin implements MinecartSpeedAccessor, MinecartCouplerAccess {

    @Unique
    private static final TrackedData<java.util.Optional<java.util.UUID>> BTB_FRONT_COUPLER =
            DataTracker.registerData(
                    AbstractMinecartEntity.class,
                    TrackedDataHandlerRegistry.OPTIONAL_UUID
            );

    @Unique
    private static final TrackedData<Optional<UUID>> BTB_BACK_COUPLER =
            DataTracker.registerData(
                    AbstractMinecartEntity.class,
                    TrackedDataHandlerRegistry.OPTIONAL_UUID
            );

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void beyond_the_block$initCouplerTracker(CallbackInfo ci) {
        AbstractMinecartEntity self = (AbstractMinecartEntity)(Object)this;
        self.getDataTracker().startTracking(BTB_FRONT_COUPLER, java.util.Optional.empty());
        self.getDataTracker().startTracking(BTB_BACK_COUPLER, java.util.Optional.empty());
    }

    @Unique
    public void beyond_the_block$syncCouplers() {
        AbstractMinecartEntity self = (AbstractMinecartEntity)(Object)this;
        self.getDataTracker().set(
                BTB_FRONT_COUPLER,
                java.util.Optional.ofNullable(this.beyond_the_block$couplers.get(CouplerSide.FRONT))
        );
        self.getDataTracker().set(
                BTB_BACK_COUPLER,
                java.util.Optional.ofNullable(this.beyond_the_block$couplers.get(CouplerSide.BACK))
        );
    }

    @Unique
    public java.util.UUID beyond_the_block$getSyncedCoupler(CouplerSide side) {
        AbstractMinecartEntity self = (AbstractMinecartEntity)(Object)this;
        return (side == CouplerSide.FRONT
                ? self.getDataTracker().get(BTB_FRONT_COUPLER)
                : self.getDataTracker().get(BTB_BACK_COUPLER))
                .orElse(null);
    }

    @Unique
    private double beyond_the_block$customSpeed = 0.0D;

    @Unique
    private final MinecartCouplerComponent beyond_the_block$couplers = new MinecartCouplerComponent();

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
        this.beyond_the_block$customSpeed = 0.0D;
    }

    @Override
    public MinecartCouplerComponent beyond_the_block$getCouplers() {
        return beyond_the_block$couplers;
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

        if (state.getBlock() instanceof SpeedRailBlock) {
            if (state.get(SpeedRailBlock.POWERED)) {
                int level = state.get(SpeedRailBlock.SPEED_LEVEL);
                this.beyond_the_block$customSpeed = switch (level) {
                    case 1 -> 0.2D;
                    case 2 -> 0.4D;
                    case 3 -> 0.6D;
                    case 4 -> 0.8D;
                    default -> this.beyond_the_block$customSpeed;
                };
            } else {
                return;
            }
        }

        if (!(state.getBlock() instanceof AbstractRailBlock)) {
            return;
        }

        if (state.isOf(Blocks.POWERED_RAIL) && !state.get(PoweredRailBlock.POWERED)) {
            return;
        }

        double target = this.beyond_the_block$customSpeed;
        if (target <= 0.0D) {
            return;
        }

        Vec3d velocity = self.getVelocity();
        if (velocity.lengthSquared() < 1.0E-6D) {
            return;
        }

        Vec3d direction = beyond_the_block$getRailDirection(state, velocity);
        self.setVelocity(direction.x * target, direction.y * target, direction.z * target);
        self.velocityModified = true;
    }

    @Unique
    private static Vec3d beyond_the_block$getRailDirection(BlockState state, Vec3d currentVelocity) {
        RailShape shape;

        if (state.contains(RailBlock.SHAPE)) {
            shape = state.get(RailBlock.SHAPE);
        } else if (state.contains(PoweredRailBlock.SHAPE)) {
            shape = state.get(PoweredRailBlock.SHAPE);
        } else if (state.contains(DetectorRailBlock.SHAPE)) {
            shape = state.get(DetectorRailBlock.SHAPE);
        } else if (state.getBlock() instanceof SpeedRailBlock && state.contains(SpeedRailBlock.SHAPE)) {
            shape = state.get(SpeedRailBlock.SHAPE);
        } else {
            return currentVelocity.normalize();
        }

        Vec3d direction = switch (shape) {
            case NORTH_SOUTH -> new Vec3d(0.0D, 0.0D, 1.0D);
            case EAST_WEST -> new Vec3d(1.0D, 0.0D, 0.0D);
            case ASCENDING_EAST -> new Vec3d(1.0D, 1.0D, 0.0D);
            case ASCENDING_WEST -> new Vec3d(-1.0D, 1.0D, 0.0D);
            case ASCENDING_NORTH -> new Vec3d(0.0D, 1.0D, -1.0D);
            case ASCENDING_SOUTH -> new Vec3d(0.0D, 1.0D, 1.0D);
            case SOUTH_EAST -> new Vec3d(1.0D, 0.0D, 1.0D);
            case SOUTH_WEST -> new Vec3d(-1.0D, 0.0D, 1.0D);
            case NORTH_WEST -> new Vec3d(-1.0D, 0.0D, -1.0D);
            case NORTH_EAST -> new Vec3d(1.0D, 0.0D, -1.0D);
        };

        direction = direction.normalize();

        if (currentVelocity.lengthSquared() > 1.0E-6D && currentVelocity.dotProduct(direction) < 0.0D) {
            direction = direction.multiply(-1.0D);
        }

        return direction;
    }

    @Inject(method = "getMaxSpeed", at = @At("RETURN"), cancellable = true)
    private void beyond_the_block$getMaxSpeed(CallbackInfoReturnable<Double> cir) {
        if (beyond_the_block$customSpeed > 0.0D) {
            cir.setReturnValue(Math.max(cir.getReturnValue(), beyond_the_block$customSpeed));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void beyond_the_block$writeSpeedAndCouplers(NbtCompound nbt, CallbackInfo ci) {
        if (this.beyond_the_block$customSpeed > 0.0D) {
            nbt.putDouble("BTBCustomSpeed", this.beyond_the_block$customSpeed);
        }

        this.beyond_the_block$couplers.writeNbt(nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void beyond_the_block$readSpeedAndCouplers(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("BTBCustomSpeed")) {
            this.beyond_the_block$customSpeed = nbt.getDouble("BTBCustomSpeed");
        }

        this.beyond_the_block$couplers.readNbt(nbt);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void beyond_the_block$tickTrainMomentum(CallbackInfo ci) {
        AbstractMinecartEntity self = (AbstractMinecartEntity) (Object) this;

        if (self.getWorld().isClient) {
            return;
        }

        if (!this.beyond_the_block$couplers.hasAny()) {
            return;
        }

        AbstractMinecartEntity leader = MinecartTrainUtils.getLeader(self);
        if (leader != self) {
            return;
        }

        MinecartTrainUtils.propagateMomentum(self);
    }


    @Inject(method = "collidesWith", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$disableDirectCouplerCollision(
            Entity other,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!(other instanceof AbstractMinecartEntity otherCart)) {
            return;
        }

        UUID otherUuid = otherCart.getUuid();
        if (otherUuid.equals(this.beyond_the_block$couplers.get(CouplerSide.FRONT))
                || otherUuid.equals(this.beyond_the_block$couplers.get(CouplerSide.BACK))) {
            cir.setReturnValue(false);
        }
    }
}