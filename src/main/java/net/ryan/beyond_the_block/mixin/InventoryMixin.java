package net.ryan.beyond_the_block.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.utils.Helpers.ServerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public interface InventoryMixin {
    @Inject(method = "getMaxCountPerStack", at = @At("TAIL"), cancellable = true)
    private void netherLargeStack(CallbackInfoReturnable<Integer> cir) {
        MinecraftServer server = ServerContext.getServer();
        if (server != null && server.getWorld(World.NETHER) != null) {
                cir.setReturnValue(cir.getReturnValue() > 1 ? cir.getReturnValue() * 8 : cir.getReturnValue());
        }
    }

}

