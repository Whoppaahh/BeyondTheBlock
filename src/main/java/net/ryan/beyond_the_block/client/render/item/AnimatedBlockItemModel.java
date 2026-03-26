package net.ryan.beyond_the_block.client.render.item;

import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.item.AnimatedBlockItem;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AnimatedBlockItemModel extends AnimatedGeoModel<AnimatedBlockItem> {

    @Override
    public Identifier getModelResource(AnimatedBlockItem animatedBlockItem) {
        return new Identifier(BeyondTheBlock.MOD_ID, "geo/animated_block.geo.json");
    }

    @Override
    public Identifier getTextureResource(AnimatedBlockItem animatedBlockItem) {
        return new Identifier(BeyondTheBlock.MOD_ID, "textures/block/animated_block.png");

    }

    @Override
    public Identifier getAnimationResource(AnimatedBlockItem animatedBlockItem) {
        return new Identifier(BeyondTheBlock.MOD_ID, "animations/animated_block.animation.json");

    }
}