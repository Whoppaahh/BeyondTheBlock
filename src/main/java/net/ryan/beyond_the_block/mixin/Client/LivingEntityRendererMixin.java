package net.ryan.beyond_the_block.mixin.Client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.util.math.MathHelper;
import net.ryan.beyond_the_block.config.ModConfig;
import net.ryan.beyond_the_block.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Unique
    private float beyond$finalAlpha = 1.0F;

    @Unique
    ModConfig cfg = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    @Inject(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD")
    )
    private void beyond$computeFinalAlpha(
            T entity,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider buffers,
            int light,
            CallbackInfo ci
    ) {
        beyond$finalAlpha = 1.0F;

        // ETHEREAL VEIL
        if (entity.hasStatusEffect(ModEffects.ETHEREAL_VEIL)) {
            beyond$finalAlpha *= 0.3F;
        }

        // HORSE FADE (Horse Buff behaviour)
        if (entity instanceof AbstractHorseEntity horse) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player != null &&
                    mc.player.getVehicle() == horse &&
                    mc.options.getPerspective().isFirstPerson()) {

                float pitch = mc.player.getPitch();
                if (pitch >= cfg.horseConfig.fadePitch) {
                    beyond$finalAlpha *= MathHelper.clamp(
                            1.0F - ((pitch - cfg.horseConfig.fadePitch) / 60.0F),
                            cfg.horseConfig.minAlpha,
                            1.0F
                    );
                }
            }
        }
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"
            ),
            index = 7 // alpha
    )
    private float beyond$applyFinalAlpha(float originalAlpha) {
        return originalAlpha * beyond$finalAlpha;
    }
}
