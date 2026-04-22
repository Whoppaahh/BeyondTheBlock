package net.ryan.beyond_the_block.feature.fire;

import net.minecraft.client.render.VertexConsumer;

public class TintingVertexConsumer implements VertexConsumer {
    private final VertexConsumer delegate;
    private final float tintR;
    private final float tintG;
    private final float tintB;

    private int lastR = 255;
    private int lastG = 255;
    private int lastB = 255;
    private int lastA = 255;

    public TintingVertexConsumer(VertexConsumer delegate, int colour) {
        this.delegate = delegate;
        this.tintR = ((colour >> 16) & 0xFF) / 255.0F;
        this.tintG = ((colour >> 8) & 0xFF) / 255.0F;
        this.tintB = (colour & 0xFF) / 255.0F;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        delegate.vertex(x, y, z);
        return this;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        this.lastR = clampColour(Math.round(red * tintR));
        this.lastG = clampColour(Math.round(green * tintG));
        this.lastB = clampColour(Math.round(blue * tintB));
        this.lastA = alpha;

        delegate.color(lastR, lastG, lastB, lastA);
        return this;
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        delegate.texture(u, v);
        return this;
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        delegate.overlay(u, v);
        return this;
    }

    @Override
    public VertexConsumer light(int u, int v) {
        delegate.light(u, v);
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        delegate.normal(x, y, z);
        return this;
    }

    @Override
    public void next() {
        delegate.next();
    }

    @Override
    public void fixedColor(int red, int green, int blue, int alpha) {
        delegate.fixedColor(
                clampColour(Math.round(red * tintR)),
                clampColour(Math.round(green * tintG)),
                clampColour(Math.round(blue * tintB)),
                alpha
        );
    }

    @Override
    public void unfixColor() {
        delegate.unfixColor();
    }

    private static int clampColour(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
