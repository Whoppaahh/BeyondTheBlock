package net.ryan.beyond_the_block.mixin;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {

    @Inject(
            method = "canSpawn(Lnet/minecraft/entity/SpawnRestriction$Location;" +
                    "Lnet/minecraft/world/WorldView;" +
                    "Lnet/minecraft/util/math/BlockPos;" +
                    "Lnet/minecraft/entity/EntityType;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void beyond_the_block$denySpawnNearCampfire(
            SpawnRestriction.Location location,
            WorldView world,
            BlockPos pos,
            @Nullable EntityType<?> entityType,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (entityType == null) return;
        if (entityType.getSpawnGroup().isPeaceful()) return;
        if (!(world instanceof World realWorld)) return;
        if (realWorld.isClient()) return;

        int radius = 30;
        int radiusSq = radius * radius;

        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z > radiusSq) continue;

                for (int y = -8; y <= 8; y++) {
                    mutable.set(
                            pos.getX() + x,
                            pos.getY() + y,
                            pos.getZ() + z
                    );

                    BlockState state = realWorld.getBlockState(mutable);
                    if (state.getBlock() instanceof CampfireBlock
                            && state.get(CampfireBlock.LIT)) {
                        cir.setReturnValue(false);
                        return;
                    }
                }
            }
        }
    }
}
