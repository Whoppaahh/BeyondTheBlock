package net.ryan.beyond_the_block.utils;

import net.minecraft.block.Block;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.Identifier;

public final class HangingSignClientHelper {
    private HangingSignClientHelper() {}

    public static boolean isHangingSign(SignBlockEntity sign) {
        Block block = sign.getCachedState().getBlock();
        return block instanceof HangingSignTextureProvider;
    }

    public static Identifier getGuiTexture(SignBlockEntity sign) {
        Block block = sign.getCachedState().getBlock();
        if (block instanceof HangingSignTextureProvider provider) {
            return provider.beyond_the_block$getGuiTexture();
        }

        return new Identifier("minecraft", "entity/signs/oak");
    }
}