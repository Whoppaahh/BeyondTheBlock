package net.ryan.beyond_the_block.client.render.entity;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public final class MinecartChainRenderer {

    private static final int SEGMENTS = 12;
    private static final float SAG = 0.15f;

    public static void render(
            MatrixStack matrices,
            VertexConsumerProvider consumers,
            Vec3d from,
            Vec3d to,
            int light
    ) {
        VertexConsumer buffer = consumers.getBuffer(RenderLayer.getLeash());

        Vec3d delta = to.subtract(from);

        for (int i = 0; i <= SEGMENTS; i++) {
            float t = i / (float) SEGMENTS;
            float sag = (float) (Math.sin(Math.PI * t) * SAG);

            Vec3d pos = from.add(delta.multiply(t)).add(0, -sag, 0);

            buffer.vertex(matrices.peek().getPositionMatrix(),
                            (float) pos.x,
                            (float) pos.y,
                            (float) pos.z
                    ).color(40, 40, 40, 255)
                    .light(light)
                    .next();
        }
    }
}