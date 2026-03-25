package net.ryan.beyond_the_block.mixin.Blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.Configs;
import net.ryan.beyond_the_block.utils.Helpers.DoubleOpenablesHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin {

    @Inject(method = "onUse", at = @At("TAIL"))
    private void btb$afterToggle(
            BlockState state,
            World world,
            BlockPos pos,
            PlayerEntity player,
            Hand hand,
            BlockHitResult hit,
            CallbackInfoReturnable<ActionResult> cir
    ) {

        if (world.isClient) return;
        if (player == null || player.isSneaking()) return;

        if (!Configs.server().features.openables.enableTrapdoors) return;
        if (!Configs.server().features.openables.enableRecursiveOpening) return;

        DoubleOpenablesHandler.propagateTrapdoors(
                world,
                pos,
                Configs.server().features.openables.recursiveOpeningMaxBlocksDistance
        );
    }
}


