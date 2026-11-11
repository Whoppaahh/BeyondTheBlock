package net.ryan.beyond_the_block.feature.rails;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.advancements.ModCriteria;
import net.ryan.beyond_the_block.utils.accessors.MinecartCouplerAccess;

import java.util.List;
import java.util.UUID;

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

        if (stack.isOf(Items.SHEARS)) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            }

            MinecartCouplerComponent couplers = ((MinecartCouplerAccess) self).beyond_the_block$getCouplers();
            if (!couplers.hasAny()) {
                return ActionResult.PASS;
            }

            // Unlink both sides
            if (world instanceof ServerWorld serverWorld) {
                for (CouplerSide side : CouplerSide.values()) {
                    UUID otherId = couplers.get(side);
                    if (otherId != null) {
                        Entity other = serverWorld.getEntity(otherId);
                        if (other instanceof AbstractMinecartEntity otherCart) {
                            MinecartCouplerComponent otherCouplers = ((MinecartCouplerAccess) otherCart).beyond_the_block$getCouplers();
                            otherCouplers.clear(getOpposite(side));
                            ((MinecartCouplerAccess) otherCart).beyond_the_block$syncCouplers();
                        }
                        MinecartChainLinkManager.removeLinkEntity(serverWorld, self.getUuid(), otherId);
                    }
                    couplers.clear(side);
                }
                ((MinecartCouplerAccess) self).beyond_the_block$syncCouplers();

                serverWorld.playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                serverWorld.spawnParticles(ParticleTypes.SCRAPE, self.getX(), self.getY() + 0.5, self.getZ(), 10, 0.2, 0.2, 0.2, 0.1);
            }
            return ActionResult.SUCCESS;
        }

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
            serverWorld.playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.BLOCK_CHAIN_PLACE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            serverWorld.spawnParticles(ParticleTypes.COMPOSTER, self.getX(), self.getY() + 0.5, self.getZ(), 10, 0.2, 0.2, 0.2, 0.1);
        }

        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        if (player instanceof ServerPlayerEntity serverPlayer) {
            List<AbstractMinecartEntity> train = MinecartTrainUtils.collectTrain(self);
            ModCriteria.MINECART_LINKED.trigger(serverPlayer, train.size());
        }

        MinecartLinkingState.clear(player);
        return ActionResult.SUCCESS;
    }

    private static CouplerSide getSideToward(AbstractMinecartEntity source, AbstractMinecartEntity target) {
        Vec3d sourcePos = source.getPos();
        Vec3d targetPos = target.getPos();
        Vec3d toTarget = targetPos.subtract(sourcePos).normalize();

        // Use rotation vector if moving, otherwise fall back to default orientation logic
        Vec3d forward = source.getRotationVector().normalize();
        if (source.getVelocity().lengthSquared() > 0.001) {
            forward = source.getVelocity().normalize();
        }

        if (forward.lengthSquared() < 1.0E-6D || toTarget.lengthSquared() < 1.0E-6D) {
            // Default to X or Z based on major axis if stationary and rotation is weird
            return toTarget.x + toTarget.z >= 0 ? CouplerSide.FRONT : CouplerSide.BACK;
        }

        return forward.dotProduct(toTarget) >= 0.0D ? CouplerSide.FRONT : CouplerSide.BACK;
    }

    private static CouplerSide getOpposite(CouplerSide side) {
        return side == CouplerSide.FRONT ? CouplerSide.BACK : CouplerSide.FRONT;
    }
}