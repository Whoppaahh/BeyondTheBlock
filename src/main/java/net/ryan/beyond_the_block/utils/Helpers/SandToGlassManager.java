package net.ryan.beyond_the_block.utils.Helpers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SandToGlassManager {
    private static final Map<BlockPos, QueuedSand> MELTING = new ConcurrentHashMap<>();

    public static class QueuedSand {
        int timeLeft;
        BlockState original;

        public QueuedSand(int ticks, BlockState original) {
            this.timeLeft = ticks;
            this.original = original;
        }
    }

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(SandToGlassManager::tick);
    }

    public static void queueSand(BlockPos pos, BlockState state) {
        pos = pos.toImmutable();
        if (!MELTING.containsKey(pos)) {
            MELTING.put(pos.toImmutable(), new QueuedSand(1, state));
        }
    }

    private static void tick(ServerWorld world) {
        for (Iterator<Map.Entry<BlockPos, QueuedSand>> it = MELTING.entrySet().iterator(); it.hasNext(); ) {
            var entry = it.next();
            BlockPos pos = entry.getKey();
            QueuedSand qs = entry.getValue();

            qs.timeLeft--;

            if (qs.timeLeft <= 0) {
                BlockState current = world.getBlockState(pos);

                if (current.isOf(Blocks.SAND)) {
                    world.setBlockState(pos, Blocks.GLASS.getDefaultState(), 3);
                    world.playSound(null, pos, SoundEvents.BLOCK_GLASS_PLACE,
                            SoundCategory.BLOCKS, 1.0f, 1.0f);
                } else if (current.isOf(Blocks.RED_SAND)) {
                    world.setBlockState(pos, Blocks.TINTED_GLASS.getDefaultState(), 3);
                    world.playSound(null, pos, SoundEvents.BLOCK_GLASS_PLACE,
                            SoundCategory.BLOCKS, 1.0f, 1.0f);
                }

                it.remove(); // only after converting
            }
        }
    }

}

