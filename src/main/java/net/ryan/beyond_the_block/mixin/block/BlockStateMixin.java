package net.ryan.beyond_the_block.mixin.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.utils.Helpers.RestoreManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class BlockStateMixin {

    @Inject(
            method = "onStacksDropped",
            at = @At("HEAD"),
            cancellable = true
    )
    private void beyondtheblock$preventInventoryDrops(
            ServerWorld world, BlockPos pos, ItemStack stack, boolean dropExperience, CallbackInfo ci
    ) {
        if (world.isClient) return;

        // This flag is set only during Creeper explosions
        if (RestoreManager.CREEPER_EXPLODING.get()) {
            ci.cancel(); // <-- THIS stops container inventories
        }
    }
}

