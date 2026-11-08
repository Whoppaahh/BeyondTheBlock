package net.ryan.beyond_the_block.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.WorldView;
import net.ryan.beyond_the_block.utils.CampfireProtectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {

    @Inject(
            method = "canSpawn(Lnet/minecraft/entity/SpawnRestriction$Location;"
                    + "Lnet/minecraft/world/WorldView;"
                    + "Lnet/minecraft/util/math/BlockPos;"
                    + "Lnet/minecraft/entity/EntityType;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void beyond_the_block$denySpawnNearCampfire(
            SpawnRestriction.Location location,
            WorldView world,
            BlockPos pos,
            EntityType<?> entityType,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (entityType == null) {
            return;
        }

        if (entityType.getSpawnGroup().isPeaceful()) {
            return;
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        if (CampfireProtectionManager.isProtected(serverWorld, pos)) {
            cir.setReturnValue(false);
        }
    }
}