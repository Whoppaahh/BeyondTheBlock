package net.ryan.beyond_the_block.mixin.Blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlantBlock.class)
public abstract class PlantBlockMixin {
    @Inject(method = "canPlantOnTop", at = @At("RETURN"), cancellable = true)
    private void allowPlantsOnSnow(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        // If vanilla already allows placement, leave it
        if (cir.getReturnValue()) return;

        cir.setReturnValue(floor.isOf(Blocks.SNOW_BLOCK));
    }
}
