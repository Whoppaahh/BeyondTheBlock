package net.ryan.beyond_the_block.item.AnimatedItem;

import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.BeyondTheBlock;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AnimatedItemModel extends AnimatedGeoModel<AnimatedItem> {

    @Override
    public Identifier getModelResource(AnimatedItem animatedItem) {
        return new Identifier(BeyondTheBlock.MOD_ID, "geo/animated_item.geo.json");
    }

    @Override
    public Identifier getTextureResource(AnimatedItem animatedItem) {
        return  new Identifier(BeyondTheBlock.MOD_ID, "textures/item/texture.png");
    }

    @Override
    public Identifier getAnimationResource(AnimatedItem animatedItem) {
        return new Identifier(BeyondTheBlock.MOD_ID, "animations/model.animation.json");
    }
}
