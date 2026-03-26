package net.ryan.beyond_the_block.utils.Helpers;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.config.access.Configs;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public final class DoubleOpenablesHandler {

    private DoubleOpenablesHandler() {}

    /* ============================================================
       UseBlockCallback — DOORS ONLY
       ============================================================ */

    public static ActionResult onUse(
            PlayerEntity player,
            World world,
            Hand hand,
            BlockHitResult hit
    ) {
        if (player == null) return ActionResult.PASS;
        if (player.isSneaking()) return ActionResult.PASS;
        if (world.isClient) return ActionResult.PASS;

        BlockPos pos = hit.getBlockPos();
        BlockState state = world.getBlockState(pos);


        if (Configs.server().features.openables.enableModIncompatibilityCheck) {
            if (FabricLoader.getInstance().isModLoaded("quark")) {
                BeyondTheBlock.LOGGER.warn("Quark detected. Disable one to avoid duplicate behavior.");
            }
            if (FabricLoader.getInstance().isModLoaded("doubledoors")
                    || FabricLoader.getInstance().isModLoaded("double_doors")) {
                BeyondTheBlock.LOGGER.warn("Double Doors mod detected. Disable one to avoid duplicate behavior.");
            }
        }

        if (!Configs.server().features.openables.enableDoors) return ActionResult.PASS;
        if (!(state.getBlock() instanceof DoorBlock)) return ActionResult.PASS;

        // Drive doors from LOWER only (matches vanilla + Serilum)
        if (state.get(DoorBlock.HALF) != DoubleBlockHalf.LOWER) {
            return ActionResult.PASS;
        }

        boolean originalOpen = state.get(Properties.OPEN);

        if (Configs.server().features.openables.enableRecursiveOpening) {
            toggleDoorRecursive(
                    world,
                    pos,
                    state,
                    originalOpen,
                    0,
                    Configs.server().features.openables.recursiveOpeningMaxBlocksDistance
            );
        } else {
            toggle(world, pos, state);
        }

        // Fully handled — suppress vanilla
        return ActionResult.CONSUME;
    }

    /* ============================================================
       Trapdoors — called from TrapdoorBlockMixin @At("TAIL")
       ============================================================ */

    public static void propagateTrapdoors(World world, BlockPos origin, int maxDistance) {
        if (world.isClient) return;

        BlockState originNow = world.getBlockState(origin);
        if (!(originNow.getBlock() instanceof TrapdoorBlock)) return;

        // POST-toggle value (vanilla already changed it before our TAIL inject runs)
        boolean targetOpen = originNow.get(Properties.OPEN);

        Direction.Axis requiredAxis = originNow.get(TrapdoorBlock.FACING).getAxis();

        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.add(origin);
        visited.add(origin);

        while (!queue.isEmpty()) {
            BlockPos pos = queue.removeFirst();
            if (manhattan(origin, pos) > maxDistance) continue;

            BlockState s = world.getBlockState(pos);
            if (!(s.getBlock() instanceof TrapdoorBlock)) continue;

            if (s.get(TrapdoorBlock.FACING).getAxis() != requiredAxis) continue;

            // Skip origin; toggle any connected trapdoor not matching the origin's new state
            if (!pos.equals(origin) && s.get(Properties.OPEN) != targetOpen) {
                toggle(world, pos, s);
            }

            for (Direction dir : Direction.values()) {
                BlockPos next = pos.offset(dir);
                if (visited.add(next)) queue.add(next);
            }
        }
    }

    public static void propagateFenceGates(World world, BlockPos origin, int maxDistance) {
        if (world.isClient) return;

        BlockState originNow = world.getBlockState(origin);
        if (!(originNow.getBlock() instanceof FenceGateBlock)) return;

        // POST-toggle value
        boolean targetOpen = originNow.get(Properties.OPEN);

        Direction.Axis requiredAxis = originNow.get(FenceGateBlock.FACING).getAxis();

        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.add(origin);
        visited.add(origin);

        while (!queue.isEmpty()) {
            BlockPos pos = queue.removeFirst();
            if (manhattan(origin, pos) > maxDistance) continue;

            BlockState s = world.getBlockState(pos);
            if (!(s.getBlock() instanceof FenceGateBlock)) continue;

            if (s.get(FenceGateBlock.FACING).getAxis() != requiredAxis) continue;

            if (!pos.equals(origin) && s.get(Properties.OPEN) != targetOpen) {
                toggle(world, pos, s);
            }

            for (Direction dir : Direction.values()) {
                BlockPos next = pos.offset(dir);
                if (visited.add(next)) queue.add(next);
            }
        }
    }


    /* ============================================================
       Door recursion
       ============================================================ */

    private static void toggleDoorRecursive(
            World world,
            BlockPos pos,
            BlockState state,
            boolean originalOpen,
            int depth,
            int maxDepth
    ) {
        if (depth >= maxDepth) return;
        if (!state.contains(Properties.OPEN)) return;
        if (state.get(Properties.OPEN) != originalOpen) return;

        toggle(world, pos, state);

        Direction facing = state.get(DoorBlock.FACING);
        DoorHinge hinge = state.get(DoorBlock.HINGE);

        Direction step = (hinge == DoorHinge.LEFT)
                ? facing.rotateYClockwise()
                : facing.rotateYCounterclockwise();

        BlockPos nextPos = pos.offset(step);
        BlockState next = world.getBlockState(nextPos);

        if (!(next.getBlock() instanceof DoorBlock)) return;
        if (next.get(DoorBlock.HALF) != DoubleBlockHalf.LOWER) return;
        if (next.get(DoorBlock.FACING) != facing) return;
        if (next.get(DoorBlock.HINGE) == hinge) return;

        toggleDoorRecursive(
                world,
                nextPos,
                next,
                originalOpen,
                depth + 1,
                maxDepth
        );
    }

    /* ============================================================
       Helpers
       ============================================================ */

    private static void toggle(World world, BlockPos pos, BlockState state) {
        world.setBlockState(
                pos,
                state.cycle(Properties.OPEN),
                Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD
        );
    }

    private static int manhattan(BlockPos a, BlockPos b) {
        return Math.abs(a.getX() - b.getX())
                + Math.abs(a.getY() - b.getY())
                + Math.abs(a.getZ() - b.getZ());
    }
}
