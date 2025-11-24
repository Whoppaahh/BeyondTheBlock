package net.ryan.beyond_the_block.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.ryan.beyond_the_block.BeyondTheBlock;
import net.ryan.beyond_the_block.mixin.Accessors.ExplosionAccessor;
import net.ryan.beyond_the_block.utils.Helpers.RestoreManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Explosion.class)
public abstract class CreeperExplosionMixin {

    @Inject(method = "collectBlocksAndDamageEntities", at = @At("TAIL"))
    private void onCollectBlocks(CallbackInfo ci) {
        Explosion explosion = (Explosion) (Object) this;
        World world = ((ExplosionAccessor) explosion).getWorld();
        Entity source = ((ExplosionAccessor) explosion).getEntity();

        if (world.isClient() || !(source instanceof CreeperEntity)) return;

        List<BlockPos> blocksToSave = explosion.getAffectedBlocks();
        BeyondTheBlock.LOGGER.info("[BeyondTheBlock] Saving {} blocks from Creeper explosion", blocksToSave.size());

        for (BlockPos pos : blocksToSave) {
            if (!world.getBlockState(pos).isAir()) {
                RestoreManager.saveAndScheduleRestore(world, blocksToSave);
            }
        }

        explosion.clearAffectedBlocks(); // Prevent vanilla drops
    }
}

