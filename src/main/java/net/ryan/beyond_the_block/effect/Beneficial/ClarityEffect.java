package net.ryan.beyond_the_block.effect.Beneficial;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.effect.ModEffects;
import net.ryan.beyond_the_block.utils.Helpers.HighlightTracker;

import java.util.HashSet;
import java.util.Set;

public class ClarityEffect extends StatusEffect {
    private static final BlockPos.Mutable LAST_SCAN_POS = new BlockPos.Mutable();
    private static int tickCounter = 0;
    private static int MAX_HIGHLIGHTED = 32;

    public ClarityEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }

    public static void tickHandler(MinecraftClient minecraftClient) {
        ClientPlayerEntity player = minecraftClient.player;
        ClientWorld world = minecraftClient.world;

        if (player == null || world == null) return;

        if (!player.hasStatusEffect(ModEffects.CLARITY)){
            HighlightTracker.clear();
            return;
        }

        int amplifier = player.getStatusEffect(ModEffects.CLARITY).getAmplifier();
        tickCounter++;
        if (tickCounter < 5 && player.getBlockPos().isWithinDistance(LAST_SCAN_POS, 1)) return;
        tickCounter = 0;
        LAST_SCAN_POS.set(player.getBlockPos());

        // Clear and rescan
        HighlightTracker.clear();

        int radius = 10 + amplifier * 5;
        radius = Math.min(radius, MAX_HIGHLIGHTED);
        Set<BlockPos> highlights = new HashSet<>();

        BlockPos.iterateOutwards(player.getBlockPos(), radius, radius, radius).forEach(pos -> {
           // if (highlights.contains(pos)) return;

            if (isHighlightBlock(world.getBlockState(pos).getBlock())) {
                //world.addParticle(ParticleTypes.GLOW, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0, 0);
                highlights.add(pos.toImmutable());
            }
        });

        HighlightTracker.setHighlights(highlights);
    }
    private static boolean isHighlightBlock(net.minecraft.block.Block block) {
        return block.getDefaultState().isIn(ConventionalBlockTags.ORES) || block.getDefaultState().isIn(ConventionalBlockTags.CHESTS);
        // Add your custom ores here too!
    }
}
