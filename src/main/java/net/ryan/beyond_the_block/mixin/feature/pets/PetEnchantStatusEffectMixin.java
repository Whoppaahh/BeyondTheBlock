package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class PetEnchantStatusEffectMixin {

    @Inject(
            method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void btb$petEnchantStatusEffect(
            StatusEffectInstance effect,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if ((Object) this instanceof TameableEntity pet && pet.isTamed()) {
            if (PetEnchantDispatcher.statusEffectApplied(pet, effect)) {
                cir.setReturnValue(false);
            }
        }
    }
}