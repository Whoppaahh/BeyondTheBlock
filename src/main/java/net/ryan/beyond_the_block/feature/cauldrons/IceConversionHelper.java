package net.ryan.beyond_the_block.feature.cauldrons;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.content.block.Cauldrons.ModdedFluidCauldronBlock;

import static net.ryan.beyond_the_block.content.block.ModBlocks.MODDED_FLUID_CAULDRON_BLOCK;

public class IceConversionHelper {

    // Frequency: once every 80 ticks (4 seconds)
    private static final int FREEZE_INTERVAL = 80;

    public static void tick(ServerWorld world) {

        if (world.getTime() % FREEZE_INTERVAL != 0) return;

        for (PlayerEntity player : world.getPlayers()) {
            BlockPos center = player.getBlockPos();
            int radius = 8;

            BlockPos.Mutable pos = new BlockPos.Mutable();

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    for (int dy = -6; dy <= 6; dy++) {
                        pos.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                        BlockState state = world.getBlockState(pos);
                        tryFreezeWater(world, pos, state);
                    }
                }
            }
        }
    }


    // Call for each water cauldron during world tick
    public static void tryFreezeWater(ServerWorld world, BlockPos pos, BlockState state) {

        // Only vanilla water cauldron
        if (!state.isOf(Blocks.WATER_CAULDRON)) return;

        int level = state.get(LeveledCauldronBlock.LEVEL);

        // Can't freeze empty or invalid level
        if (level < 1) return;

        // Must be cold enough (vanilla rule)
        float temperature = world.getBiome(pos).value().getTemperature();
        if (temperature > 0.15f) return;

        // Optional: require sky exposure
        if (!world.isSkyVisible(pos.up())) return;

        // Increase freeze progress (use level)
        if (level < 3) {
            world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, level + 1), 3);
            return;
        }

        // Level == 3 → turn into ICE cauldron
        world.setBlockState(
                pos,
                MODDED_FLUID_CAULDRON_BLOCK.getDefaultState()
                        .with(LeveledCauldronBlock.LEVEL, 3)
                        .with(ModdedFluidCauldronBlock.CONTENT, ModdedFluidCauldronBlock.Content.ICE),
                3
        );
    }
}
