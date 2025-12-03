package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.utils.GUI.FloatingXPManager;
import net.ryan.beyond_the_block.utils.XPOrbs.OrbExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin implements OrbExtension {

    @Shadow private PlayerEntity target;
    @Shadow private int amount;


    @Inject(method = "onPlayerCollision", at = @At("TAIL"))
    private void beyond_the_block$onPickup(PlayerEntity player, CallbackInfo ci) {

        if (player.world.isClient) {
            FloatingXPManager.onXPPickedUp(beyond_the_block$getAmount());
        }
    }

    @Override
    public PlayerEntity beyond_the_block$getTargetPlayer() {
        return this.target;
    }

    @Override
    public void beyond_the_block$setTargetPlayer(PlayerEntity player) {
        this.target = player;
    }

    @Override
    public int beyond_the_block$getAmount() {
        return this.amount;
    }

    @Override
    public void beyond_the_block$setAmount(int value) {
        this.amount = value;
    }
}

