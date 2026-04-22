package net.ryan.beyond_the_block.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.feature.hanging_signs.HangingSignClientHelper;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
import java.util.stream.IntStream;

public class HangingSignEditScreen extends Screen {
    private final SignBlockEntity sign;
    private final String[] text;
    private final Identifier texture;

    private int ticksSinceOpened;
    private int currentRow;
    private SelectionManager selectionManager;

    public HangingSignEditScreen(SignBlockEntity sign, boolean filtered) {
        super(Text.translatable("hanging_sign.edit"));
        this.sign = sign;
        this.texture = HangingSignClientHelper.getGuiTexture(sign);
        this.text = IntStream.range(0, 4)
                .mapToObj(row -> sign.getTextOnRow(row, filtered))
                .map(Text::getString)
                .toArray(String[]::new);
    }

    @Override
    protected void init() {
        Objects.requireNonNull(this.client).keyboard.setRepeatEvents(true);

        this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 100,
                this.height / 4 + 144,
                200,
                20,
                ScreenTexts.DONE,
                button -> this.finishEditing()
        ));

        this.sign.setEditable(false);

        this.selectionManager = new SelectionManager(
                () -> this.text[this.currentRow],
                message -> {
                    this.text[this.currentRow] = message;
                    this.sign.setTextOnRow(this.currentRow, Text.literal(message));
                },
                SelectionManager.makeClipboardGetter(this.client),
                SelectionManager.makeClipboardSetter(this.client),
                line -> this.client.textRenderer.getWidth(line) <= 90
        );
    }

    @Override
    public void removed() {
        Objects.requireNonNull(this.client).keyboard.setRepeatEvents(false);

        ClientPlayNetworkHandler networkHandler = this.client.getNetworkHandler();
        if (networkHandler != null) {
            networkHandler.sendPacket(new UpdateSignC2SPacket(
                    this.sign.getPos(),
                    this.text[0],
                    this.text[1],
                    this.text[2],
                    this.text[3]
            ));
        }

        this.sign.setEditable(true);
    }

    @Override
    public void tick() {
        ++this.ticksSinceOpened;
        if (!this.sign.getType().supports(this.sign.getCachedState())) {
            this.finishEditing();
        }
    }

    private void finishEditing() {
        this.sign.markDirty();
        Objects.requireNonNull(this.client).setScreen(null);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        this.selectionManager.insert(chr);
        return true;
    }

    @Override
    public void close() {
        this.finishEditing();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_UP) {
            this.currentRow = (this.currentRow - 1) & 3;
            this.selectionManager.putCursorAtEnd();
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.currentRow = (this.currentRow + 1) & 3;
            this.selectionManager.putCursorAtEnd();
            return true;
        }

        return this.selectionManager.handleSpecialKey(keyCode) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 40, 0xFFFFFF);

        renderSign(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void renderSign(MatrixStack matrices) {
        matrices.push();

        matrices.translate(this.width / 2.0D, 125.0D, 0.0D);

        renderSignBackground(matrices);
        renderSignText(matrices);

        matrices.pop();
    }

    private void renderSignBackground(MatrixStack matrices) {
        matrices.push();

        matrices.translate(0.0D, -13.0D, 0.0D);
        matrices.scale(4.5F, 4.5F, 1.0F);
        RenderSystem.setShaderTexture(0, this.texture);
        drawTexture(matrices, -8, -8, 0, 0, 16, 16, 16, 16);

        matrices.pop();
    }

    private void renderSignText(MatrixStack matrices) {
        matrices.push();

        // Move text slightly in front of the background
        matrices.translate(0.0D, 0.0D, 0.01D);

        // Scale text down to fit the hanging sign properly
        //matrices.scale(0.5F, 0.5F, 1.0F);

        int color = this.sign.getTextColor().getSignColor();
        boolean blink = this.ticksSinceOpened / 6 % 2 == 0;

        int selectionStart = this.selectionManager.getSelectionStart();
        int selectionEnd = this.selectionManager.getSelectionEnd();
        int lineHeight = 10;
        int totalHeightOffset = 4 * lineHeight / 2;
        int cursorY = this.currentRow * lineHeight - totalHeightOffset;

        for (int row = 0; row < this.text.length; ++row) {
            String string = this.text[row];
            if (string != null) {
                if (this.textRenderer.isRightToLeft()) {
                    string = this.textRenderer.mirror(string);
                }

                int drawX = -this.textRenderer.getWidth(string) / 2;
                int drawY = row * lineHeight - totalHeightOffset;

                this.textRenderer.draw(matrices, string, drawX, drawY, color);

                if (row == this.currentRow && selectionStart >= 0 && blink) {
                    int cursorOffset = this.textRenderer.getWidth(
                            string.substring(0, Math.max(Math.min(selectionStart, string.length()), 0))
                    );
                    int cursorX = cursorOffset - this.textRenderer.getWidth(string) / 2;

                    if (selectionStart >= string.length()) {
                        this.textRenderer.draw(matrices, "_", cursorX, cursorY, color);
                    }
                }
            }
        }

        for (int row = 0; row < this.text.length; ++row) {
            String string = this.text[row];
            if (string != null && row == this.currentRow && selectionStart >= 0) {
                int cursorOffset = this.textRenderer.getWidth(
                        string.substring(0, Math.max(Math.min(selectionStart, string.length()), 0))
                );
                int cursorX = cursorOffset - this.textRenderer.getWidth(string) / 2;

                if (blink && selectionStart < string.length()) {
                    fill(matrices, cursorX, cursorY - 1, cursorX + 1, cursorY + 9, 0xFF000000 | color);
                }

                if (selectionEnd != selectionStart) {
                    int a = Math.min(selectionStart, selectionEnd);
                    int b = Math.max(selectionStart, selectionEnd);

                    int left = this.textRenderer.getWidth(string.substring(0, a)) - this.textRenderer.getWidth(string) / 2;
                    int right = this.textRenderer.getWidth(string.substring(0, b)) - this.textRenderer.getWidth(string) / 2;

                    fill(matrices, Math.min(left, right), cursorY, Math.max(left, right), cursorY + 9, 0x800000FF);
                }
            }
        }

        matrices.pop();
    }
}