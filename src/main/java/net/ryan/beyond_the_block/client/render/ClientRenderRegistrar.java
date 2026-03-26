package net.ryan.beyond_the_block.client.render;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.ryan.beyond_the_block.client.hud.ClientHudRegistrar;
import net.ryan.beyond_the_block.client.render.blockentity.*;
import net.ryan.beyond_the_block.client.render.entity.CobwebProjectileRenderer;
import net.ryan.beyond_the_block.client.render.entity.WitherSkeletonHorseRenderer;
import net.ryan.beyond_the_block.client.render.entity.WitherZombieHorseRenderer;
import net.ryan.beyond_the_block.client.render.entity.WitherZombieRenderer;
import net.ryan.beyond_the_block.client.render.item.CupidArrowEntityRenderer;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.block.ModBlocks;
import net.ryan.beyond_the_block.content.blockentity.DyedWaterCauldronBlockEntity;
import net.ryan.beyond_the_block.content.blockentity.ModBlockEntities;
import net.ryan.beyond_the_block.content.entity.ModEntities;
import net.ryan.beyond_the_block.content.item.Armour.ModArmourMaterials;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.content.village.GuardVillager.Render.GuardEntityRenderer;
import net.ryan.beyond_the_block.content.village.ModVillagers;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.core.BeyondTheBlockClient;
import net.ryan.beyond_the_block.feature.paths.PathPreviewState;

public class ClientRenderRegistrar {
    private static boolean modOresLayersRegistered = false;
    // New tag for automatic ore registration
    private static final TagKey<Block> MOD_ORES =
            TagKey.of(Registry.BLOCK.getKey(), new Identifier(BeyondTheBlock.MOD_ID, "mod_ores"));

    public static void register(){
        registerBlockEntities();
        registerEntityRenderers();
        registerBlockRenderLayers();
        registerDynamicLights();
    }
    private static void registerBlockEntities() {
        BlockEntityRendererFactories.register(ModBlockEntities.PEDESTAL_BLOCK_ENTITY, PedestalBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_CORE_BLOCK_ENTITY, ShrineCoreBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_HEADS_BLOCK_ENTITY, ShrineHeadsBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHRINE_DECOR_BLOCK_ENTITY, ShrineDecorBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.PLAYER_VAULT_BLOCK_ENTITY, PlayerVaultBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SINGLE_INPUT_BLOCK_ENTITY, SingleInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.DOUBLE_INPUT_BLOCK_ENTITY, DoubleInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.ANIMATED_BLOCK_ENTITY, AnimatedBlockRenderer::new);

    }
    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.WITHER_ZOMBIE, WitherZombieRenderer::new);
        EntityRendererRegistry.register(ModEntities.WITHER_ZOMBIE_HORSE, WitherZombieHorseRenderer::new);
        EntityRendererRegistry.register(ModEntities.WITHER_SKELETON_HORSE, WitherSkeletonHorseRenderer::new);
        EntityRendererRegistry.register(ModVillagers.GUARD_VILLAGER, GuardEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.COBWEB_PROJECTILE, CobwebProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.CUPID_ARROW, CupidArrowEntityRenderer::new);
    }
    private static void registerBlockRenderLayers() {
        // Manual translucency
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WATER_TORCH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_WATER_TORCH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SPEED_RAIL_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LAVA_LAMP_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MODDED_FLUID_CAULDRON_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DYED_WATER_CAULDRON_BLOCK, RenderLayer.getTranslucent());

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null && world.getBlockEntity(pos) instanceof DyedWaterCauldronBlockEntity be) {
                return be.getColor();
            }
            return 0x3F76E4; // default water tint
        }, ModBlocks.DYED_WATER_CAULDRON_BLOCK);


        // Backup tick retry (stops after success)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!modOresLayersRegistered) attemptRegisterModOres();
            ClientHudRegistrar.updatePathPreview(client);
        });

        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            if(PathPreviewState.hasPreview() && Configs.client().hud.paths.previewMode){
                ClientHudRegistrar.renderPathPreview(context.matrixStack());
            }
        });
    }

    private static void attemptRegisterModOres() {
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
                BeyondTheBlockClient.LOGGER.info("✅ Registered {} mod ores for Cutout render layer from beyond_the_block:mod_ores", count);
            } else {
                BeyondTheBlockClient.LOGGER.warn("⚠️ Tag beyond_the_block:mod_ores was found but empty");
            }
        });
    }

    private static boolean isAmberineArmor(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ArmorItem armor
                && armor.getMaterial() == ModArmourMaterials.AMBERINE;
    }

    private static boolean isAmberineToolOrWeapon(ItemStack stack) {
        return stack != null && (stack.isOf(ModItems.AMBERINE_SWORD) ||
                stack.isOf(ModItems.AMBERINE_PICKAXE) ||
                stack.isOf(ModItems.AMBERINE_AXE) ||
                stack.isOf(ModItems.AMBERINE_SHOVEL) ||
                stack.isOf(ModItems.AMBERINE_HOE));
    }

    private static boolean isAmberineItem(ItemStack stack) {
        return stack != null && (isAmberineArmor(stack) || isAmberineToolOrWeapon(stack)
                || stack.isOf(ModBlocks.AMBERINE_BLOCK.asItem()) || stack.isOf(ModItems.AMBERINE_ITEM));
    }

    private static void registerDynamicLights() {
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
}
