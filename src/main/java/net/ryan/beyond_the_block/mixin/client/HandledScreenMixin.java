package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.client.hud.ScaledTextRenderer;
import org.spongepowered.asm.mixin.Mixin;
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
            method = "render",
            at = @At("TAIL")
    )
    private void renderScaledStackCounts(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        HandledScreen<?> handled = (HandledScreen<?>)(Object)this;

        int screenX = ((HandledScreenAccessor)this).getX();
        int screenY = ((HandledScreenAccessor)this).getY();
        float scale = 0.8f;
        int paddingX = 1;
        int paddingY = 1;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        for (Slot slot : handled.getScreenHandler().slots) {
            ItemStack stack = slot.getStack();
            if (!stack.isEmpty() && stack.getCount() > 1) {
                String text = String.valueOf(stack.getCount());
                ScaledTextRenderer.Segment segment = new ScaledTextRenderer.Segment(text, scale, 0xFFFFFF);
                int scaledWidth = ScaledTextRenderer.getMultiScaledTextWidth(textRenderer,
                        Collections.singletonList(segment));

                matrices.push();
                matrices.translate(0, 0, 300);
                ScaledTextRenderer.drawMultiScaledText(
                        matrices,
                        textRenderer,
                        slot.x + screenX + 16 - scaledWidth - paddingX,
                        slot.y + screenY + 16 - (int)(textRenderer.fontHeight * scale) - paddingY,
                        Collections.singletonList(segment)
                );
                matrices.pop();
            }
        }

        // Cursor stack
        ItemStack cursorStack = handled.getScreenHandler().getCursorStack();
        if (!cursorStack.isEmpty() && cursorStack.getCount() > 1) {
            String text = String.valueOf(cursorStack.getCount());
            ScaledTextRenderer.Segment segment = new ScaledTextRenderer.Segment(text, scale, 0xFFFFFF);
            int scaledWidth = ScaledTextRenderer.getMultiScaledTextWidth(textRenderer,
                    Collections.singletonList(segment));

            matrices.push();
            matrices.translate(0, 0, 500);
            ScaledTextRenderer.drawMultiScaledText(
                    matrices,
                    textRenderer,
                    mouseX + 10 - scaledWidth - paddingX,
                    mouseY + 10 - (int)(textRenderer.fontHeight * scale) - paddingY,
                    Collections.singletonList(segment)
            );
            matrices.pop();
        }
    }
}

