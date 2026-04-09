package net.ryan.beyond_the_block.mixin.accessors;

import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.SignType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(TexturedRenderLayers.class)
public interface TexturedRenderLayersAccessor {

    @Accessor("WOOD_TYPE_TEXTURES")
    static Map<SignType, SpriteIdentifier> btb$getWoodTypeTextures() {
        throw new AssertionError();
    }

    @Invoker("createSignTextureId")
    static SpriteIdentifier btb$createSignTextureId(SignType type) {
        throw new AssertionError();
    }
}