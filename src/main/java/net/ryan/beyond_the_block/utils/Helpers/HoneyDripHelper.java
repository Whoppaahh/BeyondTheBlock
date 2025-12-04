package net.ryan.beyond_the_block.utils.Helpers;


import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.ryan.beyond_the_block.block.ModdedFluidCauldronBlock;

import static net.ryan.beyond_the_block.block.ModBlocks.MODDED_FLUID_CAULDRON_BLOCK;

public class HoneyDripHelper {

    public static void tick(ServerWorld world) {

        for (BeehiveBlockEntity hive : HiveTracker.getHives()) {

            if (hive.isRemoved()) continue;

            BlockState hiveState = hive.getCachedState();
            int honeyLevel = hiveState.get(BeehiveBlock.HONEY_LEVEL);

            if (honeyLevel < 5) continue;

            tryFillCauldronBelow(world, hive.getPos());
        }
    }

    private static void tryFillCauldronBelow(ServerWorld world, BlockPos hivePos) {
        BlockPos.Mutable mutable = hivePos.mutableCopy();

        // Up to 4 blocks below the hive
        for (int i = 0; i < 4; i++) {
            mutable.move(Direction.DOWN);
            BlockState state = world.getBlockState(mutable);

            // Empty cauldron → start honey fill
            if (state.isOf(Blocks.CAULDRON)) {
                world.setBlockState(mutable, MODDED_FLUID_CAULDRON_BLOCK.getDefaultState()
                        .with(LeveledCauldronBlock.LEVEL, 1)
                        .with(ModdedFluidCauldronBlock.CONTENT, ModdedFluidCauldronBlock.Content.HONEY), 3);
                return;
            }

            // Existing honey-filled cauldron → increase level
            if (state.getBlock() instanceof ModdedFluidCauldronBlock &&
                    state.get(ModdedFluidCauldronBlock.CONTENT) == ModdedFluidCauldronBlock.Content.HONEY) {

                int level = state.get(LeveledCauldronBlock.LEVEL);
                if (level < 3) {
                    world.setBlockState(mutable, state.with(LeveledCauldronBlock.LEVEL, level + 1), 3);
                }
                return;
            }

            // Stop if solid block blocks dripping
            if (!state.getMaterial().isReplaceable()) return;
        }
    }
}
