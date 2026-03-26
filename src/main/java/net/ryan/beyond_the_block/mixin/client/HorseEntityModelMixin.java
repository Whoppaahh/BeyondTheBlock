package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.ryan.beyond_the_block.config.access.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HorseEntityModel.class)
public abstract class HorseEntityModelMixin {

    @ModifyVariable(
            method = "setAngles(Lnet/minecraft/entity/passive/AbstractHorseEntity;FFFFF)V",
            at = @At("HEAD"),
            ordinal = 4, // headPitch parameter
            argsOnly = true)
    private float horsebuff$modifyHeadPitch(
            float headPitch,
            AbstractHorseEntity entity
    ) {
        if (!Configs.client().visuals.horses.headPitchOffset) return headPitch;
        if (!entity.hasPassengers()) return headPitch;

        return headPitch - (float) Math.toRadians(Configs.client().visuals.horses.headOffsetDegrees);
    }



}

