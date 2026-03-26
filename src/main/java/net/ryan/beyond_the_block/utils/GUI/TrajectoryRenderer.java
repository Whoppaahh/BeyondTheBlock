package net.ryan.beyond_the_block.utils.GUI;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.utils.ProjectileHelpers.TrajectoryHelper;
import net.ryan.beyond_the_block.utils.ProjectileHelpers.TrajectoryPath;

import java.util.List;

public final class TrajectoryRenderer {

    public static void render(WorldRenderContext ctx) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        if (!Configs.client().hud.trajectory.enabled) {
            TrajectoryHUD.lastPath = TrajectoryPath.EMPTY;
            return;
        }

        VertexConsumerProvider providers = ctx.consumers();
        if (providers == null) {
            TrajectoryHUD.lastPath = TrajectoryPath.EMPTY;
            return;
        }

        TrajectoryPath path = TrajectoryHelper.computeTrajectory(client, ctx.tickDelta());
        TrajectoryHUD.lastPath = path; // for HUD range text

        if (path.isEmpty()) return;

        Camera camera = ctx.camera();
        Vec3d camPos = camera.getPos();

        MatrixStack matrices = ctx.matrixStack();
        Matrix4f posMat = matrices.peek().getPositionMatrix();
        Matrix3f normMat = matrices.peek().getNormalMatrix();

        VertexConsumer vc = providers.getBuffer(RenderLayer.getLines());

        // Choose base color from config and hit kind
        int color;
        switch (path.hitKind) {
            case BLOCK -> color = Configs.client().hud.trajectory.colorBlock;
            case ENTITY -> color = Configs.client().hud.trajectory.colorEntity;
            case NONE -> color = Configs.client().hud.trajectory.colorNone;
            default -> color = Configs.client().hud.trajectory.colorNone;
        }

        float baseR = ((color >> 16) & 0xFF) / 255.0F;
        float baseG = ((color >> 8) & 0xFF) / 255.0F;
        float baseB = (color & 0xFF) / 255.0F;

        float nx = 0f, ny = 1f, nz = 0f; // arbitrary normal

        List<Vec3d> points = path.points;
        int segments = points.size() - 1;
        if (segments <= 0) return;

        int lines = Configs.client().hud.trajectory.thickLine ? Math.max(1, Configs.client().hud.trajectory.thicknessLines) : 1;

        for (int l = 0; l < lines; l++) {
            float offsetScale = Configs.client().hud.trajectory.thickLine ? (l - (lines - 1) / 2.0f) * Configs.client().hud.trajectory.thicknessOffset : 0.0f;

            for (int i = 0; i < segments; i++) {
                Vec3d p1 = points.get(i);
                Vec3d p2 = points.get(i + 1);

                Vec3d dir = p2.subtract(p1).normalize();
                Vec3d side = new Vec3d(-dir.z, 0, dir.x);
                if (side.lengthSquared() > 0.0001) {
                    side = side.normalize().multiply(offsetScale);
                } else {
                    side = Vec3d.ZERO;
                }

                Vec3d o1 = p1.add(side);
                Vec3d o2 = p2.add(side);

                float t1 = segments <= 1 ? 0f : (float)i / (float)segments;
                float t2 = segments <= 1 ? 1f : (float)(i + 1) / (float)segments;

                float a1 = Configs.client().hud.trajectory.gradient ? (0.3f + 0.7f * (1.0f - t1)) : 1.0f;
                float a2 = Configs.client().hud.trajectory.gradient ? (0.3f + 0.7f * (1.0f - t2)) : 1.0f;

                vc.vertex(posMat,
                                (float)(o1.x - camPos.x),
                                (float)(o1.y - camPos.y),
                                (float)(o1.z - camPos.z)
                        ).color(baseR, baseG, baseB, a1)
                        .normal(normMat, nx, ny, nz)
                        .next();

                vc.vertex(posMat,
                                (float)(o2.x - camPos.x),
                                (float)(o2.y - camPos.y),
                                (float)(o2.z - camPos.z)
                        ).color(baseR, baseG, baseB, a2)
                        .normal(normMat, nx, ny, nz)
                        .next();
            }
        }

