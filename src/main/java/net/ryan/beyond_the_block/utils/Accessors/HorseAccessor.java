package net.ryan.beyond_the_block.utils.Accessors;

import net.minecraft.inventory.SimpleInventory;
import net.ryan.beyond_the_block.feature.horses.StayNearData;

public interface HorseAccessor {
    void beyond$setSaddledPublic(boolean saddled);
    void beyond$setTamedPublic(boolean tamed);
    SimpleInventory beyond$getItemsPublic();

    void beyond$setStayData(StayNearData stayNearData);
    StayNearData beyond$getStayData();
}
