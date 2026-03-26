package net.ryan.beyond_the_block.mixin.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.config.access.Configs;
import net.ryan.beyond_the_block.content.enchantment.Armour.boots.LeapOfFaithTracker;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.content.particle.ModParticles;
import net.ryan.beyond_the_block.feature.horses.MountUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements LeapOfFaithTracker {


    @Unique
    private static final TrackedData<Integer> AIR_JUMP_COUNT =
            DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);


    @Inject(
            method = "getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F",
            at = @At("RETURN"),
            cancellable = true
    )
    private void horsebuff$removeMountedMiningPenalty(
            BlockState state,
            CallbackInfoReturnable<Float> cir
    ) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (!Configs.server().features.horses.removeMiningPenalty) return;
        if (!player.hasVehicle()) return;
        if (!(player.getVehicle() instanceof AbstractHorseEntity)) return;

        /*
         * Vanilla applies a 0.2F multiplier when mounted.
         * Undo that by dividing it back out.
         */
        float speed = cir.getReturnValue();
        cir.setReturnValue(speed / 0.2F);
    }

    @Override
    public TrackedData<Integer> emeraldEmpire$getAirJumpCountKey() {
        return AIR_JUMP_COUNT;
    }
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initTrackedData(CallbackInfo ci) {
        this.dataTracker.startTracking(AIR_JUMP_COUNT, 0);
       // EmeraldEmpire.LOGGER.info("Started Tracking Jumps");
    }

    @Inject(method = "jump", at = @At("HEAD"))
    private void onJump(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (player.isOnGround()) {
            ((LeapOfFaithTracker) player).setAirJumpCount(1); // First jump made
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if ((Object)this instanceof PlayerEntity player) {
            if (player.isOnGround() || player.isTouchingWater() || player.isInLava()) {
               // if (this.dataTracker.get(AIR_JUMP_COUNT) != 0) {
                    this.dataTracker.set(AIR_JUMP_COUNT, 0);
                //}
            }
            if (!player.isOnGround() && ((LeapOfFaithTracker) player).getAirJumpCount() > 0) {
                player.fallDistance = 0.0F;
            }

        }
    }

    //region Frozen Momentum
    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    private void applyFrozenMomentum(CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (EnchantmentHelper.getLevel(ModEnchantments.FROZEN_MOMENTUM, player.getEquippedStack(EquipmentSlot.LEGS)) > 0) {
            // Check if player is on ice or snow
            BlockState blockState = player.world.getBlockState(player.getBlockPos().down());

            // If the block under the player is snow or ice, increase movement speed
            if (blockState.isOf(Blocks.SNOW) || blockState.isOf(Blocks.ICE) || blockState.isOf(Blocks.PACKED_ICE)) {
                int level = EnchantmentHelper.getLevel(ModEnchantments.FROZEN_MOMENTUM, player.getEquippedStack(EquipmentSlot.LEGS));

                // Increase movement speed based on enchantment level
                float speed = 0.1f; // Base speed
                if (level == 1) {
                    speed += 0.1f;  // Level 1: 10% faster on snow/ice
                } else if (level == 2) {
                    speed += 0.2f;  // Level 2: 20% faster on snow/ice
                }

                // Apply the modified speed
                cir.setReturnValue(speed);
            }
        }
    }

    @Inject(method = "travel", at = @At("HEAD"))
    private void applyIceMovementBoost(Vec3d movementInput, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (EnchantmentHelper.getLevel(ModEnchantments.FROZEN_MOMENTUM, player.getEquippedStack(EquipmentSlot.LEGS)) > 0) {
            // Apply movement boost on ice blocks (including packed ice)
            BlockState blockState = player.world.getBlockState(player.getBlockPos().down());

            if (blockState.isOf(Blocks.ICE) || blockState.isOf(Blocks.PACKED_ICE)) {
                player.addVelocity(0.1, 0, 0.1);  // Slight boost in movement on ice
            }

            // Apply movement boost on snow blocks
            if (blockState.isOf(Blocks.SNOW)) {
                player.addVelocity(0.05, 0, 0.05);  // Slight boost on snow blocks
            }
        }
    }

//endregion
    //region Graceful Movement
    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void applyGracefulMovementFallDamage(float fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        int level = EnchantmentHelper.getLevel(ModEnchantments.GRACEFUL_MOVEMENT, player.getEquippedStack(EquipmentSlot.LEGS));

        if (level > 0) {

            // Reduce fall damage based on enchantment level
            if (fallDistance > 3) {  // If the fall distance is significant enough
                float reducedFallDamage = fallDistance * 0.5f * (level == 1 ? 0.5f : 0.25f);  // Level 1 reduces 50%, Level 2 reduces 75%
                cir.setReturnValue(Math.max(0, reducedFallDamage));  // Prevent any negative fall damage
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void applySlowFalling(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        int level = EnchantmentHelper.getLevel(ModEnchantments.GRACEFUL_MOVEMENT, player.getEquippedStack(EquipmentSlot.LEGS));

        if (level > 0) {

            // Apply slow falling effect when the player is in mid-air
            if (!player.isOnGround()) {
                // Apply slow falling for a short duration (1-2 seconds based on enchantment level)
                if (level == 1) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 40, 0, false, false, false)); // 2 seconds
                } else if (level == 2) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 60, 0, false, false, false)); // 3 seconds
                }
            }
        }
    }
