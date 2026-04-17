package net.ryan.beyond_the_block.mixin.feature.rails;

import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.client.render.entity.MinecartChainRenderer;
import net.ryan.beyond_the_block.content.block.SpeedRailBlock;
import net.ryan.beyond_the_block.utils.LinkedMinecartComponent;
import net.ryan.beyond_the_block.utils.MinecartCouplerComponent;
import net.ryan.beyond_the_block.utils.MinecartLinkingState;
import net.ryan.beyond_the_block.utils.MinecartTrainUtils;
import net.ryan.beyond_the_block.utils.accessors.MinecartSpeedAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(AbstractMinecartEntity.class)
public abstract class MinecartMixin implements MinecartSpeedAccessor, MinecartLinkAccess {

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

        // Case 1: speed rail sets the stored speed
        if (state.getBlock() instanceof SpeedRailBlock) {
            if (state.get(SpeedRailBlock.POWERED)) {
                int level = state.get(SpeedRailBlock.SPEED_LEVEL);
                this.beyond_the_block$customSpeed = switch (level) {
                    case 1 -> 0.2;
                    case 2 -> 0.4;
                    case 3 -> 0.6;
                    case 4 -> 0.8;
                    default -> this.beyond_the_block$customSpeed;
                };
            } else {
                // unpowered speed rail should behave like vanilla stop rail
                return;
            }
        }

        // Case 2: only preserve stored speed on plain rails
        if (!(state.getBlock() instanceof AbstractRailBlock)) {
            return;
        }

        if (state.isOf(Blocks.POWERED_RAIL) && !state.get(PoweredRailBlock.POWERED)) {
            return; // let vanilla braking happen
        }

        double target = this.beyond_the_block$customSpeed;
        if (target <= 0.0) {
            return;
        }

        Vec3d v = self.getVelocity();
        if (v.lengthSquared() < 1.0E-6) {
            return;
        }

        Vec3d dir = beyond_the_block$getRailDirection(state, v);
        self.setVelocity(dir.x * target, dir.y * target, dir.z * target);
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

        Vec3d dir = switch (shape) {
            case NORTH_SOUTH -> new Vec3d(0.0, 0.0, 1.0);
            case EAST_WEST -> new Vec3d(1.0, 0.0, 0.0);

            case ASCENDING_EAST -> new Vec3d(1.0, 1.0, 0.0);
            case ASCENDING_WEST -> new Vec3d(-1.0, 1.0, 0.0);
            case ASCENDING_NORTH -> new Vec3d(0.0, 1.0, -1.0);
            case ASCENDING_SOUTH -> new Vec3d(0.0, 1.0, 1.0);

            case SOUTH_EAST -> new Vec3d(1.0, 0.0, 1.0);
            case SOUTH_WEST -> new Vec3d(-1.0, 0.0, 1.0);
            case NORTH_WEST -> new Vec3d(-1.0, 0.0, -1.0);
            case NORTH_EAST -> new Vec3d(1.0, 0.0, -1.0);
        };

        dir = dir.normalize();

        if (currentVelocity.lengthSquared() > 1.0E-6 && currentVelocity.dotProduct(dir) < 0.0) {
            dir = dir.multiply(-1.0);
        }

        return dir;
    }
    @Inject(method = "getMaxSpeed", at = @At("RETURN"), cancellable = true)
    private void beyond_the_block$getMaxSpeed(CallbackInfoReturnable<Double> cir) {
        if (beyond_the_block$customSpeed > 0) {
            cir.setReturnValue(Math.max(cir.getReturnValue(), beyond_the_block$customSpeed));
        }
    }
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void beyond_the_block$writeSpeed(NbtCompound nbt, CallbackInfo ci) {
        if (this.beyond_the_block$customSpeed > 0.0) {
            nbt.putDouble("BTBCustomSpeed", this.beyond_the_block$customSpeed);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void beyond_the_block$readSpeed(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("BTBCustomSpeed")) {
            this.beyond_the_block$customSpeed = nbt.getDouble("BTBCustomSpeed");
        }
    }

    @Unique
    private final LinkedMinecartComponent beyond_the_block$link = new LinkedMinecartComponent();

    @Inject(method = "tick", at = @At("TAIL"))
    private void beyond_the_block$tickLink(CallbackInfo ci) {
        AbstractMinecartEntity self = (AbstractMinecartEntity)(Object)this;

        if (self.getWorld().isClient) return;
        if (!beyond_the_block$link.hasLink()) return;

        // Leader election
        UUID selfId = self.getUuid();
        UUID linkedId = beyond_the_block$link.getLinkedCart();

        if (linkedId == null || selfId.compareTo(linkedId) > 0) return;

        MinecartTrainUtils.propagateMomentum(self);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void beyond_the_block$writeCouplers(NbtCompound nbt, CallbackInfo ci) {
        beyond_the_block$couplers.writeNbt(nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void beyond_the_block$readCouplers(NbtCompound nbt, CallbackInfo ci) {
        beyond_the_block$couplers.readNbt(nbt);
    }

    @Inject(
            method = "interact",
            at = @At("HEAD"),
            cancellable = true
    )
    private void beyond_the_block$handleLink(
            PlayerEntity player,
            Hand hand,
            CallbackInfoReturnable<ActionResult> cir
    ) {
        if (!player.isSneaking() || !player.getStackInHand(hand).isEmpty()) return;

        AbstractMinecartEntity self = (AbstractMinecartEntity)(Object)this;

        if (player.getWorld().isClient) {
            cir.setReturnValue(ActionResult.SUCCESS);
            return;
        }

        AbstractMinecartEntity selected = MinecartLinkingState.get(player);

        if (selected == null) {
            MinecartLinkingState.set(player, self);
        } else if (selected != self) {
            beyond_the_block$link.setLinkedCart(selected.getUuid());
            ((MinecartMixin)(Object)selected)
                    .beyond_the_block$link
                    .setLinkedCart(self.getUuid());

            MinecartLinkingState.clear(player);
        }

        cir.setReturnValue(ActionResult.SUCCESS);
    }

    @Inject(
            method = "collidesWith",
            at = @At("HEAD"),
            cancellable = true
    )
    private void beyond_the_block$disableLinkedCollision(
            Entity other,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (other instanceof AbstractMinecartEntity cart &&
                beyond_the_block$link.hasLink() &&
                cart.getUuid().equals(beyond_the_block$link.getLinkedCart())) {
            cir.setReturnValue(false);
        }
    }


    @Unique
    private final MinecartCouplerComponent beyond_the_block$couplers =
            new MinecartCouplerComponent();

    @Override
    public MinecartCouplerComponent beyond_the_block$getCouplers() {
        return beyond_the_block$couplers;
    }

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void beyond_the_block$renderChain(
            AbstractMinecartEntity cart,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider consumers,
            int light,
            CallbackInfo ci
    ) {
        MinecartMixin accessor = (MinecartMixin)(Object) cart;

        if (!accessor.beyond_the_block$hasLink()) return;

        AbstractMinecartEntity other =
                cart.getWorld()
                        .getEntity(accessor.beyond_the_block$getLinkedCart());

        if (!(other instanceof AbstractMinecartEntity)) return;

        Vec3d from = cart.getLerpedPos(tickDelta).add(0, 0.4, 0);
        Vec3d to   = other.getLerpedPos(tickDelta).add(0, 0.4, 0);

        matrices.push();
        matrices.translate(-from.x, -from.y, -from.z);

        MinecartChainRenderer.render(
                matrices,
                consumers,
                Vec3d.ZERO,
                to.subtract(from),
                light
        );

        matrices.pop();
    }
}