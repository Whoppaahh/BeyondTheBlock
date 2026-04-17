package net.ryan.beyond_the_block.client.render.layer;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.ryan.beyond_the_block.content.registry.ModBlocks;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.core.BeyondTheBlockClient;

public class BlockRenderLayerRegistrar {

    public static boolean modOresLayersRegistered = false;
    // New tag for automatic ore registration
    private static final TagKey<Block> MOD_ORES =
            TagKey.of(Registry.BLOCK.getKey(), new Identifier(BeyondTheBlock.MOD_ID, "mod_ores"));


    public static void register(){
        registerBlockRenderLayers();
    }
    private static void registerBlockRenderLayers() {
        // Manual translucency
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RESIN_CLUMP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PALE_HANGING_MOSS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PINK_PETALS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHERRY_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PALE_OAK_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_CHERRY_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_PALE_OAK_SAPLING, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHERRY_LEAVES, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PALE_OAK_LEAVES, RenderLayer.getCutoutMipped());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WATER_TORCH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_WATER_TORCH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SPEED_RAIL_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LAVA_LAMP_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MODDED_FLUID_CAULDRON_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DYED_WATER_CAULDRON_BLOCK, RenderLayer.getTranslucent());
    }
    public static void attemptRegisterModOres() {
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
}
