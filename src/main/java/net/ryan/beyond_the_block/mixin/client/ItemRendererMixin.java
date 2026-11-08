package net.ryan.beyond_the_block.mixin.client;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.ryan.beyond_the_block.client.visual.GlowManager;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(
            method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void adjustVanillaOverlay(TextRenderer textRenderer, ItemStack stack, int x, int y,
                                     @Nullable String countLabel, CallbackInfo ci) {
        // If we are in a screen where we render custom quantities (handled in HandledScreenMixin),
        // we should cancel vanilla if count > 1.
        // If countLabel is NOT null, it means vanilla (or another mod) is trying to render a CUSTOM label
        // (like trade price adjustments). In that case, we should only cancel if it's actually an item count
        // we are replacing. Since we only replace count > 1, we let vanilla handle the rest.

        if (stack.getCount() > 1 && countLabel == null) {
            // Only cancel if we are in a screen where HandledScreenMixin will take over,
            // or if it's the InGameHud (which also handles it).
            // This ensures that screens like the MerchantScreen (trade screen) still show
            // counts if they are not being covered by our custom renderer.
            
            boolean isHandledScreen = MinecraftClient.getInstance().currentScreen instanceof HandledScreen;
            boolean isMerchantScreen = MinecraftClient.getInstance().currentScreen instanceof MerchantScreen;
            boolean isInGameHud = MinecraftClient.getInstance().currentScreen == null;

            if ((isHandledScreen && !isMerchantScreen) || isInGameHud) {
                ci.cancel();
            }
        }
    }

    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/item/ItemStack;" +
                    "Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;" +
                    "ZLnet/minecraft/client/util/math/MatrixStack;" +
                    "Lnet/minecraft/client/render/VertexConsumerProvider;" +
                    "IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0 // this is the FIRST int (light)
    )
    private int injectGlowLight(int light, ItemStack stack) {

        if (GlowManager.shouldGlow(stack)) {
            return GlowManager.FULL_BRIGHT;
        }

        return light;
    }
}
