package net.ryan.beyond_the_block.utils.helpers;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public final class BetterLadderPlacement {

    public static TypedActionResult<ItemStack> onUseItem(
            PlayerEntity player,
            World world,
            Hand hand
    ) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() != Items.LADDER) return TypedActionResult.pass(stack);
        if (player.isSneaking()) return TypedActionResult.pass(stack);

        BlockHitResult hit = raycast(player, world);
        if (hit == null) return TypedActionResult.pass(stack);

        BlockPos clickedPos = hit.getBlockPos();
        Direction side = hit.getSide();
        BlockState clickedState = world.getBlockState(clickedPos);

        Direction vertical = getVerticalIntent(player);

        /* ------------------------------------------------------------
           Case A: Vertical ladder chaining
           ------------------------------------------------------------ */
        if (vertical != null) {
            BlockPos ladderPos = findAdjacentLadder(world, clickedPos, side);
            if (ladderPos != null) {
                BlockState ladderState = world.getBlockState(ladderPos);
                Direction facing = ladderState.get(LadderBlock.FACING);

                BlockPos target = findVerticalPlacement(world, ladderPos, vertical, facing);
                if (target != null && world.getBlockState(target).isAir()) {
                    place(world, target, facing, player, stack);
                    return TypedActionResult.success(stack, world.isClient());
                }
            }
        }

        /* ------------------------------------------------------------
           Case B: Initial / normal placement
           ------------------------------------------------------------ */
        Direction facing;
        BlockPos placePos;

        if (side.getAxis().isHorizontal()) {
            // Wall click
            facing = side;
            placePos = clickedPos.offset(side);
        } else {
            // Ground / ceiling
            facing = player.getHorizontalFacing().getOpposite();
            placePos = clickedPos.offset(side);
        }

        if (world.getBlockState(placePos).isAir()) {
            place(world, placePos, facing, player, stack);
            return TypedActionResult.success(stack, world.isClient());
        }

        return TypedActionResult.pass(stack);
    }

    /* ============================================================
       Helpers
       ============================================================ */

    private static BlockHitResult raycast(PlayerEntity player, World world) {
        double reach = player.isCreative() ? 5.0D : 4.5D;
        return (BlockHitResult) player.raycast(reach, 0.0F, false);
    }

    private static Direction getVerticalIntent(PlayerEntity player) {
        float pitch = player.getPitch();
        if (pitch <= 15) return Direction.UP;
        if (pitch >= 30) return Direction.DOWN;
        return null;
    }

    private static BlockPos findAdjacentLadder(World world, BlockPos pos, Direction hitSide) {
        // Direct
        if (world.getBlockState(pos).getBlock() instanceof LadderBlock) {
            return pos;
        }

        // Behind clicked face
        BlockPos behind = pos.offset(hitSide.getOpposite());
        if (world.getBlockState(behind).getBlock() instanceof LadderBlock) {
            return behind;
        }

        // Above
        BlockPos up = pos.up();
        if (world.getBlockState(up).getBlock() instanceof LadderBlock) {
            return up;
        }

        // Below
        BlockPos down = pos.down();
        if (world.getBlockState(down).getBlock() instanceof LadderBlock) {
            return down;
        }

        return null;
    }

    private static BlockPos findVerticalPlacement(
            World world,
            BlockPos start,
            Direction vertical,
            Direction facing
    ) {
        BlockPos.Mutable pos = start.mutableCopy();

        while (true) {
            pos.move(vertical);
            BlockState state = world.getBlockState(pos);

            if (state.isAir()) {
                return pos.toImmutable();
            }

            if (!(state.getBlock() instanceof LadderBlock)
                    || state.get(LadderBlock.FACING) != facing) {
                return null;
            }
        }
    }

    private static void place(
            World world,
            BlockPos pos,
            Direction facing,
            PlayerEntity player,
            ItemStack stack
    ) {
        if (!world.isClient) {
            world.setBlockState(
                    pos,
                    Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, facing),
                    3
            );

            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }
    }

    public static ActionResult onUseBlock(
            PlayerEntity player,
            World world,
            Hand hand,
            BlockHitResult hit
    ) {
        if (player.isSneaking()) return ActionResult.PASS;
        if (world.isClient) return ActionResult.PASS;



        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() != Items.LADDER) return ActionResult.PASS;
        BlockPos clickedPos = hit.getBlockPos();
        BlockState clickedState = world.getBlockState(clickedPos);

        if (!(clickedState.getBlock() instanceof LadderBlock)) {
            return ActionResult.PASS;
        }
        Direction vertical = getVerticalIntent(player);
        if (vertical == null) return ActionResult.PASS;
        Direction facing = clickedState.get(LadderBlock.FACING);
        BlockPos target = findVerticalPlacement(world, clickedPos, vertical, facing);

        if (target != null && world.getBlockState(target).isAir()) {
            world.setBlockState(
                    target,
                    Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, facing),
                    3
            );

            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            return ActionResult.SUCCESS; // block vanilla
        }

        return ActionResult.PASS;
    }
}