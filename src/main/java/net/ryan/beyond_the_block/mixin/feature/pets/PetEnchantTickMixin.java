package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class PetEnchantTickMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void btb$petEnchantTick(CallbackInfo ci) {
        if (!((Object) this instanceof TameableEntity pet)) {
            return;
        }

        if (!pet.isTamed()) {
            return;
        }

        if (pet.getWorld().isClient) {
            return;
        }

        PetEnchantDispatcher.tick((LivingEntity) (Object) this);
        if (pet.getOwner() instanceof PlayerEntity owner) {
            PetEnchantDispatcher.ownerDistanceCheck(
                    (LivingEntity) (Object) this,
                    owner,
                    32.0D
            );
        }
    }
}