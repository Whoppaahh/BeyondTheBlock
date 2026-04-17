package net.ryan.beyond_the_block.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.client.render.entity.MinecartChainRenderer;
import net.ryan.beyond_the_block.utils.CouplerSide;
import net.ryan.beyond_the_block.utils.accessors.MinecartCouplerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(MinecartEntityRenderer.class)
public abstract class MinecartRendererMixin {

    @Inject(method = "render*", at = @At("TAIL"))
    private void beyond_the_block$renderChains(
            AbstractMinecartEntity cart,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider consumers,
            int light,
            CallbackInfo ci
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.gameRenderer == null || client.gameRenderer.getCamera() == null) {
            return;
        }

        MinecartCouplerAccess access = (MinecartCouplerAccess) cart;
        Vec3d cameraPos = client.gameRenderer.getCamera().getPos();

        for (CouplerSide side : CouplerSide.values()) {
            UUID otherId = access.beyond_the_block$getSyncedCoupler(side);
            if (otherId == null) {
                continue;
            }

            AbstractMinecartEntity otherCart = beyond_the_block$findLinkedCart(cart, otherId);
            if (otherCart == null) {
                continue;
            }

            // render once per linked pair
            if (cart.getUuid().compareTo(otherCart.getUuid()) > 0) {
                continue;
            }

            Vec3d forward = cart.getRotationVector().normalize();
            if (forward.lengthSquared() < 1.0E-6D) {
                forward = new Vec3d(0.0D, 0.0D, 1.0D);
            }

            Vec3d sideOffset = side == CouplerSide.FRONT
                    ? forward.multiply(0.35D)
                    : forward.multiply(-0.35D);

            Vec3d fromWorld = cart.getLerpedPos(tickDelta).add(0.0D, 0.25D, 0.0D).add(sideOffset);
            Vec3d toWorld = otherCart.getLerpedPos(tickDelta).add(0.0D, 0.25D, 0.0D);

            matrices.push();
            matrices.translate(
                    fromWorld.x - cameraPos.x,
                    fromWorld.y - cameraPos.y,
                    fromWorld.z - cameraPos.z
            );

            MinecartChainRenderer.render(
                    matrices,
                    consumers,
                    Vec3d.ZERO,
                    toWorld.subtract(fromWorld),
                    light
            );

            matrices.pop();
        }
    }

    @Unique
    private static AbstractMinecartEntity beyond_the_block$findLinkedCart(AbstractMinecartEntity cart, UUID uuid) {
        Box searchBox = cart.getBoundingBox().expand(64.0D);

        List<AbstractMinecartEntity> nearby = cart.getWorld().getEntitiesByClass(
                AbstractMinecartEntity.class,
                searchBox,
                entity -> entity.getUuid().equals(uuid)
        );

        return nearby.isEmpty() ? null : nearby.get(0);
    }
}