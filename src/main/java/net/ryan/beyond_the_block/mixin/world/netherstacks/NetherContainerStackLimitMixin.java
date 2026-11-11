package net.ryan.beyond_the_block.mixin.world.netherstacks;

import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LockableContainerBlockEntity.class)
public abstract class NetherContainerStackLimitMixin implements Inventory {

    @Override
    public int getMaxCountPerStack() {
        LockableContainerBlockEntity container =
                (LockableContainerBlockEntity) (Object) this;

        World world = container.getWorld();

        if (world != null && world.getRegistryKey() == World.NETHER) {
            return 512;
        }

        return Inventory.MAX_COUNT_PER_STACK;
    }
}