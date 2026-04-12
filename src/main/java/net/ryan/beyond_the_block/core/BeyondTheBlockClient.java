package net.ryan.beyond_the_block.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.ryan.beyond_the_block.client.bootstrap.ClientBootstrap;
import net.ryan.beyond_the_block.client.hud.TrajectoryRenderer;
import net.ryan.beyond_the_block.client.network.ConfigSyncClient;
import net.ryan.beyond_the_block.client.screen.*;
import net.ryan.beyond_the_block.client.visual.HighlightTracker;
import net.ryan.beyond_the_block.client.visual.OutlineRenderer;
import net.ryan.beyond_the_block.content.effect.FreezeEffectLayer;
import net.ryan.beyond_the_block.content.effect.FrozenCreeperLayer;
import net.ryan.beyond_the_block.content.effect.FrozenSheepWoolLayer;
import net.ryan.beyond_the_block.content.effect.FrozenSlimeLayer;
import net.ryan.beyond_the_block.content.effect.beneficial.ClarityEffect;
import net.ryan.beyond_the_block.content.enchantment.armour.boots.LeapOfFaithEnchantment;
import net.ryan.beyond_the_block.content.item.AnimatedItem;
import net.ryan.beyond_the_block.network.ClientNetworking;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;
import net.ryan.beyond_the_block.utils.visual.PlayerHeadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeyondTheBlockClient implements ClientModInitializer {

    public static final String MOD_ID = "beyond_the_block";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {


        WorldRenderEvents.BEFORE_ENTITIES.register(TrajectoryRenderer::render);
        registerScreens();
        registerClientTickEvents();
        registerLivingEntityFeatures();

        ClientBootstrap.init();
        OutlineRenderer.init();
        ConfigSyncClient.init();
    }

    @SuppressWarnings("unchecked")
    private void registerLivingEntityFeatures() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, renderer, helper, context) -> {
                    LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer =
                            (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) renderer;
                    helper.register(new FreezeEffectLayer<>(livingRenderer, livingRenderer.getModel()));
                    if(entityType == EntityType.SHEEP){
                        helper.register(new FrozenSheepWoolLayer(
                                (FeatureRendererContext<SheepEntity, SheepEntityModel<SheepEntity>>)renderer
                        ));
                    }
                    if(entityType == EntityType.SLIME){
                        helper.register(new FrozenSlimeLayer(
                                (FeatureRendererContext<SlimeEntity, SlimeEntityModel<SlimeEntity>>)renderer
                        ));
                    }
                    if(entityType == EntityType.CREEPER){
                        helper.register(new FrozenCreeperLayer(
                                (FeatureRendererContext<CreeperEntity, CreeperEntityModel<CreeperEntity>>)renderer,
                                MinecraftClient.getInstance().getEntityModelLoader()
                        ));
                    }
                }
        );
    }

    private void registerScreens() {
        HandledScreens.register(ModScreenHandlers.WOODCUTTER_SCREEN_HANDLER, WoodcutterScreen::new);
        HandledScreens.register(ModScreenHandlers.TROWEL_SCREEN_HANDLER, TrowelScreen::new);
        HandledScreens.register(ModScreenHandlers.GEM_SCREEN_HANDLER, GemScreen::new);
        HandledScreens.register(ModScreenHandlers.GUARD_SCREEN_HANDLER, GuardVillagerScreen::new);
        HandledScreens.register(ModScreenHandlers.RIDDLE_CORE_SCREEN_HANDLER, RiddleCoreScreen::new);
        HandledScreens.register(ModScreenHandlers.STAFF_SCREEN_HANDLER, StaffScreen::new);
        HandledScreens.register(ModScreenHandlers.PLAYER_VAULT_SCREEN_HANDLER, PlayerVaultScreen::new);
        HandledScreens.register(ModScreenHandlers.INFI_SCREEN_HANDLER, InfiFurnaceScreen::new);
        HandledScreens.register(ModScreenHandlers.DECRAFTER_SCREEN_HANDLER, DecrafterScreen::new);
        HandledScreens.register(ModScreenHandlers.ARMOUR_TRIM_SMITHING, ArmourTrimSmithingScreen::new);

    }

    private void registerClientTickEvents() {
        ClientNetworking.registerS2CPackets();
        ClientTickEvents.END_CLIENT_TICK.register(ClarityEffect::tickHandler);
        ClientTickEvents.END_CLIENT_TICK.register(AnimatedItem::handleLeftClick);
        ClientTickEvents.END_CLIENT_TICK.register(LeapOfFaithEnchantment::handleJumpPress);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerHeadManager.tick();
            if (client.player != null) HighlightTracker.updateHighlights(client.player);
        });
    }

}
