package net.ryan.beyond_the_block.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.utils.Helpers.BleedingParticleHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "stopRiding", at = @At("HEAD"), cancellable = true)
    private void horsebuff$preventUnderwaterDismount(CallbackInfo ci) {
        if (!Configs.server().features.horses.enableSwimming) return;

        LivingEntity self = (LivingEntity) (Object) this;

        if (!(self instanceof PlayerEntity player)) return;
        if (!player.isTouchingWater()) return;

        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof AbstractHorseEntity)) return;

        // Prevent forced underwater eject
        ci.cancel();
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void btb$spawnBloodParticles(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
            LivingEntity self = (LivingEntity) (Object) this;

            if (!self.getWorld().isClient && amount > 0) {
                Vec3d attackerVec = source.getAttacker() != null
                        ? source.getAttacker().getPos().subtract(self.getPos())
                        : new Vec3d(0, 0, 0); // for fire, fall, etc.

                BleedingParticleHandler.spawnBloodSplatter((ServerWorld) self.getWorld(), self.getPos(), attackerVec, amount);
            }
    }

}