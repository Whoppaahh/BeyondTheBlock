package net.ryan.beyond_the_block.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.content.entity.model.RaftEntityModel;

@Environment(EnvType.CLIENT)
public class RaftEntityRenderer extends BoatEntityRenderer {
    private final EntityModel<BoatEntityRenderState> model;
    private final Identifier texture;

    public RaftEntityRenderer(EntityRendererFactory.Context context, EntityModelLayer layer) {
        super(context);
        this.texture = layer.id().withPath((path) -> "textures/entity/" + path + ".png");
        this.model = new RaftEntityModel(context.getPart(layer));
    }

    protected EntityModel<BoatEntityRenderState> getModel() {
        return this.model;
    }

    protected RenderLayer getRenderLayer() {
        return this.model.getLayer(this.texture);
    }
