package net.ryan.beyond_the_block.utils.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.BeyondTheBlock;
import org.jetbrains.annotations.NotNull;

public class IconButton extends ButtonWidget {
    private ItemStack icon;
    private boolean selected = false;
    private boolean isUpgrade = false;
    private boolean isLeft = true;

    public IconButton(int x, int y, Item item, boolean isUpgrade, boolean isLeft, PressAction onPress) {
        super(x, y, 30, 25, Text.empty(), onPress);
        this.icon = new ItemStack(item);
        this.isUpgrade = isUpgrade;
        this.isLeft = isLeft;
    }

    public IconButton(int x, int y, Item item, PressAction onPress) {
        super(x, y, 30, 25, Text.empty(), onPress);
        this.icon = new ItemStack(item);
    }

    public void setIcon(Item item){
        this.icon = new ItemStack(item);
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean getSelected(){
        return this.selected;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            Identifier texture = getTexture();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, texture);

            matrices.push();
            matrices.translate(x, y, 0); // Move to screen position
            if (!this.isUpgrade) {
                matrices.scale(0.27f, 0.2f, 1f); // Scale down texture to fit in GUI
                drawTexture(matrices, 0, 0, 0, 0, 128, 112, 128, 112);
            } else {
                matrices.scale(0.35f, 0.35f, 1f); // Scale down texture to fit in GUI
                drawTexture(matrices, 0, 0, 0, 0, 104, 104, 104, 104);

            }
            matrices.pop();
            // Render the item as the button icon
            if (!this.isUpgrade) {
                client.getItemRenderer().renderInGui(icon, this.x + 10, this.y + 3);
            } else {
                client.getItemRenderer().renderInGui(icon, this.x + 10, this.y + 10);
            }
        }
    }

    private @NotNull Identifier getTexture() {
        Identifier texture;
        // Draw a simple button background
        if (this.selected && !this.isUpgrade) {
            texture = this.isLeft ? new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/tab_left_selected.png") : new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/tab_right_selected.png");
        } else if (this.isUpgrade) {
            texture = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/upgrade_button.png");
        } else {
            texture = this.isLeft ? new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/tab_left_unselected.png") : new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/tab_right_unselected.png");
        }
        return texture;
    }
}
