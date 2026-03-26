package net.ryan.beyond_the_block.client.render.item;

import net.ryan.beyond_the_block.content.item.AnimatedBlockItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class AnimatedBlockItemRenderer extends GeoItemRenderer<AnimatedBlockItem> {
    public AnimatedBlockItemRenderer() {
        super(new AnimatedBlockItemModel());
    }
}
