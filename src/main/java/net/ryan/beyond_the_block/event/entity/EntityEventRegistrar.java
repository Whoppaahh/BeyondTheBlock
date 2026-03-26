package net.ryan.beyond_the_block.event.entity;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.ryan.beyond_the_block.client.hud.BreedingHUDRenderer;
import net.ryan.beyond_the_block.client.hud.FollowerHudRenderer;
import net.ryan.beyond_the_block.client.hud.TrajectoryHUD;
import net.ryan.beyond_the_block.content.effect.FreezeEffectLayer;
import net.ryan.beyond_the_block.content.entity.CupidArrowEntity;
import net.ryan.beyond_the_block.feature.projectile.ArrowRecoveryHandler;

import java.util.List;

import static net.ryan.beyond_the_block.client.hud.FollowerHudRenderer.getFollowers;

public class EntityEventRegistrar {

    public static void register(){
        registerEntityEvents();
    }
    private static void registerEntityEvents() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(ArrowRecoveryHandler::dropArrows);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(CupidArrowEntity::onDamage);
        ServerLivingEntityEvents.AFTER_DEATH.register(FreezeEffectLayer::onDeath);

        HudRenderCallback.EVENT.register(new BreedingHUDRenderer());
        HudRenderCallback.EVENT.register(new TrajectoryHUD());

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            List<LivingEntity> followers = getFollowers(client.player);
            if (followers.isEmpty()) return;

            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            int entryHeight = 32;
            int totalHeight = followers.size() * entryHeight;
            int startY = (screenHeight / 2) - (totalHeight / 2);
            int x = 15; // left margin

            for (LivingEntity entity : followers) {
                FollowerHudRenderer.renderFollower(context, entity, x, startY);
                startY += entryHeight;
            }
        });
    }


}
