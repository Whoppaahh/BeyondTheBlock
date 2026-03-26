package net.ryan.beyond_the_block.content.entity.Render;

import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.ryan.beyond_the_block.content.entity.CobwebProjectileEntity;

public class CobwebProjectileRenderer extends EntityRenderer<CobwebProjectileEntity> {
    private final BlockRenderManager blockRenderManager;

    public CobwebProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.blockRenderManager = ctx.getBlockRenderManager();
    }

    @Override
    public void render(CobwebProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // Move to entity position
        matrices.translate(0.0, 0.0, 0.0);

        // Rotate to face camera
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(yaw));

        // Scale down to fit projectile size
        float scale = 0.5f;
        matrices.scale(scale, scale, scale);

        // Render the cobweb block
        blockRenderManager.renderBlockAsEntity(Blocks.COBWEB.getDefaultState(), matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(CobwebProjectileEntity entity) {
        return new Identifier("minecraft", "textures/block/cobweb.png");
    }
}
