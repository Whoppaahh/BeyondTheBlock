package net.ryan.beyond_the_block.client.render;

import net.ryan.beyond_the_block.client.render.blockentity.BlockEntityRenderRegistrar;
import net.ryan.beyond_the_block.client.render.colour.ColourProviderRegistrar;
import net.ryan.beyond_the_block.client.render.entity.EntityRenderRegistrar;
import net.ryan.beyond_the_block.client.render.layer.BlockRenderLayerRegistrar;
import net.ryan.beyond_the_block.client.render.light.DynamicLightRegistrar;
import net.ryan.beyond_the_block.client.render.sign.ModSignRenderLayers;

public class ClientRenderRegistrar {

    public static void register(){
        BlockEntityRenderRegistrar.register();
        EntityRenderRegistrar.register();
        ModSignRenderLayers.register();
        BlockRenderLayerRegistrar.register();
        DynamicLightRegistrar.register();
        ColourProviderRegistrar.register();
    }
}
