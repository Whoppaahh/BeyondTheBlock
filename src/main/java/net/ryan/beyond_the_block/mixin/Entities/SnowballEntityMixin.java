package net.ryan.beyond_the_block.mixin.Entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowballEntity.class)
public abstract class SnowballEntityMixin extends ThrownItemEntity {

    public SnowballEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onEntityHit", at = @At("TAIL"))
    private void freezeOnHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity target = entityHitResult.getEntity();
        if (target instanceof LivingEntity living) {
            // Apply your custom effect
            if(!isIceCreature(living)) {
                living.addStatusEffect(new StatusEffectInstance(ModEffects.FREEZE, 100, 0, false, false, true));
                // 100 ticks = 5 seconds, amplifier 0
            }
        }
    }
    @Unique
    private static boolean isIceCreature(Entity entity) {
        if (!(entity instanceof LivingEntity living)) return false;

        Identifier id = Registry.ENTITY_TYPE.getId(living.getType());

        return switch (id.getPath()) {
            case "polar_bear", "snow_golem", "stray" -> true;
            default -> false;
        };
    }

}

