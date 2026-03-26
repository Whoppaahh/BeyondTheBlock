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
import net.ryan.beyond_the_block.client.render.entity.GuardEntityRenderer;
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
}
