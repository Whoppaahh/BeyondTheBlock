package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.blockentity.InfiFurnaceBlockEntity;
import net.ryan.beyond_the_block.content.blockentity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class InfiFurnaceBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty LIT = Properties.LIT;

    public InfiFurnaceBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(LIT, false));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world, BlockState state, BlockEntityType<T> type
    ) {
        return world.isClient ? null : checkType(type, ModBlockEntities.INFI_FURNACE_BLOCK_ENTITY,
                (world1, pos, state1, be) -> be.tick());
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof InfiFurnaceBlockEntity) {
                ItemScatterer.spawn(world, pos, ((InfiFurnaceBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(LIT)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;

        if (random.nextDouble() < 0.1) {
            world.playSound(x, y, z, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE,
                    SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        }

        Direction direction = state.get(FACING);
        Direction.Axis axis = direction.getAxis();

        double offset = random.nextDouble() * 0.6 - 0.3;
        double dx = axis == Direction.Axis.X ? direction.getOffsetX() * 0.52 : offset;
        double dz = axis == Direction.Axis.Z ? direction.getOffsetZ() * 0.52 : offset;
        double dy = random.nextDouble() * 6.0 / 16.0;

        world.addParticle(ParticleTypes.SMOKE, x + dx, y + dy + 0.1, z + dz, 0.0, 0.0, 0.0);
        world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x + dx, y + dy + 0.1, z + dz, 0.0, 0.0, 0.0);
    }


    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InfiFurnaceBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(LIT, false);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof InfiFurnaceBlockEntity) {
            return (InfiFurnaceBlockEntity) blockEntity;
        }
        return null;
    }


}


