package net.ryan.beyond_the_block.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.config.schema.ConfigClient;
import net.ryan.beyond_the_block.utils.FireColourResolver;
import net.ryan.beyond_the_block.utils.FireRenderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin {

    @Inject(method = "renderFireOverlay", at = @At("HEAD"))
    private static void beyond$applyFireOverlayTint(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (client.player == null || client.world == null) {
            return;
        }

        ConfigClient.Fire fireConfig = Configs.client().visuals.fire;

        int colour = resolveOverlayColour(client.world, client.player);
        float r = FireRenderHelper.red(colour);
        float g = FireRenderHelper.green(colour);
        float b = FireRenderHelper.blue(colour);
        float a = fireConfig.overlayOpacity;

        RenderSystem.setShaderColor(r, g, b, a);
    }

    @Inject(method = "renderFireOverlay", at = @At("RETURN"))
    private static void beyond$resetFireOverlayTint(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Unique
    private static int resolveOverlayColour(World world, Entity entity) {
        BlockPos pos = entity.getBlockPos();

        BlockState feet = world.getBlockState(pos);
        BlockState below = world.getBlockState(pos.down());

        boolean soulFire = feet.isOf(Blocks.SOUL_FIRE) || below.isOf(Blocks.SOUL_SAND) || below.isOf(Blocks.SOUL_SOIL);

        return FireColourResolver.resolve(world, pos, soulFire);
    }
}