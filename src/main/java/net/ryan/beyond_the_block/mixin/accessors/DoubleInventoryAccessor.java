package net.ryan.beyond_the_block.mixin.accessors;

import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DoubleInventory.class)
public interface DoubleInventoryAccessor {

    @Accessor("first")
    Inventory beyondTheBlock$getFirst();

    @Accessor("second")
    Inventory beyondTheBlock$getSecond();
}