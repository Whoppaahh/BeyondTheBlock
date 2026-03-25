package net.ryan.beyond_the_block.mixin.Client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.ryan.beyond_the_block.config.Configs;
import net.ryan.beyond_the_block.effect.ModEffects;
import net.ryan.beyond_the_block.utils.Helpers.CameraState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Unique
    private int fogTickCounter = 0;
    @Unique
    private final Random random = new Random();

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

        if (player.hasStatusEffect(ModEffects.MIND_FOG)) {
            StatusEffectInstance effect = player.getStatusEffect(ModEffects.MIND_FOG);
            int interval = 0; // Faster at higher amplifier
            if (effect != null) {
                interval = Math.max(10, 40 - effect.getAmplifier() * 10);
            }

            fogTickCounter++;
            if (fogTickCounter >= interval) {
                fogTickCounter = 0;
                // hotbar has 9 slots
                player.getInventory().selectedSlot = random.nextInt(9);
            }
        }
    }

    @Inject(method = "startRiding", at = @At("HEAD"))
    private void autoCamera_onMount(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (!Configs.client().hud.camera.enabled) return;
        if (entity == null) return;

        MinecraftClient client = MinecraftClient.getInstance();

        // Store previous camera only once
        if (!CameraState.hasStored) {
            CameraState.previousCameraMode = client.options.getPerspective().ordinal();
            CameraState.hasStored = true;
        }


        switch (Configs.client().hud.camera.cameraModeOnMount) {
            case ALWAYS_FIRST -> client.options.setPerspective(net.minecraft.client.option.Perspective.FIRST_PERSON);
            case ALWAYS_THIRD -> client.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
            case PREVIOUS -> client.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
        }
    }

    @Inject(method = "dismountVehicle", at = @At("HEAD"))
    private void autoCamera_onDismount(CallbackInfo ci) {
        if (!Configs.client().hud.camera.enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();

        if (CameraState.hasStored) {
            var perspectiveValues = net.minecraft.client.option.Perspective.values();
            int index = CameraState.previousCameraMode;
            if (index < 0 || index >= perspectiveValues.length) index = 0;

            client.options.setPerspective(perspectiveValues[index]);
            CameraState.hasStored = false;
        }
    }
}
