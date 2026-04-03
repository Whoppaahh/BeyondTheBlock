package net.ryan.beyond_the_block.mixin.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import net.ryan.beyond_the_block.content.enchantment.ModEnchantments;
import net.ryan.beyond_the_block.feature.combat.TemporalSliceHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityTemporalSliceMixin {

    @Unique
    private float beyond_the_block$temporalSlicePreHealth;

    @Inject(method = "damage", at = @At("HEAD"))
    private void beyond_the_block$capturePreDamageHealth(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        this.beyond_the_block$temporalSlicePreHealth = self.getHealth();
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void beyond_the_block$queueTemporalSlice(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.getWorld().isClient) {
            return;
        }

        if (!cir.getReturnValue()) {
            return;
        }

        if (!(source.getAttacker() instanceof ServerPlayerEntity attacker)) {
            return;
        }

        ItemStack weapon = attacker.getMainHandStack();
        int level = EnchantmentHelper.getLevel(ModEnchantments.TEMPORAL_SLICE, weapon);
        if (level <= 0) {
            return;
        }

        // If already marked, don't keep recursively storing new slices
        if (self.hasStatusEffect(ModEffects.TIME_DILATION)) {
            return;
        }

        float actualDamageDealt = this.beyond_the_block$temporalSlicePreHealth - self.getHealth();
        if (actualDamageDealt <= 0.0F) {
            return;
        }

        self.addStatusEffect(new StatusEffectInstance(
                ModEffects.TIME_DILATION,
                45,
                0,
                false,
                false,
                false
        ));

        TemporalSliceHandler.queue(attacker, self, actualDamageDealt, level);
    }
}