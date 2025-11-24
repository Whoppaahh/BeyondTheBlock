package net.ryan.beyond_the_block.utils.Helpers;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.ryan.beyond_the_block.config.ModConfig;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;

import java.util.HashSet;
import java.util.Set;

import static net.ryan.beyond_the_block.enchantment.MyEnchantmentHelper.*;

public class HighlightTracker {
    public static final Set<BlockPos> BLOCKS_TO_HIGHLIGHT = new HashSet<>();

    public static void setHighlights(Set<BlockPos> blocks) {
        BLOCKS_TO_HIGHLIGHT.clear();
        BLOCKS_TO_HIGHLIGHT.addAll(blocks);
    }

    public static Set<BlockPos> getHighlights() {
        return BLOCKS_TO_HIGHLIGHT;
    }

    public static void clear() {
        BLOCKS_TO_HIGHLIGHT.clear();
    }
    public static void add(BlockPos pos) {
        BLOCKS_TO_HIGHLIGHT.add(pos.toImmutable());
    }

    public static void remove(BlockPos pos) {
        BLOCKS_TO_HIGHLIGHT.remove(pos);
    }

    public static void updateHighlights(PlayerEntity player) {
        BLOCKS_TO_HIGHLIGHT.clear();

        // Make sure we're on the client
        if (player == null || !player.getWorld().isClient) return;
        if(!AutoConfig.getConfigHolder(ModConfig.class).getConfig().enchantments.showHighlights) return;

        ItemStack tool = player.getMainHandStack();
        BlockPos targetPos = getTargetedBlock(player);
        if (targetPos == null) return;


        if (hasHeldEnchantment(player, ModEnchantments.TIMBER_CUT) || hasHeldEnchantment(player, ModEnchantments.BARKSKIN)) {
            int level = EnchantmentHelper.getLevel(ModEnchantments.TIMBER_CUT, tool);
            Enchantment enchantment = ModEnchantments.TIMBER_CUT;
            if (level <= 0){
                level = EnchantmentHelper.getLevel(ModEnchantments.BARKSKIN, tool);
                enchantment = ModEnchantments.BARKSKIN;
            }

            if (isLog(player.getWorld().getBlockState(targetPos))) {
                findLogsToHighlight(targetPos, BLOCKS_TO_HIGHLIGHT, player.getWorld(), enchantment, level);
            }
        }

        if (hasHeldEnchantment(player, ModEnchantments.DEEP_TILL)) {
            int level = EnchantmentHelper.getLevel(ModEnchantments.DEEP_TILL, tool);
            findTillableBlocksToHighlight(targetPos, BLOCKS_TO_HIGHLIGHT, player.getWorld(), level);
        }
    }

    private static BlockPos getTargetedBlock(PlayerEntity player) {
        if (MinecraftClient.getInstance().crosshairTarget instanceof BlockHitResult hit) {
            return hit.getBlockPos();
        }
        return null;
    }
}

