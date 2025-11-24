package net.ryan.beyond_the_block.mixin.Client;

import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DoubleInventory.class)
public interface DoubleInventoryAccessor {
    @Accessor("first")
    Inventory getFirst();

    @Accessor("second")
    Inventory getSecond();
}

