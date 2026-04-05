package net.ryan.beyond_the_block.mixin.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.utils.helpers.BleedingParticleHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "damage", at = @At("RETURN"))
    private void btb$spawnBloodParticles(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (!cir.getReturnValue()) return;
        if (self.getWorld().isClient || amount <= 0) return;
        if (!(self.getWorld() instanceof ServerWorld serverWorld)) return;
        Vec3d attackerVec = source.getAttacker() != null
                ? source.getAttacker().getPos().subtract(self.getPos())
                : Vec3d.ZERO; // for fire, fall, etc.

        BleedingParticleHandler.spawnBloodSplatter(serverWorld, self, attackerVec, amount);
    }
}
