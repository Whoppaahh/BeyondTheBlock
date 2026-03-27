package net.ryan.beyond_the_block.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.content.blockentity.PlayerVaultBlockEntity;
import net.ryan.beyond_the_block.utils.ReachHelper;
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

    @Inject(method = "getReachDistance", at = @At("RETURN"), cancellable = true)
    private void beyond_the_block$increaseBlockInteractionRange(CallbackInfoReturnable<Float> cir) {
        if(MinecraftClient.getInstance().player == null) return;
        ItemStack stack = MinecraftClient.getInstance().player.getMainHandStack();

        if (stack.isEmpty()) {
            return;
        }

        float bonus = ReachHelper.getReachBonusFloat(MinecraftClient.getInstance().player.getMainHandStack());
        if (bonus > 0.0f) {
            cir.setReturnValue((cir.getReturnValue() + bonus));
        }
    }
}

