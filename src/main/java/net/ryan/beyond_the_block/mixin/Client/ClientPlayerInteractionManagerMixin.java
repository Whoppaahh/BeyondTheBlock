package net.ryan.beyond_the_block.mixin.Client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.block.Entity.PlayerVaultBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    private void onAttackBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.world != null && client.player != null) {
            BlockEntity be = client.world.getBlockEntity(pos);

            if (be instanceof PlayerVaultBlockEntity vault && !vault.isOwner(client.player.getUuid())) {
                // Cancel client-side block break attempt
                cir.setReturnValue(false);
            }
        }
    }
}

