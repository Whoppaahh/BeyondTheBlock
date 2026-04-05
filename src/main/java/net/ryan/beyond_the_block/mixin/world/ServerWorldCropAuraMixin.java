package net.ryan.beyond_the_block.mixin.world;

import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.feature.world.crops.CropGrowthAuraHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldCropAuraMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void beyond_the_block$tickCropAura(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;
        CropGrowthAuraHandler.tick(world);
    }
}