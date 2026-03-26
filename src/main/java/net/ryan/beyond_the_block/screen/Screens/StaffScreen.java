package net.ryan.beyond_the_block.screen.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.screen.Handlers.StaffScreenHandler;


public class StaffScreen extends HandledScreen<StaffScreenHandler> {

    private static final Identifier STAFF_GUI_TEXTURE = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/1_slot_container.png");

    public StaffScreen(StaffScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, STAFF_GUI_TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);

    }

    @Override
    protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
        super.drawMouseoverTooltip(matrices, x, y);
        Slot slot = this.handler.getSlot(0); // replace with your slot index

        if (!slot.hasStack() && isPointWithinBounds(slot.x, slot.y, 16, 16, x, y)) {
            renderTooltip(matrices, Text.of("Insert Pickaxe or Axe"), x, y);
        }
    }


}
