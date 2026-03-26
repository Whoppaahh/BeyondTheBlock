package net.ryan.beyond_the_block.content.entity;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.BiomeKeys;
import net.ryan.beyond_the_block.content.entity.model.WitherZombieModel;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class ModEntities {

    public static void registerEntities() {
        registerAttributes();
        registerSpawnRestrictions();
        registerSpawns();
    }

    public static final EntityType<CupidArrowEntity> CUPID_ARROW =
            Registry.register(Registry.ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "cupid_arrow"),
                    FabricEntityTypeBuilder.<CupidArrowEntity>create(SpawnGroup.MISC, CupidArrowEntity::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4)
                            .trackedUpdateRate(20)
                            .build());

    public static final EntityType<WitherZombieHorse> WITHER_ZOMBIE_HORSE =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "wither_zombie_horse"),
                    FabricEntityTypeBuilder.createMob()
                            .spawnGroup(SpawnGroup.MONSTER)
                            .entityFactory(WitherZombieHorse::new)
                            .dimensions(EntityDimensions.fixed(1.3965F, 1.6F))
                            .trackRangeBlocks(10)
                            .trackedUpdateRate(3)
                            .build()
            );
    public static final EntityType<WitherSkeletonHorse> WITHER_SKELETON_HORSE =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "wither_skeleton_horse"),
                    FabricEntityTypeBuilder.createMob()
                            .spawnGroup(SpawnGroup.MONSTER)
                            .entityFactory(WitherSkeletonHorse::new)
                            .dimensions(EntityDimensions.fixed(1.3965F, 1.6F))
                            .trackRangeBlocks(10)
                            .trackedUpdateRate(3)
                            .build()
            );

    public static final EntityType<WitherZombie> WITHER_ZOMBIE =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "wither_zombie"),
                    FabricEntityTypeBuilder.createMob()
                            .spawnGroup(SpawnGroup.MONSTER)
                            .entityFactory(WitherZombie::new)
                            .dimensions(EntityDimensions.fixed(0.6F, 1.95F))
                            .build()
            );

    public static final EntityModelLayer WITHER_ZOMBIE_MODEL = new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID, "wither_zombie"), "main");

    public static final EntityType<CobwebProjectileEntity> COBWEB_PROJECTILE =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier(BeyondTheBlock.MOD_ID, "cobweb_projectile"),
                    FabricEntityTypeBuilder.<CobwebProjectileEntity>create(SpawnGroup.MISC, CobwebProjectileEntity::new)
                            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                            .trackable(64, 10)
                            .build()
            );



    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(WITHER_ZOMBIE_HORSE, WitherZombieHorse.createWitherZombieHorseAttributes());
        FabricDefaultAttributeRegistry.register(WITHER_SKELETON_HORSE, WitherSkeletonHorse.createWitherSkeletonHorseAttributes());
        FabricDefaultAttributeRegistry.register(WITHER_ZOMBIE, WitherZombie.createWitherZombieAttributes());

        EntityModelLayerRegistry.registerModelLayer(WITHER_ZOMBIE_MODEL, WitherZombieModel::getTexturedModelData);

    }

    public static void registerSpawnRestrictions() {
        SpawnRestriction.register(
                WITHER_ZOMBIE,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                ModEntities::canMonsterSpawn
        );

        SpawnRestriction.register(
                WITHER_ZOMBIE_HORSE,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                ModEntities::canMonsterSpawn
        );

        SpawnRestriction.register(
                WITHER_SKELETON_HORSE,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                ModEntities::canMonsterSpawn
        );
    }

    public static void registerSpawns() {
        // Wither Zombie spawns in all Overworld biomes
        BiomeModifications.addSpawn(
                BiomeSelectors.foundInOverworld(),
                SpawnGroup.MONSTER,
                WITHER_ZOMBIE,
                100, // spawn weight
                1,   // min group size
                2    // max group size
        );

        // Wither Zombie Horse spawns in plains & savanna biomes
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(
                        BiomeKeys.PLAINS,
                        BiomeKeys.SAVANNA
                ),
                SpawnGroup.MONSTER,
                WITHER_ZOMBIE_HORSE,
                5,
                1,
                1
        );

        // Wither Skeleton Horse spawns in desert
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(
                        BiomeKeys.DESERT
                ),
                SpawnGroup.MONSTER,
                WITHER_SKELETON_HORSE,
                5,
                1,
                1
        );
    }

    public static boolean canMonsterSpawn(EntityType<?> type, WorldAccess world, SpawnReason spawnReason, net.minecraft.util.math.BlockPos pos, Random random) {
        // Spawn on solid blocks with low light (like vanilla monsters)
        return world.getBlockState(pos.down()).isOpaque() && world.getLightLevel(pos) < 8;
    }
}
