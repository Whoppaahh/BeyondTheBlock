package net.ryan.beyond_the_block.content.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.entity.BoatEntity;
import net.ryan.beyond_the_block.content.registry.family.ModBoatVariant;

import java.util.List;
import java.util.function.Predicate;

public class ModBoatItem extends Item {
    private static final Predicate<net.minecraft.entity.Entity> RIDERS =
            entity -> entity.canHit() && entity.isCollidable();

    private final ModBoatVariant variant;

    public ModBoatItem(ModBoatVariant variant, Settings settings) {
        super(settings);
        this.variant = variant;
        DispenserBlock.registerBehavior(this, DispenserBehavior.NOOP);
    }

    public ModBoatVariant getVariant() {
        return this.variant;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);

        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        }

        List<net.minecraft.entity.Entity> list = world.getOtherEntities(
                user,
                user.getBoundingBox().stretch(user.getRotationVec(1.0F).multiply(5.0D)).expand(1.0D),
                RIDERS
        );

        if (!list.isEmpty()) {
            net.minecraft.util.math.Vec3d cameraPos = user.getCameraPosVec(1.0F);

            for (net.minecraft.entity.Entity entity : list) {
                if (entity.getBoundingBox().expand(entity.getTargetingMargin()).contains(cameraPos)) {
                    return TypedActionResult.pass(itemStack);
                }
            }
        }

        BoatEntity boatEntity = new BoatEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
        boatEntity.setVariant(this.variant);
        boatEntity.setYaw(user.getYaw());

        if (!world.isSpaceEmpty(boatEntity, boatEntity.getBoundingBox())) {
            return TypedActionResult.fail(itemStack);
        }

        if (!world.isClient) {
            world.spawnEntity(boatEntity);
            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
        }

        user.incrementStat(net.minecraft.stat.Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
