package net.ryan.beyond_the_block.mixin.world;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.feature.world.ai.GuardAIHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public abstract class ServerWorldAIInjectionMixin {

    @Inject(method = "spawnEntity", at = @At("TAIL"))
    private void beyond_the_block$applySpawnBehaviours(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        GuardAIHandler.apply(entity);
    }
}