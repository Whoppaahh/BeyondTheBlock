package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class WallWaterTorchBlock extends WallTorchBlock implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public WallWaterTorchBlock(Settings settings) {
        super(settings, ParticleTypes.SOUL_FIRE_FLAME);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        // Only allow placement in water
        if (world.getFluidState(pos).getFluid() != Fluids.WATER) {
            return null;
        }

        BlockState state = super.getPlacementState(ctx);
        return state == null ? null : state.with(WATERLOGGED, true);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos)
                && world.getFluidState(pos).getFluid() == Fluids.WATER;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state,
                                                Direction direction,
                                                BlockState neighborState,
                                                WorldAccess world,
                                                BlockPos pos,
                                                BlockPos neighborPos) {
        if (world.getFluidState(pos).getFluid() != Fluids.WATER) {
            if (world instanceof World realWorld && !realWorld.isClient) {
                Block.dropStacks(state, realWorld, pos);
                BeyondTheBlock.LOGGER.info("dropped");
            }
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED)
                ? Fluids.WATER.getStill(false)
                : super.getFluidState(state);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(WATERLOGGED)) {
            return;
        }

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5;

        if (state.contains(WallTorchBlock.FACING)) {
            Direction dir = state.get(WallTorchBlock.FACING);

            double offset = 0.27;

            x += -dir.getOffsetX() * offset;
            z += -dir.getOffsetZ() * offset;

            y += 0.2;
        }

        // Flame inside bubble
        world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0, 0.002, 0);

        // Bubble shell
        for (int i = 0; i < 3; i++) {
            double ox = (random.nextDouble() - 0.5) * 0.15;
            double oy = (random.nextDouble() - 0.5) * 0.15;
            double oz = (random.nextDouble() - 0.5) * 0.15;

            world.addParticle(ParticleTypes.BUBBLE, x + ox, y + oy, z + oz, 0, 0.01, 0);
        }
    }
}
