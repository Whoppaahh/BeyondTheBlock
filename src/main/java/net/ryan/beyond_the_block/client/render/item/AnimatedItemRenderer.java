package net.ryan.beyond_the_block.client.render.item;

import net.ryan.beyond_the_block.content.item.AnimatedItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class AnimatedItemRenderer extends GeoItemRenderer<AnimatedItem> {
    public AnimatedItemRenderer() {
        super(new AnimatedItemModel());
    }
}
