package net.ryan.beyond_the_block.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LadderBlock.class)
public abstract class LadderBlockMixin {

    @Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
    private void btb$allowFreeStanding(
            BlockState state,
            WorldView world,
            BlockPos pos,
            CallbackInfoReturnable<Boolean> cir
    ) {

        cir.setReturnValue(true);
    }

    @Unique
    private static boolean isMatchingLadder(WorldView world, BlockPos pos, Direction facing) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof LadderBlock
                && state.get(LadderBlock.FACING) == facing;
    }
}