        // Impact highlight (block outline or entity hitbox tint)
        if (Configs.client().hud.trajectory.showImpactMarker && path.hitKind != TrajectoryPath.HitKind.NONE && path.hitPos != null) {
            if (path.hitKind == TrajectoryPath.HitKind.BLOCK && path.hitBlock != null) {
                drawBlockOutline(vc, posMat, normMat, camPos, path.hitBlock, baseR, baseG, baseB);
            } else if (path.hitKind == TrajectoryPath.HitKind.ENTITY && path.hitEntityId != -1) {
                drawEntityHitboxOutline(vc, posMat, normMat, camPos, ctx.world(), path.hitEntityId,
                        baseR, baseG, baseB);
            }
        }
    }

    private static void drawBlockOutline(VertexConsumer vc,
                                         Matrix4f posMat,
                                         Matrix3f normMat,
                                         Vec3d camPos,
                                         BlockPos blockPos,
                                         float r, float g, float b) {

        MinecraftClient client = MinecraftClient.getInstance();
        var world = client.world;
        if (world == null) return;

        BlockState state = world.getBlockState(blockPos);
        VoxelShape shape = state.getOutlineShape(world, blockPos);
        if (shape.isEmpty()) return;

        Box box = shape.getBoundingBox().offset(blockPos);

        drawBoxEdges(vc, posMat, normMat, box.offset(-camPos.x, -camPos.y, -camPos.z), r, g, b, 0.8f);
    }

    private static void drawEntityHitboxOutline(VertexConsumer vc,
                                                Matrix4f posMat,
                                                Matrix3f normMat,
                                                Vec3d camPos,
                                                World world,
                                                int entityId,
                                                float r, float g, float b) {
        Entity target = world.getEntityById(entityId);
        if (target == null) return;

        Box box = target.getBoundingBox().expand(0.02); // slight inflation
        drawBoxEdges(vc, posMat, normMat, box.offset(-camPos.x, -camPos.y, -camPos.z), r, g, b, 0.6f);
    }

    private static void drawBoxEdges(VertexConsumer vc,
                                     Matrix4f posMat,
                                     Matrix3f normMat,
                                     Box box,
                                     float r, float g, float b, float a) {
        float nx = 0f, ny = 1f, nz = 0f;

        double minX = box.minX;
        double minY = box.minY;
        double minZ = box.minZ;
        double maxX = box.maxX;
        double maxY = box.maxY;
        double maxZ = box.maxZ;

        // 12 edges of the box

        // Bottom face
        line(vc, posMat, normMat, minX, minY, minZ, maxX, minY, minZ, r, g, b, a, nx, ny, nz);
        line(vc, posMat, normMat, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a, nx, ny, nz);
        line(vc, posMat, normMat, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a, nx, ny, nz);
        line(vc, posMat, normMat, minX, minY, maxZ, minX, minY, minZ, r, g, b, a, nx, ny, nz);

        // Top face
        line(vc, posMat, normMat, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a, nx, ny, nz);
        line(vc, posMat, normMat, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a, nx, ny, nz);
        line(vc, posMat, normMat, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a, nx, ny, nz);
        line(vc, posMat, normMat, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a, nx, ny, nz);

        // Vertical edges
        line(vc, posMat, normMat, minX, minY, minZ, minX, maxY, minZ, r, g, b, a, nx, ny, nz);
        line(vc, posMat, normMat, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a, nx, ny, nz);
        line(vc, posMat, normMat, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a, nx, ny, nz);
        line(vc, posMat, normMat, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a, nx, ny, nz);
    }

    private static void line(VertexConsumer vc,
                             Matrix4f posMat,
                             Matrix3f normMat,
                             double x1, double y1, double z1,
                             double x2, double y2, double z2,
                             float r, float g, float b, float a,
                             float nx, float ny, float nz) {

        vc.vertex(posMat, (float)x1, (float)y1, (float)z1)
                .color(r, g, b, a)
                .normal(normMat, nx, ny, nz)
                .next();

        vc.vertex(posMat, (float)x2, (float)y2, (float)z2)
                .color(r, g, b, a)
                .normal(normMat, nx, ny, nz)
                .next();
    }
}
