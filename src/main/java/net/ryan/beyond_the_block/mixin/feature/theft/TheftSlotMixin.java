package net.ryan.beyond_the_block.mixin.feature.theft;

import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.feature.theft.TheftDetector;
import net.ryan.beyond_the_block.feature.theft.VillageContainerTagger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slot.class)
public abstract class TheftSlotMixin {

    @Final
    @Shadow
    public Inventory inventory;


    @Inject(method = "onTakeItem", at = @At("HEAD"))
    private void beyond_the_block$onItemTaken(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        if (!(player.world instanceof ServerWorld serverWorld)) return;
        if (!(inventory instanceof BlockEntity blockEntity)) return;

        if (inventory instanceof ChestBlockEntity || inventory instanceof BarrelBlockEntity) {
            BlockPos pos = blockEntity.getPos();
            if (VillageContainerTagger.get(serverWorld).isVillageContainer(pos)) {
                TheftDetector.onItemTaken(serverWorld, pos, player);
            }
        }
    }
}
