package net.ryan.beyond_the_block.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.screen.handler.ArmourTrimSmithingScreenHandler;

public class ArmourTrimSmithingScreen extends HandledScreen<ArmourTrimSmithingScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/container/smithing.png");
    private static final Identifier ERROR_TEXTURE =
            new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/container/error.png");

    private ArmorStandEntity previewArmorStand;

    public ArmourTrimSmithingScreen(
            ArmourTrimSmithingScreenHandler handler,
            PlayerInventory inventory,
            Text title
    ) {
        super(handler, inventory, title);
        if (inventory.player != null && inventory.player.world != null) {
            this.previewArmorStand = new net.minecraft.entity.decoration.ArmorStandEntity(
                    inventory.player.world,
                    0.0,
                    0.0,
                    0.0
            );
            this.previewArmorStand.setNoGravity(true);
        }
    }

    private void updatePreviewArmorStand() {
        if (this.previewArmorStand == null) {
            return;
        }

        this.previewArmorStand.equipStack(net.minecraft.entity.EquipmentSlot.HEAD, net.minecraft.item.ItemStack.EMPTY);
        this.previewArmorStand.equipStack(net.minecraft.entity.EquipmentSlot.CHEST, net.minecraft.item.ItemStack.EMPTY);
        this.previewArmorStand.equipStack(net.minecraft.entity.EquipmentSlot.LEGS, net.minecraft.item.ItemStack.EMPTY);
        this.previewArmorStand.equipStack(net.minecraft.entity.EquipmentSlot.FEET, net.minecraft.item.ItemStack.EMPTY);
        this.previewArmorStand.equipStack(net.minecraft.entity.EquipmentSlot.MAINHAND, net.minecraft.item.ItemStack.EMPTY);
        this.previewArmorStand.equipStack(net.minecraft.entity.EquipmentSlot.OFFHAND, net.minecraft.item.ItemStack.EMPTY);

        net.minecraft.item.ItemStack previewStack = this.handler.getSlot(3).hasStack()
                ? this.handler.getSlot(3).getStack()
                : this.handler.getSlot(1).getStack();

        if (previewStack.isEmpty()) {
            return;
        }

        if (previewStack.getItem() instanceof net.minecraft.item.ArmorItem armorItem) {
            this.previewArmorStand.equipStack(armorItem.getSlotType(), previewStack.copy());
        } else {
            this.previewArmorStand.equipStack(net.minecraft.entity.EquipmentSlot.MAINHAND, previewStack.copy());
        }
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title, 44, 15, 0x404040);
        this.textRenderer.draw(matrices, this.playerInventoryTitle, 8, this.backgroundHeight - 94, 0x404040);
    }

    private boolean hasInvalidRecipe() {
        return this.handler.getSlot(0).hasStack()
                && this.handler.getSlot(1).hasStack()
                && this.handler.getSlot(2).hasStack()
                && !this.handler.getSlot(3).hasStack();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        updatePreviewArmorStand();

        if (this.previewArmorStand != null) {
            InventoryScreen.drawEntity(
                    this.x + 141,
                    this.y + 75,
                    25,
                    (float)(this.x + 141) - mouseX,
                    (float)(this.y + 75 - 20) - mouseY,
                    this.previewArmorStand
            );
        }

        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        int x = this.x;
        int y = this.y;

        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        if (hasInvalidRecipe()) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ERROR_TEXTURE);
            drawTexture(matrices, x + 65, y + 46, 0, 0, 28, 21, 28, 21);
        }
    }
}