package net.ryan.beyond_the_block.feature.world.crops;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.ryan.beyond_the_block.content.registry.ModEnchantments;

public final class CropGrowthAuraHandler {

    private static final int TICK_INTERVAL = 20;
    private static final int SAMPLES_PER_PLAYER = 12;

    private CropGrowthAuraHandler() {
    }

    public static void tick(ServerWorld world) {
        if (world.getTime() % TICK_INTERVAL != 0) {
            return;
        }

        for (PlayerEntity player : world.getPlayers()) {
            tickPlayer(world, player);
        }
    }

    private static void tickPlayer(ServerWorld world, PlayerEntity player) {
        int level = getCropGrowthLevel(player);
        if (level <= 0) {
            return;
        }

        int radius = 4 + (level * 2);
        Random random = world.getRandom();

        for (int i = 0; i < SAMPLES_PER_PLAYER; i++) {
            BlockPos pos = randomNearby(player.getBlockPos(), radius, random);
            BlockState state = world.getBlockState(pos);

            if (tryGrowCrop(world, pos, state, random)) {
                continue;
            }

            tryGrowSapling(world, pos, state, random);
        }
    }

    private static int getCropGrowthLevel(PlayerEntity player) {
        int max = 0;

        for (ItemStack stack : player.getHandItems()) {
            max = Math.max(max, EnchantmentHelper.getLevel(ModEnchantments.NIGHT_CULTIVATION, stack));
            max = Math.max(max, EnchantmentHelper.getLevel(ModEnchantments.GARDENS_BOUNTY, stack));
        }

        return max;
    }

    private static BlockPos randomNearby(BlockPos center, int radius, Random random) {
        int dx = random.nextInt(radius * 2 + 1) - radius;
        int dy = random.nextInt(3) - 1;
        int dz = random.nextInt(radius * 2 + 1) - radius;
        return center.add(dx, dy, dz);
    }

    private static boolean tryGrowCrop(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        if (!(state.getBlock() instanceof CropBlock crop)) {
            return false;
        }

        if (!crop.isMature(state) && random.nextFloat() < 0.35f) {
            crop.applyGrowth(world, pos, state);
            return true;
        }

        return false;
    }

    private static void tryGrowSapling(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        if (!(state.getBlock() instanceof SaplingBlock sapling)) {
            return;
        }

        if (random.nextFloat() < 0.20f) {
            sapling.generate(world, pos, state, random);
        }
    }
}