package net.ryan.beyond_the_block.mixin.world.netherstacks;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.network.NetherStorageNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class NetherStorageScreenMixin {

    @Inject(
            method = "onScreenHandlerOpened(Lnet/minecraft/screen/ScreenHandler;)V",
            at = @At("HEAD")
    )
    private void beyondTheBlock$syncNetherStorage(
            ScreenHandler screenHandler,
            CallbackInfo ci
    ) {
        ServerPlayerEntity player =
                (ServerPlayerEntity) (Object) this;

        NetherStorageNetworking.sendStorageSlots(player, screenHandler);
    }
}