package net.ryan.beyond_the_block.utils.Helpers;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.utils.SavedBlock;

import java.util.*;

public class RestoreManager {
    public static final ThreadLocal<Boolean> CREEPER_EXPLODING = ThreadLocal.withInitial(() -> false);
    private static final Map<ServerWorld, List<PendingRestore>> scheduled = new HashMap<>();
    private static final int RESTORE_DELAY_TICKS = 200; // 5 seconds (20 ticks * 5)

    public static void saveAndScheduleRestore(World world, List<BlockPos> blocks) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        if(blocks.isEmpty()) return;

        List<SavedBlock> saved = new ArrayList<>();

        for(BlockPos pos : blocks){
            BlockState state = serverWorld.getBlockState(pos);
            if (state.isAir()) continue; // Skip air blocks

            BlockEntity be = serverWorld.getBlockEntity(pos);
            saved.add(new SavedBlock(pos, state, be));

            serverWorld.removeBlock(pos, false);
        }
        blocks.clear();

        for(SavedBlock block : saved){
            scheduled.computeIfAbsent(serverWorld, k -> new ArrayList<>())
                    .add(new PendingRestore(block, RESTORE_DELAY_TICKS));
        }

    }

    // Call this every tick (via ServerTickEvents.END_WORLD_TICK)
    public static void tick(ServerWorld world) {
        List<PendingRestore> list = scheduled.get(world);
        if (list == null || list.isEmpty()) return;

        Iterator<PendingRestore> it = list.iterator();
        while (it.hasNext()) {
            PendingRestore pending = it.next();
            pending.ticks--;
            if (pending.ticks <= 0) {
                pending.saved.restore(world);
                it.remove();
            }
        }
    }
    private static class PendingRestore {
        final SavedBlock saved;
        int ticks;

        PendingRestore(SavedBlock saved, int ticks) {
            this.saved = saved;
            this.ticks = ticks;
        }
    }

}

