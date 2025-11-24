package net.ryan.beyond_the_block.utils.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static net.minecraft.client.gui.DrawableHelper.drawTexture;

public class ScrollBarWidget {
    private final int x;
    private final int y;
    private final int height;
    private int totalItems;
    private final int itemsPerPage;
    private float scrollAmount = 0.0F;
    private boolean scrolling = false;
    private int scrollOffset = 0;

    public ScrollBarWidget(int x, int y, int height, int totalItems, int itemsPerPage) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.totalItems = totalItems;
        this.itemsPerPage = itemsPerPage;
    }

    public void render(MatrixStack matrices, MinecraftClient client) {
        // Draw the scrollbar background (static)
        RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/gui/container/creative_inventory/tab_items.png"));
        // Draw scrollbar background
        drawTexture(matrices, x, y, 174, 16, 16, height, 256, 256);

        // Draw handle
        int handleHeight = getHandleHeight();
        int handleY = y + (int)((height - handleHeight) * scrollAmount);

        //EmeraldEmpire.LOGGER.info("Expected bottom: " + (y + height - handleHeight));
       // EmeraldEmpire.LOGGER.info("Actual handleY: " + handleY);

        // Draw the scrollbar handle (movable part)
        RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/gui/container/creative_inventory/tabs.png"));
        drawTexture(matrices, x - 3, handleY + 2, 228, 0, 16, handleHeight + 2, 256, 256); // Handle texture (you can adjust the texture region based on hover or style)
        //fill(matrices, x - 3, handleY, x + 13, handleY + handleHeight, 0x66FF0000); // translucent red overlay
        //fill(matrices, x, y, x + 1, y + height, 0xFF00FF00); // Green vertical line at scrollbar X

    }

    private int getMaxScrollOffset() {
        return Math.max(0, totalItems - itemsPerPage);
    }

    private int getHandleHeight() {
        float ratio = (float) itemsPerPage / (float) totalItems;
       // EmeraldEmpire.LOGGER.info("Handle Height: " + height * ratio);
        return MathHelper.clamp((int)(height * ratio), 20, 20);
    }

    public void onMouseScroll(double amount) {
        float step = 1.0F / Math.max(1, getMaxScrollOffset());
        scrollAmount += (float) (-amount * step);
        scrollAmount = MathHelper.clamp(scrollAmount, 0.0F, 1.0F);
        updateOffset();
    }

    public boolean onMouseClicked(double mouseX, double mouseY) {
        if (isInside(mouseX, mouseY)) {
            scrolling = true;
            return true;
        }
        return false;
    }

    public boolean onMouseReleased(double mouseX, double mouseY) {
        scrolling = false;
        return false;
    }

    public boolean onMouseDragged(double mouseX, double mouseY) {
        if (scrolling) {
            float relative = (float)(mouseY - y) / (float)(height - getHandleHeight());
            scrollAmount = MathHelper.clamp(relative, 0.0F, 1.0F);
            updateOffset();
            return true;
        }
        return false;
    }

    private void updateOffset() {
        scrollOffset = (int)(scrollAmount * getMaxScrollOffset() + 0.5);
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void updateItemCount(int totalItems) {
        this.totalItems = totalItems;
        this.scrollOffset = 0;
        this.scrollAmount = 0.0F;
    }

    private boolean isInside(double mouseX, double mouseY) {
        return mouseX >= x && mouseX < x + 12 && mouseY >= y && mouseY < y + height;
    }

}
