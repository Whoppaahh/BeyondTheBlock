package net.ryan.beyond_the_block.mixin.Client;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.ryan.beyond_the_block.config.ModConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseEntityModel.class)
public abstract class HorseEntityModelMixin {

    @Unique
    ModConfig cfg = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    @ModifyVariable(
            method = "setAngles(Lnet/minecraft/entity/passive/AbstractHorseEntity;FFFFF)V",
            at = @At("HEAD"),
            ordinal = 4, // headPitch parameter
            argsOnly = true)
    private float horsebuff$modifyHeadPitch(
            float headPitch,
            AbstractHorseEntity entity
    ) {
        if (!cfg.horseConfig.headPitchOffset) return headPitch;
        if (!entity.hasPassengers()) return headPitch;

        return headPitch - (float) Math.toRadians(cfg.horseConfig.headOffsetDegrees);
    }



}

