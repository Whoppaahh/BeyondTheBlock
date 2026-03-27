package net.ryan.beyond_the_block.utils.helpers;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.utils.SavedBlock;

import java.util.*;

import static net.ryan.beyond_the_block.network.packets.PacketIDs.SYNC_RESTORE_PACKET_ID;


public class RestoreManager {
    public static final ThreadLocal<Boolean> CREEPER_EXPLODING = ThreadLocal.withInitial(() -> false);
    private static final Map<ServerWorld, List<PendingRestore>> scheduled = new HashMap<>();

    public static final Set<BlockPos> CLIENT_PROTECTED = new HashSet<>();
    private static final Map<ServerWorld, Set<BlockPos>> protectedPositions = new HashMap<>();
    private static final int BASE_DELAY = 60;
    private static final int MAX_JITTER = 80;
    private static final int RESTORES_PER_TICK = 6;


    private RestoreManager() {}

    public static void saveAndScheduleRestore(World world, List<BlockPos> blocks) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        if(blocks.isEmpty()) return;

        Random random = serverWorld.getRandom();
        List<SavedBlock> saved = new ArrayList<>();

        for(BlockPos pos : blocks){
            BlockState state = serverWorld.getBlockState(pos);
            if (state.isAir()) continue; // Skip air blocks

            BlockEntity be = serverWorld.getBlockEntity(pos);
            saved.add(new SavedBlock(pos, state, be));

            serverWorld.removeBlockEntity(pos);
            serverWorld.setBlockState(
                    pos,
                    Blocks.AIR.getDefaultState(),
                    Block.NOTIFY_LISTENERS | Block.SKIP_DROPS
            );
        }
        blocks.clear();

        Set<BlockPos> protectedSet =
                protectedPositions.computeIfAbsent(serverWorld, w -> new HashSet<>());
        List<PendingRestore> restoreList =
                scheduled.computeIfAbsent(serverWorld, w -> new ArrayList<>());


        for(SavedBlock block : saved){
            protectedSet.add(block.getPos());

            // SEND TO CLIENTS TRACKING THIS CHUNK
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(block.getPos());
            buf.writeBoolean(true); // add

            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                ServerPlayNetworking.send(player, SYNC_RESTORE_PACKET_ID, buf);
            }

            int delay = BASE_DELAY + random.nextInt(MAX_JITTER);
            restoreList.add(new PendingRestore(block, delay));

        }

    }

    // Call this every tick (via ServerTickEvents.END_WORLD_TICK)
    public static void tick(ServerWorld world) {
        List<PendingRestore> list = scheduled.get(world);
        if (list == null || list.isEmpty()) return;

        int restored = 0;
        Iterator<PendingRestore> it = list.iterator();
        while (it.hasNext() && restored < RESTORES_PER_TICK) {
            PendingRestore pending = it.next();
            pending.ticks--;
            if (pending.ticks <= 0) {
                pending.saved.restore(world);
                protectedPositions.getOrDefault(world, Set.of())
                                .remove(pending.saved.getPos());
                // notify client
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(pending.saved.getPos());
                buf.writeBoolean(false); // remove

                for (ServerPlayerEntity player : world.getPlayers()) {
                    ServerPlayNetworking.send(player, SYNC_RESTORE_PACKET_ID, buf);
                }
                it.remove();
                restored++;
            }
        }
    }

    public static boolean isProtectedClient(BlockPos pos) {
        return CLIENT_PROTECTED.contains(pos);
    }

    public static boolean isProtected(ServerWorld world, BlockPos pos) {
        return protectedPositions
                .getOrDefault(world, Set.of())
                .contains(pos);
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

