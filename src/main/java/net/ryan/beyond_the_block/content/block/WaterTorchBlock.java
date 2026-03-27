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

public class WaterTorchBlock extends TorchBlock implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (world.getFluidState(pos).getFluid() != Fluids.WATER) {
            if (world instanceof World realWorld && !realWorld.isClient) {
                Block.dropStacks(state, realWorld, pos);
            }
        }
        super.onBroken(world, pos, state);
    }

    public WaterTorchBlock(Settings settings) {
        super(settings, ParticleTypes.SOUL_FIRE_FLAME);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
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

        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                Direction opposite = direction.getOpposite();

                BlockState wallState = ModBlocks.WALL_WATER_TORCH_BLOCK
                        .getDefaultState()
                        .with(WallTorchBlock.FACING, opposite)
                        .with(WATERLOGGED, world.getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);

                if (wallState.canPlaceAt(world, pos)) {
                    return wallState;
                }
            }
        }

        // fallback to standing torch
        BlockState floorState = this.getDefaultState().with(WATERLOGGED, world.getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);

        if (floorState.canPlaceAt(world, pos)) {
            return floorState;
        }

        return null;
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
