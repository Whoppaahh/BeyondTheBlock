package net.ryan.beyond_the_block.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.block.Entity.DyedWaterCauldronBlockEntity;

public class DyedWaterCauldronBlock extends LeveledCauldronBlock {

    public DyedWaterCauldronBlock(Settings settings) {
        super(settings, precipitation -> false, CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(LEVEL, 1)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, net.minecraft.util.math.BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (world.isClient) return ActionResult.SUCCESS;

        ItemStack stack = player.getStackInHand(hand);
        int level = state.get(LEVEL);

        if (!(world.getBlockEntity(pos) instanceof DyedWaterCauldronBlockEntity be)) {
            return ActionResult.PASS;
        }

        // Mix dye into cauldron water
        if (stack.getItem() instanceof DyeItem dyeItem) {
            int rgb = toRgb(dyeItem.getColor());
            be.updateColorWithNewDye(rgb);

            if (!player.isCreative()) stack.decrement(1);

            world.playSound(null, pos, SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1, 1.1f);
            return ActionResult.SUCCESS;
        }

        // Dyeable items get colored by the cauldron
        if (stack.getItem() instanceof net.minecraft.item.DyeableItem dyeable) {
            int color = be.getColor();
            dyeable.setColor(stack, color);

            if (!player.isCreative()) drainOne(world, pos, state);
            world.playSound(null, pos, SoundEvents.ENTITY_FISHING_BOBBER_SPLASH,
                    SoundCategory.BLOCKS, 1, 1);

            return ActionResult.SUCCESS;
        }

        // Prevent bucket / bottle extraction of dyed water
        if (stack.isOf(net.minecraft.item.Items.BUCKET)
                || stack.isOf(net.minecraft.item.Items.GLASS_BOTTLE)
                || stack.isOf(net.minecraft.item.Items.WATER_BUCKET)) {

            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    private void drainOne(World world, net.minecraft.util.math.BlockPos pos, BlockState state) {
        int level = state.get(LEVEL);
        if (level <= 1) {
            world.removeBlockEntity(pos);
            world.setBlockState(pos, net.minecraft.block.Blocks.CAULDRON.getDefaultState());
        } else {
            world.setBlockState(pos, state.with(LEVEL, level - 1));
        }
    }

    private int toRgb(net.minecraft.util.DyeColor c) {
        float[] f = c.getColorComponents();
        return ((int)(f[0] * 255) << 16) | ((int)(f[1] * 255) << 8) | ((int)(f[2] * 255));
    }
}

