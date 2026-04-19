package net.ryan.beyond_the_block.utils;

import net.minecraft.block.Block;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public final class HangingSignClientHelper {
    private static final Identifier FALLBACK =
            new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/hanging_signs/oak.png");

    private HangingSignClientHelper() {}

    public static boolean isHangingSign(SignBlockEntity blockEntity) {
        Block block = blockEntity.getCachedState().getBlock();
        return block instanceof HangingSignTextureProvider;
    }

    public static Identifier getGuiTexture(SignBlockEntity blockEntity) {
        Block block = blockEntity.getCachedState().getBlock();
        if (block instanceof HangingSignTextureProvider provider) {
            return provider.beyond_the_block$getGuiTexture();
        }
        return FALLBACK;
    }
}
