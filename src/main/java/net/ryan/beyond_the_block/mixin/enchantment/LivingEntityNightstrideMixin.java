package net.ryan.beyond_the_block.mixin.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityNightstrideMixin {

    @Inject(method = "canWalkOnFluid", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$canWalkOnFluid(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (!(self instanceof PlayerEntity player)) {
            return;
        }

        int level = EnchantmentHelper.getLevel(
                ModEnchantments.NIGHT_STRIDE,
                player.getEquippedStack(EquipmentSlot.LEGS)
        );

        if (level < 3) {
            return;
        }

        if (!state.isOf(Fluids.WATER)) {
            return;
        }
        // Sneaking/diving should disable it immediately
        if (player.isSneaking() || player.isSwimming() || player.isSubmergedInWater()) {
            return;
        }

        cir.setReturnValue(true);
    }

    @Inject(method = "travel", at = @At("HEAD"))
    private void beyond_the_block$nightstrideSurfaceRun(Vec3d movementInput, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (!(self instanceof PlayerEntity player)) {
            return;
        }

        int level = EnchantmentHelper.getLevel(
                ModEnchantments.NIGHT_STRIDE,
                player.getEquippedStack(EquipmentSlot.LEGS)
        );

        if (level < 3) {
            return;
        }

        // If the player wants to dive or is already underwater/swimming, do nothing
        if (player.isSneaking() || player.isSwimming() || player.isSubmergedInWater()) {
            return;
        }

        BlockPos below = player.getBlockPos().down();

        if (!player.getWorld().getFluidState(below).isOf(Fluids.WATER)) {
            return;
        }

        Vec3d vel = player.getVelocity();

        // Keep them on the surface instead of dipping in
        if (vel.y < 0.0D) {
            player.setVelocity(vel.x, 0.0D, vel.z);
        }

        player.setOnGround(true);
        player.fallDistance = 0.0F;
        player.velocityModified = true;
    }
}
