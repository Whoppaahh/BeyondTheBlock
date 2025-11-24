package net.ryan.beyond_the_block.utils.GUI;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

public class ScaledTextRenderer {

    public static void drawMultiScaledText(MatrixStack matrices, TextRenderer renderer, float x, float y,
                                           List<Segment> segments) {
        matrices.push();
        float currentX = x;

        for (Segment segment : segments) {
            matrices.push();
            matrices.translate(currentX, y, 0);
            matrices.scale(segment.scale, segment.scale, 1.0f);

            renderer.draw(matrices, segment.text, 0, 0, segment.color);

            // Advance position correctly based on the scaled width
            currentX += renderer.getWidth(segment.text) * segment.scale;

            matrices.pop();
        }

        matrices.pop();
    }
    public static int getMultiScaledTextWidth(TextRenderer renderer, List<Segment> segments) {
        int totalWidth = 0;
        for (Segment segment : segments) {
            totalWidth += (int) (renderer.getWidth(segment.text) * segment.scale);
        }
        return totalWidth;
    }

    public static class Segment {
        public final String text;
        public final float scale;
        public final int color;

        public Segment(String text, float scale, int color) {
            this.text = text;
            this.scale = scale;
            this.color = color;
        }
    }
}
