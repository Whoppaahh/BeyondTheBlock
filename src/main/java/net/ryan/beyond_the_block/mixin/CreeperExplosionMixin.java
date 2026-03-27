package net.ryan.beyond_the_block.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.ryan.beyond_the_block.utils.helpers.RestoreManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public abstract class CreeperExplosionMixin {

    @Shadow
    @Final private World world;
    @Shadow @Final private ObjectArrayList<BlockPos> affectedBlocks;
    @Shadow @Final
    private Entity entity;

    @Inject(method = "affectWorld", at = @At("HEAD"), cancellable = true)
    private void beyondtheblock$affectWorld(boolean particles, CallbackInfo ci) {
        if (world.isClient) return;
        if (!(entity instanceof CreeperEntity)) return;

        RestoreManager.CREEPER_EXPLODING.set(true);

        RestoreManager.saveAndScheduleRestore(world, affectedBlocks);
        affectedBlocks.clear();

        RestoreManager.CREEPER_EXPLODING.remove();
        ci.cancel();
    }
}

