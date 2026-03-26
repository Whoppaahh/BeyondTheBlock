package net.ryan.beyond_the_block.content.item;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.content.entity.CupidArrowEntity;

public class CupidArrowEntityRenderer extends ProjectileEntityRenderer<CupidArrowEntity> {
    public CupidArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(CupidArrowEntity entity) {
        return new Identifier(BeyondTheBlock.MOD_ID, "textures/item/cupid_arrow.png");
    }
}

