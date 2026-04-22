package net.ryan.beyond_the_block.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.config.schema.ConfigClient;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.feature.fire.FireColourResolver;
import net.ryan.beyond_the_block.feature.fire.FireRenderHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin {

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void beyond$renderCustomFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (client.player == null || client.world == null) {
            return;
        }

        ConfigClient.Fire fireConfig = Configs.client().visuals.fire;

        int colour = resolveOverlayColour(client.world, client.player);
        float r = FireRenderHelper.red(colour);
        float g = FireRenderHelper.green(colour);
        float b = FireRenderHelper.blue(colour);
        float a = fireConfig.overlayOpacity;
        float heightScale = Math.max(0.1F, fireConfig.overlayHeight);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(r, g, b, a);

        for (int i = 0; i < 2; ++i) {
            RenderSystem.setShaderTexture(0, i == 0 ? new Identifier(BeyondTheBlock.MOD_ID, "textures/block/fire_0.png") : new Identifier(BeyondTheBlock.MOD_ID, "textures/block/fire_1.png"));

            matrices.push();
            matrices.translate((float)(-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float)(i * 2 - 1) * 10.0F));

            Matrix4f matrix = matrices.peek().getPositionMatrix();
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

            float x1 = -0.5F;
            float x2 = 0.5F;
            float y1 = -0.5F * heightScale;
            float y2 = 0.5F * heightScale;

            buffer.vertex(matrix, x1, y1, -0.5F).texture(1.0F, 1.0F).next();
            buffer.vertex(matrix, x2, y1, -0.5F).texture(0.0F, 1.0F).next();
            buffer.vertex(matrix, x2, y2, -0.5F).texture(0.0F, 0.0F).next();
            buffer.vertex(matrix, x1, y2, -0.5F).texture(1.0F, 0.0F).next();

            BufferRenderer.drawWithShader(buffer.end());
            matrices.pop();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        ci.cancel();
    }

    @Unique
    private static int resolveOverlayColour(World world, Entity entity) {
        BlockPos pos = entity.getBlockPos();
        BlockState feet = world.getBlockState(pos);
        boolean soulFire = feet.isOf(Blocks.SOUL_FIRE);
        return FireColourResolver.resolve(world, pos, soulFire);
    }
}