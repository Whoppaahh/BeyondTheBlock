package net.ryan.beyond_the_block.mixin.world;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.event.GuardVillagersEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public abstract class ServerWorldSpawnMixin {

    @Inject(method = "addEntity", at = @At("HEAD"))
    private void beyond_the_block$onSpawnedEvent(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        GuardVillagersEvents.ON_SPAWNED_ENTITY_EVENT.invoker()
                .onSpawned((ServerWorld) (Object) this, entity);
    }
}