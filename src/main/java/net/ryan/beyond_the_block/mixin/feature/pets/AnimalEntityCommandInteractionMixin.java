package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.ryan.beyond_the_block.feature.pets.PetCommandAccessor;
import net.ryan.beyond_the_block.feature.pets.PetCommandState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityCommandInteractionMixin {

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void btb$cycleCommandState(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        AnimalEntity self = (AnimalEntity) (Object) this;

        if (!(self instanceof TameableEntity pet)) {
            return;
        }

        if (!(pet instanceof PetCommandAccessor accessor)) {
            return;
        }

        if (!pet.isTamed()) {
            return;
        }

        if (!pet.isOwner(player)) {
            return;
        }

        if (player.isSneaking()) {
            return;
        }

        ItemStack heldStack = player.getStackInHand(hand);
        if (!heldStack.isEmpty()) {
            return;
        }

        if (pet.getWorld().isClient) {
            cir.setReturnValue(ActionResult.SUCCESS);
            return;
        }

        PetCommandState nextState = accessor.btb$getPetCommandState().next();
        accessor.btb$setPetCommandState(nextState);

        cir.setReturnValue(ActionResult.CONSUME);
    }
}