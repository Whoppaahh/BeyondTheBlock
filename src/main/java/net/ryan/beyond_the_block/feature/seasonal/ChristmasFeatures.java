package net.ryan.beyond_the_block.feature.seasonal;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.feature.naming.EntityTagManager;

public class ChristmasFeatures {

    public static void register() {

        // Start hook
        HolidayFeatureRegistry.registerStart(HolidayManager.Holiday.CHRISTMAS, () -> ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!world.isClient && entity instanceof VillagerEntity villager) {
                if (villager.getCustomName() == null) {
                    villager.setCustomName(Text.literal("🎄 Festive Villager"));
                    ((EntityTagManager) villager).beyondTheBlock$setHasChristmasName(true);
                }
                villager.equipStack(EquipmentSlot.HEAD, ModBlocks.SANTA_HAT.asItem().getDefaultStack());
                ((EntityTagManager) villager).beyondTheBlock$setHasChristmasName(true);
            }
        }));

        // End hook
        HolidayFeatureRegistry.registerEnd(HolidayManager.Holiday.CHRISTMAS, () -> {
            for (ServerWorld world : HolidayManager.currentServer.getWorlds()) {
                var box = world.getWorldBorder().asVoxelShape().getBoundingBox();

                for (VillagerEntity villager : world.getEntitiesByClass(VillagerEntity.class, box, e -> e instanceof EntityTagManager flag && (flag.beyondTheBlock$isWearingSantaHat() || flag.beyondTheBlock$hasChristmasName()))) {
                    EntityTagManager flag = (EntityTagManager) villager;

                    if (flag.beyondTheBlock$isWearingSantaHat()) villager.equipStack(EquipmentSlot.HEAD, null);
                    if (flag.beyondTheBlock$hasChristmasName()) villager.setCustomName(null);

                    flag.beyondTheBlock$setWearingSantaHat(false);
                    flag.beyondTheBlock$setHasChristmasName(false);
                }
            }
        });
    }
}
