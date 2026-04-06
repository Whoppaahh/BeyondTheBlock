package net.ryan.beyond_the_block.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.screen.handler.WoodcutterScreenHandler;

@Environment(EnvType.CLIENT)
public class WoodcutterScreen extends HandledScreen<WoodcutterScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier("minecraft", "textures/gui/container/stonecutter.png");

    public WoodcutterScreen(WoodcutterScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundHeight = 166;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }
}
