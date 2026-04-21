package net.ryan.beyond_the_block.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

public abstract class AbstractEyeblossomBlock extends FlowerBlock {

    protected Block otherVariant;

    public AbstractEyeblossomBlock(StatusEffect suspiciousStewEffect, int effectDuration, Settings settings) {
        super(suspiciousStewEffect, effectDuration, settings);
    }

    public void setOtherVariant(Block otherVariant) {
        this.otherVariant = otherVariant;
    }

    protected Block getSwitchedBlock() {
        return this.otherVariant;
    }

    protected abstract boolean isOpenVariant();

    @Override
    public void onPlaced(net.minecraft.world.World world, BlockPos pos, BlockState state, net.minecraft.entity.LivingEntity placer, net.minecraft.item.ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!world.isClient) {
            world.createAndScheduleBlockTick(pos, this, 20);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            net.minecraft.world.WorldAccess world,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if (world instanceof net.minecraft.world.World realWorld && !realWorld.isClient) {
            realWorld.createAndScheduleBlockTick(pos, this, 20);
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        if (!world.getDimension().hasSkyLight()) {
            return;
        }

        boolean shouldBeOpen = shouldBeOpen(world);
        boolean currentlyOpen = this.isOpenVariant();

        if (shouldBeOpen != currentlyOpen) {
            Block replacement = getSwitchedBlock();

            world.setBlockState(pos, replacement.getDefaultState(), Block.NOTIFY_ALL);
            playSwitchEffects(world, pos, shouldBeOpen);
            notifyNearbyEyeblossoms(world, pos);
        } else {
            world.createAndScheduleBlockTick(pos, this, 20);
        }
    }

    protected boolean shouldBeOpen(ServerWorld world) {
        long time = world.getTimeOfDay() % 24000L;
        return time >= 13000L && time < 23000L;
    }

    protected void playSwitchEffects(ServerWorld world, BlockPos pos, boolean opening) {
        world.playSound(
                null,
                pos,
                opening ? SoundEvents.BLOCK_SPORE_BLOSSOM_BREAK : SoundEvents.BLOCK_SMALL_DRIPLEAF_FALL,
                SoundCategory.BLOCKS,
                1.0F,
                opening ? 1.1F : 0.9F
        );

        for (int i = 0; i < 6; i++) {
            double x = pos.getX() + 0.25D + world.random.nextDouble() * 0.5D;
            double y = pos.getY() + 0.2D + world.random.nextDouble() * 0.5D;
            double z = pos.getZ() + 0.25D + world.random.nextDouble() * 0.5D;

            world.spawnParticles(
                    opening ? net.minecraft.particle.ParticleTypes.GLOW : net.minecraft.particle.ParticleTypes.SPORE_BLOSSOM_AIR,
                    x, y, z,
                    1,
                    0.0D, 0.0D, 0.0D,
                    0.0D
            );
        }
    }

    protected void notifyNearbyEyeblossoms(ServerWorld world, BlockPos origin) {
        BlockPos.iterateOutwards(origin, 3, 1, 3).forEach(pos -> {
            if (pos.equals(origin)) return;

            BlockState nearbyState = world.getBlockState(pos);
            if (nearbyState.getBlock() instanceof AbstractEyeblossomBlock) {
                world.createAndScheduleBlockTick(pos, nearbyState.getBlock(), 2 + world.random.nextInt(5));
            }
        });
    }
}