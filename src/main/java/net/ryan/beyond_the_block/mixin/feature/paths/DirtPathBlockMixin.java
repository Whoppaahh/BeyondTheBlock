package net.ryan.beyond_the_block.mixin.feature.paths;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DirtPathBlock.class)
public abstract class DirtPathBlockMixin extends Block {

    protected DirtPathBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
    private void allowFenceOnTop(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState above = world.getBlockState(pos.up());
        if (above.getBlock() instanceof FenceBlock || above.getBlock() instanceof FenceGateBlock || above.getBlock() instanceof WallBlock) {
            cir.setReturnValue(true); // path can survive with fence/wall on top
        }
    }
}

