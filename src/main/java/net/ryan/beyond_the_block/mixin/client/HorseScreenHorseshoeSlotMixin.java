package net.ryan.beyond_the_block.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseScreen.class)
public abstract class HorseScreenHorseshoeSlotMixin
        extends HandledScreen<HorseScreenHandler> {

    private static final Identifier HORSE_TEXTURE =
            new Identifier("textures/gui/container/horse.png");

    protected HorseScreenHorseshoeSlotMixin(
            HorseScreenHandler handler,
            PlayerInventory inventory,
            Text title
    ) {
        super(handler, inventory, title);
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void btb$drawHorseshoeSlot(
            MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci
    ) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // Match logical slot position: new HorseshoeSlot(..., 8, 54)
        int slotX = x + 8;
        int slotY = y + 54;

        RenderSystem.setShaderTexture(0, HORSE_TEXTURE);

        /*
         * Draw vanilla slot graphic from horse.png.
         * These UVs may need tweaking depending on your exact texture.
         * The common slot is 18x18.
         */
        drawTexture(
                matrices,
                slotX - 1,
                slotY - 1,
                0,
                166,
                18,
                18
        );
    }
}
