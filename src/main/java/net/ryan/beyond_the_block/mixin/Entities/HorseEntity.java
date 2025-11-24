package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.SimpleInventory;
import net.ryan.beyond_the_block.utils.Accessors.HorseAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractHorseEntity.class)
public abstract class HorseEntity implements HorseAccessor {

    @Shadow
    protected SimpleInventory items;

    // Access the private static HORSE_FLAGS tracked data field
    @Accessor("HORSE_FLAGS")
    public static TrackedData<Byte> getHorseFlags() {
        throw new AssertionError();
    }

    // Access the protected SimpleInventory items field (saddle and armor inventory)
    @Accessor("items")
    public abstract SimpleInventory getItems();

    // Helper method to set saddle flag on this horse instance
    @Override
    public void emeraldEmpire$setSaddledPublic(boolean saddled) {
        AbstractHorseEntity self = (AbstractHorseEntity) (Object) this;
        DataTracker tracker = self.getDataTracker();
        byte flags = tracker.get(getHorseFlags());
        if (saddled) {
            flags |= 0x4; // SADDLED_FLAG
        } else {
            flags &= ~0x4;
        }
        tracker.set(getHorseFlags(), flags);
    }

    // Helper method to set tamed flag on this horse instance
    @Override
    public void emeraldEmpire$setTamedPublic(boolean tamed) {
        AbstractHorseEntity self = (AbstractHorseEntity) (Object) this;
        DataTracker tracker = self.getDataTracker();
        byte flags = tracker.get(getHorseFlags());
        if (tamed) {
            flags |= 0x2; // TAMED_FLAG
        } else {
            flags &= ~0x2;
        }
        tracker.set(getHorseFlags(), flags);
    }
    @Override
    public SimpleInventory emeraldEmpire$getItemsPublic() {
        return this.items;
    }
}

