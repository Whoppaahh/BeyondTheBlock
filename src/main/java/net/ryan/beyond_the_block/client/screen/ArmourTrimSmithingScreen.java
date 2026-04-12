package net.ryan.beyond_the_block.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.screen.handler.ArmourTrimSmithingScreenHandler;

public class ArmourTrimSmithingScreen extends ForgingScreen<ArmourTrimSmithingScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/container/smithing.png");

    public ArmourTrimSmithingScreen(
            ArmourTrimSmithingScreenHandler handler,
            PlayerInventory inventory,
            Text title
    ) {
        super(handler, inventory, title, TEXTURE);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = this.x;
        int y = this.y;

        drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        if (!this.handler.getSlot(0).hasStack()) {
            drawTexture(matrices, x + 8, y + 48, 0, 84, 16, 16);
        }

        if (!this.handler.getSlot(1).hasStack()) {
            drawTexture(matrices, x + 26, y + 48, 16, 84, 16, 16);
        }

        if (!this.handler.getSlot(2).hasStack()) {
            drawTexture(matrices, x + 44, y + 48, 32, 84, 16, 16);
        }

        if (this.handler.getSlot(3).hasStack()) {
            drawTexture(matrices, x + 65, y + 46, 48, 84, 28, 21);
        }
    }
}