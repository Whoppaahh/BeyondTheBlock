package net.ryan.beyond_the_block.client.render.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public final class MinecartChainRenderer {

    private static final ItemStack CHAIN_STACK = new ItemStack(Items.CHAIN);
    private static final int SEGMENTS = 8;
    private static final float SAG = 0.05f;
    private static final float SCALE = 0.45f;

    private MinecartChainRenderer() {
    }

    public static void render(
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

            float yaw = (float) Math.atan2(tangent.x, tangent.z);
            float pitch = (float) -Math.asin(tangent.y);

            matrices.push();
            matrices.translate(pos.x, pos.y, pos.z);
            matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(yaw));
            matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(pitch));
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
        float sag = (float) (Math.sin(Math.PI * t) * SAG);
        return from.add(delta.multiply(t)).add(0.0D, -sag, 0.0D);
    }
}