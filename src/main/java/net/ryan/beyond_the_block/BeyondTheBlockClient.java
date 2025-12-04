package net.ryan.beyond_the_block;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.ryan.beyond_the_block.block.Entity.ModBlockEntities;
import net.ryan.beyond_the_block.block.Entity.Render.*;
import net.ryan.beyond_the_block.block.ModBlocks;
import net.ryan.beyond_the_block.effect.Beneficial.ClarityEffect;
import net.ryan.beyond_the_block.effect.FreezeEffectLayer;
import net.ryan.beyond_the_block.enchantment.Armour.boots.LeapOfFaithEnchantment;
import net.ryan.beyond_the_block.entity.ModEntities;
import net.ryan.beyond_the_block.entity.Render.CobwebProjectileRenderer;
import net.ryan.beyond_the_block.entity.Render.WitherSkeletonHorseRenderer;
import net.ryan.beyond_the_block.entity.Render.WitherZombieHorseRenderer;
import net.ryan.beyond_the_block.entity.Render.WitherZombieRenderer;
import net.ryan.beyond_the_block.item.AnimatedBlock.AnimatedBlockItemRenderer;
import net.ryan.beyond_the_block.item.AnimatedItem.AnimatedItem;
import net.ryan.beyond_the_block.item.AnimatedItem.AnimatedItemRenderer;
import net.ryan.beyond_the_block.item.Armour.ModArmourMaterials;
import net.ryan.beyond_the_block.item.CupidArrowEntityRenderer;
import net.ryan.beyond_the_block.item.ModItems;
import net.ryan.beyond_the_block.mixin.Client.LivingEntityRendererAccessor;
import net.ryan.beyond_the_block.network.ClientNetworking;
import net.ryan.beyond_the_block.particle.*;
import net.ryan.beyond_the_block.screen.ModScreenHandlers;
import net.ryan.beyond_the_block.screen.Screens.*;
import net.ryan.beyond_the_block.utils.GUI.FloatingXPManager;
import net.ryan.beyond_the_block.utils.GUI.PlayerHeadManager;
import net.ryan.beyond_the_block.utils.GUI.TrajectoryRenderer;
import net.ryan.beyond_the_block.utils.Helpers.HighlightTracker;
import net.ryan.beyond_the_block.utils.OutlineRenderer;
import net.ryan.beyond_the_block.utils.Snow.SnowDebugRenderer;
import net.ryan.beyond_the_block.village.GuardVillager.Render.GuardEntityRenderer;
import net.ryan.beyond_the_block.village.ModVillagers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class BeyondTheBlockClient implements ClientModInitializer {

    public static final String MOD_ID = "beyond_the_block";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private boolean modOresLayersRegistered = false;
    // New tag for automatic ore registration
    private static final TagKey<Block> MOD_ORES =
            TagKey.of(Registry.BLOCK.getKey(), new Identifier(BeyondTheBlock.MOD_ID, "mod_ores"));


    @Override
    public void onInitializeClient() {
        WorldRenderEvents.BEFORE_ENTITIES.register(TrajectoryRenderer::render);
        registerBlockEntities();
        registerGeoItems();
        registerParticles();
        registerLivingEntityFeatures();
        registerScreens();
        registerClientTickEvents();
        registerEntityRenderers();
        registerBlockRenderLayers();
        registerDynamicLights();

        FloatingXPManager.register();

        OutlineRenderer.init();
        SnowDebugRenderer.init();

    }


    private void registerBlockEntities() {
        BlockEntityRendererFactories.register(ModBlockEntities.PEDESTAL_BLOCK_ENTITY, PedestalBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_CORE_BLOCK_ENTITY, ShrineCoreBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_HEADS_BLOCK_ENTITY, ShrineHeadsBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_DECOR_BLOCK_ENTITY, ShrineDecorBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.PLAYER_VAULT_BLOCK_ENTITY, PlayerVaultBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SINGLE_INPUT_BLOCK_ENTITY, SingleInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.DOUBLE_INPUT_BLOCK_ENTITY, DoubleInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.ANIMATED_BLOCK_ENTITY, AnimatedBlockRenderer::new);
    }

    private void registerGeoItems() {
        GeckoLib.initialize();
        GeoItemRenderer.registerItemRenderer(ModItems.ANIMATED_ITEM, new AnimatedItemRenderer());
        GeoItemRenderer.registerItemRenderer(ModItems.ANIMATED_BLOCK_ITEM, new AnimatedBlockItemRenderer());
    }

    private void registerParticles() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(ModParticles.ROSETTE_PARTICLE, RosetteParticle.Factory::new);
        registry.register(ModParticles.AMBERINE_PARTICLE, AmberineParticle.Factory::new);
        registry.register(ModParticles.RUBY_PARTICLE, RubyParticle.Factory::new);
        registry.register(ModParticles.AZUROS_PARTICLE, AzurosParticle.Factory::new);
        registry.register(ModParticles.CHROMITE_PARTICLE, ChromiteParticle.Factory::new);
        registry.register(ModParticles.MIRANITE_PARTICLE, MiraniteParticle.Factory::new);
        registry.register(ModParticles.NOCTURNITE_PARTICLE, NocturniteParticle.Factory::new);
        registry.register(ModParticles.INDIGRA_PARTICLE, IndigraParticle.Factory::new);
        registry.register(ModParticles.BLEED_SWEEP_PARTICLE, BleedSweepParticle.Factory::new);
    }

    @SuppressWarnings("unchecked")
    private void registerLivingEntityFeatures() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, renderer, helper, context) -> {
                    LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer =
                            (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) renderer;
                    ((LivingEntityRendererAccessor) livingRenderer).getFeatures()
                            .add(new FreezeEffectLayer<>(livingRenderer, livingRenderer.getModel()));
                }
        );
    }

    private void registerScreens() {
        HandledScreens.register(ModScreenHandlers.TROWEL_SCREEN_HANDLER, TrowelScreen::new);
        HandledScreens.register(ModScreenHandlers.GEM_SCREEN_HANDLER, GemScreen::new);
        HandledScreens.register(ModScreenHandlers.GUARD_SCREEN_HANDLER, GuardVillagerScreen::new);
        HandledScreens.register(ModScreenHandlers.RIDDLE_CORE_SCREEN_HANDLER, RiddleCoreScreen::new);
        HandledScreens.register(ModScreenHandlers.STAFF_SCREEN_HANDLER, StaffScreen::new);
        HandledScreens.register(ModScreenHandlers.PLAYER_VAULT_SCREEN_HANDLER, PlayerVaultScreen::new);
        HandledScreens.register(ModScreenHandlers.INFI_SCREEN_HANDLER, InfiFurnaceScreen::new);
        HandledScreens.register(ModScreenHandlers.DECRAFTER_SCREEN_HANDLER, DecrafterScreen::new);
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

    private void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.WITHER_ZOMBIE, WitherZombieRenderer::new);
        EntityRendererRegistry.register(ModEntities.WITHER_ZOMBIE_HORSE, WitherZombieHorseRenderer::new);
        EntityRendererRegistry.register(ModEntities.WITHER_SKELETON_HORSE, WitherSkeletonHorseRenderer::new);
        EntityRendererRegistry.register(ModVillagers.GUARD_VILLAGER, GuardEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.COBWEB_PROJECTILE, CobwebProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.CUPID_ARROW, CupidArrowEntityRenderer::new);
    }

    /* ------------------------------- TAG-DRIVEN RENDER LAYERS ------------------------------- */

    private void registerBlockRenderLayers() {
        // Manual translucency
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LAVA_LAMP_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MODDED_FLUID_CAULDRON_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DYED_WATER_CAULDRON_BLOCK, RenderLayer.getCutout());


        // Backup tick retry (stops after success)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!modOresLayersRegistered) attemptRegisterModOres();
        });
    }

    private void attemptRegisterModOres() {
        if (modOresLayersRegistered) return;

        Registry.BLOCK.getEntryList(MOD_ORES).ifPresent(entryList -> {
            int count = 0;
            for (RegistryEntry<Block> entry : entryList) {
                Block block = entry.value();
                BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
                count++;
            }

            if (count > 0) {
                modOresLayersRegistered = true;
                LOGGER.info("✅ Registered {} mod ores for Cutout render layer from beyond_the_block:mod_ores", count);
            } else {
                LOGGER.warn("⚠️ Tag beyond_the_block:mod_ores was found but empty");
            }
        });
    }
    private void registerDynamicLights() {
        DynamicLightHandlers.registerDynamicLightHandler(EntityType.PLAYER, player -> {
            int maxLight = 0;

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR && isAmberineArmor(player.getEquippedStack(slot))) {
                    maxLight = 15;
                }
            }

            if (isAmberineItem(player.getMainHandStack()) || isAmberineItem(player.getOffHandStack())) {
                maxLight = 15;
            }

            return maxLight;
        });
    }

    private boolean isAmberineArmor(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ArmorItem armor
                && armor.getMaterial() == ModArmourMaterials.AMBERINE;
    }

    private boolean isAmberineToolOrWeapon(ItemStack stack) {
        return stack != null && (stack.isOf(ModItems.AMBERINE_SWORD) ||
                stack.isOf(ModItems.AMBERINE_PICKAXE) ||
                stack.isOf(ModItems.AMBERINE_AXE) ||
                stack.isOf(ModItems.AMBERINE_SHOVEL) ||
                stack.isOf(ModItems.AMBERINE_HOE));
    }

    private boolean isAmberineItem(ItemStack stack) {
        return stack != null && (isAmberineArmor(stack) || isAmberineToolOrWeapon(stack)
                || stack.isOf(ModBlocks.AMBERINE_BLOCK.asItem()) || stack.isOf(ModItems.AMBERINE_ITEM));
    }
}
