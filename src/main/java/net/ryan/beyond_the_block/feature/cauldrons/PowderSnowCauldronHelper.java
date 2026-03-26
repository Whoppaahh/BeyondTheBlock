package net.ryan.beyond_the_block.feature.cauldrons;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class PowderSnowCauldronHelper {

        // Call from END_WORLD_TICK
        public static void tick(ServerWorld world) {
            // 300 ticks ~ 15 seconds
            if (world.getTime() % 300 != 0) return;

            if (!world.isRaining()) return; // includes snowing

            for (PlayerEntity player : world.getPlayers()) {
                BlockPos center = player.getBlockPos();
                int radius = 8;

                BlockPos.Mutable pos = new BlockPos.Mutable();
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        for (int dy = -4; dy <= 4; dy++) {
                            pos.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                            accumulateAt(world, pos);
                        }
                    }
                }
            }
        }

    private static boolean isSnowingAt(ServerWorld world, BlockPos pos) {
        // Must be raining/snowing globally
        if (!world.isRaining()) return false;

        // Must be able to see sky
        if (!world.isSkyVisible(pos)) return false;

        // Biome must be cold enough for snow
        float temp = world.getBiome(pos).value().getTemperature();
        return temp <= 0.15F;
    }


    private static void accumulateAt(ServerWorld world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);

            // Only empty vanilla cauldrons or powder snow cauldrons
            boolean isEmptyCauldron = state.isOf(Blocks.CAULDRON);
            boolean isPowderCauldron = state.isOf(Blocks.POWDER_SNOW_CAULDRON);

            if (!isEmptyCauldron && !isPowderCauldron) return;

            BlockPos above = pos.up();

            // Must be snowing on this position and open to sky
            if (!world.isSkyVisible(above)) return;
            if (!isSnowingAt(world, above)) return;

            float temp = world.getBiome(pos).value().getTemperature();
            if (temp > 0.15f) return; // only in cold enough biomes

            if (isEmptyCauldron) {
                // Start accumulation as powder snow cauldron level 1
                BlockState newState = Blocks.POWDER_SNOW_CAULDRON.getDefaultState()
                        .with(LeveledCauldronBlock.LEVEL, 1);
                world.setBlockState(pos, newState, Block.NOTIFY_ALL);
            } else {
                // Increase existing powder snow level
                int level = state.get(LeveledCauldronBlock.LEVEL);
                if (level >= 3) return;

                BlockState newState = state.with(LeveledCauldronBlock.LEVEL, level + 1);
                world.setBlockState(pos, newState, Block.NOTIFY_ALL);
            }

            // Optional particles + sound
            world.spawnParticles(
                    ParticleTypes.SNOWFLAKE,
                    pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5,
                    3, 0.1, 0.1, 0.1, 0.01
            );
            world.playSound(
                    null, pos,
                    SoundEvents.BLOCK_SNOW_PLACE,
                    SoundCategory.BLOCKS,
                    0.4f, 1.3f
            );
        }
    }

