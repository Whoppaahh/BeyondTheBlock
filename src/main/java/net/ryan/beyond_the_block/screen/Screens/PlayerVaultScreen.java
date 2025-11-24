package net.ryan.beyond_the_block.screen.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.screen.Handlers.PlayerVaultScreenHandler;

public class PlayerVaultScreen extends HandledScreen<PlayerVaultScreenHandler> {

    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/generic_54.png");


    public PlayerVaultScreen(PlayerVaultScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, handler.getTitle());
        this.backgroundHeight = 114 + 6 * 18;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        // Render the chest background (you can customize the texture if needed)
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
    }

}

