package net.ryan.beyond_the_block.mixin.Blocks;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.ModConfig;
import net.ryan.beyond_the_block.utils.Helpers.DoubleOpenablesHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceGateBlock.class)
public abstract class FenceGateBlockMixin {

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

        ModConfig cfg = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if (!cfg.doubleOpenables.enableFenceGates) return;
        if (!cfg.doubleOpenables.enableRecursiveOpening) return;

        DoubleOpenablesHandler.propagateFenceGates(
                world,
                pos,
                cfg.doubleOpenables.recursiveOpeningMaxBlocksDistance
        );
    }
}


