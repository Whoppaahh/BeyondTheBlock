package net.ryan.beyond_the_block.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class FloatingXPText {
    private String text;
    private float alpha = 1.0f;
    private float scale = 1.0f;
    private int age;
    private final int maxAge = 40; // 2 seconds
    private int pulseTicks = 0;
    private double offsetY = 0;


    public FloatingXPText(int xpAmount) {
        this.text = "+" + xpAmount;
        resetAge();
        triggerPulse();
    }

    public void addXP(int extra) {
        int current = Integer.parseInt(text.replace("+", ""));
        this.text = "+" + (current + extra);
        resetAge();
        triggerPulse();
    }

    public void resetAge() {
        this.age = 0;
    }

    private void triggerPulse() {
        this.pulseTicks = 5; // 5 ticks = 1/4 second
    }

    public boolean isAlive() {
        return age < maxAge;
    }

    public void tick() {
        age++;

        // Fade out over lifetime
        alpha = 1.0f - ((float) age / maxAge);

        // Rise up slightly over time
        offsetY += 0.15;

        // Pulse animation
        if (pulseTicks > 0) {
            pulseTicks--;
            float t = 1.0f - (pulseTicks / 5.0f);
            scale = 1.0f + 0.25f * (float) Math.sin(t * Math.PI);
        } else {
            scale = 1.0f;
        }
    }

    public void render(MatrixStack matrices, int baseX, int baseY, int index) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer tr = client.textRenderer;

        matrices.push();

        float yOffset = (float) (baseY - offsetY - index * 12);
        float alpha255 = alpha * 255.0f;
        int color = 0x00FF00 | ((int) alpha255 << 24);

        matrices.translate(baseX, yOffset, 0);
        matrices.scale(scale, scale, 1.0f);

        tr.drawWithShadow(matrices, text, -tr.getWidth(text) / 2f, 0, color);
        matrices.pop();
    }
}

