package net.ryan.beyond_the_block.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.screen.handler.WoodcutterScreenHandler;

@Environment(EnvType.CLIENT)
public class WoodcutterScreen extends HandledScreen<WoodcutterScreenHandler> {

    // You can keep the vanilla texture if you want the GUI to look identical.
    // If you later make your own texture, replace this with your mod texture path.
    private static final Identifier TEXTURE =
            new Identifier("minecraft", "textures/gui/container/stonecutter.png");

    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private boolean canCraft;

    public WoodcutterScreen(WoodcutterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        this.titleY = 4;
    }

    @Override
    protected void init() {
        super.init();
        this.scrollAmount = 0.0F;
        this.scrollOffset = 0;
        this.canCraft = this.handler.hasRecipes();
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        this.canCraft = this.handler.hasRecipes();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;

        if (this.canCraft) {
            int left = this.x + 52;
            int top = this.y + 14;
            int first = this.scrollOffset + 12;

            for (int i = this.scrollOffset; i < first && i < this.handler.getRecipeCount(); ++i) {
                int visibleIndex = i - this.scrollOffset;
                int buttonX = left + visibleIndex % 4 * 16;
                int buttonY = top + visibleIndex / 4 * 18;

                if (mouseX >= buttonX && mouseX < buttonX + 16 && mouseY >= buttonY && mouseY < buttonY + 18) {
                    if (this.handler.onButtonClick(this.client.player, i)) {
                        if (this.client != null && this.client.interactionManager != null) {
                            this.client.interactionManager.clickButton(this.handler.syncId, i);
                        }
                        this.client.player.playSound(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F, 1.0F);
                    }
                    return true;
                }
            }

            int scrollbarX = this.x + 119;
            int scrollbarY = this.y + 9;
            if (mouseX >= scrollbarX && mouseX < scrollbarX + 12 && mouseY >= scrollbarY && mouseY < scrollbarY + 54) {
                this.mouseClicked = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldShowScrollbar()) {
            int minY = this.y + 14;
            int maxY = minY + 54;
            this.scrollAmount = ((float) mouseY - (float) minY - 7.5F) / ((float) (maxY - minY) - 15.0F);
            this.scrollAmount = Math.max(0.0F, Math.min(1.0F, this.scrollAmount));
            this.scrollOffset = (int) ((double) (this.scrollAmount * (float) this.getMaxScroll()) + 0.5D) * 4;
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.shouldShowScrollbar()) {
            int max = this.getMaxScroll();
            float step = (float) amount / (float) max;
            this.scrollAmount = Math.max(0.0F, Math.min(1.0F, this.scrollAmount - step));
            this.scrollOffset = (int) ((double) (this.scrollAmount * (float) max) + 0.5D) * 4;
        }

        return true;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        int scrollThumbY = (int) (41.0F * this.scrollAmount);
        drawTexture(
                matrices,
                this.x + 119,
                this.y + 15 + scrollThumbY,
                176 + (this.shouldShowScrollbar() ? 0 : 12),
                0,
                12,
                15
        );

        int recipeLeft = this.x + 52;
        int recipeTop = this.y + 14;
        int recipeEnd = this.scrollOffset + 12;
        this.drawRecipeButtons(matrices, mouseX, mouseY, recipeLeft, recipeTop, recipeEnd);
        this.drawRecipeIcons(recipeLeft, recipeTop, recipeEnd);
    }

    private void drawRecipeButtons(MatrixStack matrices, int mouseX, int mouseY, int x, int y, int recipeEndIndex) {
        for (int i = this.scrollOffset; i < recipeEndIndex && i < this.handler.getRecipeCount(); ++i) {
            int visibleIndex = i - this.scrollOffset;
            int buttonX = x + visibleIndex % 4 * 16;
            int buttonY = y + visibleIndex / 4 * 18;
            int u = this.backgroundWidth;

            if (i == this.handler.getSelectedRecipe()) {
                u += 16;
            } else if (mouseX >= buttonX && mouseY >= buttonY && mouseX < buttonX + 16 && mouseY < buttonY + 18) {
                u += 32;
            }

            drawTexture(matrices, buttonX, buttonY - 1, u, 166, 16, 18);
        }
    }

    private void drawRecipeIcons(int x, int y, int recipeEndIndex) {
        for (int i = this.scrollOffset; i < recipeEndIndex && i < this.handler.getRecipeCount(); ++i) {
            int visibleIndex = i - this.scrollOffset;
            int iconX = x + visibleIndex % 4 * 16;
            int iconY = y + visibleIndex / 4 * 18;
            this.client.getItemRenderer().renderInGui(this.handler.getOutputStack(i), iconX, iconY);
        }
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);

        int recipeLeft = this.x + 52;
        int recipeTop = this.y + 14;
        int recipeEnd = this.scrollOffset + 12;

        for (int i = this.scrollOffset; i < recipeEnd && i < this.handler.getRecipeCount(); ++i) {
            int visibleIndex = i - this.scrollOffset;
            int buttonX = recipeLeft + visibleIndex % 4 * 16;
            int buttonY = recipeTop + visibleIndex / 4 * 18;

            if (mouseX >= buttonX && mouseX < buttonX + 16 && mouseY >= buttonY && mouseY < buttonY + 18) {
                this.renderTooltip(matrices, this.handler.getOutputStack(i), mouseX - this.x, mouseY - this.y);
            }
        }
    }

    private boolean shouldShowScrollbar() {
        return this.canCraft && this.handler.getRecipeCount() > 12;
    }

    protected int getMaxScroll() {
        return (this.handler.getRecipeCount() + 4 - 1) / 4 - 3;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
