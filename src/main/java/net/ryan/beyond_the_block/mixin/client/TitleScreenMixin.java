package net.ryan.beyond_the_block.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Unique
    private static final Identifier CUSTOM_LOGO = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/mod_title.png");
    @Unique
    private static final Identifier TRANSPARENT = new Identifier(BeyondTheBlock.MOD_ID, "textures/gui/transparent.png");


    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V",
                    ordinal = 1 // title texture
            )
    )
    private Identifier modifyTitleTexture(Identifier original) {
        return Configs.client().visuals.title.customLogo ?
                TRANSPARENT : original;

    }
    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V",
                    ordinal = 2 // edition texture
            )
    )
    private Identifier modifyEditionTexture(Identifier original) {
        return Configs.client().visuals.title.customLogo ?
                TRANSPARENT : original;

    }
    // Inject your own logo rendering
    @Inject(method = "render", at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V",
            ordinal = 1, // right before vanilla title
            shift = At.Shift.BEFORE
    ))
    private void renderCustomLogo(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(!Configs.client().visuals.title.customLogo) return;

        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();

        int logoWidth = 315;
        int logoHeight = 75;

        int x = (screenWidth - logoWidth) / 2;
        int y = 25;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CUSTOM_LOGO);
        RenderSystem.enableBlend(); // ensure blending is enabled
        RenderSystem.blendFunc(1, 771); // vanilla blend mode
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, logoWidth, logoHeight, logoWidth, logoHeight);
    }
}
