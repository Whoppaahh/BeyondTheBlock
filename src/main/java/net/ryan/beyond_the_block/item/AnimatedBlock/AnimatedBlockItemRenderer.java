package net.ryan.beyond_the_block.item.AnimatedBlock;

import net.ryan.beyond_the_block.item.AnimatedBlockItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class AnimatedBlockItemRenderer extends GeoItemRenderer<AnimatedBlockItem> {
    public AnimatedBlockItemRenderer() {
        super(new AnimatedBlockItemModel());
    }
}
