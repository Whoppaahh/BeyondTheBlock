package net.ryan.beyond_the_block.mixin.feature.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.ryan.beyond_the_block.effect.Beneficial.SoulLinkEffect;
import net.ryan.beyond_the_block.effect.ModEffects;
import net.ryan.beyond_the_block.enchantment.ModEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityStatusMixin {

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

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void PreventEtherealDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof LivingEntity attacker) {
            if (attacker.hasStatusEffect(ModEffects.ETHEREAL_VEIL)) {
                cir.setReturnValue(false); // Cancel damage dealt by ethereal entity
            }
        }
    }
    @Inject(method = "damage", at = @At("TAIL"))
    private void ApplySoulLinkDamageSharing(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;  // This is the entity taking damage

        if (entity.hasStatusEffect(ModEffects.SOUL_LINK)) {
            StatusEffectInstance effectInstance = entity.getStatusEffect(ModEffects.SOUL_LINK);
            if (effectInstance != null && effectInstance.getEffectType() instanceof SoulLinkEffect soulLink) {
                LivingEntity linked = SoulLinkEffect.getLinkedEntityFor(entity);
                if (linked != null && linked.isAlive() && linked != entity) {
                    linked.damage(DamageSource.MAGIC, amount);
                    entity.world.playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entity.world.addParticle(ParticleTypes.END_ROD, linked.getX(), linked.getY(), linked.getZ(), 0.1, 0.1, 0.1);

                }
            }
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onSoulLinkDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.hasStatusEffect(ModEffects.SOUL_LINK)) {
            SoulLinkEffect.clearLink(self);
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
