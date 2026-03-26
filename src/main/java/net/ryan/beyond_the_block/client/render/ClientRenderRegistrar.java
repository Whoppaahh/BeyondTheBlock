package net.ryan.beyond_the_block.client.render;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

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
