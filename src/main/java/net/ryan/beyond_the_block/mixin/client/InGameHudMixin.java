package net.ryan.beyond_the_block.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.ryan.beyond_the_block.client.hud.FloatingXPManager;
import net.ryan.beyond_the_block.client.hud.ScaledTextRenderer;
import net.ryan.beyond_the_block.content.registry.ModEffects;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Collections;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private int scaledHeight;
    @Final
    @Shadow
    private ItemRenderer itemRenderer;


    @Inject(method = "render", at = @At("TAIL"))
    private void renderOverlay(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        Identifier powderOverlay = new Identifier("textures/misc/powder_snow_outline.png");
        Identifier bleedOverlay = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/blood_overlay.png");

        if(client.player == null)return;
        if (client.player.hasStatusEffect(ModEffects.FREEZE)) {
            renderThisOverlay(client, powderOverlay, 0.5f);
        }
        if (client.player.hasStatusEffect(ModEffects.BLEED)) {
            StatusEffectInstance bleed = client.player.getStatusEffect(ModEffects.BLEED);
            int duration = bleed != null ? bleed.getDuration() : 0;
            float alpha = 0;
            if (bleed != null) {
                alpha = getAlpha(bleed, duration);
            }

            renderThisOverlay(client, bleedOverlay, alpha);
        }

    }

    @Unique
    private static float getAlpha(StatusEffectInstance bleed, int duration) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;
        if (player == null) return 0f;
        // Health scale: 1.0 = full health, 0.0 = dead
        float healthRatio = player.getHealth() / player.getMaxHealth();
        // Invert: 0.0 = full health, 1.0 = nearly dead
        float intensity = 1.0f - healthRatio;
        // Clamp to prevent fully invisible or fully opaque
        intensity = MathHelper.clamp(intensity, 0.1f, 0.9f); // Always visible, never too strong
        // Add smooth pulse (sine wave oscillating between 0.85–1.0)
        float pulse = 0.925f + 0.075f * MathHelper.sin((player.age + duration) * 0.15f);
        // Final alpha: intensity-based strength * pulse factor
        float alpha = intensity * pulse;
        return MathHelper.clamp(alpha, 0.1f, 0.95f);
    }




    @Unique
    private void renderThisOverlay(MinecraftClient client, Identifier overlay, float alpha){
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, overlay);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha); // Semi-transparent

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(0, height, -90).texture(0.0F, 1.0F).next();
        buffer.vertex(width, height, -90).texture(1.0F, 1.0F).next();
        buffer.vertex(width, 0, -90).texture(1.0F, 0.0F).next();
        buffer.vertex(0, 0, -90).texture(0.0F, 0.0F).next();
        tessellator.draw();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
    @Inject(at = @At("RETURN"), method = "renderExperienceBar")
    public void renderExperienceProgress(MatrixStack matrixStack, int x, CallbackInfo info) {
        PlayerEntity player = this.client.player;
        if (player == null) return;

        //Displaying currentXP and XP needed for next level
        int experienceNeeded = player.getNextLevelExperience();
        float currentProgress = player.experienceProgress;
        int currentExperience = (int) (currentProgress * experienceNeeded);

        int x1 = x - 2;
        int x2 = x + 182 + 2;
        int y = this.scaledHeight - 32 + 4;

        // Get the color based on progress
        int progressColor = getProgressColor(currentProgress);

        this.renderNumber(matrixStack, String.valueOf(currentExperience), x1, y, true, progressColor);
        this.renderNumber(matrixStack, String.valueOf(experienceNeeded), x2, y, false, Color.WHITE.getRGB());
        FloatingXPManager.render(matrixStack);
    }

    // Shift *all* 5 TextRenderer.draw(...) calls in renderExperienceBar
    @ModifyArg(
            method = "renderExperienceBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"
            ),
            index = 3, // argument index for Y position
            require = 1,
            allow = 5
    )
    private float beyond_the_block$shiftXPTextUp(float originalY) {
        // Move the vanilla XP level text up a bit
        return originalY - 2.5F; // adjust this offset as you prefer
    }

    @Unique
    private void renderNumber(
            MatrixStack matrixStack,
            String number,
            int x,
            int y,
            boolean rightAligned,
            int color) {
        TextRenderer textRenderer = this.client.textRenderer;
        int renderX = x + (rightAligned ? -textRenderer.getWidth(number) : 0);
        int renderY = y + Math.round((5 - textRenderer.fontHeight + 2) / 2f);

        textRenderer.draw(matrixStack, number, renderX + 1, renderY, Color.BLACK.getRGB());
        textRenderer.draw(matrixStack, number, renderX - 1, renderY, Color.BLACK.getRGB());
        textRenderer.draw(matrixStack, number, renderX, renderY + 1, Color.BLACK.getRGB());
        textRenderer.draw(matrixStack, number, renderX, renderY - 1, Color.BLACK.getRGB());

        textRenderer.draw(matrixStack, number, renderX, renderY, color);

    }
    @Unique
    private int getProgressColor(float progress) {
        progress = Math.min(Math.max(progress, 0f), 1f); // clamp 0-1

        // Define key colors:
        int white = 0xFFFFFFFF;
        int yellow = 0xFFFFFF00;
        int green = 0xFF00FF00;

        // We'll interpolate based on the closest segment
        float t;
        int startColor;
        int endColor;

        if (progress < 0.5f) {
            // First half: white -> yellow
            t = progress / 0.5f;
            startColor = white;
            endColor = yellow;
        } else {
            // Second half: yellow -> green
            t = (progress - 0.5f) / 0.5f;
            startColor = yellow;
            endColor = green;
        }

        return lerpColor(startColor, endColor, t);
    }

    @Unique
    private int lerpColor(int startColor, int endColor, float t) {
        int startR = (startColor >> 16) & 0xFF;
        int startG = (startColor >> 8) & 0xFF;
        int startB = startColor & 0xFF;

        int endR = (endColor >> 16) & 0xFF;
        int endG = (endColor >> 8) & 0xFF;
        int endB = endColor & 0xFF;

        int r = (int) (startR + (endR - startR) * t);
        int g = (int) (startG + (endG - startG) * t);
        int b = (int) (startB + (endB - startB) * t);

        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    @Unique
    private void drawScaledItemCount(MatrixStack matrices, int x, int y, int count, float scale) {
        if (count <= 1) return;

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        String text = String.valueOf(count);
        ScaledTextRenderer.Segment segment = new ScaledTextRenderer.Segment(text, scale, 0xFFFFFF);
        int scaledWidth = ScaledTextRenderer.getMultiScaledTextWidth(textRenderer, Collections.singletonList(segment));

        matrices.push();
        matrices.translate(0, 0, 500); // above items
        ScaledTextRenderer.drawMultiScaledText(
                matrices,
                textRenderer,
                x + 16 - scaledWidth - 1,
                y + 16 - (int)(textRenderer.fontHeight * scale) - 1,
                Collections.singletonList(segment)
        );
        matrices.pop();
    }


    @Inject(method = "renderHotbarItem", at = @At("HEAD"), cancellable = true)
    private void injectRenderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci) {
        if (stack.isEmpty()) return;

        if (client.player != null) {
            // Render item normally
            this.itemRenderer.renderInGuiWithOverrides(player, stack, x, y, seed);

            // Render custom scaled overlay
            drawScaledItemCount(new MatrixStack(), x, y, stack.getCount(), 0.8f);

            ci.cancel(); // prevent vanilla code from running
        }
    }

}
