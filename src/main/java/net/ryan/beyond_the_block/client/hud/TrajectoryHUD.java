package net.ryan.beyond_the_block.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.projectile.TrajectoryPath;

public class TrajectoryHUD implements HudRenderCallback {

        public static TrajectoryPath lastPath = TrajectoryPath.EMPTY;

        @Override
        public void onHudRender(MatrixStack matrices, float tickDelta) {
            if (lastPath == null || lastPath.isEmpty()) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            if (!Configs.client().hud.trajectory.enabled) return;

            Vec3d eye = client.player.getCameraPosVec(tickDelta);
            Vec3d end;

            if (lastPath.hitPos != null) {
                end = lastPath.hitPos;
            } else {
                end = lastPath.points.get(lastPath.points.size() - 1);
            }

            double dist = eye.distanceTo(end);
            String text = String.format("Range: %.1fm", dist);

            TextRenderer tr = client.textRenderer;
            int screenW = client.getWindow().getScaledWidth();
            int screenH = client.getWindow().getScaledHeight();

            // Top-right of crosshair
            int x = screenW / 2 + 8;
            int y = screenH / 2 - 18;

            tr.draw(matrices, text, x, y, 0xFFFFFF);
        }
    }
