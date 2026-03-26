package net.ryan.beyond_the_block.client.render.blockentity;

import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.blockentity.AnimatedBlockEntity;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AnimatedBlockModel extends AnimatedGeoModel<AnimatedBlockEntity> {

    @Override
    public Identifier getModelResource(AnimatedBlockEntity animatedBlockEntity) {
        return new Identifier(BeyondTheBlock.MOD_ID, "geo/animated_block.geo.json");
    }

    @Override
    public Identifier getTextureResource(AnimatedBlockEntity animatedBlockEntity) {
        return new Identifier(BeyondTheBlock.MOD_ID, "textures/block/animated_block.png");
    }

    @Override
    public Identifier getAnimationResource(AnimatedBlockEntity animatedBlockEntity) {
        return new Identifier(BeyondTheBlock.MOD_ID, "animations/animated_block.animation.json");
    }
}
