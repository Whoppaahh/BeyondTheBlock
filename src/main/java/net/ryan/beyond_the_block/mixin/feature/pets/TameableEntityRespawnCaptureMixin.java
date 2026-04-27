package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.feature.pets.PendingPetRespawnState;
import net.ryan.beyond_the_block.feature.pets.PetHomeAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TameableEntity.class)
public abstract class TameableEntityRespawnCaptureMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void btb$capturePetForRespawn(DamageSource source, CallbackInfo ci) {
        TameableEntity pet = (TameableEntity) (Object) this;

        if (pet.getWorld().isClient) {
            return;
        }

        if (!(pet.getWorld() instanceof ServerWorld world)) {
            return;
        }

        if (!pet.isTamed()) {
            return;
        }

        if (!(pet instanceof PetHomeAccessor homeAccessor)) {
            return;
        }

        if (!homeAccessor.btb$hasPetHome()) {
            return;
        }

        PendingPetRespawnState.get(world).add(pet, homeAccessor.btb$getPetHomePos());
    }
}