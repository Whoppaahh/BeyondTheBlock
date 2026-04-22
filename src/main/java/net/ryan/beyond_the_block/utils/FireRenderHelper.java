package net.ryan.beyond_the_block.utils;


public final class FireRenderHelper {
    private FireRenderHelper() {
    }

    public static float red(int colour) {
        return ((colour >> 16) & 0xFF) / 255.0F;
    }

    public static float green(int colour) {
        return ((colour >> 8) & 0xFF) / 255.0F;
    }

    public static float blue(int colour) {
        return (colour & 0xFF) / 255.0F;
    }
}