package net.ryan.beyond_the_block.feature.cauldrons;


import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.block.cauldrons.ModdedFluidCauldronBlock;

public class ModdedFluidCauldronHandler {

    // -----------------------------
    // MAIN ENTRY
    // -----------------------------
    public static ActionResult handleCauldronUse(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = player.getStackInHand(hand);

        // A) Player clicked a normal cauldron → maybe convert to modded fluid cauldron
        if (state.isOf(Blocks.CAULDRON)) {
            return handleEmptyCauldron(player, world, pos, stack);
        }

        // B) Player clicked a modded cauldron
        if (state.getBlock() instanceof ModdedFluidCauldronBlock) {
            return handleModdedCauldron(player, world, pos, state, stack, hand);
        }

        return ActionResult.PASS;
    }


    // -----------------------------
    // FILL EMPTY CAULDRON WITH FLUID
    // -----------------------------
    private static ActionResult handleEmptyCauldron(PlayerEntity player, World world, BlockPos pos, ItemStack stack) {
        ModdedFluidCauldronBlock.Content content = null;

        if (stack.isOf(Items.SLIME_BALL)) {
            content = ModdedFluidCauldronBlock.Content.SLIME;
        } else if (stack.isOf(Items.HONEY_BOTTLE) || stack.isOf(Items.HONEYCOMB)) {
            content = ModdedFluidCauldronBlock.Content.HONEY;
        } else if (stack.isOf(Items.MAGMA_CREAM)) {
            content = ModdedFluidCauldronBlock.Content.MAGMA;
        }

        // Not a valid filler item
        if (content == null) return ActionResult.PASS;

        // Convert the cauldron to the modded one, level 1
        BlockState newState = ModBlocks.MODDED_FLUID_CAULDRON_BLOCK.getDefaultState()
                .with(LeveledCauldronBlock.LEVEL, 1)
                .with(ModdedFluidCauldronBlock.CONTENT, content);

        world.setBlockState(pos, newState, 3);

        // Handle item cost
        if (!player.isCreative()) {
            if (stack.isOf(Items.HONEY_BOTTLE)) {
                stack.decrement(1);
                player.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
            } else {
                stack.decrement(1);
            }
        }

        // Play sound + particles
        playFillSound(world, pos, content);
        spawnFillParticles(world, pos, content);

        return ActionResult.SUCCESS;
    }


    // -----------------------------
    // INTERACT WITH EXISTING MODDED CAULDRON
    // -----------------------------
    private static ActionResult handleModdedCauldron(
            PlayerEntity player, World world, BlockPos pos,
            BlockState state, ItemStack stack, Hand hand) {

        ModdedFluidCauldronBlock.Content content = state.get(ModdedFluidCauldronBlock.CONTENT);
        int level = state.get(LeveledCauldronBlock.LEVEL);

        // A) Filling: same item → increase level
        if (canFill(content, stack)) {
            if (level < 3) {
                world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, level + 1), 3);

                if (!player.isCreative()) {
                    if (stack.isOf(Items.HONEY_BOTTLE)) {
                        stack.decrement(1);
                        player.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
                    } else {
                        stack.decrement(1);
                    }
                }

                playFillSound(world, pos, content);
                spawnFillParticles(world, pos, content);
            }

            return ActionResult.SUCCESS;
        }

        // B) Extract block when full (level 3) + empty hand
        if (level == 3 && stack.isEmpty()) {

            Item output = switch (content) {
                case SLIME -> Items.SLIME_BLOCK;
                case HONEY -> Items.HONEY_BLOCK;
                case MAGMA -> Items.MAGMA_BLOCK;
                case ICE -> Items.ICE;
            };

            player.setStackInHand(hand, new ItemStack(output));

            // Reset to empty cauldron
            world.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 3);

            world.playSound(null, pos,
                    SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS,
                    1.0F, 1.0F);

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }


    // -----------------------------
    // VALID FILL MATERIALS
    // -----------------------------
    private static boolean canFill(ModdedFluidCauldronBlock.Content content, ItemStack stack) {
        return switch (content) {
            case SLIME -> stack.isOf(Items.SLIME_BALL);
            case HONEY -> stack.isOf(Items.HONEY_BOTTLE) || stack.isOf(Items.HONEYCOMB);
            case MAGMA -> stack.isOf(Items.MAGMA_CREAM);
            case ICE -> false;
        };
    }


    // -----------------------------
    // PARTICLE EFFECTS
    // -----------------------------
    private static void spawnFillParticles(World world, BlockPos pos, ModdedFluidCauldronBlock.Content content) {
        if (!(world instanceof ServerWorld sw)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.05;
        double z = pos.getZ() + 0.5;

        switch (content) {
            case SLIME -> sw.spawnParticles(ParticleTypes.ITEM_SLIME, x, y, z,
                    8, 0.25, 0.1, 0.25, 0.01);
            case HONEY -> {
                sw.spawnParticles(ParticleTypes.DRIPPING_HONEY, x, y, z,
                        10, 0.25, 0.15, 0.25, 0.01);
                sw.spawnParticles(ParticleTypes.FALLING_HONEY, x, y, z,
                        4, 0.25, 0.15, 0.25, 0.02);
            }
            case MAGMA -> {
                sw.spawnParticles(ParticleTypes.FLAME, x, y, z,
                        12, 0.25, 0.1, 0.25, 0.01);
                sw.spawnParticles(ParticleTypes.LAVA, x, y, z,
                        4, 0.25, 0.1, 0.25, 0.02);
            }
        }
    }


    // -----------------------------
    // SOUND EFFECTS
    // -----------------------------
    private static void playFillSound(World world, BlockPos pos, ModdedFluidCauldronBlock.Content content) {
        SoundEvent sound = switch (content) {
            case SLIME -> SoundEvents.ENTITY_SLIME_SQUISH;
            case HONEY -> SoundEvents.BLOCK_HONEY_BLOCK_BREAK;
            case MAGMA -> SoundEvents.BLOCK_LAVA_EXTINGUISH;
            case ICE -> SoundEvents.BLOCK_SNOW_PLACE;
        };

        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
