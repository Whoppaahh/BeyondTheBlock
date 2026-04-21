package net.ryan.beyond_the_block.content.world.biome;

import net.minecraft.client.sound.MusicType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.ryan.beyond_the_block.content.registry.ModParticles;
import net.ryan.beyond_the_block.content.world.feature.ModPlacedFeatures;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class ModBiomes {

    public static final RegistryKey<Biome> CHERRY_GROVE = RegistryKey.of(
            Registry.BIOME_KEY,
            new Identifier(BeyondTheBlock.MOD_ID, "cherry_grove")
    );

    public static final RegistryKey<Biome> PALE_GARDEN = RegistryKey.of(
            Registry.BIOME_KEY,
            new Identifier(BeyondTheBlock.MOD_ID, "pale_garden")
    );

    public static Biome createCherryGrove() {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        DefaultBiomeFeatures.addFarmAnimals(spawnBuilder);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnBuilder);

        spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.PIG, 5, 2, 4));
        spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.SHEEP, 8, 2, 4));
        spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.RABBIT, 4, 2, 3));

        GenerationSettings.Builder generationBuilder = new GenerationSettings.Builder();

        DefaultBiomeFeatures.addLandCarvers(generationBuilder);
        DefaultBiomeFeatures.addAmethystGeodes(generationBuilder);
        DefaultBiomeFeatures.addDungeons(generationBuilder);
        DefaultBiomeFeatures.addMineables(generationBuilder);
        DefaultBiomeFeatures.addSprings(generationBuilder);
        DefaultBiomeFeatures.addFrozenTopLayer(generationBuilder);

        DefaultBiomeFeatures.addDefaultOres(generationBuilder);
        DefaultBiomeFeatures.addDefaultDisks(generationBuilder);
        DefaultBiomeFeatures.addDefaultFlowers(generationBuilder);
        DefaultBiomeFeatures.addForestGrass(generationBuilder);

        generationBuilder.feature(
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.CHERRY_TREE_PLACED
        );

        return new Biome.Builder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(0.7F)
                .downfall(0.8F)
                .effects(new BiomeEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .skyColor(getSkyColor(0.7F))
                        .grassColor(0xB6DB61)
                        .foliageColor(0xDDA0DD)
                        .particleConfig(new net.minecraft.world.biome.BiomeParticleConfig(ModParticles.CHERRY_LEAF_PARTICLE, 0.005F))
                        .moodSound(BiomeMoodSound.CAVE)
                        .additionsSound(new BiomeAdditionsSound(SoundEvents.MUSIC_OVERWORLD_JUNGLE_AND_FOREST, 0.0111D))
                        .music(MusicType.createIngameMusic(SoundEvents.MUSIC_OVERWORLD_MEADOW))
                        .build())
                .spawnSettings(spawnBuilder.build())
                .generationSettings(generationBuilder.build())
                .build();
    }

    public static Biome createPaleGarden() {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        // Pale Garden should not naturally spawn passive animal mobs
        // Keep hostile/ambient cave spawns
        DefaultBiomeFeatures.addBatsAndMonsters(spawnBuilder);

        // Optional: if you want a more "empty" feel, replace the above with your own custom hostile spawn list

        GenerationSettings.Builder generationBuilder = new GenerationSettings.Builder();

        DefaultBiomeFeatures.addLandCarvers(generationBuilder);
        DefaultBiomeFeatures.addAmethystGeodes(generationBuilder);
        DefaultBiomeFeatures.addDungeons(generationBuilder);
        DefaultBiomeFeatures.addMineables(generationBuilder);
        DefaultBiomeFeatures.addSprings(generationBuilder);
        DefaultBiomeFeatures.addFrozenTopLayer(generationBuilder);

        DefaultBiomeFeatures.addDefaultOres(generationBuilder);
        DefaultBiomeFeatures.addDefaultDisks(generationBuilder);

        // I would avoid normal flower / lush grass helpers here unless you want a less faithful version

        // Pale oak trees
        generationBuilder.feature(
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.PALE_OAK_TREE_PLACED
        );

        // Pale moss blocks / carpet on ground
        generationBuilder.feature(
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.PALE_MOSS_PATCH_PLACED
        );

//        // Hanging pale moss
//        generationBuilder.feature(
//                GenerationStep.Feature.VEGETAL_DECORATION,
//                ModPlacedFeatures.HANGING_PALE_MOSS_PLACED
//        );
//
////        // Eyeblossom if you are backporting it too
//        generationBuilder.feature(
//                GenerationStep.Feature.VEGETAL_DECORATION,
//                ModPlacedFeatures.EYEBLOSSOM_PATCH_PLACED
//        );

        // Optional custom feature for creaking hearts inside pale oaks
        // generationBuilder.feature(
        //         GenerationStep.Feature.VEGETAL_DECORATION,
        //         ModPlacedFeatures.CREAKING_HEART_PLACED
        // );

        return new Biome.Builder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(0.7F)
                .downfall(0.8F)
                .effects(new BiomeEffects.Builder()
                        .waterColor(0x76889D)
                        .waterFogColor(0x5F6E82)
                        .fogColor(0xB7BDBF)
                        .skyColor(getSkyColor(0.7F))
                        .grassColor(0x98A19A)
                        .foliageColor(0xAEB5AE)
                        // Optional subtle drifting particle, if you have one
                        // .particleConfig(new net.minecraft.world.biome.BiomeParticleConfig(ModParticles.PALE_LEAF_PARTICLE, 0.003F))
                        .moodSound(BiomeMoodSound.CAVE)
                        .additionsSound(new BiomeAdditionsSound(SoundEvents.AMBIENT_CAVE, 0.02D))
                        .music(MusicType.createIngameMusic(SoundEvents.MUSIC_OVERWORLD_JUNGLE_AND_FOREST))
                        .build())
                .spawnSettings(spawnBuilder.build())
                .generationSettings(generationBuilder.build())
                .build();
    }

    public static void register() {
        Registry.register(BuiltinRegistries.BIOME, CHERRY_GROVE.getValue(), createCherryGrove());
        Registry.register(BuiltinRegistries.BIOME, PALE_GARDEN.getValue(), createPaleGarden());
    }

    private static int getSkyColor(float temperature) {
        float f = temperature / 3.0F;
        f = Math.max(-1.0F, Math.min(f, 1.0F));
        return MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }
}