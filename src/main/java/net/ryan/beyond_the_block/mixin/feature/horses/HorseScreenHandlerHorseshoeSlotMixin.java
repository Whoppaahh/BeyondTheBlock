package net.ryan.beyond_the_block.mixin.feature.horses;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.ryan.beyond_the_block.feature.horses.HorseEquipmentAccessor;
import net.ryan.beyond_the_block.feature.horses.HorseshoeInventory;
import net.ryan.beyond_the_block.feature.horses.HorseshoeSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseScreenHandler.class)
public abstract class HorseScreenHandlerHorseshoeSlotMixin extends ScreenHandler {

    @Shadow @Final
    private AbstractHorseEntity entity;

    protected HorseScreenHandlerHorseshoeSlotMixin(int syncId) {
        super(null, syncId);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void btb$addHorseshoeSlot(
            int syncId,
            PlayerInventory playerInventory,
            Inventory inventory,
            AbstractHorseEntity entity,
            CallbackInfo ci
    ) {
        if (entity instanceof HorseEquipmentAccessor accessor) {
            this.addSlot(new HorseshoeSlot(
                    new HorseshoeInventory(accessor),
                    0,
                    8,
                    54
            ));
        }
    }
}
