package net.ryan.beyond_the_block.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SweetBerryBushBlock.class)
public abstract class SweetBerryBlockMixin {

    @Inject(method = "onEntityCollision", cancellable = true, at = @At("HEAD"))
    private void sneak_through_berries_entityInside(
            BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci
    ) {
        if (entity instanceof PlayerEntity player)  {
            if (player.isSneaking()) {
                ci.cancel();
            }
        }
    }
}
