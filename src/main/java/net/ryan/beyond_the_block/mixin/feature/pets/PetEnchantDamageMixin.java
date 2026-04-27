package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.ryan.beyond_the_block.content.enchantment.pets.PetEnchantDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class PetEnchantDamageMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void btb$petEnchantDamage(
            DamageSource source,
            float amount,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if ((Object) this instanceof TameableEntity pet && pet.isTamed()) {
            if (PetEnchantDispatcher.damage(pet, source, amount)) {
                cir.setReturnValue(false);
            }
        }
    }
}
