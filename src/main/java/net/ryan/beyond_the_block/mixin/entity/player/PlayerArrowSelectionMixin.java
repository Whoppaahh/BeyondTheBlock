package net.ryan.beyond_the_block.mixin.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.feature.player.ranged.PlayerArrowSelectionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerArrowSelectionMixin {

    @Inject(method = "getArrowType", at = @At("HEAD"), cancellable = true)
    private void btb$getArrowType(ItemStack weaponStack, CallbackInfoReturnable<ItemStack> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        ItemStack selected = PlayerArrowSelectionHandler.selectArrow(player, weaponStack);
        if (!selected.isEmpty()) {
            cir.setReturnValue(selected);
        }
    }
}