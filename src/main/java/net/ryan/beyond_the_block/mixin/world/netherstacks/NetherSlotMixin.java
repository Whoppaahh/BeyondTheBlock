package net.ryan.beyond_the_block.mixin.world.netherstacks;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class NetherSlotMixin {

    @Final
    @Shadow
    public Inventory inventory;

    @Inject(method = "getMaxItemCount(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void netherLargeStack(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (inventory == null || stack.isEmpty()) return;

        // Detect Nether world from the inventory owner (if any)
        if (!(inventory instanceof BlockEntity be)) return;

        World world = be.getWorld();
        if (world instanceof ServerWorld serverWorld &&
                serverWorld.getRegistryKey() == World.NETHER) {
            int perItemLimit = stack.getItem().getMaxCount() * 8;
            int containerLimit = inventory.getMaxCountPerStack();
            // Cap by both per-item and container limits
            int maxAllowed = Math.min(perItemLimit, containerLimit);
            BeyondTheBlock.LOGGER.info("Max Allowed: {}", maxAllowed);
            cir.setReturnValue(maxAllowed);
        }
    }
}
