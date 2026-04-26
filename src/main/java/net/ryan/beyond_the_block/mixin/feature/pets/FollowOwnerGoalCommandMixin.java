package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.ryan.beyond_the_block.feature.pets.PetCommandAccessor;
import net.ryan.beyond_the_block.feature.pets.PetCommandState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FollowOwnerGoal.class)
public abstract class FollowOwnerGoalCommandMixin {

    @Shadow @Final
    private TameableEntity tameable;

    @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
    private void btb$canStart(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this.tameable instanceof PetCommandAccessor accessor) {
            PetCommandState state = accessor.btb$getPetCommandState();
            if (state == PetCommandState.WANDER || state == PetCommandState.STAY) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
    private void btb$canContinue(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this.tameable instanceof PetCommandAccessor accessor) {
            PetCommandState state = accessor.btb$getPetCommandState();
            if (state == PetCommandState.WANDER || state == PetCommandState.STAY) {
                cir.setReturnValue(false);
            }
        }
    }
}
