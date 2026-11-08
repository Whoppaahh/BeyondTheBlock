package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.client.hud.ScaledTextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen {

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(
            method = "drawItem",
            at = @At("TAIL")
    )
    private void renderCursorStackCount(
            ItemStack stack,
            int x,
            int y,
            String amountText,
            CallbackInfo ci
    ) {
        if (stack.isEmpty() || stack.getCount() <= 1) {
            return;
        }

        // Avoid rendering counts for temporary touchscreen drag stacks.
        HandledScreen<?> handled = (HandledScreen<?>) (Object) this;

        ItemStack cursorStack = handled.getScreenHandler().getCursorStack();

        if (cursorStack.isEmpty() || cursorStack.getCount() <= 1) {
            return;
        }

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        MatrixStack matrices = new MatrixStack();
        matrices.translate(0, 0, 375);

        drawCustomCount(
                matrices,
                textRenderer,
                stack.getCount(),
                x,
                y,
                0.8f,
                1,
                1
        );
    }

    @Inject(
            method = "drawMouseoverTooltip",
            at = @At("HEAD")
    )
    private void renderScaledStackCounts(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci) {
        renderCustomStackCounts(matrices, mouseX, mouseY);
    }

    @Unique
    private void renderCustomStackCounts(MatrixStack matrices, int mouseX, int mouseY) {
        HandledScreen<?> handled = (HandledScreen<?>) (Object) this;

        HandledScreenAccessor accessor = (HandledScreenAccessor) this;

        int screenX = accessor.getX();
        int screenY = accessor.getY();

        float scale = 0.8f;
        int paddingX = 1;
        int paddingY = 1;

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        matrices.push();
        matrices.translate(0, 0, 375);

        // Inventory and container slots
        for (Slot slot : handled.getScreenHandler().slots) {
            ItemStack stack = slot.getStack();

            if (!stack.isEmpty() && stack.getCount() > 1
                    && !(handled instanceof MerchantScreen)) {

                int slotX = slot.x + screenX;
                int slotY = slot.y + screenY;

                drawCustomCount(
                        matrices,
                        textRenderer,
                        stack.getCount(),
                        slotX,
                        slotY,
                        scale,
                        paddingX,
                        paddingY
                );
            }
        }
        matrices.pop();
    }

    @Unique
    private void drawCustomCount(
            MatrixStack matrices,
            TextRenderer textRenderer,
            int count,
            int x,
            int y,
            float scale,
            int paddingX,
            int paddingY
    ) {
        String text = String.valueOf(count);

        ScaledTextRenderer.Segment segment =
                new ScaledTextRenderer.Segment(text, scale, 0xFFFFFF);

        int scaledWidth = ScaledTextRenderer.getMultiScaledTextWidth(
                textRenderer,
                Collections.singletonList(segment)
        );

        matrices.push();

        //matrices.translate(0, 0, 300);

        ScaledTextRenderer.drawMultiScaledText(
                matrices,
                textRenderer,
                x + 16 - scaledWidth - paddingX,
                y + 16 - (int) (textRenderer.fontHeight * scale) - paddingY,
                Collections.singletonList(segment)
        );

        matrices.pop();
    }
}

