package net.ryan.beyond_the_block.block.Cauldrons;

import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.block.Entity.DyedWaterCauldronBlockEntity;
import net.ryan.beyond_the_block.utils.ModTags;

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
    private static DyeColor nearestDyeColor(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        DyeColor best = DyeColor.WHITE;
        double bestDist = Double.MAX_VALUE;

        for (DyeColor c : DyeColor.values()) {
            float[] f = c.getColorComponents();
            int cr = (int)(f[0] * 255);
            int cg = (int)(f[1] * 255);
            int cb = (int)(f[2] * 255);

            int dr = r - cr;
            int dg = g - cg;
            int db = b - cb;

            double dist = dr * dr + dg * dg + db * db;
            if (dist < bestDist) {
                bestDist = dist;
                best = c;
            }
        }
        return best;
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (world.isClient) return ActionResult.CONSUME;

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

    private static Block resolveColoredVariant(Block original, DyeColor color) {
        Identifier id = Registry.BLOCK.getId(original);

        String path = id.getPath();

        // Expect <color>_<base>
        int underscore = path.indexOf('_');
        if (underscore < 0) return null;

        String base = path.substring(underscore + 1);
        String recoloredPath = color.getName() + "_" + base;

        Identifier recoloredId = new Identifier(id.getNamespace(), recoloredPath);
        if (!Registry.BLOCK.containsId(recoloredId)) return null;

        return Registry.BLOCK.get(recoloredId);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient) return;
        if (!(entity instanceof ItemEntity itemEntity)) return;

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof DyedWaterCauldronBlockEntity dyed)) return;

        ItemStack stack = itemEntity.getStack();
        if (!(stack.getItem() instanceof BlockItem blockItem)) return;

        if (itemEntity.getStack().getOrCreateNbt().getBoolean("btbDyed")) return;

        Block block = blockItem.getBlock();
        if (!block.getDefaultState().isIn(ModTags.Blocks.CAULDRON_RECOLORABLE)) return;

        DyeColor dye = nearestDyeColor(dyed.getColor());
        Block recolored = resolveColoredVariant(block, dye);
        if (recolored == null || recolored == block) return;
        ItemStack newStack = new ItemStack(recolored, stack.getCount());
        newStack.getOrCreateNbt().putBoolean("btbDyed", true);

        itemEntity.setStack(newStack);
        drainOne(world, pos, state);

        world.playSound(null, pos,
                SoundEvents.ITEM_DYE_USE,
                SoundCategory.BLOCKS, 1, 1);
    }

}
