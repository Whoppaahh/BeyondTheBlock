package net.ryan.beyond_the_block.mixin.entity.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.ryan.beyond_the_block.feature.player.mounts.PlayerMountInteractionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerMountInteractionMixin {

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void btb$interact(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        ActionResult result = PlayerMountInteractionHandler.trySneakKick(player, entity);
        if (result != null) {
            cir.setReturnValue(result);
        }
    }
}