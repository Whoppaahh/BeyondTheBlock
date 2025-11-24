package net.ryan.beyond_the_block.mixin.Client;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

            ci.cancel(); // prevent vanilla overlay

    }
}
