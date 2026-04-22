package net.ryan.beyond_the_block.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.ryan.beyond_the_block.feature.fire.FireColourResolver;
import net.ryan.beyond_the_block.feature.fire.TintingVertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderManager.class)
@Environment(EnvType.CLIENT)
public abstract class FireBlockRenderManagerMixin {

    @Unique
    private static final ThreadLocal<Boolean> beyond_the_block$fireReentry =
            ThreadLocal.withInitial(() -> false);

    @Inject(
            method = "renderBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void beyond$renderTintedVanillaFire(
            BlockState state,
            BlockPos pos,
            BlockRenderView world,
            MatrixStack matrices,
            VertexConsumer vertexConsumer,
            boolean cull,
            Random random,
            CallbackInfo ci
    ) {
        if (beyond_the_block$fireReentry.get()) {
            return;
        }

        if (!state.isOf(Blocks.FIRE) && !state.isOf(Blocks.SOUL_FIRE)) {
            return;
        }

        boolean soulFire = state.isOf(Blocks.SOUL_FIRE);
        int colour = FireColourResolver.resolve(world, pos, soulFire);

        TintingVertexConsumer tinted = new TintingVertexConsumer(vertexConsumer, colour);

        beyond_the_block$fireReentry.set(true);
        try {
            ((BlockRenderManager) (Object) this).renderBlock(
                    state,
                    pos,
                    world,
                    matrices,
                    tinted,
                    cull,
                    random
            );
        } finally {
            beyond_the_block$fireReentry.set(false);
        }

        ci.cancel();
    }
}