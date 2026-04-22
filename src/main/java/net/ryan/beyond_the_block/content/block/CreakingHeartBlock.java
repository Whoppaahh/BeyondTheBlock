package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.ryan.beyond_the_block.utils.CreakingHeartState;
import net.ryan.beyond_the_block.utils.ModTags;

public class CreakingHeartBlock extends PillarBlock {
    public static final EnumProperty<CreakingHeartState> CREAKING_HEART_STATE =
            EnumProperty.of("creaking_heart_state", CreakingHeartState.class);

    public static final BooleanProperty NATURAL = BooleanProperty.of("natural");

    public CreakingHeartBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(AXIS, Direction.Axis.Y)
                .with(CREAKING_HEART_STATE, CreakingHeartState.UPROOTED)
                .with(NATURAL, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState placed = this.getDefaultState()
                .with(AXIS, ctx.getSide().getAxis())
                .with(CREAKING_HEART_STATE, CreakingHeartState.UPROOTED)
                .with(NATURAL, false);

        return updateState(placed, ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state,
                                                Direction direction,
                                                BlockState neighborState,
                                                WorldAccess world,
                                                BlockPos pos,
                                                BlockPos neighborPos) {
        world.createAndScheduleBlockTick(pos, this, 1);
        return state;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockState updated = updateState(state, world, pos);
        if (updated != state) {
            world.setBlockState(pos, updated, Block.NOTIFY_ALL);
        }
    }

    private static BlockState updateState(BlockState state, WorldView world, BlockPos pos) {
        if (!shouldBeEnabled(state, world, pos)) {
            return state.with(CREAKING_HEART_STATE, CreakingHeartState.UPROOTED);
        }

        CreakingHeartState current = state.get(CREAKING_HEART_STATE);
        if (current == CreakingHeartState.UPROOTED) {
            return state.with(CREAKING_HEART_STATE, CreakingHeartState.DORMANT);
        }

        return state;
    }

    public static boolean shouldBeEnabled(BlockState state, WorldView world, BlockPos pos) {
        Direction.Axis axis = state.get(AXIS);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis() != axis) continue;

            BlockState neighbor = world.getBlockState(pos.offset(direction));
            if (!isValidPaleOakLog(neighbor, axis)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isValidPaleOakLog(BlockState state, Direction.Axis axis) {
        if (!state.isIn(ModTags.Blocks.PALE_OAK_LOGS)) {
            return false;
        }

        return state.contains(AXIS) && state.get(AXIS) == axis;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return PillarBlock.changeRotation(state, rotation);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS, CREAKING_HEART_STATE, NATURAL);
    }
}