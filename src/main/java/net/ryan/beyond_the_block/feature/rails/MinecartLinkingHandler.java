package net.ryan.beyond_the_block.feature.rails;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.ryan.beyond_the_block.utils.accessors.MinecartCouplerAccess;

public final class MinecartLinkingHandler {

    private MinecartLinkingHandler() {
    }


    public static ActionResult onUseEntity(PlayerEntity player, net.minecraft.world.World world, Hand hand, Entity entity, net.minecraft.util.hit.EntityHitResult hitResult) {
        if (!(entity instanceof AbstractMinecartEntity self)) {
            return ActionResult.PASS;
        }

        if (!player.isSneaking()) {
            return ActionResult.PASS;
        }

        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.CHAIN)) {
            return ActionResult.PASS;
        }

        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        AbstractMinecartEntity first = MinecartLinkingState.get(player);

        if (first == null) {
            MinecartLinkingState.set(player, self);
            return ActionResult.SUCCESS;
        }

        if (first == self) {
            MinecartLinkingState.clear(player);
            return ActionResult.SUCCESS;
        }

        if (first.isRemoved()) {
            MinecartLinkingState.clear(player);
            return ActionResult.FAIL;
        }

        if (MinecartTrainUtils.areInSameTrain(first, self)) {
            MinecartLinkingState.clear(player);
            return ActionResult.FAIL;
        }

        CouplerSide firstSide = getSideToward(first, self);
        CouplerSide selfSide = getOpposite(firstSide);

        MinecartCouplerComponent firstCouplers =
                ((MinecartCouplerAccess) first).beyond_the_block$getCouplers();
        MinecartCouplerComponent selfCouplers =
                ((MinecartCouplerAccess) self).beyond_the_block$getCouplers();

        if (firstCouplers.isOccupied(firstSide) || selfCouplers.isOccupied(selfSide)) {
            MinecartLinkingState.clear(player);
            return ActionResult.FAIL;
        }

        firstCouplers.set(firstSide, self.getUuid());
        selfCouplers.set(selfSide, first.getUuid());

        ((MinecartCouplerAccess) first).beyond_the_block$syncCouplers();
        ((MinecartCouplerAccess) self).beyond_the_block$syncCouplers();

        if (first.getWorld() instanceof ServerWorld serverWorld) {
            MinecartChainLinkManager.ensureLinkEntity(serverWorld, first, self);
        }

        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        MinecartLinkingState.clear(player);
        return ActionResult.SUCCESS;
    }

    private static CouplerSide getSideToward(AbstractMinecartEntity source, AbstractMinecartEntity target) {
        var forward = source.getRotationVector().normalize();
        var toTarget = target.getPos().subtract(source.getPos()).normalize();

        if (forward.lengthSquared() < 1.0E-6D || toTarget.lengthSquared() < 1.0E-6D) {
            return CouplerSide.FRONT;
        }

        return forward.dotProduct(toTarget) >= 0.0D ? CouplerSide.FRONT : CouplerSide.BACK;
    }

    private static CouplerSide getOpposite(CouplerSide side) {
        return side == CouplerSide.FRONT ? CouplerSide.BACK : CouplerSide.FRONT;
    }
}