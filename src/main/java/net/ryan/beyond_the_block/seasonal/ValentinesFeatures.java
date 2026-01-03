package net.ryan.beyond_the_block.seasonal;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;
import net.ryan.beyond_the_block.utils.Naming.EntityTagManager;

public class ValentinesFeatures {

    public static void register() {

        HolidayFeatureRegistry.registerStart(HolidayManager.Holiday.VALENTINES, () -> {
            // Force animals into love mode immediately
            for (ServerWorld world : HolidayManager.currentServer.getWorlds()) {
                for (AnimalEntity animal : world.getEntitiesByClass(AnimalEntity.class,
                        world.getWorldBorder().asVoxelShape().getBoundingBox(),
                        e -> true)) {
                    if (animal.getLoveTicks() == 0 && animal.getBreedingAge() == 0) {
                        animal.setLoveTicks(600);
                        ((EntityTagManager) animal).beyondTheBlock$setWasLoveForced(true);
                    }
                }
            }
        });

        // End hook
        HolidayFeatureRegistry.registerEnd(HolidayManager.Holiday.VALENTINES, () -> {
            for (ServerWorld world : HolidayManager.currentServer.getWorlds()) {
                for (AnimalEntity animal : world.getEntitiesByClass(AnimalEntity.class,
                        world.getWorldBorder().asVoxelShape().getBoundingBox(),
                        e -> e instanceof EntityTagManager flag && flag.beyondTheBlock$wasLoveForced())) {

                    ((EntityTagManager) animal).beyondTheBlock$setWasLoveForced(false);
                    animal.setLoveTicks(0); // reset love ticks
                }
            }
        });
    }
}
