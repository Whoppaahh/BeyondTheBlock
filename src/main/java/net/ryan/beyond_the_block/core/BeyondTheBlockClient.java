package net.ryan.beyond_the_block.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
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
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.client.bootstrap.ClientBootstrap;
import net.ryan.beyond_the_block.client.hud.TrajectoryRenderer;
import net.ryan.beyond_the_block.client.network.ConfigSyncClient;
import net.ryan.beyond_the_block.client.render.trim.ArmourTrimBakedTextureManager;
import net.ryan.beyond_the_block.client.render.trim.ArmourTrimItemPredicates;
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
import net.ryan.beyond_the_block.content.registry.ModItems;
import net.ryan.beyond_the_block.content.registry.family.ModArmourTrim;
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
        registerResourceReloads();
        ArmourTrimItemPredicates.register();
        registerTrimItemColors();

        ClientBootstrap.init();
        OutlineRenderer.init();
        ConfigSyncClient.init();

    }

    private void registerTrimItemColors() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                    if (tintIndex != 1) {
                        return -1;
                    }

                    return ModArmourTrim.getTrim(stack)
                            .map(trim -> getTrimMaterialColor(trim.materialId()))
                            .orElse(-1);
                },
                Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS,
                Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS,
                Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS,
                Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
                Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS,
                Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS,

                ModItems.RUBY_HELMET, ModItems.RUBY_CHESTPLATE, ModItems.RUBY_LEGGINGS, ModItems.RUBY_BOOTS,
                ModItems.NOCTURNITE_HELMET, ModItems.NOCTURNITE_CHESTPLATE, ModItems.NOCTURNITE_LEGGINGS, ModItems.NOCTURNITE_BOOTS,
                ModItems.AZUROS_HELMET, ModItems.AZUROS_CHESTPLATE, ModItems.AZUROS_LEGGINGS, ModItems.AZUROS_BOOTS,
                ModItems.AMBERINE_HELMET, ModItems.AMBERINE_CHESTPLATE, ModItems.AMBERINE_LEGGINGS, ModItems.AMBERINE_BOOTS,
                ModItems.MIRANITE_HELMET, ModItems.MIRANITE_CHESTPLATE, ModItems.MIRANITE_LEGGINGS, ModItems.MIRANITE_BOOTS,
                ModItems.CHROMITE_HELMET, ModItems.CHROMITE_CHESTPLATE, ModItems.CHROMITE_LEGGINGS, ModItems.CHROMITE_BOOTS,
                ModItems.ROSETTE_HELMET, ModItems.ROSETTE_CHESTPLATE, ModItems.ROSETTE_LEGGINGS, ModItems.ROSETTE_BOOTS,
                ModItems.XIRION_HELMET, ModItems.XIRION_CHESTPLATE, ModItems.XIRION_LEGGINGS, ModItems.XIRION_BOOTS
        );
    }

    private static int getTrimMaterialColor(Identifier materialId) {
        return switch (materialId.getPath()) {
            case "quartz" -> 0xE3D4C4;
            case "iron" -> 0xECECEC;
            case "netherite" -> 0x625859;
            case "redstone" -> 0xB02E26;
            case "copper" -> 0xB4684D;
            case "gold" -> 0xDEB12D;
            case "emerald" -> 0x47A036;
            case "diamond" -> 0x6EECD2;
            case "lapis" -> 0x416E97;
            case "amethyst" -> 0x9A5CC6;
            default -> -1;
        };
    }

    private void registerResourceReloads() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return new Identifier(BeyondTheBlock.MOD_ID, "armour_trim_baked_textures");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        ArmourTrimBakedTextureManager.clear();
                    }
                }
        );
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
