package net.ryan.beyond_the_block.event.entity;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.client.hud.BreedingHUDRenderer;
import net.ryan.beyond_the_block.client.hud.FollowerHudRenderer;
import net.ryan.beyond_the_block.client.hud.TrajectoryHUD;
import net.ryan.beyond_the_block.content.effect.FreezeEffectLayer;
import net.ryan.beyond_the_block.content.entity.CupidArrowEntity;
import net.ryan.beyond_the_block.event.ModEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.ryan.beyond_the_block.client.hud.FollowerHudRenderer.getFollowers;

public class EntityEventRegistrar {
    private static void registerEntityEvents() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(ModEvents::dropArrows);
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

    private static ActionResult onEntityUsed(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        if (world.isClient) return ActionResult.PASS;

        if (!(entity instanceof ArmorStandEntity armorStand)) return ActionResult.PASS;

        if (player.isSneaking()) {
            transferArmourPartial(player, armorStand, world);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
