package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.SignEditorOpenS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.client.screen.HangingSignEditScreen;
import net.ryan.beyond_the_block.utils.HangingSignClientHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onSignEditorOpen", at = @At("HEAD"), cancellable = true)
    private void beyond_the_block$openHangingSignEditor(SignEditorOpenS2CPacket packet, CallbackInfo ci) {
        if (this.client.world == null) {
            return;
        }

        BlockPos pos = packet.getPos();
        BlockEntity blockEntity = this.client.world.getBlockEntity(pos);

        if (!(blockEntity instanceof SignBlockEntity signBlockEntity)) {
            return;
        }

        if (!HangingSignClientHelper.isHangingSign(signBlockEntity)) {
            return;
        }

        this.client.execute(() -> {
            if (this.client.world == null) {
                return;
            }

            BlockEntity current = this.client.world.getBlockEntity(pos);
            if (current instanceof SignBlockEntity currentSign && HangingSignClientHelper.isHangingSign(currentSign)) {
                this.client.setScreen(new HangingSignEditScreen(currentSign, this.client.shouldFilterText()));
            }
        });

        ci.cancel();
    }
}