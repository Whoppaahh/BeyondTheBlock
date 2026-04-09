package net.ryan.beyond_the_block.client.render.layer;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.SignType;
import net.ryan.beyond_the_block.content.registry.family.ModSignTypes;
import net.ryan.beyond_the_block.mixin.accessors.TexturedRenderLayersAccessor;

public final class ModSignRenderLayers {

    private ModSignRenderLayers() {}

    public static void register() {
        register(ModSignTypes.CHERRY);
        register(ModSignTypes.PALE_OAK);
        register(ModSignTypes.BAMBOO);
    }

    private static void register(SignType type) {
        TexturedRenderLayersAccessor.btb$getWoodTypeTextures()
                .put(type, TexturedRenderLayersAccessor.btb$createSignTextureId(type));

//        EntityModelLayerRegistry.registerModelLayer(
//                EntityModelLayers.createSign(type),
//                SignBlockEntityRenderer::getTexturedModelData
//        );
    }
}
