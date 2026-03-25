package net.ryan.beyond_the_block.utils;

import net.minecraft.block.BannerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GlowManager {

    public static final int FULL_BRIGHT = 0xF000F0;

    // -------- ITEMS --------
    public static boolean shouldGlow(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;

        // NBT Glow tag
        if (stack.hasNbt() && stack.getNbt().getBoolean("Glowing")) return true;

        // Auto-glow logic for banners/items
        if (stack.getItem() instanceof BannerItem) return true;
        if (stack.getItem() instanceof BlockItem bi) return shouldGlow(bi.getBlock().getDefaultState());

        return false;
    }

    // -------- ENTITIES --------
    public static boolean shouldGlow(LivingEntity entity) {
        if (entity == null) return false;

        if (entity instanceof Glowable glowable) return glowable.bt$isGlowing();

        // fallback: check NBT (optional)
        return false;
    }

    public static boolean shouldGlow(ItemFrameEntity frame) {
        if (frame == null) return false;

        if (frame instanceof Glowable glowable) return glowable.bt$isGlowing();

        // fallback: glow if the held item is glowing
        return shouldGlow(frame.getHeldItemStack());
    }

    // -------- BLOCKS BY TYPE --------
    public static boolean shouldGlow(BlockState state) {
        return state.isIn(BlockTags.WOOL);
    }

    // -------- BLOCKS BY POSITION --------
    public static boolean shouldGlow(World world, BlockPos pos) {
        if (world == null || pos == null) return false;

        BlockState state = world.getBlockState(pos);

        // Wool blocks
        if (state.isIn(BlockTags.WOOL)) return true;

        // Banners: check Glowable mixin
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof Glowable glowable) return glowable.bt$isGlowing();

        return false;
    }
}