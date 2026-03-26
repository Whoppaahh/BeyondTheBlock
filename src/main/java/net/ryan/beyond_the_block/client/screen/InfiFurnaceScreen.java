package net.ryan.beyond_the_block.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.screen.handler.InfiFurnaceScreenHandler;

public class InfiFurnaceScreen extends HandledScreen<InfiFurnaceScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/infi_furnace.png");
    private static final Identifier FLAME = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/icons/lit_progress.png");
    private static final Identifier ARROW = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/icons/burn_progress.png");

    public InfiFurnaceScreen(InfiFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // 🔥 Flame (scale down 56x56 to 14x14, i.e., 1/4 scale)
        if (handler.getCookTime() > 0) {
            float scale = 14f / 56f; // ≈ 0.25
            matrices.push();
            matrices.translate(x + 56, y + 54, 0); // Move origin to draw point
            matrices.scale(scale, scale, 1f); // Scale down
            RenderSystem.setShaderTexture(0, FLAME);
            drawTexture(matrices, 0, 0, 0, 0, 56, 56, 56, 56); // Draw full 56x56, scaled down
            matrices.pop();
        }

        // ➡️ Arrow (scale down from 96x64 to something like 24x17)
        int cookTime = handler.getCookTime();
        int cookTimeTotal = handler.getCookTimeTotal();
        if (cookTimeTotal > 0 && cookTime > 0) {
            int textureWidth = 96;
            int textureHeight = 64;

            int drawWidth = (cookTime * textureWidth) / cookTimeTotal;

            float scaleX = 24f / textureWidth; // Desired on-screen width
            float scaleY = 17f / textureHeight;

            matrices.push();
            matrices.translate(x + 79, y + 34, 0);
            matrices.scale(scaleX, scaleY, 1f);
            RenderSystem.setShaderTexture(0, ARROW);
            drawTexture(matrices, 0, 0, 0, 0, drawWidth, textureHeight, textureWidth, textureHeight);
            matrices.pop();
        }
    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}

