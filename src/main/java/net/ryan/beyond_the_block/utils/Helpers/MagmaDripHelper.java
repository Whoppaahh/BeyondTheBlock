package net.ryan.beyond_the_block.utils.Helpers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.ryan.beyond_the_block.block.ModBlocks;
import net.ryan.beyond_the_block.block.ModdedFluidCauldronBlock;

public class MagmaDripHelper {

    // Call from END_WORLD_TICK
    public static void tick(ServerWorld world) {
        // e.g. every 40 ticks (~2 seconds)
        if (world.getTime() % 40 != 0) return;

        for (PlayerEntity player : world.getPlayers()) {
            BlockPos center = player.getBlockPos();
            int radius = 8;

            BlockPos.Mutable pos = new BlockPos.Mutable();
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    for (int dy = -6; dy <= 6; dy++) {
                        pos.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                        tryDripAt(world, pos);
                    }
                }
            }
        }
    }

    private static void tryDripAt(ServerWorld world, BlockPos dripPos) {
        BlockState dripState = world.getBlockState(dripPos);
        if (!dripState.isOf(Blocks.POINTED_DRIPSTONE)) return;

        if (dripState.get(PointedDripstoneBlock.VERTICAL_DIRECTION) != Direction.DOWN) return;

        // Check EXACT heat source structure:
        // 1 block above = MAGMA BLOCK
        BlockState magmaState = world.getBlockState(dripPos.up());
        if (!magmaState.isOf(Blocks.MAGMA_BLOCK)) return;

        // 2 blocks above = STILL LAVA SOURCE
        BlockState lavaState = world.getBlockState(dripPos.up(2));
        boolean validLava =
                lavaState.getFluidState().isStill() &&
                        lavaState.getFluidState().isOf(net.minecraft.fluid.Fluids.LAVA);
        if (!validLava) return;

        // Now look 1–4 blocks below the dripstone
        for (int i = 1; i <= 4; i++) {
            BlockPos checkPos = dripPos.down(i);
            BlockState state = world.getBlockState(checkPos);

            // Air or replaceable blocks are allowed in the drip path
            if (state.isAir() || state.getMaterial().isReplaceable()) {
                continue; // keep searching
            }

            // Case A — empty vanilla cauldron → start magma level 1
            if (state.isOf(Blocks.CAULDRON)) {
                BlockState newState = ModBlocks.MODDED_FLUID_CAULDRON_BLOCK.getDefaultState()
                        .with(ModdedFluidCauldronBlock.CONTENT, ModdedFluidCauldronBlock.Content.MAGMA)
                        .with(ModdedFluidCauldronBlock.LEVEL, 1);
                world.setBlockState(checkPos, newState, Block.NOTIFY_ALL);
                spawnMagmaDrip(world, dripPos, checkPos);
                return;
            }

            // Case B — existing magma cauldron → increase level
            if (state.getBlock() instanceof ModdedFluidCauldronBlock &&
                    state.get(ModdedFluidCauldronBlock.CONTENT) == ModdedFluidCauldronBlock.Content.MAGMA) {

                int level = state.get(ModdedFluidCauldronBlock.LEVEL);
                if (level < 3) {
                    world.setBlockState(
                            checkPos,
                            state.with(ModdedFluidCauldronBlock.LEVEL, level + 1),
                            Block.NOTIFY_ALL);
                    spawnMagmaDrip(world, dripPos, checkPos);
                }
                return;
            }

            // If we hit ANY other non-passable block → invalid structure
            return;
        }
    }

    private static void spawnMagmaDrip(ServerWorld world, BlockPos dripstonePos, BlockPos cauldronPos) {
        // Drip particle
        double midX = dripstonePos.getX() + 0.5;
        double midY = dripstonePos.getY() - 0.2;
        double midZ = dripstonePos.getZ() + 0.5;

        world.spawnParticles(
                ParticleTypes.DRIPPING_LAVA,
                midX, midY, midZ,
                4, 0.03, 0.05, 0.03, 0.01
        );

        world.playSound(
                null, cauldronPos,
                SoundEvents.BLOCK_LAVA_EXTINGUISH,
                SoundCategory.BLOCKS,
                0.4f, 1.4f
        );
    }
}

