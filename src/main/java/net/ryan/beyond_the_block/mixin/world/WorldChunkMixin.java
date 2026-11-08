package net.ryan.beyond_the_block.mixin.world;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.ryan.beyond_the_block.utils.CampfireProtectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {

    @Shadow
    public abstract World getWorld();

    @Inject(
            method = "setBlockState",
            at = @At("RETURN")
    )
    private void beyond_the_block$updateCampfireIndex(
            BlockPos pos,
            BlockState state,
            boolean moved,
            CallbackInfoReturnable<BlockState> cir
    ) {
        BlockState oldState = cir.getReturnValue();

        if (oldState == null) {
            return;
        }

        World world = this.getWorld();

        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        BlockState newState = ((WorldChunk) (Object) this).getBlockState(pos);

        CampfireProtectionManager.onBlockStateChanged(
                serverWorld,
                pos,
                oldState,
                newState
        );
    }
}
