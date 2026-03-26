package net.ryan.beyond_the_block.content.entity.Render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieHorseEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public class WitherSkeletonHorseRenderer extends ZombieHorseEntityRenderer {
    public WitherSkeletonHorseRenderer(EntityRendererFactory.Context context) {
        super(context, EntityModelLayers.SKELETON_HORSE);
    }
    private static final Identifier WITHER_SKELETON_HORSE_TEXTURE = new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/horse/wither_skeleton_horse.png");

    @Override
    public Identifier getTexture(AbstractHorseEntity entity) {
        return WITHER_SKELETON_HORSE_TEXTURE;
    }
}
