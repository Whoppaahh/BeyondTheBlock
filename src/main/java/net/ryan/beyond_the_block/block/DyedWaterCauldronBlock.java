package net.ryan.beyond_the_block.block;

import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.block.Entity.DyedWaterCauldronBlockEntity;

public class DyedWaterCauldronBlock extends LeveledCauldronBlock implements BlockEntityProvider {

    public static final IntProperty COLOR_INDEX = IntProperty.of("color_index", 0, 3);

    public DyedWaterCauldronBlock(Settings settings) {
        super(settings, p -> false, CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR);

        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(LEVEL, 1)
                        .with(COLOR_INDEX, 0)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, COLOR_INDEX);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyedWaterCauldronBlockEntity(pos, state);
    }

    public static int toRgb(DyeColor c) {
        float[] f = c.getColorComponents();
        return ((int)(f[0] * 255) << 16) | ((int)(f[1] * 255) << 8) | ((int)(f[2] * 255));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (world.isClient) return ActionResult.SUCCESS;

        ItemStack stack = player.getStackInHand(hand);

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof DyedWaterCauldronBlockEntity be)) {
            return ActionResult.PASS;
        }

        // Reset dyed cauldron back to vanilla water cauldron using a water bucket
        if (stack.isOf(Items.WATER_BUCKET)) {

            int level = state.get(LEVEL);

            // Replace with vanilla water cauldron while keeping the same level
            BlockState newState = Blocks.WATER_CAULDRON.getDefaultState()
                    .with(LeveledCauldronBlock.LEVEL, level);


            // Remove dyed BE + apply new state
            be.reset();
            world.removeBlockEntity(pos);
            world.setBlockState(pos, newState, Block.NOTIFY_ALL);

            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY,
                    SoundCategory.BLOCKS, 1.0f, 1.0f);

            // Do NOT consume the water bucket (vanilla behavior)
            return ActionResult.SUCCESS;
        }


        // Add more dye
        if (stack.getItem() instanceof DyeItem dyeItem) {
            int rgb = toRgb(dyeItem.getColor());
            be.mixDye(rgb);

            if (!player.isCreative()) stack.decrement(1);
            world.playSound(null, pos, SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1, 1.1f);

            incrementColourIndex(world, pos, state);
            return ActionResult.SUCCESS;
        }

        // Dyeable items
        if (stack.getItem() instanceof DyeableItem dyeable) {
            int color = be.getColor();
            dyeable.setColor(stack, color);

            //if (!player.isCreative())
                drainOne(world, pos, state);
            world.playSound(null, pos, SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, SoundCategory.BLOCKS, 1, 1);

            return ActionResult.SUCCESS;
        }

        // Prevent filling bottles/buckets
        if (stack.isOf(Items.BUCKET)
                || stack.isOf(Items.GLASS_BOTTLE)) {
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    private void incrementColourIndex(World world, BlockPos pos, BlockState state) {
        if (world.isClient) return;

        if (!state.contains(COLOR_INDEX)) return;

        int current = state.get(COLOR_INDEX);
        int next = (current + 1) & 3; // 0–3 loop

        world.setBlockState(pos, state.with(COLOR_INDEX, next), Block.NOTIFY_ALL);
    }

    private void drainOne(World world, BlockPos pos, BlockState state) {
        int level = state.get(LEVEL);
        if (level <= 1) {
            if (world.getBlockEntity(pos) instanceof DyedWaterCauldronBlockEntity be)
                be.reset();
            world.removeBlockEntity(pos);
            world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
        } else {
            world.setBlockState(pos, state.with(LEVEL, level - 1));
        }
    }
}
