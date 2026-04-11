package net.ryan.beyond_the_block.client.render.blockentity;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.ryan.beyond_the_block.core.BeyondTheBlock;

public final class HangingSignModelLayers {
    public static final EntityModelLayer HANGING_SIGN_CEILING =
            new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID, "hanging_sign_ceiling"), "main");

    public static final EntityModelLayer HANGING_SIGN_CEILING_MIDDLE =
            new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID, "hanging_sign_ceiling_middle"), "main");

    public static final EntityModelLayer HANGING_SIGN_WALL =
            new EntityModelLayer(new Identifier(BeyondTheBlock.MOD_ID, "hanging_sign_wall"), "main");

    private HangingSignModelLayers() {
    }
}