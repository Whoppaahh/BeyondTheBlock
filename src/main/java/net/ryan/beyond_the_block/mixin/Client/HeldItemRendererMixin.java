package net.ryan.beyond_the_block.mixin.Client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Shadow
    @Final
    private ItemRenderer itemRenderer;

    @Inject(
            method = "renderFirstPersonItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hideFirstPersonInvisibility(
            AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci
    ) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Only cancel for the *local* player in first person
        if (player == client.player
                && client.options.getPerspective().isFirstPerson()
                && player.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            ci.cancel();
        }

    }
}

