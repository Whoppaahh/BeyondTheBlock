package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.config.Configs;
import net.ryan.beyond_the_block.utils.Accessors.HorseAccessor;
import net.ryan.beyond_the_block.utils.Helpers.StayNearData;
import net.ryan.beyond_the_block.utils.Naming.NameEngine;
import net.ryan.beyond_the_block.utils.Naming.NameableMob;
import net.ryan.beyond_the_block.utils.Naming.TitleResolvers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public abstract class HorseEntityMixin implements NameableMob, HorseAccessor {

    @Unique
    private StayNearData horsebuff$stayData;

    @Inject(method = "getPrimaryPassenger", at = @At("HEAD"), cancellable = true)
    private void forceFirstPassengerAsController(CallbackInfoReturnable<LivingEntity> cir) {
        AbstractHorseEntity horse = (AbstractHorseEntity)(Object)this;

        if (horse.getPassengerList().isEmpty()) {
            return; // let vanilla handle null
        }

        Entity first = horse.getPassengerList().get(0);
        if (first instanceof LivingEntity living) {
            cir.setReturnValue(living);
        }
    }


    @Inject(method = "updatePassengerPosition", at = @At("HEAD"), cancellable = true)
    private void handleSecondSeat(Entity passenger, CallbackInfo ci) {
        if (!passenger.isAlive() || passenger.isRemoved()) {
            ci.cancel();
            return;
        }

        AbstractHorseEntity horse = (AbstractHorseEntity)(Object)this;

        int index = horse.getPassengerList().indexOf(passenger);
        if (index != 1) return; // only override second passenger

        float yawRad = horse.bodyYaw * 0.017453292F;

        float xOffset = 0.0F;
        float yOffset = (float)(horse.getMountedHeightOffset() + passenger.getHeightOffset());
        float zOffset = -0.4F;

        Vec3d offset = new Vec3d(xOffset, yOffset, zOffset)
                .rotateY(-yawRad);

        passenger.setPosition(
                horse.getX() + offset.x,
                horse.getY() + offset.y,
                horse.getZ() + offset.z
        );

        passenger.setYaw(horse.getYaw());

        if (passenger instanceof LivingEntity living) {
            living.bodyYaw = horse.bodyYaw;
            living.headYaw = horse.getHeadYaw();
            living.prevBodyYaw = living.bodyYaw;
            living.prevHeadYaw = living.headYaw;
        }

        ci.cancel();
    }


    @Override
    public void beyond$setStayData(StayNearData data) {
        this.horsebuff$stayData = data;
    }

    @Override
    public StayNearData beyond$getStayData() {
        return horsebuff$stayData;
    }


    @Inject(method = "tick", at = @At("HEAD"))
    private void horsebuff$stayNearTick(CallbackInfo ci) {
        AbstractHorseEntity horse = (AbstractHorseEntity)(Object)this;

        if (horse.hasPassengers()) {
//            horse.getPassengerList().removeIf(p -> !p.isAlive());
            horsebuff$stayData = null;
            horse.setAngry(false);
            return;
        }
        if (!Configs.server().features.horses.preventWandering) return;

        StayNearData data = this.beyond$getStayData();
        if (data == null) return;


        double dist = horse.getPos().distanceTo(horsebuff$stayData.center);

        if (dist > Configs.server().features.horses.stayRadius) {
            horse.getNavigation().startMovingTo(
                    horsebuff$stayData.center.x,
                    horsebuff$stayData.center.y,
                    horsebuff$stayData.center.z,
                    1.0
            );
        }
    }


    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void horsebuff$swimMounted(CallbackInfo ci) {
        AbstractHorseEntity horse = (AbstractHorseEntity)(Object)this;

        if (Configs.server().features.horses.increaseStepHeight && horse.hasPassengers()) {
            horse.stepHeight = Math.max(horse.stepHeight, 1.1F);
        }


        if (!horse.hasPassengers()) return;
        if (!horse.isTouchingWater()) return;

        if (!Configs.server().features.horses.enableSwimming) return;

        if (!Configs.server().features.horses.undeadCanSwim &&
                (horse instanceof ZombieHorseEntity || horse instanceof SkeletonHorseEntity)) {
            return;
        }

        Vec3d vel = horse.getVelocity();

        if (vel.y < 0.05) {
            horse.setVelocity(vel.x, vel.y + 0.04, vel.z);
        }

        horse.fallDistance = 0;
    }


    @Inject(method = "setTame", at = @At("TAIL"))
    private void beyond$onHorseTamed(boolean tame, CallbackInfo ci) {
        if (!tame) return;

        AbstractHorseEntity horse = (AbstractHorseEntity)(Object)this;
        if (horse.getWorld().isClient()) return;
        if (horse.hasCustomName()) return;

        if (!Configs.client().visuals.names.enabled || !Configs.client().visuals.names.nameTamed) return;

        TitleResolvers.Title title = TitleResolvers.resolve(horse);
        if (title == null) return;

        NameEngine.assignName(
                horse,
                this,
                title.title(),
                title.colour(),
                Configs.client(),
                Configs.client().visuals.names.alliteration
        );
    }
    @Shadow
    protected SimpleInventory items;

    // Access the private static HORSE_FLAGS tracked data field
    @Accessor("HORSE_FLAGS")
    public static TrackedData<Byte> getHorseFlags() {
        throw new AssertionError();
    }

    // Access the protected SimpleInventory items field (saddle and armor inventory)
    @Accessor("items")
    public abstract SimpleInventory getItems();

    // Helper method to set saddle flag on this horse instance
    @Override
    public void beyond$setSaddledPublic(boolean saddled) {
        AbstractHorseEntity self = (AbstractHorseEntity) (Object) this;
        DataTracker tracker = self.getDataTracker();
        byte flags = tracker.get(getHorseFlags());
        if (saddled) {
            flags |= 0x4; // SADDLED_FLAG
        } else {
            flags &= ~0x4;
        }
        tracker.set(getHorseFlags(), flags);
    }

    // Helper method to set tamed flag on this horse instance
    @Override
    public void beyond$setTamedPublic(boolean tamed) {
        AbstractHorseEntity self = (AbstractHorseEntity) (Object) this;
        DataTracker tracker = self.getDataTracker();
        byte flags = tracker.get(getHorseFlags());
        if (tamed) {
            flags |= 0x2; // TAMED_FLAG
        } else {
            flags &= ~0x2;
        }
        tracker.set(getHorseFlags(), flags);
    }
    @Override
    public SimpleInventory beyond$getItemsPublic() {
        return this.items;
    }
}
