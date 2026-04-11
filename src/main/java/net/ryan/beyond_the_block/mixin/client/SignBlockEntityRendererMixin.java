package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.math.Vec3f;
import net.ryan.beyond_the_block.client.render.blockentity.HangingSignModelLayers;
import net.ryan.beyond_the_block.content.block.HangingSignBlock;
import net.ryan.beyond_the_block.content.block.WallHangingSignBlock;
import net.ryan.beyond_the_block.core.BeyondTheBlock;
import net.ryan.beyond_the_block.mixin.accessors.SignTypeAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SignBlockEntityRenderer.class)
public abstract class SignBlockEntityRendererMixin {
    @Shadow
    @Final
    private TextRenderer textRenderer;

    @Unique
    private ModelPart btb$ceilingModel;

    @Unique
    private ModelPart btb$ceilingMiddleModel;

    @Unique
    private ModelPart btb$wallModel;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void btb$initHangingModels(BlockEntityRendererFactory.Context context, CallbackInfo ci) {
        this.btb$ceilingModel = context.getLayerModelPart(HangingSignModelLayers.HANGING_SIGN_CEILING);
        this.btb$ceilingMiddleModel = context.getLayerModelPart(HangingSignModelLayers.HANGING_SIGN_CEILING_MIDDLE);
        this.btb$wallModel = context.getLayerModelPart(HangingSignModelLayers.HANGING_SIGN_WALL);
    }

    @Inject(
            method = "render(Lnet/minecraft/block/entity/SignBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void btb$renderHangingSigns(SignBlockEntity entity, float tickDelta, MatrixStack matrices,
                                        VertexConsumerProvider vertexConsumers, int light, int overlay,
                                        CallbackInfo ci) {
        BlockState state = entity.getCachedState();

        if (!(state.getBlock() instanceof HangingSignBlock) &&
                !(state.getBlock() instanceof WallHangingSignBlock)) {
            return;
        }

        matrices.push();

        float rotationDegrees = btb$getRotationDegrees(state);

        matrices.translate(0.5D, 0.9375D, 0.5D);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotationDegrees));
        matrices.translate(0.0D, -0.3125D, 0.0D);

        // MODEL
        matrices.push();
        matrices.scale(1.0F, -1.0F, -1.0F);

        ModelPart model = btb$getModel(state);
        Identifier texture = btb$getTexture(state);
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
        model.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();

        // FRONT TEXT
        matrices.push();
        btb$renderText(entity, matrices, vertexConsumers, light);
        matrices.pop();

        // BACK TEXT
        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
        btb$renderText(entity, matrices, vertexConsumers, light);
        matrices.pop();

        matrices.pop();
        ci.cancel();
    }

    @Unique
    private float btb$getRotationDegrees(BlockState state) {
        if (state.getBlock() instanceof HangingSignBlock) {
            return -(state.get(HangingSignBlock.ROTATION) * 360.0F / 16.0F);
        }

        if (state.getBlock() instanceof WallHangingSignBlock) {
            return -state.get(WallHangingSignBlock.FACING).asRotation();
        }

        return 0.0F;
    }

    @Unique
    private ModelPart btb$getModel(BlockState state) {
        if (state.getBlock() instanceof HangingSignBlock) {
            return state.get(HangingSignBlock.ATTACHED)
                    ? this.btb$ceilingMiddleModel
                    : this.btb$ceilingModel;
        }

        return this.btb$wallModel;
    }

    @Unique
    private Identifier btb$getTexture(BlockState state) {
        SignType signType = ((AbstractSignBlock) state.getBlock()).getSignType();
        String woodName = ((SignTypeAccessor) (Object) signType).btb$getName();
        return new Identifier(BeyondTheBlock.MOD_ID, "textures/entity/signs/hanging/" + woodName + ".png");
    }

    @Unique
    private void btb$renderText(SignBlockEntity entity, MatrixStack matrices,
                                VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.translate(0.0D, -0.34D, 0.073D);

        float textScale = 0.9F * 0.015625F;
        matrices.scale(textScale, -textScale, textScale);

        int color = entity.getTextColor().getSignColor();
        boolean glowing = entity.isGlowingText();

        for (int i = 0; i < 4; i++) {
            Text text = entity.getTextOnRow(i, false);
            OrderedText orderedText = btb$getOrderedTextFromText(text);

            float x = -this.textRenderer.getWidth(orderedText) / 2.0F;
            float y = i * 10 - 20;

            if (glowing) {
                this.textRenderer.drawWithOutline(
                        orderedText,
                        x,
                        y,
                        color,
                        getGlowOutlineColor(entity),
                        matrices.peek().getPositionMatrix(),
                        vertexConsumers,
                        light
                );
            } else {
                this.textRenderer.draw(
                        orderedText,
                        x,
                        y,
                        color,
                        false,
                        matrices.peek().getPositionMatrix(),
                        vertexConsumers,
                        false,
                        0,
                        light
                );
            }
        }
        matrices.pop();

    }

    @Unique
    private int getGlowOutlineColor(SignBlockEntity entity) {
        if (entity.getTextColor() == net.minecraft.util.DyeColor.BLACK) {
            return 0xF0EBCC;
        }

        int color = entity.getTextColor().getSignColor();
        int r = (int) ((color >> 16 & 255) * 0.4D);
        int g = (int) ((color >> 8 & 255) * 0.4D);
        int b = (int) ((color & 255) * 0.4D);
        return (r << 16) | (g << 8) | b;
    }

    @Unique
    private OrderedText btb$getOrderedTextFromText(Text text) {
        List<OrderedText> wrapped = this.textRenderer.wrapLines(text, 90);
        return wrapped.isEmpty() ? OrderedText.EMPTY : wrapped.get(0);
    }

}