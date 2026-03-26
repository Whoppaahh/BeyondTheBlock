package net.ryan.beyond_the_block.mixin.entity;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.entity.SlimeMergeGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends net.minecraft.entity.mob.MobEntity {
    @Shadow public abstract int getSize();
    @Shadow public abstract void setSize(int size, boolean resetHealth);

    @Unique
    private int recombineCooldown = 0;
    @Unique
    private int INITIAL_COOLDOWN = 200;

    protected SlimeEntityMixin(EntityType<? extends net.minecraft.entity.mob.MobEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "initialize", at = @At("TAIL"))
    private void onInitializeInject(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        this.recombineCooldown = INITIAL_COOLDOWN;
    }

    @Inject(method = "setSize", at = @At("TAIL"))
    private void onSetSize(int size, boolean resetHealth, CallbackInfo ci) {
        if (size < 4) {
            this.recombineCooldown = INITIAL_COOLDOWN;
        }
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void addMergeGoal(CallbackInfo ci) {
        SlimeEntity self = (SlimeEntity)(Object)this;
        self.goalSelector.add(5, new SlimeMergeGoal(self));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (this.world.isClient) return;

        if (recombineCooldown > 0) {
            recombineCooldown--;
            return;
        }

        if (this.getSize() >= 4 || this.isRemoved()) return;

        // Check collisions with nearby slimes of the same size
        List<SlimeEntity> nearby = this.world.getEntitiesByClass(
                SlimeEntity.class,
                this.getBoundingBox().expand(0.5),
                s -> s != (Object)this && s.getSize() == this.getSize() && !s.isRemoved()
        );

        if (!nearby.isEmpty()) {
            if (this.random.nextFloat() > 0.20f) {
                // Skip combining 80% of the time
                return;
            }
            SlimeEntity partner = nearby.get(0);

            // Spawn bigger slime at average position
            double newX = (this.getX() + partner.getX()) / 2.0;
            double newY = (this.getY() + partner.getY()) / 2.0;
            double newZ = (this.getZ() + partner.getZ()) / 2.0;

            SlimeEntity biggerSlime = EntityType.SLIME.create(this.world);
            if (biggerSlime != null) {
                biggerSlime.setSize(this.getSize() + 1, true);
                biggerSlime.refreshPositionAndAngles(newX, newY, newZ, this.getYaw(), this.getPitch());
                this.world.spawnEntity(biggerSlime);

                // Set cooldown on the bigger slime to avoid immediate recombine
                ((SlimeEntityMixin)(Object)biggerSlime).recombineCooldown = INITIAL_COOLDOWN;
            }

            // Remove the two smaller slimes
            this.discard();
            partner.discard();
        }
    }

}
