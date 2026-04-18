package net.ryan.beyond_the_block.client.render.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.ryan.beyond_the_block.content.entity.MinecartChainLinkEntity;

import java.util.List;
import java.util.UUID;

public class MinecartChainLinkRenderer extends EntityRenderer<MinecartChainLinkEntity> {

    private static final ItemStack CHAIN_STACK = new ItemStack(Items.CHAIN);

    private static final int SEGMENTS = 10;
    private static final float SAG = 0.04f;
    private static final float SCALE = 0.35f;

    // Push the chain anchors out from the minecart body
    private static final double HORIZONTAL_OFFSET = 0.65D;
    private static final double VERTICAL_OFFSET = 0.18D;

    public MinecartChainLinkRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(
            MinecartChainLinkEntity entity,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light
    ) {
        Entity first = getClientEntity(entity.getFirstCart(), entity);
        Entity second = getClientEntity(entity.getSecondCart(), entity);

        if (!(first instanceof AbstractMinecartEntity firstCart) || !(second instanceof AbstractMinecartEntity secondCart)) {
            return;
        }

        Vec3d firstAnchor = getAnchor(firstCart, secondCart, tickDelta);
        Vec3d secondAnchor = getAnchor(secondCart, firstCart, tickDelta);

        Vec3d base = entity.getLerpedPos(tickDelta);
        Vec3d from = firstAnchor.subtract(base);
        Vec3d to = secondAnchor.subtract(base);

        renderChain(matrices, vertexConsumers, from, to, light);
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private static Vec3d getAnchor(AbstractMinecartEntity source, AbstractMinecartEntity target, float tickDelta) {
        Vec3d sourcePos = source.getLerpedPos(tickDelta);
        Vec3d targetPos = target.getLerpedPos(tickDelta);

        Vec3d toward = targetPos.subtract(sourcePos);
        if (toward.lengthSquared() < 1.0E-6D) {
            toward = new Vec3d(0.0D, 0.0D, 1.0D);
        } else {
            toward = toward.normalize();
        }

        return sourcePos
                .add(toward.multiply(HORIZONTAL_OFFSET))
                .add(0.0D, VERTICAL_OFFSET, 0.0D);
    }

    private static Entity getClientEntity(UUID uuid, Entity around) {
        if (uuid == null || MinecraftClient.getInstance().world == null) {
            return null;
        }

        List<Entity> entities = MinecraftClient.getInstance().world.getOtherEntities(
                around,
                new Box(
                        around.getX() - 128.0D, around.getY() - 128.0D, around.getZ() - 128.0D,
                        around.getX() + 128.0D, around.getY() + 128.0D, around.getZ() + 128.0D
                ),
                e -> uuid.equals(e.getUuid())
        );

        return entities.isEmpty() ? null : entities.get(0);
    }

    private static void renderChain(
            MatrixStack matrices,
            VertexConsumerProvider consumers,
            Vec3d from,
            Vec3d to,
            int light
    ) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        Vec3d delta = to.subtract(from);

        if (delta.lengthSquared() < 1.0E-6D) {
            return;
        }

        for (int i = 0; i <= SEGMENTS; i++) {
            float t = i / (float) SEGMENTS;
            Vec3d pos = getPoint(from, to, t);

            float prevT = Math.max(0.0f, t - (1.0f / SEGMENTS));
            float nextT = Math.min(1.0f, t + (1.0f / SEGMENTS));

            Vec3d prev = getPoint(from, to, prevT);
            Vec3d next = getPoint(from, to, nextT);
            Vec3d tangent = next.subtract(prev);

            if (tangent.lengthSquared() < 1.0E-6D) {
                tangent = delta;
            }

            tangent = tangent.normalize();

            float segYaw = (float) Math.atan2(tangent.x, tangent.z);
            float horizontalLen = (float) Math.sqrt(tangent.x * tangent.x + tangent.z * tangent.z);
            float segPitch = (float) -Math.atan2(tangent.y, horizontalLen);

            matrices.push();
            matrices.translate(pos.x, pos.y, pos.z);
            matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(segYaw));
            matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(segPitch));

            // Rotate so the chain item lies along the connection direction instead of standing upright
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));


            matrices.scale(SCALE, SCALE, SCALE);

            itemRenderer.renderItem(
                    CHAIN_STACK,
                    ModelTransformation.Mode.FIXED,
                    light,
                    OverlayTexture.DEFAULT_UV,
                    matrices,
                    consumers,
                    0
            );

            matrices.pop();
        }
    }

    private static Vec3d getPoint(Vec3d from, Vec3d to, float t) {
        Vec3d delta = to.subtract(from);

        double sagAmount = Math.sin(Math.PI * t) * SAG;
        return from.add(delta.multiply(t)).add(0.0D, -sagAmount, 0.0D);
    }

    @Override
    public Identifier getTexture(MinecartChainLinkEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}