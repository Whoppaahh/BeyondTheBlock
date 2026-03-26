package net.ryan.beyond_the_block.mixin.feature.horses;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.feature.horses.StayNearData;
import net.ryan.beyond_the_block.utils.Accessors.HorseAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public class HorseEntityBehaviourMixin implements HorseAccessor {
    @Unique
    private StayNearData btb$stayData;
    @Unique private float beyond$baseStepHeight = -1f;

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

    @Override
    public void beyond$setStayData(StayNearData data) {
        this.btb$stayData = data;
    }

    @Override
    public StayNearData beyond$getStayData() {
        return btb$stayData;
    }


    @Inject(method = "tick", at = @At("HEAD"))
    private void horsebuff$stayNearTick(CallbackInfo ci) {
        AbstractHorseEntity horse = (AbstractHorseEntity)(Object)this;

        if (horse.hasPassengers()) {
//            horse.getPassengerList().removeIf(p -> !p.isAlive());
            btb$stayData = null;
            horse.setAngry(false);
            return;
        }
        if (!Configs.server().features.horses.preventWandering) return;

        StayNearData data = this.beyond$getStayData();
        if (data == null) return;


        double dist = horse.getPos().distanceTo(btb$stayData.center);

        if (dist > Configs.server().features.horses.stayRadius) {
            horse.getNavigation().startMovingTo(
                    btb$stayData.center.x,
                    btb$stayData.center.y,
                    btb$stayData.center.z,
                    1.0
            );
        }
    }


    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void horsebuff$swimMounted(CallbackInfo ci) {
        AbstractHorseEntity horse = (AbstractHorseEntity)(Object)this;

        if (Configs.server().features.horses.increaseStepHeight && horse.hasPassengers()) {
            beyond$baseStepHeight = horse.stepHeight;
            horse.stepHeight = Math.max(horse.stepHeight, 1.1F);
        }


        if (!horse.hasPassengers()){
            horse.stepHeight = beyond$baseStepHeight;
            return;
        }
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
}
