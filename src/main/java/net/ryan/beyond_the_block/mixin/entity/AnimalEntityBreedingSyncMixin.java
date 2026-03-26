package net.ryan.beyond_the_block.mixin.entity;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.mixin.accessors.AnimalAgeAccessor;
import net.ryan.beyond_the_block.network.ServerNetworking;
import net.ryan.beyond_the_block.network.sync.breeding.BreedingInfoSync;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityBreedingSyncMixin {

    @Unique
    private int btb$ticksSinceLastSync = 0;

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void btb$syncBreedingAge(CallbackInfo ci) {
        AnimalEntity self = (AnimalEntity) (Object) this;
        World world = self.getWorld();
        if (world.isClient()) return;

        btb$ticksSinceLastSync++;
        if (btb$ticksSinceLastSync >= 20) { // once per second
            btb$ticksSinceLastSync = 0;
            int age = ((AnimalAgeAccessor) self).btb$getAge();
            List<ServerPlayerEntity> tracking = net.fabricmc.fabric.api.networking.v1.PlayerLookup.tracking(self).stream().toList();

            for (ServerPlayerEntity p : tracking) {
                BreedingInfoSync.syncBreedingInfo(p, self, age);
            }
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void beyond$allowSecondHorseRider(
            PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir
    ) {
        if (!((Object)this instanceof AbstractHorseEntity horse)) return;

        if (!horse.isTame() || !horse.isSaddled()) return;
        if (player.hasVehicle()) return;

        if (horse.getPassengerList().size() == 1) {
            player.startRiding(horse, true);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}