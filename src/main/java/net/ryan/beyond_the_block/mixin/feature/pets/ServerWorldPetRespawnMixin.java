package net.ryan.beyond_the_block.mixin.feature.pets;

import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.feature.pets.PendingPetRespawnState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldPetRespawnMixin {

    @Unique
    private long btb$lastPetRespawnDay = -1L;

    @Inject(method = "tick", at = @At("TAIL"))
    private void btb$tickPetRespawns(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;

        long timeOfDay = world.getTimeOfDay();
        long day = timeOfDay / 24000L;
        long dayTime = timeOfDay % 24000L;

        // Dawn window. Keeps this from running constantly.
        if (dayTime > 100L) {
            return;
        }

        if (btb$lastPetRespawnDay == day) {
            return;
        }

        btb$lastPetRespawnDay = day;

        PendingPetRespawnState.get(world).respawnReadyPets(world);
    }
}