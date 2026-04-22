package net.ryan.beyond_the_block.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.fire.FireColourResolver;
import net.ryan.beyond_the_block.feature.fire.FireRenderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin {

    @Inject(method = "renderFireOverlay", at = @At("HEAD"))
    private static void beyond$applyFireTint(MinecraftClient client, net.minecraft.client.util.math.MatrixStack matrices, CallbackInfo ci) {
        if (client.player == null || client.world == null) {
            return;
        }

        int colour = resolveOverlayColour(client.world, client.player);

        float r = FireRenderHelper.red(colour);
        float g = FireRenderHelper.green(colour);
        float b = FireRenderHelper.blue(colour);
        float a = Configs.client().visuals.fire.overlayOpacity;

        RenderSystem.setShaderColor(r, g, b, a);
    }

    @Inject(method = "renderFireOverlay", at = @At("TAIL"))
    private static void beyond$resetFireTint(MinecraftClient client, net.minecraft.client.util.math.MatrixStack matrices, CallbackInfo ci) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Unique
    private static boolean isSoulFire(World world, Entity entity) {
        BlockPos pos = entity.getBlockPos();
        BlockState feet = world.getBlockState(pos);
        return feet.isOf(Blocks.SOUL_FIRE);
    }

    @Unique
    private static int resolveOverlayColour(World world, Entity entity) {
        BlockPos pos = entity.getBlockPos();
        boolean soulFire = isSoulFire(world, entity);
        return FireColourResolver.resolve(world, pos, soulFire);
    }
}