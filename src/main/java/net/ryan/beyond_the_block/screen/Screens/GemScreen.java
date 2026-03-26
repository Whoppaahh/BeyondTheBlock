package net.ryan.beyond_the_block.screen.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.screen.Handlers.GemScreenHandler;


public class GemScreen extends HandledScreen<GemScreenHandler> {

    private static final Identifier GEM_GUI_TEXTURE = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/gem_station.png");

    public GemScreen(GemScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GEM_GUI_TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // Check if the input is locked
        if (handler.isInputLocked()) {
            // Get slot coordinates
            Slot inputSlot = handler.slots.get(0);
            int slotX = x + inputSlot.x;
            int slotY = y + inputSlot.y;

            // Draw translucent red overlay
            fill(matrices, slotX, slotY, slotX + 16, slotY + 16, 0x80FF0000);
        }
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {

        Slot input = this.handler.getSlot(0);
        Slot gem1 = this.handler.getSlot(1);
        Slot gem2 = this.handler.getSlot(2);
        Slot gem3 = this.handler.getSlot(3);
        Slot output = this.handler.getSlot(4);

        if(handler.isInputLocked() && isPointWithinBounds(input.x, input.y, 16, 16, x, y)){
            renderTooltip(matrices, Text.of("Cannot remove input now changes have been made."), x, y);
            return;
        }
        if (!input.hasStack() && isPointWithinBounds(input.x, input.y, 16, 16, x, y)) {
            renderTooltip(matrices, Text.of("Insert Sword"), x, y);
        }
        if (!gem1.hasStack() && isPointWithinBounds(gem1.x, gem1.y, 16, 16, x, y)) {
            renderTooltip(matrices, Text.of("Insert Gem"), x, y);
        }
        if (!gem2.hasStack() && isPointWithinBounds(gem2.x, gem2.y, 16, 16, x, y)) {
            renderTooltip(matrices, Text.of("Insert Gem"), x, y);
        }
        if (!gem3.hasStack() && isPointWithinBounds(gem3.x, gem3.y, 16, 16, x, y)) {
            renderTooltip(matrices, Text.of("Insert Gem"), x, y);
        }
        if (!output.hasStack() && isPointWithinBounds(output.x, output.y, 16, 16, x, y)) {
            renderTooltip(matrices, Text.of("No Changes."), x, y);
        }
        super.drawMouseoverTooltip(matrices, x, y);
    }


}