//endregion
    //region Radiant Aura
    @Inject(method = "tick", at = @At("HEAD"))
    private void applyRadiantAuraEffect(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Check if player has Radiant Aura enchantment
        int level = EnchantmentHelper.getLevel(ModEnchantments.RADIANT_AURA, player.getEquippedStack(EquipmentSlot.HEAD));

        if (level > 0) {

            // Check for nearby entities within a radius of 10 blocks
            double radius = 10.0;  // Base radius
            if (level == 2) radius = 15.0;  // Increase radius for higher level

            // Loop through entities within the radius and apply healing effect
            for (Entity entity : player.world.getEntitiesByClass(Entity.class, player.getBoundingBox().expand(radius), e -> e instanceof LivingEntity)) {
                if (entity instanceof LivingEntity && entity != player) {
                    LivingEntity livingEntity = (LivingEntity) entity;

                    // Only apply healing to players or hostile mobs (you can change this logic if you want)
                    if (livingEntity instanceof PlayerEntity) {
                        // Apply healing effect (1 for level 1, 2 for level 2)
                        int healingAmount = level == 1 ? 1 : 2;
                        livingEntity.heal(healingAmount);
                    }
                }
            }
        }
    }
//endregion



    @Inject(method = "spawnSweepAttackParticles", at = @At("HEAD"), cancellable = true)
    private void onBleed(CallbackInfo ci){
        PlayerEntity player = (PlayerEntity)(Object)this;
        double d = -MathHelper.sin(player.getYaw() * (float) (Math.PI / 180.0));
        double e = MathHelper.cos(player.getYaw() * (float) (Math.PI / 180.0));
        if (player.world instanceof ServerWorld && player.getMainHandStack().isOf(ModItems.RUBY_SWORD)) {
            ((ServerWorld)player.world).spawnParticles(ModParticles.BLEED_SWEEP_PARTICLE, player.getX() + d, player.getBodyY(0.5), player.getZ() + e, 0, d, 0.0, e, 0.0);
            ci.cancel();
        }
    }

    @Inject(method = "getArrowType", at = @At("HEAD"), cancellable = true)
    private void arrowInjection(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (!(stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem)) return;

        PlayerEntity self = (PlayerEntity) (Object) this;
        ItemStack best = findBestArrow(self); // always pick best arrow first

        int infinityLevel = EnchantmentHelper.getLevel(Enchantments.INFINITY, stack);

        // Level 2+ fallback: dummy arrow if no arrows
        if (infinityLevel >= 2 && best.isEmpty()) {
            best = new ItemStack(Items.ARROW);
        }

        // Level 3 tweak: tipped/spectral arrows never consumed
        if (infinityLevel == 3 && !best.isEmpty() &&
                (best.isOf(Items.TIPPED_ARROW) || best.isOf(Items.SPECTRAL_ARROW))) {
            best = best.copy();
        }

        if (!best.isEmpty()) {
            cir.setReturnValue(best);
        }
    }

    @Unique
        private ItemStack findBestArrow(PlayerEntity player) {
            ItemStack tipped = ItemStack.EMPTY;
            ItemStack normal = ItemStack.EMPTY;

            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (stack.isEmpty()) continue;

                if (stack.isOf(Items.SPECTRAL_ARROW)) {
                    return stack; // highest priority
                }
                if (stack.isOf(Items.TIPPED_ARROW) && tipped.isEmpty()) {
                    tipped = stack;
                }
                if (stack.isOf(Items.ARROW) && normal.isEmpty()) {
                    normal = stack;
                }
            }

            if (!tipped.isEmpty()) return tipped;
            if (!normal.isEmpty()) return normal;
            return ItemStack.EMPTY;
        }

    @Inject(
            method = "interact",
            at = @At("HEAD"),
            cancellable = true
    )
    private void beyond_the_block$sneakKickMount(
            Entity entity,
            Hand hand,
            CallbackInfoReturnable<ActionResult> cir
    ) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.getWorld().isClient) return;
        if (!player.isSneaking()) return;

        // Hard PvP gate: never affect players
        if (entity instanceof PlayerEntity) return;

        // Only act if mounting is involved
        if (!entity.hasVehicle() && entity.getPassengerList().isEmpty()) return;

        MountUtils.safelyKickOffMount(entity);

        // Prevent vanilla interaction (mounting, trading, etc.)
        cir.setReturnValue(ActionResult.SUCCESS);
    }

    }



