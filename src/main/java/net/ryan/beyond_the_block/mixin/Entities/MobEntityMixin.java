package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.ryan.beyond_the_block.config.Configs;
import net.ryan.beyond_the_block.effect.ModEffects;
import net.ryan.beyond_the_block.village.GuardVillager.GuardEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MobEntity.class)
public class MobEntityMixin {


    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    public void setTarget(LivingEntity target, CallbackInfo cir) {
        MobEntity mob = ((MobEntity) (Object) this);
        if (target instanceof PlayerEntity) {
            if (target.hasStatusEffect(ModEffects.ETHEREAL_VEIL)
                    || mob.hasStatusEffect(StatusEffects.BLINDNESS)) {
                mob.setTarget(null);
                cir.cancel();
            }
        }
    }

    @Inject(method = "canTarget", at = @At("HEAD"), cancellable = true)
    public void canTarget(EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        MobEntity mob = (MobEntity) (Object) this;
        if (type == EntityType.PLAYER) {
            LivingEntity target = mob.getTarget();
            if (target instanceof PlayerEntity) {
                if (target.hasStatusEffect(ModEffects.ETHEREAL_VEIL)
                        || mob.hasStatusEffect(StatusEffects.BLINDNESS)) {
                    cir.setReturnValue(false);  // Prevent the mob from targeting the player
                }
            }
        }
    }
    @Inject(method = "setTarget", at = @At("TAIL"))
    private void onSetTarget(@Nullable LivingEntity target, CallbackInfo ci) {
        if (target == null || ((MobEntity)(Object)this) instanceof GuardEntity) {
            return;
        }
        boolean isVillager = target.getType() == EntityType.VILLAGER || target instanceof GuardEntity;
        if (isVillager) {
            List<MobEntity> list = ((MobEntity)(Object)this).getWorld().getNonSpectatingEntities(MobEntity.class, ((MobEntity)(Object)this).getBoundingBox().expand(Configs.server().features.guards.guardVillagerHelpRange, 5.0D, Configs.server().features.guards.guardVillagerHelpRange));
            for (MobEntity mobEntity : list) {
                if ((mobEntity instanceof GuardEntity || ((MobEntity)(Object)this).getType() == EntityType.IRON_GOLEM) && mobEntity.getTarget() == null) {
                    mobEntity.setTarget(((MobEntity)(Object)this));
                }
            }
        }

        if (((MobEntity)(Object)this) instanceof IronGolemEntity golem && target instanceof GuardEntity) {
            golem.setTarget(null);
        }
    }
}
