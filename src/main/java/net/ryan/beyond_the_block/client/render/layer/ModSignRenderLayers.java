package net.ryan.beyond_the_block.client.render.layer;

import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.ryan.beyond_the_block.content.registry.family.ModSignTypes;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.mixin.accessors.TexturedRenderLayersAccessor;

public final class ModSignRenderLayers {

    private ModSignRenderLayers() {}

    public static void register() {
        registerType(ModSignTypes.CHERRY, "btb_cherry");
        registerType(ModSignTypes.PALE_OAK, "btb_pale_oak");
        registerType(ModSignTypes.BAMBOO, "btb_bamboo");
    }

    private static void registerType(SignType type, String textureName) {
        TexturedRenderLayersAccessor.btb$getWoodTypeTextures().put(
                type,
                new SpriteIdentifier(
                        TexturedRenderLayers.SIGNS_ATLAS_TEXTURE,
                        new Identifier(BeyondTheBlock.MOD_ID, "entity/signs/" + textureName)
                )
        );
    }
}
