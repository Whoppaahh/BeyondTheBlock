package net.ryan.beyond_the_block.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.ryan.beyond_the_block.feature.fire.FireColourResolver;
import net.ryan.beyond_the_block.feature.fire.TintingVertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderManager.class)
@Environment(EnvType.CLIENT)
public abstract class BlockRenderManagerMixin {

    @Inject(
            method = "renderBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void beyond$hideNearbyLeavesWhenMounted(
            BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, CallbackInfo ci
    ) {
        if (!(state.getBlock() instanceof LeavesBlock)) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (!(mc.player.getVehicle() instanceof AbstractHorseEntity)) return;
        if (mc.gameRenderer == null || mc.gameRenderer.getCamera() == null) return;

        Vec3d camPos = mc.gameRenderer.getCamera().getPos();
        Vec3d blockCenter = Vec3d.ofCenter(pos);

        // Hide leaf blocks very near / around the rider camera
        if (camPos.squaredDistanceTo(blockCenter) <= 0.75 * 0.75) {
            ci.cancel();
        }
    }
}