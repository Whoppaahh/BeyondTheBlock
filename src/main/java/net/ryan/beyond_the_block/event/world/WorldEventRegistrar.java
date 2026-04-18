package net.ryan.beyond_the_block.event.world;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.ryan.beyond_the_block.content.entity.SheepColours;
import net.ryan.beyond_the_block.content.registry.ModEntities;
import net.ryan.beyond_the_block.feature.blockconversion.BlockConversionHandler;
import net.ryan.beyond_the_block.feature.cauldrons.HoneyDripHelper;
import net.ryan.beyond_the_block.feature.cauldrons.IceConversionHelper;
import net.ryan.beyond_the_block.feature.cauldrons.MagmaDripHelper;
import net.ryan.beyond_the_block.feature.cauldrons.PowderSnowCauldronHelper;
import net.ryan.beyond_the_block.feature.paths.PathSpeedHelper;
import net.ryan.beyond_the_block.utils.helpers.HelmetEnchantmentHandler;
import net.ryan.beyond_the_block.utils.helpers.NightstrideHelper;
import net.ryan.beyond_the_block.utils.helpers.RestoreManager;

public class WorldEventRegistrar {

    public static void register(){
        registerBlockConversions();
        registerServerTickEvents();
    }
    private static void registerBlockConversions() {
        UseBlockCallback.EVENT.register(BlockConversionHandler::handleBlockConversion);

    }


    private static void registerServerTickEvents() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            NightstrideHelper.register();
            HelmetEnchantmentHandler.register();

            RestoreManager.tick(world);
            HoneyDripHelper.tick(world);
            PowderSnowCauldronHelper.tick(world);
            MagmaDripHelper.tick(world);
            IceConversionHelper.tick(world);
        });


        ServerTickEvents.END_SERVER_TICK.register(PathSpeedHelper::tickSpeed);
        ServerEntityEvents.ENTITY_LOAD.register(SheepColours::randomiseColours);
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!(entity instanceof ZombieEntity zombie)) return;

            // Prevent re-processing
            if (zombie.hasVehicle()) return;

            // Optional: only natural-ish zombies
            if (zombie.isPersistent()) return; // skips named / special cases

            // Surface check (optional)
            if (!world.isSkyVisible(zombie.getBlockPos())) return;

            if (world.random.nextFloat() < 0.2F) {

                ZombieHorseEntity horse = world.random.nextFloat() < 0.5F
                        ? ModEntities.WITHER_SKELETON_HORSE.create(world)
                        : ModEntities.WITHER_ZOMBIE_HORSE.create(world);

                if (horse == null) return;

                horse.refreshPositionAndAngles(
                        zombie.getX(),
                        zombie.getY(),
                        zombie.getZ(),
                        zombie.getYaw(),
                        zombie.getPitch()
                );
                horse.setTame(true);
                horse.setPersistent();
                world.spawnEntity(horse);

                zombie.startRiding(horse, true);

            }
        });
    }



}
