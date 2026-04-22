package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
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
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState placed = this.getDefaultState()
                .with(AXIS, ctx.getSide().getAxis())
                .with(CREAKING_HEART_STATE, CreakingHeartState.UPROOTED)
                .with(NATURAL, false);

        return updateState(placed, ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockState updated = updateState(state, world, pos);

        if (updated != state) {
            world.setBlockState(pos, updated, Block.NOTIFY_ALL);

            CreakingHeartState oldState = state.get(CREAKING_HEART_STATE);
            CreakingHeartState newState = updated.get(CREAKING_HEART_STATE);

            if (oldState != newState) {
                if (newState == CreakingHeartState.AWAKE) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK,
                            SoundCategory.BLOCKS, 0.6F, 1.2F);
                } else if (oldState == CreakingHeartState.AWAKE && newState == CreakingHeartState.DORMANT) {
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK,
                            SoundCategory.BLOCKS, 0.35F, 0.8F);
                }
            }
        }

        // Keep natural hearts polling occasionally so day/night transitions update
        if (updated.get(NATURAL) && updated.get(CREAKING_HEART_STATE) != CreakingHeartState.UPROOTED) {
            world.createAndScheduleBlockTick(pos, this, 20);
        }
    }

    private static BlockState updateState(BlockState state, WorldView world, BlockPos pos) {
        if (!shouldBeEnabled(state, world, pos)) {
            return state.with(CREAKING_HEART_STATE, CreakingHeartState.UPROOTED);
        }

        boolean natural = state.get(NATURAL);

        if (!natural) {
            return state.with(CREAKING_HEART_STATE, CreakingHeartState.DORMANT);
        }

        if (world instanceof World realWorld) {
            boolean awake = shouldBeAwake(realWorld, pos);
            return state.with(CREAKING_HEART_STATE,
                    awake ? CreakingHeartState.AWAKE : CreakingHeartState.DORMANT);
        }

        return state.with(CREAKING_HEART_STATE, CreakingHeartState.DORMANT);
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

    private static boolean shouldBeAwake(World world, BlockPos pos) {
        if (world.isClient()) {
            return false;
        }

        if (!world.getDimension().hasSkyLight()) {
            return false;
        }

        // Night only
        if (!world.isNight()) {
            return false;
        }

        // Optional: require a reasonably dark environment
        return world.isNight() || world.getLightLevel(pos) <= 7;
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