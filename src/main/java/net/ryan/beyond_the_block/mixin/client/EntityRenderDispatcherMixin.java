package net.ryan.beyond_the_block.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.feature.fire.FireColourResolver;
import net.ryan.beyond_the_block.feature.fire.FireRenderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @Inject(
            method = "renderFire",
            at = @At("HEAD"),
            cancellable = true)

    private void beyond$applyFireTint(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            Entity entity,
            CallbackInfo ci
    ) {
        if (entity.world == null) return;

        BlockPos pos = entity.getBlockPos();
        World world = entity.world;

        BlockState below = world.getBlockState(pos.down());
        boolean soulFire = below.isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS);

        int colour = FireColourResolver.resolve(world, pos, soulFire);

        float r = FireRenderHelper.red(colour);
        float g = FireRenderHelper.green(colour);
        float b = FireRenderHelper.blue(colour);

        float width = entity.getWidth() * 1.4F;
        float height = entity.getHeight() * 1.4F;

        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-45.0F));

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(r, g, b, 1.0F);

        for (int i = 0; i < 2; ++i) {
            RenderSystem.setShaderTexture(0, i == 0 ? new Identifier(BeyondTheBlock.MOD_ID, "textures/block/fire_0.png") : new Identifier(BeyondTheBlock.MOD_ID, "textures/block/fire_1.png"));

            Matrix4f matrix = matrices.peek().getPositionMatrix();
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

            float x1 = -width / 2.0F;
            float x2 = width / 2.0F;
            float y1 = 0.0F;
            float y2 = height;

            buffer.vertex(matrix, x1, y1, 0.0F).texture(1.0F, 1.0F).next();
            buffer.vertex(matrix, x2, y1, 0.0F).texture(0.0F, 1.0F).next();
            buffer.vertex(matrix, x2, y2, 0.0F).texture(0.0F, 0.0F).next();
            buffer.vertex(matrix, x1, y2, 0.0F).texture(1.0F, 0.0F).next();

            BufferRenderer.drawWithShader(buffer.end());
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();

        ci.cancel();
    }

    @Inject(
            method = "renderFire",
            at = @At("TAIL")
    )
    private void beyond$resetFireTint(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            Entity entity,
            CallbackInfo ci
    ) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}