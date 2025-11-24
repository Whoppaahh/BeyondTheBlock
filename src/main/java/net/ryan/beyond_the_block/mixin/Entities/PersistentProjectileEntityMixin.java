package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.ryan.beyond_the_block.utils.Helpers.ArrowHitsAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {

    @Inject(method = "onEntityHit", at = @At("HEAD"))
    private void arrowdrops$onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity target = entityHitResult.getEntity();
        if (!(target instanceof LivingEntity living)) return;

        PersistentProjectileEntity self = (PersistentProjectileEntity) (Object) this;

        // 🚫 Ignore Infinity arrows (creative-only pickup)
        if (self.pickupType == PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY) {
            return;
        }

        NbtCompound arrowData = new NbtCompound();
        if (self instanceof SpectralArrowEntity) {
            arrowData.putString("type", "spectral");
        } else  {
            arrowData.putString("type", "normal");
        }

        ((ArrowHitsAccess) living).beyondTheBlock$getArrowHits().add(arrowData);
    }
}
