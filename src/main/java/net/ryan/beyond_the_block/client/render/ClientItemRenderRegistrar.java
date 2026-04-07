package net.ryan.beyond_the_block.client.render;

import net.ryan.beyond_the_block.client.render.item.AnimatedBlockItemRenderer;
import net.ryan.beyond_the_block.client.render.item.AnimatedItemRenderer;
import net.ryan.beyond_the_block.content.registry.ModItems;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ClientItemRenderRegistrar {
    public static void register() {
        GeckoLib.initialize();
        GeoItemRenderer.registerItemRenderer(ModItems.ANIMATED_ITEM, new AnimatedItemRenderer());
        GeoItemRenderer.registerItemRenderer(ModItems.ANIMATED_BLOCK_ITEM, new AnimatedBlockItemRenderer());
    }
}
