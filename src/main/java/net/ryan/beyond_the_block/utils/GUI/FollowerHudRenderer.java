package net.ryan.beyond_the_block.utils.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class FollowerHudRenderer {
    private static final Identifier HEARTS_TEXTURE = new Identifier("minecraft", "textures/gui/icons.png");


    public static List<LivingEntity> getFollowers(PlayerEntity player) {
        List<LivingEntity> followers = new ArrayList<>();

        // Search in loaded entities around player
        followers.addAll(player.world.getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(64), entity -> {
            if (entity instanceof TameableEntity tameable) {
                return player.equals(tameable.getOwner());
            }
            return false;
        }));

        return followers;
    }

    public static void renderFollower(MatrixStack context, LivingEntity entity, int x, int y) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Compute scale based on entity size
        double width = entity.getWidth();
        double height = entity.getHeight();
        // Background for readability
        int bgWidth = Math.max(50, 30 + (int)(width * 5) + 30);
        DrawableHelper.fill(context, x - 8, y - 4, x + bgWidth, y + 26, 0x66000000);

        // 1️⃣ Render the entity model facing forward
        // Temporarily adjust yaw to face front
        float prevBodyYaw = entity.bodyYaw;
        float prevYaw = entity.getYaw();
        entity.bodyYaw = 180f;
        entity.setYaw(180f);

        // Base scale for small mobs
        int scale = (int) (15 + (height * 10));
        scale = Math.max(12, Math.min(scale, 30)); // clamp between 12 and 30

        InventoryScreen.drawEntity(x + 5, y + 22, scale, 0, 0, entity);
        // Restore original yaw
        entity.bodyYaw = prevBodyYaw;
        entity.setYaw(prevYaw);

        // 2️⃣ Health info
        float health = entity.getHealth();
        float max = entity.getMaxHealth();
        int percent = Math.round((health / max) * 100);
        int color = getHealthColor(health / max);

        // 3️⃣ Distance
        PlayerEntity player = client.player;
        double distance = Math.sqrt(player != null ? player.squaredDistanceTo(entity) : 0);
        String distText = String.format("%.1fm", distance);

        // 4️⃣ Draw heart and text
        int textX = x + 5 + (int)(width * 5);
        MinecraftClient.getInstance().getTextureManager().bindTexture(HEARTS_TEXTURE);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, HEARTS_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        DrawableHelper.drawTexture(context, textX + 10, y + 3, 52, 0, 9, 9, 256, 256);
        RenderSystem.disableBlend();
        client.textRenderer.drawWithShadow(context, percent + "%", textX + 20, y + 4, color, true);
        client.textRenderer.drawWithShadow(context, distText, textX + 20, y + 14, 0xAAAAAA, true);
    }

    private static int getHealthColor(float pct) {
        if (pct > 0.66f) return 0x55FF55; // green
        if (pct > 0.33f) return 0xFFFF55; // yellow
        return 0xFF5555;                  // red
    }
}

