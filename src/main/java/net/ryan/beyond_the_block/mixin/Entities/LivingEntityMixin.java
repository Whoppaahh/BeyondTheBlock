package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.effect.Beneficial.SoulLinkEffect;
import net.ryan.beyond_the_block.effect.ModEffects;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.enchantment.MyEnchantmentHelper;
import net.ryan.beyond_the_block.utils.ProjectileHelpers.ArrowHitsAccess;
import net.ryan.beyond_the_block.utils.Helpers.BleedingParticleHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements ArrowHitsAccess {

    @Unique
    private final List<NbtCompound> arrowdrops$arrowHits = new ArrayList<>();

    @Override
    public List<NbtCompound> beyondTheBlock$getArrowHits() {
        return arrowdrops$arrowHits;
    }

    // Save
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void arrowdrops$writeArrowData(NbtCompound nbt, CallbackInfo ci) {
        NbtList list = new NbtList();
        for (NbtCompound data : arrowdrops$arrowHits) {
            list.add(data.copy());
        }
        nbt.put("ArrowDropsData", list);
    }

    // Load
    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void arrowdrops$readArrowData(NbtCompound nbt, CallbackInfo ci) {
        arrowdrops$arrowHits.clear();
        if (nbt.contains("ArrowDropsData", NbtElement.LIST_TYPE)) {
            NbtList list = nbt.getList("ArrowDropsData", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < list.size(); i++) {
                arrowdrops$arrowHits.add(list.getCompound(i));
            }
        }
    }


    @Unique
    private int getEnchantmentLevelForPlayer(PlayerEntity player, Enchantment enchantment) {
        for (ItemStack armorPiece : player.getArmorItems()) {
            int level = EnchantmentHelper.getLevel(enchantment, armorPiece);
            if (level > 0) {
                return level;
            }
        }
        return 0; // No enchantment found
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void emeraldEmpire$spawnBloodParticles(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (!self.getWorld().isClient && amount > 0) {
            Vec3d attackerVec = source.getAttacker() != null
                    ? source.getAttacker().getPos().subtract(self.getPos())
                    : new Vec3d(0, 0, 0); // for fire, fall, etc.

            BleedingParticleHandler.spawnBloodSplatter((ServerWorld) self.getWorld(), self.getPos(), attackerVec, amount);
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void PreventEtherealDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof LivingEntity attacker) {
            if (attacker.hasStatusEffect(ModEffects.ETHEREAL_VEIL)) {
                cir.setReturnValue(false); // Cancel damage dealt by ethereal entity
            }
        }
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void ApplyEchoGuard(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // Check if the entity is a player and is wearing Echo Guard chestplate
        if ((Object) this instanceof PlayerEntity player) {
            if (player != null && EnchantmentHelper.getLevel(ModEnchantments.ECHO_GUARD, player.getEquippedStack(EquipmentSlot.CHEST)) > 0) {
                // Reflect a portion of the damage back to the attacker
                Entity attacker = source.getSource();
                if (attacker instanceof LivingEntity) {
                    float reflectedDamage = amount * 0.25f; // 25% of the damage is reflected
                    attacker.damage(DamageSource.player(player), reflectedDamage);
                    // Play a sound to indicate the damage reflection
                    player.world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, 1.0F, 1.0F);

                    // Show a particle effect to visually indicate the reflection
                    player.world.addParticle(ParticleTypes.EXPLOSION, player.getX(), player.getY(), player.getZ(), 0.1, 0.1, 0.1);
                }
            }
        }
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void ApplySoulLinkDamageSharing(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;  // This is the entity taking damage

        if (entity.hasStatusEffect(ModEffects.SOUL_LINK)) {
            StatusEffectInstance effectInstance = entity.getStatusEffect(ModEffects.SOUL_LINK);
            if (effectInstance != null && effectInstance.getEffectType() instanceof SoulLinkEffect soulLink) {
                LivingEntity linked = SoulLinkEffect.getLinkedEntityFor(entity);
                if (linked != null && linked.isAlive() && linked != entity) {
                    linked.damage(DamageSource.MAGIC, amount);
                    entity.world.playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entity.world.addParticle(ParticleTypes.END_ROD, linked.getX(), linked.getY(), linked.getZ(),  0.1, 0.1, 0.1);

                }
            }
        }
    }

    @Inject(method = "damage", at = @At("TAIL"))
    private void ApplyLifeSteal(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandStack();

            int level = EnchantmentHelper.getLevel(ModEnchantments.LIFE_STEAL, weapon);

            if (level > 0 && attacker.isAlive()) {
                float healAmount = amount * 0.1f * level; // Heal 10% per enchantment level
                attacker.heal(healAmount);
            }
        }
    }

    @Inject(method = "onAttacking", at = @At("HEAD"))
    private void onAttack(Entity target, CallbackInfo ci) {
        if (!(target instanceof LivingEntity)) return;

        if (target instanceof PlayerEntity player) {
            if (EnchantmentHelper.getLevel(ModEnchantments.NIGHTFALL_CLEAVE, player.getMainHandStack()) > 0) {
                // Only trigger at night or in dark areas
                if (isDarkArea(player) && player.getMainHandStack().getItem() instanceof AxeItem) {
                    // Damage and blindness effect to nearby entities
                    ApplyNightfallCleaveEffect(player, (LivingEntity) target);
                }
            }
        }
    }

    @Unique
    private boolean isDarkArea(PlayerEntity player) {
        // Return true if the player is in a dark area, e.g., caves or the Nether
        return player.world.getLightLevel(player.getBlockPos()) < 5 || player.world.isNight() || !player.world.getDimension().bedWorks();
    }

    @Unique
    private void ApplyNightfallCleaveEffect(PlayerEntity player, LivingEntity target) {
        // Apply the dark energy wave effect to nearby entities
        double radius = 5.0;  // Radius of the effect
        Box box = new Box(target.getX() - radius, target.getY() - radius, target.getZ() - radius,
                target.getX() + radius, target.getY() + radius, target.getZ() + radius);

        player.world.getEntitiesByClass(LivingEntity.class, box, e -> e != player)
                .forEach(entity -> {
                    entity.damage(DamageSource.MAGIC, 2.0F);
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 300, 0));
                });


        // Optionally: Play a sound or particle effect when the cleave occurs (e.g., dark energy particles)
        player.world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GHAST_SCREAM,
                SoundCategory.PLAYERS, 0.2F, 1.0F);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onSoulLinkDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.hasStatusEffect(ModEffects.SOUL_LINK)) {
            SoulLinkEffect.clearLink(self);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void ApplyWardingGlyph(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.world.isClient) return; // Skip client-side

        // Check if the entity has Warding Glyph
        for (ItemStack armorPiece : entity.getArmorItems()) {
            if (MyEnchantmentHelper.hasArmorEnchantment(entity, ModEnchantments.WARDING_GLYPH)) {
                int wardingLevel = EnchantmentHelper.getLevel(ModEnchantments.WARDING_GLYPH, armorPiece);
                if (wardingLevel > 0) {
                    // Logic to reflect one negative status effect every X seconds
                    int reflectionCooldown = 100 * wardingLevel; // Adjust time based on enchantment level
                    if (entity.age % reflectionCooldown == 0) {
                        reflectNegativeStatusEffect(entity);
                    }
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    private void freezeStopAI(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.world.isClient) return; // Skip client-side

        if (entity.hasStatusEffect(ModEffects.FREEZE)) {
            // Optional: prevent AI movement
            ci.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void freezeInPlace(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.world.isClient) return; // Skip client-side
        if (entity.hasStatusEffect(ModEffects.FREEZE)) {
            ci.cancel();
        }
    }

    @Inject(method = "updateLimbs", at = @At("HEAD"), cancellable = true)
    private void freezeStopMovement(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.world.isClient) return; // Skip client-side
        if (entity.hasStatusEffect(ModEffects.FREEZE)) {
            ci.cancel();
        }
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void preventJump(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.hasStatusEffect(ModEffects.FREEZE)) {
            ci.cancel(); // cancels jump completely
        }
    }


    @Unique
    private void reflectNegativeStatusEffect(LivingEntity entity) {
        // Check for negative effects
        for (StatusEffectInstance effect : entity.getStatusEffects()) {
            if (!effect.getEffectType().isBeneficial()) { // Negative effect check
                LivingEntity attacker = entity.getAttacker();
                entity.removeStatusEffect(effect.getEffectType()); // Remove the effect
                if (attacker != null && attacker != entity) {
                    attacker.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), effect.getDuration(), effect.getAmplifier()));
                    // Play a sound and show a particle effect when reflecting the negative effect
                    entity.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_GENERIC_HURT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entity.world.addParticle(ParticleTypes.SMOKE, entity.getX(), entity.getY(), entity.getZ(), 0.2, 0.2, 0.1);
                }
                break;
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void reduceStatusEffectDuration(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player) {
            int mindWardLevel = getEnchantmentLevelForPlayer(player, ModEnchantments.MIND_WARD);
            if (mindWardLevel > 0) {
                // Logic for reducing the duration of negative effects
                for (StatusEffectInstance effect : entity.getStatusEffects()) {
                    if(entity.getStatusEffects().isEmpty()){
                        return;
                    }
                        if (!effect.getEffectType().isBeneficial()) {
                            int newDuration = effect.getDuration() - (10 * mindWardLevel);
                            entity.removeStatusEffect(effect.getEffectType());
                            entity.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), Math.max(newDuration, 1), effect.getAmplifier()));
                        }
                    }
                }
            }
        }

}