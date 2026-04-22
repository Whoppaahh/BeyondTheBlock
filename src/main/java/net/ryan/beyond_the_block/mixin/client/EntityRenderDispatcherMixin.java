package net.ryan.beyond_the_block.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.feature.fire.FireColourResolver;
import net.ryan.beyond_the_block.feature.fire.FireRenderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @Inject(method = "renderFire", at = @At("HEAD"))
    private void beyond$applyFireTint(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            Entity entity,
            CallbackInfo ci
    ) {
        if (entity.world == null) {
            return;
        }

        BlockPos pos = entity.getBlockPos();
        World world = entity.world;

        BlockState below = world.getBlockState(pos.down());
        boolean soulFire = below.isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS);

        int colour = FireColourResolver.resolve(world, pos, soulFire);

        float r = FireRenderHelper.red(colour);
        float g = FireRenderHelper.green(colour);
        float b = FireRenderHelper.blue(colour);

        RenderSystem.setShaderColor(r, g, b, 1.0F);
    }

    @Inject(method = "renderFire", at = @At("TAIL"))
    private void beyond$resetFireTint(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            Entity entity,
            CallbackInfo ci
    ) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}