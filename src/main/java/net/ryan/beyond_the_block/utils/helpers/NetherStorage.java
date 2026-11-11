package net.ryan.beyond_the_block.utils.helpers;

import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.mixin.accessors.DoubleInventoryAccessor;

public final class NetherStorage {

    private NetherStorage() {
    }

    public static boolean isNetherStorage(Inventory inventory) {
        if (inventory instanceof NetherLargeInventory largeInventory) {
            return largeInventory.beyondTheBlock$isNetherStorage();
        }

        if (inventory instanceof LockableContainerBlockEntity container) {
            World world = container.getWorld();

            return world != null
                    && world.getRegistryKey() == World.NETHER;
        }

        if (inventory instanceof DoubleInventory) {
            DoubleInventoryAccessor accessor =
                    (DoubleInventoryAccessor) inventory;

            return isNetherStorage(accessor.beyondTheBlock$getFirst())
                    || isNetherStorage(accessor.beyondTheBlock$getSecond());
        }

        return false;
    }

    public static boolean isNetherStorage(Slot slot) {
        return isNetherStorage(slot.inventory);
    }

    public static int getStackLimit(Slot slot, ItemStack stack) {
        if (!isNetherStorage(slot)) {
            return Math.min(
                    stack.getMaxCount(),
                    slot.inventory.getMaxCountPerStack()
            );
        }

        return stack.getMaxCount() * 8;
    }
}