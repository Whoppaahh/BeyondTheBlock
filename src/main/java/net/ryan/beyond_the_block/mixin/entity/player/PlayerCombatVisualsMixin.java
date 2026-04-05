package net.ryan.beyond_the_block.mixin.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.content.particle.ModParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerCombatVisualsMixin {

    @Inject(method = "spawnSweepAttackParticles", at = @At("HEAD"), cancellable = true)
    private void btb$rubySweepParticles(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (!(player.getWorld() instanceof ServerWorld serverWorld)) return;
        if (!player.getMainHandStack().isOf(ModItems.RUBY_SWORD)) return;

        double d = -MathHelper.sin(player.getYaw() * (float)(Math.PI / 180.0));
        double e = MathHelper.cos(player.getYaw() * (float)(Math.PI / 180.0));

        serverWorld.spawnParticles(
                ModParticles.BLEED_SWEEP_PARTICLE,
                player.getX() + d,
                player.getBodyY(0.5),
                player.getZ() + e,
                0,
                d, 0.0, e, 0.0
        );

        ci.cancel();
    }
}