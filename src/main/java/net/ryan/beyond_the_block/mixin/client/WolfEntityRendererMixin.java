package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.WolfEntityRenderer;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.entity.passive.WolfEntity;
import net.ryan.beyond_the_block.client.render.entity.WolfCollarTagFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfEntityRenderer.class)
public abstract class WolfEntityRendererMixin
        extends MobEntityRenderer<WolfEntity, WolfEntityModel<WolfEntity>> {

    protected WolfEntityRendererMixin(
            EntityRendererFactory.Context context,
            WolfEntityModel<WolfEntity> entityModel,
            float shadowRadius
    ) {
        super(context, entityModel, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void btb$addCollarTagFeature(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.addFeature(new WolfCollarTagFeatureRenderer(this));
    }
}