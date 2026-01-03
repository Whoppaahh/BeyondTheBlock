package net.ryan.beyond_the_block.utils.Helpers;

import net.minecraft.block.*;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.ryan.beyond_the_block.block.Cauldrons.ModdedFluidCauldronBlock;

import java.util.List;

import static net.ryan.beyond_the_block.block.ModBlocks.MODDED_FLUID_CAULDRON_BLOCK;

public class HoneyDripHelper {
    private static int dripCount = 0;

    public static void tick(ServerWorld world) {

        // Snapshot to prevent concurrent modification
        for (BlockPos hivePos : List.copyOf(HiveTracker.getHives())) {

            BlockState hiveState = world.getBlockState(hivePos);

            // If block removed, not a beehive anymore → untrack
            if (!(hiveState.getBlock() instanceof BeehiveBlock)) {
                HiveTracker.remove(hivePos);
                continue;
            }

            // Ensure valid block entity
            if (!(world.getBlockEntity(hivePos) instanceof BeehiveBlockEntity)) {
                HiveTracker.remove(hivePos);
                continue;
            }

            // Ensure honey level exists
            if (!hiveState.contains(BeehiveBlock.HONEY_LEVEL)) continue;
            int honeyLevel = hiveState.get(BeehiveBlock.HONEY_LEVEL);

            if (honeyLevel <= 0) continue;

            // Attempt dripping
            if (tryFillCauldronBelow(world, hivePos, honeyLevel)) {
                dripCount++;
                if(dripCount >= 3) {
                    // Only reduce level on successful drip
                    world.setBlockState(
                            hivePos,
                            hiveState.with(BeehiveBlock.HONEY_LEVEL, honeyLevel - 1),
                            Block.NOTIFY_ALL
                    );
                    dripCount = 0;
                }
            }
        }
    }

    private static boolean tryFillCauldronBelow(ServerWorld world, BlockPos hivePos, int honeyLevel) {

        BlockPos.Mutable mutable = hivePos.mutableCopy();

        for (int i = 0; i < 4; i++) {
            mutable.move(Direction.DOWN);
            BlockState state = world.getBlockState(mutable);

            // === Empty vanilla cauldron ===
            if (state.isOf(Blocks.CAULDRON)) {
                world.setBlockState(
                        mutable,
                        MODDED_FLUID_CAULDRON_BLOCK.getDefaultState()
                                .with(LeveledCauldronBlock.LEVEL, 1)
                                .with(ModdedFluidCauldronBlock.CONTENT, ModdedFluidCauldronBlock.Content.HONEY),
                        Block.NOTIFY_ALL
                );
                return true;
            }

            // === Existing honey cauldron ===
            if (state.getBlock() instanceof ModdedFluidCauldronBlock &&
                    state.get(ModdedFluidCauldronBlock.CONTENT) == ModdedFluidCauldronBlock.Content.HONEY) {

                int level = state.get(LeveledCauldronBlock.LEVEL);

                if (level < 3) {
                    world.setBlockState(
                            mutable,
                            state.with(LeveledCauldronBlock.LEVEL, level + 1),
                            Block.NOTIFY_ALL
                    );
                }
                return true;
            }

            // === Drip blocked by solid block ===
            if (!state.getMaterial().isReplaceable()) return false;
        }

        return false;
    }
}
