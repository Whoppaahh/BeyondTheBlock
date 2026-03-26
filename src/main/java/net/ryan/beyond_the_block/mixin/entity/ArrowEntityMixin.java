package net.ryan.beyond_the_block.mixin.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.feature.projectile.HomingTrackedData;
import net.ryan.beyond_the_block.mixin.feature.projectiles.ArrowEntityAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends PersistentProjectileEntity {

    protected ArrowEntityMixin(EntityType<? extends PersistentProjectileEntity> type, World world) {
        super(type, world);
    }

    @Shadow
    private Potion potion;

    private static final double AOE_RADIUS = 3.0;
    @Unique private static final double HOMING_RADIUS = 30.0;
    @Unique private static final double MIN_SPEED = 0.1;
    @Unique private static final int SCAN_COOLDOWN_TICKS = 10;
    @Unique private static final double MAX_TURN_DEGREES = 15.0;

    @Unique private UUID homingTargetUuid = null;
    @Unique private int targetScanCooldown = 0;

    @Unique private int homingMaxTargets = 1; // from enchantment level
    @Unique private int homingTargetsHit = 0;
    @Unique private final List<UUID> hitTargets = new ArrayList<>();

    // Apply potion effects to all nearby living entities
    private void applyAOE(Vec3d pos, Potion potion, Iterable<StatusEffectInstance> customEffects, LivingEntity owner, LivingEntity target) {
        ArrowEntity self = (ArrowEntity)(Object)this;

        List<LivingEntity> nearby = self.world.getEntitiesByClass(
                LivingEntity.class,
                new Box(
                        pos.x - AOE_RADIUS, pos.y - AOE_RADIUS, pos.z - AOE_RADIUS,
                        pos.x + AOE_RADIUS, pos.y + AOE_RADIUS, pos.z + AOE_RADIUS
                ),
                e -> e.isAlive() && e != owner && e != target // avoid hitting the shooter
        );

        for (LivingEntity entity : nearby) {
            // Apply potion effects from the arrow's Potion
            for (StatusEffectInstance effect : potion.getEffects()) {
                entity.addStatusEffect(new StatusEffectInstance(
                        effect.getEffectType(),
                        Math.max(effect.getDuration() / 8, 1),
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.shouldShowParticles()
                ), owner);
            }

            // Apply any custom effects added to the arrow
            for (StatusEffectInstance effect : customEffects) {
                entity.addStatusEffect(effect, owner);
            }
        }
    }
    @Unique
    private void updateHomingMaxTargets() {
        if (this.getOwner() instanceof LivingEntity shooter) {
            ItemStack mainHand = shooter.getMainHandStack();
            int level = EnchantmentHelper.getLevel(ModEnchantments.HOMING, mainHand);
            homingMaxTargets = Math.max(1, level); // max 3 you can clamp if you want
        } else {
            homingMaxTargets = 1;
        }
    }


    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initHomingDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(HomingTrackedData.HAS_HOMING, false);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeHomingNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("HasHoming", this.dataTracker.get(HomingTrackedData.HAS_HOMING));
        if (homingTargetUuid != null) {
            nbt.putUuid("HomingTargetUuid", homingTargetUuid);
        }
        nbt.putInt("HomingTargetsHit", homingTargetsHit);
        nbt.putInt("HomingMaxTargets", homingMaxTargets);

        NbtList hitListNbt = new NbtList();
        for (UUID uuid : hitTargets) {
            NbtCompound compound = new NbtCompound();
            compound.putUuid("uuid", uuid);
            hitListNbt.add(compound);

        }
        nbt.put("HitTargets", hitListNbt);

        nbt.putInt("ScanCooldown", targetScanCooldown);
    }


    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readHomingNbt(NbtCompound nbt, CallbackInfo ci) {
        this.dataTracker.set(HomingTrackedData.HAS_HOMING, nbt.getBoolean("HasHoming"));
        if (nbt.containsUuid("HomingTargetUuid")) {
            homingTargetUuid = nbt.getUuid("HomingTargetUuid");
        }
        homingTargetsHit = nbt.getInt("HomingTargetsHit");
        homingMaxTargets = nbt.getInt("HomingMaxTargets");

        hitTargets.clear();
        if (nbt.contains("HitTargets", 9)) { // 9 = NbtList
            NbtList hitListNbt = nbt.getList("HitTargets", 10); // 10 = NbtCompound
            for (int i = 0; i < hitListNbt.size(); i++) {
                NbtCompound compound = hitListNbt.getCompound(i);
                if (compound.containsUuid("uuid")) {
                    hitTargets.add(compound.getUuid("uuid"));
                }
            }
        }
        targetScanCooldown = nbt.getInt("ScanCooldown");
    }


    @Inject(method = "tick", at = @At("HEAD"))
    private void homingTick(CallbackInfo ci) {
        if (!this.dataTracker.get(HomingTrackedData.HAS_HOMING) || this.inGround || this.isRemoved()) return;

        updateHomingMaxTargets();
        this.setNoGravity(true);

        if (this.getWorld().isClient || this.getVelocity().lengthSquared() < MIN_SPEED * MIN_SPEED) return;

        LivingEntity target = resolveTarget();
        if (target == null) return;

        Vec3d toTarget = target.getEyePos().subtract(this.getPos()).normalize();
        Vec3d currentVelocity = this.getVelocity();
        double speed = currentVelocity.length();

        Vec3d newDirection = steerToward(currentVelocity.normalize(), toTarget);
        this.setVelocity(newDirection.multiply(speed));

        if (this.getWorld() instanceof ServerWorld serverWorld && this.getWorld().getTime() % 2 == 0) {
            serverWorld.spawnParticles(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0.001);
        }
    }

    @Unique
    private LivingEntity resolveTarget() {
        World world = this.getWorld();

        if (homingTargetUuid != null) {
            Entity entity = ((ServerWorld) world).getEntity(homingTargetUuid);
            if (entity instanceof LivingEntity living && isValidTarget(living)) {
                return living;
            }
            homingTargetUuid = null;
        }

        if (--targetScanCooldown <= 0) {
            targetScanCooldown = SCAN_COOLDOWN_TICKS;
            LivingEntity newTarget = findNearestVisibleTarget();
            if (newTarget != null) {
                homingTargetUuid = newTarget.getUuid();
                return newTarget;
            }
        }

        return null;
    }

    @Unique
    private Vec3d steerToward(Vec3d current, Vec3d desired) {
        double angle = Math.acos(MathHelper.clamp(current.dotProduct(desired), -1.0, 1.0));
        double maxTurn = Math.toRadians(MAX_TURN_DEGREES);
        if (angle < maxTurn) return desired;

        Vec3d axis = current.crossProduct(desired).normalize();
        double t = maxTurn / angle;
        return slerp(current, desired, t);
    }

    @Unique
    private Vec3d slerp(Vec3d v0, Vec3d v1, double t) {
        double dot = MathHelper.clamp(v0.dotProduct(v1), -1.0, 1.0);
        double theta = Math.acos(dot) * t;
        Vec3d relative = v1.subtract(v0.multiply(dot)).normalize();
        return v0.multiply(Math.cos(theta)).add(relative.multiply(Math.sin(theta)));
    }

    @Unique
    private boolean isValidTarget(LivingEntity entity) {
        return entity.isAlive()
                && !entity.isSpectator()
                && entity != this.getOwner()
                && (!(this.getOwner() instanceof LivingEntity owner) || !owner.isTeammate(entity));
    }

    @Unique
    private LivingEntity findNearestVisibleTarget() {
        Vec3d pos = this.getPos();
        Vec3d dir = this.getVelocity().normalize();
        Box searchBox = new Box(pos, pos).expand(HOMING_RADIUS);

        return this.getWorld().getEntitiesByClass(LivingEntity.class, searchBox, e -> isValidTarget(e) && canSee(e)
                && !hitTargets.contains(e.getUuid()))
                .stream()
                .min(Comparator.comparingDouble(e -> {
                    Vec3d to = e.getEyePos().subtract(pos).normalize();
                    double angleWeight = 1.0 - dir.dotProduct(to);
                    return pos.distanceTo(e.getPos()) * (1.0 + angleWeight);
                }))
                .orElse(null);
    }

    @Unique
    private boolean canSee(Entity entity) {
        Vec3d start = this.getPos();
        Vec3d end = entity.getEyePos();
        HitResult hit = this.getWorld().raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
        return hit.getType() == HitResult.Type.MISS || hit.getPos().squaredDistanceTo(end) < 1.0;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickAOE(CallbackInfo ci) {
        ArrowEntity self = (ArrowEntity)(Object)this;
        Set<StatusEffectInstance> effects = ((ArrowEntityAccessor) self).getEffects();
        if (!self.world.isClient && self.isOnGround() && !effects.isEmpty() && potion != null) {
            Entity ownerEntity = self.getEffectCause();
            LivingEntity owner = ownerEntity instanceof LivingEntity le ? le : null;
            applyAOE(self.getPos(), potion, effects, owner, null);
            // Clear potion so it doesn't repeatedly apply
            effects.clear();
            potion = Potions.EMPTY;
        }
    }


    @Inject(method = "onHit", at = @At("HEAD"))
    private void onHitMerged(LivingEntity target, CallbackInfo ci) {
        if (this.getWorld().isClient || target == null) return;

        ArrowEntity self = (ArrowEntity)(Object)this;
        Set<StatusEffectInstance> effects = ((ArrowEntityAccessor) self).getEffects();
        if (potion == null || (potion == Potions.EMPTY && effects.isEmpty())) return;
        Entity ownerEntity = self.getEffectCause();
        LivingEntity lie = ownerEntity instanceof LivingEntity le ? le : null;

        applyAOE(self.getPos(), potion, effects, lie, target);

        // Lightning enchantment logic
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity shooter) {
            ItemStack mainHand = shooter.getMainHandStack();
            if (mainHand.getItem() instanceof BowItem) {
                int level = EnchantmentHelper.getLevel(ModEnchantments.WRATH_OF_THOR, mainHand);
                if (level > 0) {
                    BlockPos pos = target.getBlockPos();
                    ServerWorld server = (ServerWorld) this.getWorld();
                    for (int i = 0; i < level; i++) {
                        EntityType.LIGHTNING_BOLT.spawn(server, null, null, null, pos, SpawnReason.TRIGGERED, true, true);
                    }
                }
            }
        }

        // Homing target logic
        hitTargets.add(target.getUuid());
        homingTargetsHit++;

        if (homingTargetsHit < homingMaxTargets) {
            homingTargetUuid = null;
            targetScanCooldown = 0;
            this.setNoGravity(true);
            this.setVelocity(this.getVelocity().normalize().multiply(this.getVelocity().length())); // maintain speed
        } else {
            this.dataTracker.set(HomingTrackedData.HAS_HOMING, false);
            homingTargetUuid = null;
        }
    }
}
